import Dependencies._

showCurrentGitBranch

git.useGitDescribe := true

lazy val commonSettings = Seq(
  organization := "org.hathitrust.htrc",
  organizationName := "HathiTrust Research Center",
  organizationHomepage := Some(url("https://www.hathitrust.org/htrc")),
  scalaVersion := "2.12.10",
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-language:postfixOps",
    "-language:implicitConversions"
  ),
  externalResolvers := Seq(
    Resolver.defaultLocal,
    Resolver.mavenLocal,
    "HTRC Nexus Repository" at "https://nexus.htrc.illinois.edu/content/groups/public",
  ),
  packageOptions in (Compile, packageBin) += Package.ManifestAttributes(
    ("Git-Sha", git.gitHeadCommit.value.getOrElse("N/A")),
    ("Git-Branch", git.gitCurrentBranch.value),
    ("Git-Version", git.gitDescribedVersion.value.getOrElse("N/A")),
    ("Git-Dirty", git.gitUncommittedChanges.value.toString),
    ("Build-Date", new java.util.Date().toString)
  ),
//  wartremoverWarnings ++= Warts.unsafe.diff(Seq(
//    Wart.DefaultArguments,
//    Wart.NonUnitStatements,
//    Wart.Any
//  ))
)

lazy val ammoniteSettings = Seq(
  libraryDependencies +=
    {
      val version = scalaBinaryVersion.value match {
        case "2.10" => "1.0.3"
        case _ => "2.0.4"
      }
      "com.lihaoyi" % "ammonite" % version % Test cross CrossVersion.full
    },
  sourceGenerators in Test += Def.task {
    val file = (sourceManaged in Test).value / "amm.scala"
    IO.write(file, """object amm extends App { ammonite.Main.main(args) }""")
    Seq(file)
  }.taskValue,
  fork in (Test, run) := false
)

lazy val `extract-seqfiles-key` = (project in file("."))
  .enablePlugins(GitVersioning, GitBranchPrompt, JavaAppPackaging)
  .settings(commonSettings)
  .settings(ammoniteSettings)
//  .settings(spark("2.4.4"))
  .settings(spark_dev("2.4.4"))
  .settings(
    name := "extract-seqfiles-key",
    description := "Extracts the keys from sequence files holding paired data",
    licenses += "Apache2" -> url("http://www.apache.org/licenses/LICENSE-2.0"),
    libraryDependencies ++= Seq(
      "org.rogach"                    %% "scallop"              % "3.3.2",
      "org.hathitrust.htrc"           %% "scala-utils"          % "2.10.1",
      "org.hathitrust.htrc"           %% "spark-utils"          % "1.3",
      "com.gilt"                      %% "gfc-time"             % "0.0.7",
      "ch.qos.logback"                %  "logback-classic"      % "1.2.3",
      "org.codehaus.janino"           %  "janino"               % "3.1.0",
      "org.scalacheck"                %% "scalacheck"           % "1.14.3"      % Test,
      "org.scalatest"                 %% "scalatest"            % "3.1.0"       % Test
    ),
    dependencyOverrides ++= Seq(
      "com.google.guava" % "guava" % "15.0",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7.1"
    )
  )
