package shov.studio.pkh.data.auth.contract

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val createdAtEpochSeconds: Long,
)
