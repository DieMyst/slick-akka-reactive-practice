name := "slick-akka-reactive-examples"

val akkaHttp = "1.0-RC4"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.3",
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "com.typesafe.akka" % "akka-stream-experimental_2.11" % akkaHttp,
  "com.typesafe.akka" % "akka-http-core-experimental_2.11" % akkaHttp,
  "com.typesafe.akka" % "akka-http-experimental_2.11" % akkaHttp,
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "com.zaxxer" % "HikariCP" % "2.3.8"
)
    