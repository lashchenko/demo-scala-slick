import sbt.Keys._

val project = Project(
  id = "demo-scala-slick",
  base = file("."),
  settings = Seq(
    name := "demo-scala-slick",
    scalaVersion := "2.11.8",
    
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % "3.1.1",
      "com.google.inject" % "guice" % "4.1.0",
      "net.codingwell" %% "scala-guice" % "4.0.1",
      "com.h2database" % "h2" % "1.4.193"
    ),

    parallelExecution in Test := false
  )
)
