// gallia-parquet

// ===========================================================================
lazy val root = (project in file("."))
  .settings(
    organizationName     := "Gallia Project",
    organization         := "io.github.galliaproject", // *must* match groupId for sonatype
    name                 := "gallia-parquet",
    version              := GalliaCommonSettings.CurrentGalliaVersion,
    homepage             := Some(url("https://github.com/galliaproject/gallia-parquet")),
    scmInfo              := Some(ScmInfo(
        browseUrl  = url("https://github.com/galliaproject/gallia-parquet"),
        connection =     "scm:git@github.com:galliaproject/gallia-parquet.git")),
    licenses             := Seq("BSL 1.1" -> url("https://github.com/galliaproject/gallia-parquet/blob/master/LICENSE")),
    description          := "A Scala library for data manipulation" )
  .settings(GalliaCommonSettings.mainSettings:_*)

// ===========================================================================
lazy val parquetVersion = "1.12.2"
lazy val hadoopVersion  = "3.3.1"

// ---------------------------------------------------------------------------
libraryDependencies ++= Seq(
  "io.github.galliaproject" %% "gallia-avro"   % GalliaCommonSettings.CurrentGalliaVersion, // depends on avro, not core
  "org.apache.parquet"      %  "parquet-avro"  % parquetVersion,
  "org.apache.hadoop"       %  "hadoop-client" % hadoopVersion) // required, see https://issues.apache.org/jira/browse/PARQUET-1126

// ===========================================================================
sonatypeRepository     := "https://s01.oss.sonatype.org/service/local"
sonatypeCredentialHost :=         "s01.oss.sonatype.org"
publishMavenStyle      := true
publishTo              := sonatypePublishToBundle.value

// ===========================================================================

