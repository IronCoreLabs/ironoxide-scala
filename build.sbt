ThisBuild / organization := "com.ironcorelabs"
ThisBuild / scalaVersion := "2.12.10"

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
    "Typesafe repository".at("https://repo.typesafe.com/typesafe/release/")
  ),
  libraryDependencies ++= Seq(
    "org.scodec"       %% "scodec-bits"    % "1.1.12",
    "com.ironcorelabs" % "ironoxide-java"  % "0.5.0",
    "org.typelevel"    %% "cats-effect"    % "2.0.0",
    "com.ironcorelabs" %% "cats-scalatest" % "2.4.0" % Test,
    "org.scalatest"    %% "scalatest"      % "3.0.5" % Test
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

licenses := Seq("AGPL-3.0" -> url("https://www.gnu.org/licenses/agpl-3.0.txt"))
// Add the default sonatype repository setting
publishTo := sonatypePublishTo.value

homepage := Some(url("http://github.com/ironcorelabs/ironoxide-scala"))

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ =>
  false
}

useGpg := true

usePgpKeyHex("E84BBF42")

pomExtra := (
  <scm>
      <url>git@github.com:IronCoreLabs/ironoxide-java.git</url>
      <connection>scm:git@github.com:IronCoreLabs/ironoxide-java.git</connection>
    </scm>
    <developers>
      {
    Seq(
      ("coltfred", "Colt Frederickson"),
      ("clintfred", "Clint Frederickson"),
      ("skeet70", "Murph Murphy"),
      ("ernieturner", "Ernie Turner"),
      ("bobwall23", "Bob Wall")
    ).map {
      case (id, name) =>
        <developer>
            <id>{id}</id>
            <name>{name}</name>
            <url>http://github.com/{id}</url>
          </developer>
    }
  }
    </developers>
)

import ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true),
  pushChanges
)
