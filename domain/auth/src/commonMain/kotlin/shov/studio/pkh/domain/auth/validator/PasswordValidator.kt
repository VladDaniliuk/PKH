package shov.studio.pkh.domain.auth.validator

import org.koin.core.annotation.Factory

@Factory
class PasswordValidator {
    fun isValid(password: String): Boolean =
        password.length >= MIN_PASSWORD_LENGTH &&
            password.any(Char::isLetter) &&
            password.any(Char::isDigit)

    private companion object {
        const val MIN_PASSWORD_LENGTH = 8
    }
}