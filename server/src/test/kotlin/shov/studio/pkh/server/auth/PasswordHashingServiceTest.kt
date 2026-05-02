package shov.studio.pkh.server.auth

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PasswordHashingServiceTest {
    private val service = Argon2PasswordHashingService(
        iterations = 1,
        memoryKiB = 1_024,
        parallelism = 1,
    )

    @Test
    fun verifiesOriginalPassword() {
        val hash = service.hashPassword("password1")

        assertTrue(service.verifyPassword("password1", hash))
    }

    @Test
    fun rejectsDifferentPassword() {
        val hash = service.hashPassword("password1")

        assertFalse(service.verifyPassword("password2", hash))
    }
}
