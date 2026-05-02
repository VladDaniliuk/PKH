package shov.studio.pkh.server.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.time.Instant
import java.util.Date

data class AccessTokenConfig(
    val issuer: String,
    val audience: String,
    val secret: String,
    val ttlSeconds: Long,
)

data class AccessTokenPrincipal(
    val userId: String,
    val expiresAtEpochSeconds: Long,
)

class AccessTokenService(
    private val config: AccessTokenConfig,
) {
    private val algorithm: Algorithm = Algorithm.HMAC256(config.secret)
    private val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(config.issuer)
        .withAudience(config.audience)
        .build()

    fun issueAccessToken(
        userId: String,
        issuedAt: Instant = Instant.now(),
    ): String {
        val expiresAt = issuedAt.plusSeconds(config.ttlSeconds)

        return JWT.create()
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .withSubject(userId)
            .withIssuedAt(Date.from(issuedAt))
            .withExpiresAt(Date.from(expiresAt))
            .sign(algorithm)
    }

    fun validateAccessToken(token: String): AccessTokenPrincipal? =
        try {
            val decodedToken = verifier.verify(token)
            val expiresAt = decodedToken.expiresAt?.toInstant()?.epochSecond ?: return null
            val userId = decodedToken.subject ?: return null
            AccessTokenPrincipal(
                userId = userId,
                expiresAtEpochSeconds = expiresAt,
            )
        } catch (_: JWTVerificationException) {
            null
        }
}
