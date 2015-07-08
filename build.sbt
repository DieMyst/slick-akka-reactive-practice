name := "slick-akka-reactive-examples"

val akkaHttpVersion = "1.0-RC4"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "com.typesafe.akka" % "akka-stream-experimental_2.11" % akkaHttpVersion,
  "com.typesafe.akka" % "akka-http-core-experimental_2.11" % akkaHttpVersion,
  "com.typesafe.akka" % "akka-http-experimental_2.11" % akkaHttpVersion,
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "com.zaxxer" % "HikariCP-java6" % "2.3.3",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.3"
)
    