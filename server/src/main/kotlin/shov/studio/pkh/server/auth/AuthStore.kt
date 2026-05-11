package shov.studio.pkh.server.auth

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import shov.studio.pkh.server.database.AuthSessionsTable
import shov.studio.pkh.server.database.UsersTable

class AuthStore(
    private val database: Database,
) {
    fun findUserByEmail(email: String): UserRecord? =
        transaction(database) {
            UsersTable
                .selectAll()
                .where { UsersTable.email eq email }
                .singleOrNull()
                ?.toUserRecord()
        }

    fun findUserById(id: String): UserRecord? =
        transaction(database) {
            UsersTable
                .selectAll()
                .where { UsersTable.id eq id }
                .singleOrNull()
                ?.toUserRecord()
        }

    fun createUser(user: UserRecord): UserRecord =
        transaction(database) {
            UsersTable.insert {
                it[id] = user.id
                it[email] = user.email
                it[passwordHash] = user.passwordHash
                it[createdAtEpochSeconds] = user.createdAtEpochSeconds
                it[updatedAtEpochSeconds] = user.updatedAtEpochSeconds
            }
            user
        }

    fun insertSession(session: AuthSessionRecord): AuthSessionRecord =
        transaction(database) {
            AuthSessionsTable.insert {
                it[id] = session.id
                it[userId] = session.userId
                it[refreshTokenHash] = session.refreshTokenHash
                it[expiresAtEpochSeconds] = session.expiresAtEpochSeconds
                it[revokedAtEpochSeconds] = session.revokedAtEpochSeconds
                it[replacedBySessionId] = session.replacedBySessionId
                it[createdAtEpochSeconds] = session.createdAtEpochSeconds
            }
            session
        }

    fun findSessionByRefreshTokenHash(refreshTokenHash: String): AuthSessionRecord? =
        transaction(database) {
            AuthSessionsTable
                .selectAll()
                .where { AuthSessionsTable.refreshTokenHash eq refreshTokenHash }
                .singleOrNull()
                ?.toAuthSessionRecord()
        }

    fun rotateSession(rotation: RefreshTokenRotation) {
        transaction(database) {
            AuthSessionsTable.update({ AuthSessionsTable.id eq rotation.revokedSession.id }) {
                it[revokedAtEpochSeconds] = rotation.revokedSession.revokedAtEpochSeconds
                it[replacedBySessionId] = rotation.revokedSession.replacedBySessionId
            }
            AuthSessionsTable.insert {
                it[id] = rotation.newSession.id
                it[userId] = rotation.newSession.userId
                it[refreshTokenHash] = rotation.newSession.refreshTokenHash
                it[expiresAtEpochSeconds] = rotation.newSession.expiresAtEpochSeconds
                it[revokedAtEpochSeconds] = rotation.newSession.revokedAtEpochSeconds
                it[replacedBySessionId] = rotation.newSession.replacedBySessionId
                it[createdAtEpochSeconds] = rotation.newSession.createdAtEpochSeconds
            }
        }
    }

    fun revokeSession(sessionId: String, revokedAtEpochSeconds: Long) {
        transaction(database) {
            AuthSessionsTable.update({ AuthSessionsTable.id eq sessionId }) {
                it[AuthSessionsTable.revokedAtEpochSeconds] = revokedAtEpochSeconds
            }
        }
    }

    private fun ResultRow.toUserRecord(): UserRecord =
        UserRecord(
            id = this[UsersTable.id],
            email = this[UsersTable.email],
            passwordHash = this[UsersTable.passwordHash],
            createdAtEpochSeconds = this[UsersTable.createdAtEpochSeconds],
            updatedAtEpochSeconds = this[UsersTable.updatedAtEpochSeconds],
        )

    private fun ResultRow.toAuthSessionRecord(): AuthSessionRecord =
        AuthSessionRecord(
            id = this[AuthSessionsTable.id],
            userId = this[AuthSessionsTable.userId],
            refreshTokenHash = this[AuthSessionsTable.refreshTokenHash],
            expiresAtEpochSeconds = this[AuthSessionsTable.expiresAtEpochSeconds],
            revokedAtEpochSeconds = this[AuthSessionsTable.revokedAtEpochSeconds],
            replacedBySessionId = this[AuthSessionsTable.replacedBySessionId],
            createdAtEpochSeconds = this[AuthSessionsTable.createdAtEpochSeconds],
        )
}
