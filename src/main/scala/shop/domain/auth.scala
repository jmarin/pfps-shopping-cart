package shop.domain

import io.estatico.newtype.macros.newtype
import java.{ util => ju }
import shop.http.auth.roles.UserName

object auth {

  // --------- user registration -----------

  @newtype case class NewUserName(value: String)
  @newtype case class NewEmail(value: String) // TODO: Use refined instead of newtype here
  @newtype case class NewPassword(value: String)

  case class CreateUser(
      username: NewUserName,
      email: NewEmail,
      password: NewPassword
  )

  // --------- user login -----------

  @newtype case class Email(value: String) // TODO: Use refined instead of newtype here
  @newtype case class Password(value: String)

  case class LoginUser(
      username: Option[UserName],
      email: Option[Email],
      password: Password
  )
}
