import Settings._

val `vcardcleanser` = project
  .in(file("."))
  .enablePlugins(DeployApp, DockerPlugin)
  .settings(defaultSettings: _*)
  .settings(
     libraryDependencies ++= Dependencies.Dist, parallelExecution in Test := false
)


