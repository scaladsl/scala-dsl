name := "edsl"

version := "1.0"

scalaVersion := "2.11.8"

//unmanagedSourceDirectories in Compile += baseDirectory.value / "src/generated/java"
//unmanagedSourceDirectories in Compile += baseDirectory.value / "src/generated/scala"

//mainClass in assembly := Some("org.edsl.Application")

libraryDependencies ++= Seq(
  // configuration
  //"com.typesafe" % "config" % "1.3.0",

  // logging
  //"com.typesafe.scala-logging" %% "scala-logging" % "3.4.0",
  //"org.slf4j" % "slf4j-log4j12" % "1.7.21",
  //"log4j" % "log4j" % "1.2.17"
)

