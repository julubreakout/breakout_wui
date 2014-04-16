// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "HTWG Repo" at "http://lenny2.in.htwg-konstanz.de:8081/artifactory/libs-snapshot-local"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.0")

addSbtPlugin("de.johoop" % "jacoco4sbt" % "2.1.4")	