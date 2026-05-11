package shov.studio.pkh.data.auth.contract

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val user: User,
    val tokens: AuthTokens,
)
