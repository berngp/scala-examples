// factor out common settings into a sequence
lazy val commonSettings = Seq(
  organization := "org.github.berngp",
  version := "0.1.0",
  // set the Scala version used for the project
  scalaVersion := "2.11.7"
)

// define ModuleID for library dependencies
lazy val scalacheck = "org.scalacheck" %% "scalacheck" % "1.12.0"
lazy val scalatest  = "org.scalatest"  %% "scalatest"  % "2.2.6"
lazy val scalactic  = "org.scalactic"  %% "scalactic"  % "2.2.6"

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    // set the name of the project
    name := "Intel Test",
    // add a test dependency on ScalaCheck
    libraryDependencies ++= Seq(
      scalacheck % Test,
      scalatest  % Test,
      scalactic
    ),

    // reduce the maximum number of errors shown by the Scala compiler
    maxErrors := 20,
    // increase the time between polling for file changes when using continuous execution
    pollInterval := 1000,
    // append several options to the list of options passed to the Java compiler
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    // append -deprecation to the options passed to the Scala compiler
    scalacOptions += "-deprecation",
    // define the statements initially evaluated when entering 'console', 'consoleQuick', or 'consoleProject'
    initialCommands := """
      |import System.{currentTimeMillis => now}
      |def time[T](f: => T): T = {
      |  val start = now
      |  try { f } finally { println("Elapsed: " + (now - start)/1000.0 + " s") }
      |}""".stripMargin,

    // add a maven-style repository
    resolvers += "name" at "url",
    // add a sequence of maven-style repositories
    resolvers ++= Seq("name" at "url"),

    // set Ivy logging to be at the highest level
    ivyLoggingLevel := UpdateLogging.Full,
    // disable updating dynamic revisions (including -SNAPSHOT versions)
    //offline := true,
    // set the prompt (for this build) to include the project id.
    shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " },
    // set the prompt (for the current project) to include the username
    // disable printing timing information, but still print [success]
    //showTiming := false,
    // disable printing a message indicating the success or failure of running a task
    //showSuccess := false,
    // change the format used for printing task completion time
    timingFormat := {
        import java.text.DateFormat
        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
    },

    fork in Test := true,

    // add a JVM option to use when forking a JVM for 'run'
    javaOptions += "-Xmx2G",

    // Execute tests in the current project serially
    //   Tests from other projects may still run concurrently.
    parallelExecution in Test := false,

    logLevel in compile := Level.Warn,
    logLevel := Level.Warn,
    persistLogLevel := Level.Debug,
    traceLevel := 10,


    testOptions in Test += Tests.Argument("-eDST")
)
