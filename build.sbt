import play.Project._
import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := """breakout_wui"""

organization := "julubreakout"

crossPaths := false

version := "1.0-SNAPSHOT"

resolvers += "HTWG Repo" at "http://lenny2.in.htwg-konstanz.de:8081/artifactory/libs-snapshot-local"

resolvers += "Jacoco Repo" at "http://repo.scala-sbt.org/scalasbt/repo/de.johoop/"

resolvers += "Db4o Repo" at "http://source.db4o.com/maven"

publishTo := Some("HTWG Resolver" at "http://lenny2.in.htwg-konstanz.de:8081/artifactory/libs-snapshot-local;build.timestamp=" + new java.util.Date().getTime)

libraryDependencies ++= Seq(
	"org.webjars" %% "webjars-play" % "2.2.0", 
	"org.webjars" % "bootstrap" % "2.3.1",
	"com.db4o" % "db4o-full-java5" % "7.13-SNAPSHOT",
	"com.google.inject" % "guice" % "3.0",
	"julubreakout" % "core" % "0.0.1-SNAPSHOT",
	"org.hibernate" % "hibernate" % "3.5.4-Final",
	"org.hibernate" % "hibernate-annotations" % "3.5.6-Final",
	"mysql" % "mysql-connector-java" % "5.1.26",
	"org.slf4j" % "slf4j-api" % "1.5.8",
	"org.slf4j" % "slf4j-simple" % "1.5.8",
	"javassist" % "javassist" % "3.12.1.GA",
	"org.ektorp" % "org.ektorp" % "1.3.0",
	"com.google.inject.extensions" % "guice-multibindings" % "3.0")

playJavaSettings

jacoco.settings

parallelExecution      in jacoco.Config := false

jacoco.outputDirectory in jacoco.Config := file("target/jacoco")

scalacOptions ++= Seq("-deprecation")