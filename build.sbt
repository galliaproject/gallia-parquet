// gallia-parquet

// ===========================================================================
ThisBuild / organizationName     := "Gallia Project"
ThisBuild / organization         := "io.github.galliaproject" // *must* match groupId for sonatype
ThisBuild / organizationHomepage := Some(url("https://github.com/galliaproject"))
ThisBuild / startYear            := Some(2021)
ThisBuild / version              := "0.6.0-SNAPSHOT"
ThisBuild / description          := "A Scala library for data manipulation"
ThisBuild / homepage             := Some(url("https://github.com/galliaproject/gallia-parquet"))
ThisBuild / licenses             := Seq("Apache 2" -> url("https://github.com/galliaproject/gallia-parquet/blob/master/LICENSE"))
ThisBuild / developers           := List(Developer(
  id    = "anthony-cros",
  name  = "Anthony Cros",
  email = "contact.galliaproject@gmail.com",
  url   = url("https://github.com/anthony-cros")))
ThisBuild / scmInfo              := Some(ScmInfo(
  browseUrl  = url("https://github.com/galliaproject/gallia-parquet"),
  connection =     "scm:git@github.com:galliaproject/gallia-parquet.git"))

// ===========================================================================
lazy val root = (project in file("."))
  .settings(
    name   := "gallia-parquet",
    target := baseDirectory.value / "bin" / "parquet")
  .settings(GalliaCommonSettings.mainSettings:_*)

// ===========================================================================
lazy val parquetVersion = "1.12.2"
lazy val hadoopVersion  = "3.3.1"

// ---------------------------------------------------------------------------
libraryDependencies ++= Seq(
  "io.github.galliaproject" %% "gallia-avro"   % version.value, // NOTICE: depends on avro, not core
  "org.apache.parquet"      %  "parquet-avro"  % parquetVersion,
  "org.apache.hadoop"       %  "hadoop-client" % hadoopVersion) // required, see https://issues.apache.org/jira/browse/PARQUET-1126

// ===========================================================================
sonatypeRepository     := "https://s01.oss.sonatype.org/service/local"
sonatypeCredentialHost :=         "s01.oss.sonatype.org"
publishMavenStyle      := true
publishTo              := sonatypePublishToBundle.value

// ===========================================================================

