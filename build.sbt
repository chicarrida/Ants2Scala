name := "Ants2Scala"

version := "0.0.1"

scalaVersion := "2.11.5"

//scalacOptions ++= Seq("-feature", "-deprecation") 

//scalacOptions ++= Seq( "-deprecation") 



mainClass in assembly := Some("ants2scala.Main")
//resolvers += Resolver.sonatypeRepo("public") 

resolvers +="Repository" at "https://oss.sonatype.org/content/repositories/public"

scalaSource in Compile := file("/home/linux/Code/Scala/Ants2Scala/src/") // the default is something like src/main/scala

libraryDependencies += 
  "org.processing" % "core" % "2.2.1"

