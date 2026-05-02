package shov.studio.pkh.server.auth

data class UserRecord(
    val id: String,
    val email: String,
    val passwordHash: String,
    val createdAtEpochSeconds: Long,
    val updatedAtEpochSeconds: Long,
)

data class AuthSessionRecord(
    val id: String,
    val userId: String,
    val refreshTokenHash: String,
    val expiresAtEpochSeconds: Long,
    val revokedAtEpochSeconds: Long?,
    val replacedBySessionId: String?,
    val createdAtEpochSeconds: Long,
) {
    val isActive: Boolean
        get() = revokedAtEpochSeconds == null && replacedBySessionId == null
}
