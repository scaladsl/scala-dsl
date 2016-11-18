import sgf.CDSL._

define {

  'services namespace {

    'user struct {
      'forename required 'string
      'surname required 'string
    }

    'phone_number enum {
      'beeline is 99777888
      'vivacell is 99777999
    }

    'authentication namespace {

      'user struct {
        'forename required 'string
        'surname required 'string
      }

      'login_request struct {
        'username required 'string
        'password required 'float
      }

      'login_reply struct {
        'user1 required 'services :: 'user
      }
    }
  }

  'services2 namespace {

    'user struct {
      'forename required 'string
      'surname required 'string
    }

    'phone_number enum {
      'beeline is 99777888
      !"""comment for enum"""
      'vivacell is 99765222
    }

    'authentication namespace {

      !"""Comment test for User struct"""
      'user struct {
      !"""field forename test comment"""
        'forename required 'string
        'surname required 'string
      }

      'login_request struct {
        'username required 'string
        'password required 'float
      }

      'login_reply struct {
        'user2 required 'services2 :: 'authentication :: 'user
      }
    }
  }
}
