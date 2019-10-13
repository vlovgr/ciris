// https://github.com/lightbend/mima/issues/422
resolvers += Resolver.url(
  "typesafe sbt-plugins",
  url("https://dl.bintray.com/typesafe/sbt-plugins")
)(Resolver.ivyStylePatterns)

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")
addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.4.2")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.12")
addSbtPlugin("com.github.tkawachi" % "sbt-doctest" % "0.9.5")
addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "0.6.1")
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "5.2.0")
addSbtPlugin("io.crashbox" % "sbt-gpg" % "0.2.0")
addSbtPlugin("org.scalameta" % "sbt-mdoc" % "1.3.5")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.2.1")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.0")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.8")
