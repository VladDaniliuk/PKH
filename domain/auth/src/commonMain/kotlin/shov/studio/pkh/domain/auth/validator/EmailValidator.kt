package shov.studio.pkh.domain.auth.validator

import org.koin.core.annotation.Factory

@Factory
class EmailValidator {
    fun isValid(email: String): Boolean {
        val normalized = email.trim()
        val atIndex = normalized.indexOf('@')
        val lastDotIndex = normalized.lastIndexOf('.')

        return normalized.length in MIN_EMAIL_LENGTH..MAX_EMAIL_LENGTH &&
            atIndex > 0 &&
            lastDotIndex > atIndex + 1 &&
            lastDotIndex < normalized.lastIndex &&
            !normalized.any(Char::isWhitespace)
    }

    private companion object {
        const val MIN_EMAIL_LENGTH = 3
        const val MAX_EMAIL_LENGTH = 320
    }
}