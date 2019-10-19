import sbt._
import scalapb.compiler.Version.scalapbVersion

object Libs {
  val ScalaVersion = "2.13.0"
  val `scalaLogging` = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  val `scalaCollectionCompat` = "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2"
  val `scalaTest` = "org.scalatest" %% "scalatest" % "3.0.8"
  val `googleGuava` = "com.google.guava" % "guava" % "23.0"
  val `mockito` = "org.mockito" %% "mockito-scala" % "1.5.16"
  val `ezVcard` = "com.googlecode.ez-vcard" % "ez-vcard" %  "0.10.5"
}
