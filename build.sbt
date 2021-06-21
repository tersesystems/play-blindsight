name := """play-blindsight"""
organization := "com.tersesystems"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.12"

organizationName := "Terse Systems"
startYear := Some(2020)
licenses += ("CC0", new URL("https://creativecommons.org/publicdomain/zero/1.0/"))

val terseLogback = "1.0.0"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

libraryDependencies += "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.1"

// https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-scala
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.3"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.3"

libraryDependencies += "com.tersesystems.logback" % "logback-tracing" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-uniqueid-appender" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-typesafe-config" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-exception-mapping" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-exception-mapping-providers" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-budget" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-turbomarker" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-honeycomb-appender" % terseLogback
libraryDependencies += "com.tersesystems.logback" %% "logback-honeycomb-playws" % terseLogback

libraryDependencies += "com.tersesystems.blacklite" % "blacklite-logback" % "1.0.1"

libraryDependencies += "com.tersesystems.blindsight" %% "blindsight-logstash" % "1.5.1"
libraryDependencies += "com.tersesystems.blindsight" %% "blindsight-jsonld" % "1.5.1"
