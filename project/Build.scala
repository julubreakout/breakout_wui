import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
	val appName         = "breakout_wui"
    val appVersion      = "1.0-SNAPSHOT"
	val appDependencies = Seq(
	    javaCore,
	    javaJdbc,
	    javaEbean,
	    "com.google.inject" % "guice" % "3.0",
	    "org.ektorp" % "org.ektorp" % "1.3.0"
	)
	
	val main = play.Project(appName, appVersion, appDependencies).settings(
		pomExtra := <build>	
					 <sourceDirectory>app</sourceDirectory>	
					 <testSourceDirectory>test</testSourceDirectory>	
					 <resources>	
					 <resource>	
					 <directory>app</directory>	
					 </resource>	
					 </resources>	
					 </build>	

	)
	
}