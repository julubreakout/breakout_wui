import play.Project._

name := """breakout_wui"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
	"org.webjars" %% "webjars-play" % "2.2.0", 
	"org.webjars" % "bootstrap" % "2.3.1",
	"julubreakout" % "core" % "0.0.1-SNAPSHOT")

playJavaSettings

scalacOptions ++= Seq("-deprecation")