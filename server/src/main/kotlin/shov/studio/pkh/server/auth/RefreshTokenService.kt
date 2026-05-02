package shov.studio.pkh.server.auth

import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Instant
import java.util.Base64
import java.util.UUID

data class RefreshTokenConfig(
    val tokenByteLength: Int = 32,
    val ttlSeconds: Long,
)

data class RefreshTokenPair(
    val rawToken: String,
    val tokenHash: String,
)

data class RefreshTokenRotation(
    val revokedSession: AuthSessionRecord,
    val newSession: AuthSessionRecord,
    val newToken: RefreshTokenPair,
)

class RefreshTokenService(
    private val config: RefreshTokenConfig,
    private val secureRandom: SecureRandom = SecureRandom(),
) {
    fun generateToken(): RefreshTokenPair {
        val tokenBytes = ByteArray(config.tokenByteLength)
        secureRandom.nextBytes(tokenBytes)
        val rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes)
        return RefreshTokenPair(
            rawToken = rawToken,
            tokenHash = hashToken(rawToken),
        )
    }

    fun hashToken(rawToken: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(rawToken.toByteArray(Charsets.UTF_8))
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
    }

    fun verifyToken(rawToken: String, tokenHash: String): Boolean =
        MessageDigest.isEqual(hashToken(rawToken).toByteArray(Charsets.UTF_8), tokenHash.toByteArray(Charsets.UTF_8))

    fun createSession(
        userId: String,
        tokenHash: String,
        sessionId: String = UUID.randomUUID().toString(),
        now: Instant = Instant.now(),
    ): AuthSessionRecord =
        AuthSessionRecord(
            id = sessionId,
            userId = userId,
            refreshTokenHash = tokenHash,
            expiresAtEpochSeconds = now.plusSeconds(config.ttlSeconds).epochSecond,
            revokedAtEpochSeconds = null,
            replacedBySessionId = null,
            createdAtEpochSeconds = now.epochSecond,
        )

    fun rotateSession(
        currentSession: AuthSessionRecord,
        newSessionId: String = UUID.randomUUID().toString(),
        now: Instant = Instant.now(),
    ): RefreshTokenRotation {
        require(currentSession.isActive) { "Cannot rotate inactive refresh session." }
        require(currentSession.expiresAtEpochSeconds > now.epochSecond) { "Cannot rotate expired refresh session." }

        val newToken = generateToken()
        val newSession = createSession(
            userId = currentSession.userId,
            tokenHash = newToken.tokenHash,
            sessionId = newSessionId,
            now = now,
        )
        val revokedSession = currentSession.copy(
            revokedAtEpochSeconds = now.epochSecond,
            replacedBySessionId = newSession.id,
        )

        return RefreshTokenRotation(
            revokedSession = revokedSession,
            newSession = newSession,
            newToken = newToken,
        )
    }
}
