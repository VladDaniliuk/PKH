package shov.studio.pkh.data.auth.contract

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresAtEpochSeconds: Long,
)

sealed interface AuthState {
    data object Unknown : AuthState
    data object Unauthenticated : AuthState
    data class Authenticated(val tokens: AuthTokens) : AuthState
}
