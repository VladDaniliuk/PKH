package shov.studio.pkh.server.database

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table

object UsersTable : Table("users") {
    val id = varchar("id", 36)
    val email = varchar("email", 320).uniqueIndex()
    val passwordHash = text("password_hash")
    val createdAtEpochSeconds = long("created_at_epoch_seconds")
    val updatedAtEpochSeconds = long("updated_at_epoch_seconds")

    override val primaryKey = PrimaryKey(id)
}

object AuthSessionsTable : Table("auth_sessions") {
    val id = varchar("id", 36)
    val userId = varchar("user_id", 36).references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val refreshTokenHash = varchar("refresh_token_hash", 128).uniqueIndex()
    val expiresAtEpochSeconds = long("expires_at_epoch_seconds")
    val revokedAtEpochSeconds = long("revoked_at_epoch_seconds").nullable()
    val replacedBySessionId = varchar("replaced_by_session_id", 36).nullable()
    val createdAtEpochSeconds = long("created_at_epoch_seconds")

    override val primaryKey = PrimaryKey(id)
}
