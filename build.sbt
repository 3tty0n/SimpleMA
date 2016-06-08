lazy val root = (project in file("."))
  .settings(baseSettings)

lazy val baseSettings = Seq(
  name := "SimpleMA",
  version := "0.1.0",
  scalaVersion := "2.11.8",
  libraryDependencies ++= luceneDependencies ++ testDependencies ++ kuromojiDependencies ++ armDependencies,
  resolvers += "Atilika Open Source repository" at "http://www.atilika.org/nexus/content/repositories/atilika"
)

lazy val luceneDependencies = Seq(
  "org.apache.lucene" % "lucene-core" % "5.2.1",
  "org.apache.lucene" % "lucene-analyzers-common" % "5.2.1",
  "org.apache.lucene" % "lucene-analyzers-kuromoji" % "5.2.1"
)

lazy val kuromojiDependencies = Seq(
  "org.atilika.kuromoji" % "kuromoji" % "0.7.7"
)

lazy val testDependencies = Seq(
  "org.scalactic" %% "scalactic" % "2.2.6",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

lazy val moduleDependencies = Seq(
  "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3"
)

lazy val armDependencies = Seq(
  "com.jsuereth" %% "scala-arm" % "1.4"
)
