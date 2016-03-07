lazy val root = (project in file("."))
                  .settings(mySettings:_*)
                  .settings(name := "scala-examples")
                  .settings(scalacOptions += "-language:postfixOps")
                  .settings(libraryDependencies ++= coreDeps)


lazy val mySettings = Seq(organization := "org.github.berngp",
                          scalaVersion := "2.11.7",
                          resolvers ++= extraResolvers,
                          ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }) ++ universalSettings


lazy val universalSettings = coreSettings ++ styleSettings ++ testSettings

// Dependencies versions.
// Versions {
val akka             = "2.3.9"
val enumeratum       = "1.2.1"
val ficus            = "1.2.0"
val logback          = "1.1.3"
val scalaZ           = "7.2.0"
val scalaz           = "7.2.0"
val scodecbits       = "1.1.0"
val slf4j            = "1.7.18"
val typesafeConfig   = "1.3.0"
val typesafeLogging  = "2.1.2"
val uuidGenerator    = "3.1.3"
// }
// Versions for Tests {
val junitInterface = "0.11"
val mockito        = "1.10.19"
val scalacheck     = "1.12.5"
val scalatest      = "2.2.6"
// }


lazy val extraResolvers = Seq(
  "Artima Maven Repository" at "http://repo.artima.com/releases",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/",
  Resolver.jcenterRepo
)

lazy val coreDeps = Seq(
  "com.beachape"               %% "enumeratum"          % enumeratum,
  "com.iheart"                 %% "ficus"               % ficus,
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % typesafeLogging,
  "org.scalactic"              %% "scalactic"           % scalatest,
  "org.scalaz"                 %% "scalaz-core"         % scalaz,
  "org.scodec"                 %% "scodec-bits"         % scodecbits,
  "org.slf4j"                   % "slf4j-api"           % slf4j,

  "ch.qos.logback"              % "logback-classic"     % logback     % "test",
  "org.scalacheck"             %% "scalacheck"          % scalacheck  % "test",
  "org.scalatest"              %% "scalatest"           % scalatest   % "test"
)

lazy val akkaDeps = Seq(
  "com.typesafe.akka"    %% "akka-actor"        % akka,
  "com.typesafe.akka"    %% "akka-slf4j"        % akka,
  "com.typesafe.akka"    %% "akka-testkit"      % akka % "test"
)

lazy val coreSettings = Seq(
  logLevel in compile := Level.Warn,
  logLevel := Level.Warn,
  persistLogLevel := Level.Debug,
  traceLevel := 10,

  scalacOptions in Compile ++= Seq(
      "-encoding", "UTF-8",
      "-target:jvm-1.8",
      "-deprecation",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xlint",
      "-Xlog-reflective-calls",
      "-Ywarn-unused-import",
      "-Yno-adapted-args",
      "-Yrangepos",
      "-Ywarn-dead-code"
      //"-Ywarn-numeric-widen"
    ),
    javacOptions in Compile ++= Seq(
        "-encoding", "UTF-8",
        "-source", "1.8",
        "-target", "1.8",
        "-deprecation",
        "-Xlint:unchecked",
        "-Xlint:deprecation"
      )
)


lazy val testSettings = Seq(
    parallelExecution in Test := false,
    fork in Test := true,
    javaOptions in Test ++= Seq(
        "-Xmx1250M",
        "-Xloggc:gc.log",
        "-XX:+PrintGCDetails",
        "-XX:+PrintGCDateStamps",
        "-XX:+PrintTenuringDistribution",
        "-XX:+PrintHeapAtGC"
    ),
    testOptions in Test += Tests.Argument("-eDST")
)



lazy val testScalastyle = taskKey[Unit]("testScalastyle")

lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")

lazy val styleSettings = Seq(
  scalastyleFailOnError := true,
  // Scalastyle on Tests
  testScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Test).toTask("").value,
  (scalastyleConfig in Test) := baseDirectory.value / "scalastyle-test-config.xml",
  (test in Test)    <<= (test in Test)    dependsOn testScalastyle,
  // Scalastyle when compiling.
  compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value,
  // Is running this on compile too much?
  (compile in Test) <<= (compile in Test) dependsOn compileScalastyle
)
