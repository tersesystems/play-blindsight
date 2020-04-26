name := """play-blindsight"""
organization := "com.tersesystems"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.11"

val terseLogback = "0.16.1"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

resolvers += Resolver.bintrayRepo("tersesystems", "maven")

libraryDependencies += "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.1"

libraryDependencies += "eu.timepit" %% "refined"                 % "0.9.13"
libraryDependencies += "com.tersesystems.logback" % "logback-tracing" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-uniqueid-appender" % terseLogback
libraryDependencies += "com.tersesystems.logback" % "logback-honeycomb-appender" % terseLogback
//libraryDependencies += "com.tersesystems.logback" % "logback-honeycomb-okhttp" % terseLogback
libraryDependencies += "com.tersesystems.logback" %% "logback-honeycomb-playws" % terseLogback
libraryDependencies += "com.tersesystems.blindsight" %% "blindsight-logstash" % "0.1.0"
