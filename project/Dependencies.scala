import sbt._

object Dependencies {

  val Version = "0.1-SNAPSHOT"
  val Dist = Seq(
    Libs.`scalaLogging`,
    Libs.`scalaTest` % Test,
    Libs.`mockito` % Test,
    Libs.`ezVcard`
  )
}
