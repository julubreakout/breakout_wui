import play.Project._
import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := """breakout_wui"""

organization := "julubreakout"

crossPaths := false

version := "1.0-SNAPSHOT"

resolvers += "HTWG Repo" at "http://lenny2.in.htwg-konstanz.de:8081/artifactory/libs-snapshot-local"

resolvers += "Jacoco Repo" at "http://repo.scala-sbt.org/scalasbt/repo/de.johoop/"

publishTo := Some("HTWG Resolver" at "http://lenny2.in.htwg-konstanz.de:8081/artifactory/libs-snapshot-local;build.timestamp=" + new java.util.Date().getTime)

libraryDependencies ++= Seq(
	"org.webjars" %% "webjars-play" % "2.2.0", 
	"org.webjars" % "bootstrap" % "2.3.1",
	"julubreakout" % "core" % "0.0.1-SNAPSHOT")

playJavaSettings

jacoco.settings

parallelExecution      in jacoco.Config := false

jacoco.outputDirectory in jacoco.Config := file("target/jacoco")

scalacOptions ++= Seq("-deprecation")