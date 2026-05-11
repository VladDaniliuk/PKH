package shov.studio.pkh.data.auth.contract

sealed interface AuthState {
    data object Unknown : AuthState
    data object Unauthenticated : AuthState
    data class Authenticated(val tokens: AuthTokens) : AuthState
}
