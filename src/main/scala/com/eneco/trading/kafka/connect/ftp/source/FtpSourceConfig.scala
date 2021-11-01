package com.eneco.trading.kafka.connect.ftp.source

import java.util

import org.apache.kafka.common.config.ConfigDef.{Importance, Type}
import org.apache.kafka.common.config.{AbstractConfig, ConfigDef}

import scala.jdk.CollectionConverters._

case class MonitorConfig(topic:String, path:String, tail:Boolean)

object KeyStyle extends Enumeration {
  type KeyStyle = Value
  val String = Value(FtpSourceConfig.StringKeyStyle)
  val Struct = Value(FtpSourceConfig.StructKeyStyle)
}
import com.eneco.trading.kafka.connect.ftp.source.KeyStyle._

object FtpSourceConfig {
  val Address = "ftp.address"
  val User = "ftp.user"
  val Password = "ftp.password"
  val MaxBackoff = "ftp.max.backoff"
  val RefreshRate = "ftp.refresh"
  val MonitorTail = "ftp.monitor.tail"
  val MonitorUpdate = "ftp.monitor.update"
  val FileMaxAge = "ftp.file.maxage"
  val KeyStyle = "ftp.keystyle"
  val StringKeyStyle = "string"
  val StructKeyStyle = "struct"
  val FileConverter = "ftp.fileconverter"
  val SourceRecordConverter = "ftp.sourcerecordconverter"
  val FtpMaxPollRecords = "ftp.max.poll.records"

  val definition: ConfigDef = new ConfigDef()
    .define(Address, Type.STRING, Importance.HIGH, "ftp address[:port]")
    .define(User, Type.STRING, Importance.HIGH, "ftp user name to login")
    .define(Password, Type.PASSWORD, Importance.HIGH, "ftp password to login")
    .define(RefreshRate, Type.STRING, Importance.HIGH, "how often the ftp server is polled; ISO8601 duration")
    .define(MaxBackoff, Type.STRING,"PT30M", Importance.HIGH, "on failure, exponentially backoff to at most this ISO8601 duration")
    .define(FileMaxAge, Type.STRING, Importance.HIGH, "ignore files older than this; ISO8601 duration")
    .define(MonitorTail, Type.LIST, "", Importance.HIGH, "comma separated lists of path:destinationtopic; tail of file is tracked")
    .define(MonitorUpdate, Type.LIST, "", Importance.HIGH, "comma separated lists of path:destinationtopic; whole file is tracked")
    .define(KeyStyle, Type.STRING, Importance.HIGH, s"what the output key is set to: `${StringKeyStyle}` => filename; `${StructKeyStyle}` => structure with filename and offset")
    .define(FileConverter, Type.CLASS, "com.eneco.trading.kafka.connect.ftp.source.SimpleFileConverter", Importance.HIGH, s"TODO")
    .define(SourceRecordConverter, Type.CLASS, "com.eneco.trading.kafka.connect.ftp.source.NopSourceRecordConverter", Importance.HIGH, s"TODO")
    .define(FtpMaxPollRecords, Type.INT, 10000, Importance.LOW, "Max number of records returned per poll")
}

// abstracts the properties away a bit
class FtpSourceConfig(props: util.Map[String, String])
  extends AbstractConfig(FtpSourceConfig.definition, props) {

  // don't leak our ugly config!
  def ftpMonitorConfigs(): Seq[MonitorConfig] = {
    lazy val topicPathRegex = "([^:]*):(.*)".r
    (getList(FtpSourceConfig.MonitorTail).asScala.map {
      case topicPathRegex(path, topic) => MonitorConfig(topic, path, tail = true)
    } ++
      getList(FtpSourceConfig.MonitorUpdate).asScala.map {
        case topicPathRegex(path, topic) => MonitorConfig(topic, path, tail = false)
      })
      .toSeq
  }

  def address(): (String, Option[Int]) = {
    lazy val hostIpRegex = "([^:]*):?([0-9]*)".r
    val hostIpRegex(host, port) = getString(FtpSourceConfig.Address)
    (host, if (port.isEmpty) None else Some(port.toInt))
  }

  def keyStyle(): KeyStyle = KeyStyle.values.find(_.toString.toLowerCase == getString(FtpSourceConfig.KeyStyle)).get

  def sourceRecordConverter(): SourceRecordConverter =
    getConfiguredInstance(FtpSourceConfig.SourceRecordConverter, classOf[SourceRecordConverter])

  def fileConverter = getClass(FtpSourceConfig.FileConverter)

  def timeoutMs() = 30*1000

  def maxPollRecords = getInt(FtpSourceConfig.FtpMaxPollRecords)
}