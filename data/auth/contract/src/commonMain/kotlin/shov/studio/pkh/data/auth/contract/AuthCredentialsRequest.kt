package shov.studio.pkh.data.auth.contract

import kotlinx.serialization.Serializable

@Serializable
data class AuthCredentialsRequest(
    val email: String,
    val password: String,
)
