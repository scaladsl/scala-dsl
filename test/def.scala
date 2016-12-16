import sgf.CDSL._

define {

  'am ::= namespace {
    'iunetworks ::= namespace {
      'ppcm ::= namespace {
        'components ::= namespace {
          'administration ::= namespace {

            'cpv ::= struct {
              'id required 'uuid('nullable -> true)
              'paretn_id required 'uuid
              'code required 'string
              'name_en required 'string
              'name_hy required 'string
              'name_ru required 'string
              'armeps_id required 'int
            }

            'cpv ::= service("/private/administration/dictionaries/cpv") {
              'list ::= post(s"/list/${'parent as 'uuid}") {
                returns repeated 'am :: 'iunetworks :: 'ppcm :: 'components :: 'administration :: 'cpv
              }

              'get ::= post(s"/get/${'id as 'uuid}") {
                returns required 'am :: 'iunetworks :: 'ppcm :: 'components :: 'administration :: 'cpv
              }

              'paht ::= post(s"/path/${'id as 'uuid}") {
                returns repeated 'am :: 'iunetworks :: 'ppcm :: 'components :: 'administration :: 'cpv
              }

              'count ::= post(s"/count") {
                returns required 'int
              }

              !"""comment for add method"""
              'add ::= post(s"/add") {
                'cpv arg 'am :: 'iunetworks :: 'ppcm :: 'components :: 'administration :: 'cpv
              }

              'save ::= put(s"/path") {
                'cpvcode arg 'am :: 'iunetworks :: 'ppcm :: 'components :: 'administration :: 'cpv
              }

            }

          }
        }
      }
    }
  }
}

//  'services namespace {

//     'user struct {
//       'forename required 'string
//       'surname required 'string('length -> 250, 'nullable -> true)
//     }

//     'phone_number enum {
//       'beeline is 99777888
//       'vivacell is 99777999
//     }

//     'authentication namespace {

//       'user struct {
//         'forename required 'string
//         'surname required 'string
//       }

//       'login_request struct {
//         'username required 'string
//         'password required 'float
//       }

//       'login_reply struct {
//         'user1 required 'services :: 'user
//       }
//     }
//   }
// }
// 'services2 namespace {

//   'user struct {
//     'forename required 'string
//     'surname required 'string
//   }

//   'phone_number enum {
//     'beeline is 99777888
//     !"""comment for enum"""
//     'vivacell is 99765222
//   }

//   'authentication namespace {

//     !"""Comment test for User struct"""
//     'user struct {
//     !"""field forename test comment"""
//       'forename required 'string
//       'surname required 'string
//     }

//     'login_request struct {
//       'username required 'string
//       'password required 'float
//     }

//     'login_reply struct {
//       'user2 required 'services2 :: 'authentication :: 'user
//     }
//   }
// }
//}
