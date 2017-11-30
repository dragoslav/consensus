import sbt.Keys._

import scala.language.postfixOps
import scalariform.formatter.preferences._

enablePlugins(ScalaJSPlugin)

name := "consensus"

description := "Consensus"

organization := "nl.lpdiy"

version := "0.1.0"

scalaVersion := "2.12.4"

resolvers ++= Seq(
  Resolver.jcenterRepo,
  DefaultMavenRepository,
  Resolver.typesafeRepo("releases"),
  Resolver.sonatypeRepo("releases")
)

scalaJSUseMainModuleInitializer := false

scalariformPreferences := scalariformPreferences.value
    .setPreference(CompactControlReadability, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DanglingCloseParenthesis, Preserve)
    .setPreference(RewriteArrowSymbols, true)
    .setPreference(DoubleIndentConstructorArguments, true)

lazy val consensus = project.in(sbt.file("."))

javacOptions ++= Seq("-encoding", "UTF-8")

scalacOptions ++= Seq(Opts.compile.deprecation, Opts.compile.unchecked, "-target:jvm-1.8", "-Ywarn-unused-import", "-Ywarn-unused", "-Xlint", "-feature")
