package shov.studio.pkh.server.auth

import shov.studio.pkh.data.auth.contract.AuthResponse
import shov.studio.pkh.data.auth.contract.AuthTokens
import shov.studio.pkh.data.auth.contract.User
import java.time.Instant
import java.util.UUID

sealed interface AuthServiceResult<out T> {
    data class Success<T>(val value: T) : AuthServiceResult<T>
    data object InvalidInput : AuthServiceResult<Nothing>
    data object Conflict : AuthServiceResult<Nothing>
    data object Unauthorized : AuthServiceResult<Nothing>
}

class AuthService(
    private val store: AuthStore,
    private val passwordHashingService: PasswordHashingService,
    private val accessTokenService: AccessTokenService,
    private val accessTokenConfig: AccessTokenConfig,
    private val refreshTokenService: RefreshTokenService,
) {
    fun register(email: String, password: String, now: Instant = Instant.now()): AuthServiceResult<AuthResponse> {
        val normalizedEmail = normalizeEmail(email)
        if (!isValidEmail(normalizedEmail) || password.isBlank()) return AuthServiceResult.InvalidInput
        if (store.findUserByEmail(normalizedEmail) != null) return AuthServiceResult.Conflict

        val user = UserRecord(
            id = UUID.randomUUID().toString(),
            email = normalizedEmail,
            passwordHash = passwordHashingService.hashPassword(password),
            createdAtEpochSeconds = now.epochSecond,
            updatedAtEpochSeconds = now.epochSecond,
        )
        store.createUser(user)

        return AuthServiceResult.Success(
            AuthResponse(
                user = user.toContractUser(),
                tokens = createTokens(user.id, now),
            ),
        )
    }

    fun login(email: String, password: String, now: Instant = Instant.now()): AuthServiceResult<AuthResponse> {
        val normalizedEmail = normalizeEmail(email)
        if (!isValidEmail(normalizedEmail) || password.isBlank()) return AuthServiceResult.Unauthorized

        val user = store.findUserByEmail(normalizedEmail) ?: return AuthServiceResult.Unauthorized
        if (!passwordHashingService.verifyPassword(password, user.passwordHash)) {
            return AuthServiceResult.Unauthorized
        }

        return AuthServiceResult.Success(
            AuthResponse(
                user = user.toContractUser(),
                tokens = createTokens(user.id, now),
            ),
        )
    }

    fun refresh(refreshToken: String, now: Instant = Instant.now()): AuthServiceResult<AuthTokens> {
        if (refreshToken.isBlank()) return AuthServiceResult.Unauthorized

        val tokenHash = refreshTokenService.hashToken(refreshToken)
        val currentSession = store.findSessionByRefreshTokenHash(tokenHash) ?: return AuthServiceResult.Unauthorized
        if (!currentSession.isActive || currentSession.expiresAtEpochSeconds <= now.epochSecond) {
            return AuthServiceResult.Unauthorized
        }
        if (!refreshTokenService.verifyToken(refreshToken, currentSession.refreshTokenHash)) {
            return AuthServiceResult.Unauthorized
        }
        if (store.findUserById(currentSession.userId) == null) return AuthServiceResult.Unauthorized

        val rotation = refreshTokenService.rotateSession(currentSession = currentSession, now = now)
        store.rotateSession(rotation)

        return AuthServiceResult.Success(
            issueAuthTokens(
                userId = currentSession.userId,
                rawRefreshToken = rotation.newToken.rawToken,
                now = now,
            ),
        )
    }

    fun logout(refreshToken: String, now: Instant = Instant.now()): AuthServiceResult<Unit> {
        if (refreshToken.isBlank()) return AuthServiceResult.Success(Unit)

        val tokenHash = refreshTokenService.hashToken(refreshToken)
        val session = store.findSessionByRefreshTokenHash(tokenHash) ?: return AuthServiceResult.Success(Unit)
        if (session.isActive && session.expiresAtEpochSeconds > now.epochSecond) {
            store.revokeSession(session.id, now.epochSecond)
        }

        return AuthServiceResult.Success(Unit)
    }

    fun currentUser(accessToken: String): AuthServiceResult<User> {
        val principal = accessTokenService.validateAccessToken(accessToken) ?: return AuthServiceResult.Unauthorized
        val user = store.findUserById(principal.userId) ?: return AuthServiceResult.Unauthorized

        return AuthServiceResult.Success(user.toContractUser())
    }

    private fun createTokens(userId: String, now: Instant): AuthTokens {
        val refreshToken = refreshTokenService.generateToken()
        val session = refreshTokenService.createSession(
            userId = userId,
            tokenHash = refreshToken.tokenHash,
            now = now,
        )
        store.insertSession(session)

        return issueAuthTokens(
            userId = userId,
            rawRefreshToken = refreshToken.rawToken,
            now = now,
        )
    }

    private fun issueAuthTokens(userId: String, rawRefreshToken: String, now: Instant): AuthTokens =
        AuthTokens(
            accessToken = accessTokenService.issueAccessToken(userId = userId, issuedAt = now),
            refreshToken = rawRefreshToken,
            accessTokenExpiresAtEpochSeconds = now.plusSeconds(accessTokenConfig.ttlSeconds).epochSecond,
        )

    private fun normalizeEmail(email: String): String = email.trim().lowercase()

    private fun isValidEmail(email: String): Boolean =
        email.length in 3..320 && email.contains("@") && !email.contains(" ")

    private fun UserRecord.toContractUser(): User =
        User(
            id = id,
            email = email,
            createdAtEpochSeconds = createdAtEpochSeconds,
        )
}
