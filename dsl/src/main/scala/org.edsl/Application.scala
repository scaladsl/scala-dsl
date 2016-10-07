package org.edsl

import org.edsl.DSL._

// Compiler - SDLC
object Application {

  def main(args: Array[String]): Unit = {

    'services namespace {
      !"""my comment for user structure"""
      'user struct {
        !"""field forename comment"""
        'forename required 'string
        !"""field surname comment"""
        'surname required 'string
      }
      'phone_number enum {
        'beeline is 99777888
        'vivacell is 96888777
      }
      'authentication namespace {
        'user struct {
          'forename required 'string
          'surname required 'string
        }
        !"""my comment for login request"""
        'login_request struct {
          'username required 'string
          'password required 'string
        }
        'login_reply struct {
          'user1 required 'services :: 'user
        }
      }
    }
  }
}
