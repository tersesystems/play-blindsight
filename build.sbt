name := """play-blindsight"""
organization := "com.tersesystems"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.11"

organizationName := "Terse Systems"
startYear := Some(2020)
licenses += ("CC0", new URL("https://creativecommons.org/publicdomain/zero/1.0/"))

val terseLogback = "0.16.1"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

resolvers += Resolver.bintrayRepo("tersesystems", "maven")

libraryDependencies += "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.1"

libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.11.0"

libraryDependencies += "com.tersesystems.logback" % "logback-tracing" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-uniqueid-appender" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-typesafe-config" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-exception-mapping" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-exception-mapping-providers" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-budget" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-turbomarker" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-honeycomb-appender" % terseLogback
libraryDependencies += "com.tersesystems.logback" %% "logback-honeycomb-playws" % terseLogback

libraryDependencies += "com.tersesystems.blindsight" %% "blindsight-logstash" % "1.2.1"
