package org.edsl

import org.edsl.DSL._

object Application {

  def main(args: Array[String]): Unit = {

    'foo namespace {
      'address struct {

      }

      'user struct {
        'username as 'string
        'address as 'address
      }
    }

  }
}
