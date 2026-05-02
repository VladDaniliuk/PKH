package shov.studio.pkh.domain.auth

import shov.studio.pkh.domain.auth.validator.EmailValidator
import shov.studio.pkh.domain.auth.validator.PasswordValidator
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthValidatorsTest {
    private val emailValidator = EmailValidator()
    private val passwordValidator = PasswordValidator()

    @Test
    fun emailValidatorAcceptsBasicEmail() {
        assertTrue(emailValidator.isValid("user@example.com"))
    }

    @Test
    fun emailValidatorRejectsMalformedEmail() {
        assertFalse(emailValidator.isValid("user example.com"))
        assertFalse(emailValidator.isValid("user@example"))
        assertFalse(emailValidator.isValid("@example.com"))
    }

    @Test
    fun passwordValidatorRequiresLengthLetterAndDigit() {
        assertTrue(passwordValidator.isValid("password1"))
        assertFalse(passwordValidator.isValid("short1"))
        assertFalse(passwordValidator.isValid("password"))
        assertFalse(passwordValidator.isValid("12345678"))
    }
}
