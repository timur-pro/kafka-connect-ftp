package com.eneco.trading.kafka.connect.ftp.source

import java.util

import org.apache.kafka.common.Configurable
import org.apache.kafka.connect.source.SourceRecord
import scala.jdk.CollectionConverters._

trait SourceRecordConverter extends Configurable {
  def convert(in:SourceRecord) : java.util.List[SourceRecord]
}

class NopSourceRecordConverter extends SourceRecordConverter{
  override def configure(props: util.Map[String, _]): Unit = {}

  override def convert(in: SourceRecord): util.List[SourceRecord] = Seq(in).asJava
}