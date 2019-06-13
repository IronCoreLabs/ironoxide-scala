ThisBuild / organization := "com.ironcorelabs"
ThisBuild / scalaVersion := "2.12.8"

lazy val root = (project in file(".")).settings(
  name := "ironoxide-scala",
  scalacOptions := Seq(
    "-deprecation",
    "-encoding",
    "UTF-8", // yes, this is 2 args
    "-feature",
    "-unchecked",
    "-Xfatal-warnings",
    "-Yno-adapted-args",
    "-Ywarn-unused:imports",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture",
    "-language:higherKinds",
    "-Ypartial-unification"
  ),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/release/"
  ),
  libraryDependencies ++= Seq(
    "org.scodec" %% "scodec-bits" % "1.1.10",
    "com.ironcorelabs" % "ironoxide-java" % "0.2.2",
    "org.typelevel" %% "cats-effect" % "1.3.1",
    "com.ironcorelabs" %% "cats-scalatest" % "2.4.0" % Test,
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
  )
)

// HACK: without these lines, the console is basically unusable,
// since all imports are reported as being unused (and then become
// fatal errors).
scalacOptions in (Compile, console) ~= {
  _.filterNot(_.startsWith("-Xlint")).filterNot(_.startsWith("-Ywarn"))
}
scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value

fork in Test := true
