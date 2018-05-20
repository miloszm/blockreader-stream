enablePlugins(JavaAppPackaging)

organization := "io.forward"

name := """blockreader-strea"""

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV       = "2.4.8"
  val scalaTestV  = "2.2.6"
  val circeVersion = "0.9.3"
  Seq(
    "com.typesafe"       % "config"                               % "1.3.0",
    "com.typesafe.akka" %% "akka-http-core"                       % akkaV,
    "com.typesafe.akka" %% "akka-stream"                          % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental"    % akkaV,
    "com.typesafe.akka" %% "akka-http-testkit-experimental"       % "2.4.2-RC3",
    "ch.qos.logback" % "logback-classic"                          % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging"               % "3.9.0",
    "org.scalatest"     %% "scalatest"                            % scalaTestV % "test",
    "io.circe"          %% "circe-core"                           % circeVersion,
    "io.circe"          %% "circe-jawn"                           % circeVersion,
    "io.circe"          %% "circe-generic"                        % circeVersion,
    "de.heikoseeberger" %% "akka-http-circe"                      % "1.19.0"
  )
}


fork in run := true
