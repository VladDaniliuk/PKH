package shov.studio.pkh.server.auth

import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory

interface PasswordHashingService {
    fun hashPassword(password: String): String

    fun verifyPassword(password: String, passwordHash: String): Boolean
}

class Argon2PasswordHashingService(
    private val argon2: Argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id),
    private val iterations: Int = 3,
    private val memoryKiB: Int = 65_536,
    private val parallelism: Int = 1,
) : PasswordHashingService {
    override fun hashPassword(password: String): String {
        val passwordChars = password.toCharArray()
        return try {
            argon2.hash(iterations, memoryKiB, parallelism, passwordChars)
        } finally {
            argon2.wipeArray(passwordChars)
        }
    }

    override fun verifyPassword(password: String, passwordHash: String): Boolean {
        val passwordChars = password.toCharArray()
        return try {
            argon2.verify(passwordHash, passwordChars)
        } finally {
            argon2.wipeArray(passwordChars)
        }
    }
}
