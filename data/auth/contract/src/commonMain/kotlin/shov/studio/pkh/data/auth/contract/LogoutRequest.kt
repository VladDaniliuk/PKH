package shov.studio.pkh.data.auth.contract

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(
    val refreshToken: String,
)
