ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "FunPar-project"
  )

libraryDependencies += "com.lihaoyi" %% "requests" % "0.7.1"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"