import Dependencies._

lazy val baseSettings = Seq(
  name := "kafka-connect-ftp",
  version := "1.0",
  scalaVersion := "2.13.6"
)

lazy val rootProject = (project in file("."))
  .settings(
    baseSettings,
    libraryDependencies ++= rootDependencies
  )