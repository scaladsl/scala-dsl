import sbt._
import Keys._

object Build extends Build {
    lazy val root = Project(id = "entity-dsl", base = file(".")) aggregate(dsl, generator)

    lazy val dsl = Project(id = "dsl", base = file("dsl"))

    lazy val generator = Project(id = "generator", base = file("generator"))
}
