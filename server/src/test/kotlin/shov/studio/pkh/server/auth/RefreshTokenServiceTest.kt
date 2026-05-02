package shov.studio.pkh.server.auth

import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class RefreshTokenServiceTest {
    private val service = RefreshTokenService(
        RefreshTokenConfig(
            tokenByteLength = 32,
            ttlSeconds = 2_592_000,
        ),
    )

    @Test
    fun hashesAndVerifiesRefreshToken() {
        val token = service.generateToken()

        assertTrue(service.verifyToken(token.rawToken, token.tokenHash))
        assertFalse(service.verifyToken("different-token", token.tokenHash))
    }

    @Test
    fun rotatesActiveSession() {
        val now = Instant.ofEpochSecond(1_700_000_000)
        val token = service.generateToken()
        val session = service.createSession(
            userId = "user-id",
            tokenHash = token.tokenHash,
            sessionId = "old-session",
            now = now,
        )

        val rotation = service.rotateSession(
            currentSession = session,
            newSessionId = "new-session",
            now = now.plusSeconds(10),
        )

        assertEquals("old-session", rotation.revokedSession.id)
        assertEquals("new-session", rotation.revokedSession.replacedBySessionId)
        assertNotNull(rotation.revokedSession.revokedAtEpochSeconds)
        assertEquals("new-session", rotation.newSession.id)
        assertEquals("user-id", rotation.newSession.userId)
        assertNotEquals(session.refreshTokenHash, rotation.newSession.refreshTokenHash)
        assertTrue(service.verifyToken(rotation.newToken.rawToken, rotation.newSession.refreshTokenHash))
    }
}
