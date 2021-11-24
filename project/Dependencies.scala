import sbt._

object Dependencies {
  val scalaMajorVersion = "2.13"
  val scala = "2.13.6"
  val scalaTestVersion = "3.2.10"
  val scalaLoggingVersion = "3.9.4"
  val kafkaVersion = "3.0.0"
  val avroVersion = "1.11.0"
  val slf4jVersion = "1.7.32"
  val confluentVersion = "3.2.0"
  val pathikritVersion = "3.9.1"
  val ftpserverVersion = "1.1.1"
  val commonsNetVersion = "3.8.0"
  val commonsCodecVersion = "1.15"
  val mockitoVersion = "3.2.10.0"
  val pegdownVersion = "1.6.0"

  val rootDependencies = Seq(
    "org.scala-lang" % "scala-library" % scala,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
    "org.apache.kafka" % "connect-api" % kafkaVersion,
    "com.github.pathikrit" %% "better-files" % pathikritVersion,
    "org.apache.ftpserver" % "ftpserver-core" % ftpserverVersion,
    "commons-net" % "commons-net" % commonsNetVersion,
    "commons-codec" % "commons-codec" % commonsCodecVersion,

    "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
    "org.scalatestplus" %% "mockito-3-4" % mockitoVersion % Test,
    "com.vladsch.flexmark" % "flexmark-all" % "0.62.2" % Test,
    "org.pegdown" % "pegdown" % pegdownVersion % Test,
    "org.slf4j" % "slf4j-log4j12" % slf4jVersion % Test
  )
}
