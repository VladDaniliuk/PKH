package shov.studio.pkh.server.auth

import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AccessTokenServiceTest {
    private val service = AccessTokenService(
        AccessTokenConfig(
            issuer = "pkh-test",
            audience = "pkh-client",
            secret = "test-secret-that-is-long-enough-for-hmac",
            ttlSeconds = 900,
        ),
    )

    @Test
    fun validatesIssuedToken() {
        val issuedAt = Instant.now()
        val token = service.issueAccessToken(userId = "user-id", issuedAt = issuedAt)

        val principal = assertNotNull(service.validateAccessToken(token))

        assertEquals("user-id", principal.userId)
        assertEquals(issuedAt.plusSeconds(900).epochSecond, principal.expiresAtEpochSeconds)
    }

    @Test
    fun rejectsInvalidToken() {
        assertNull(service.validateAccessToken("not-a-jwt"))
    }
}
