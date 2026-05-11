package shov.studio.pkh.data.auth.contract

import kotlinx.serialization.Serializable

@Serializable
data class RefreshSessionRequest(
    val refreshToken: String,
)
