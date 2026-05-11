package shov.studio.pkh.server.database

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

data class DatabaseConfig(
    val url: String = "jdbc:h2:mem:pkh;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
    val driver: String = "org.h2.Driver",
    val user: String = "sa",
    val password: String = "",
)

fun connectDatabase(config: DatabaseConfig = DatabaseConfig()): Database =
    Database.connect(
        url = config.url,
        driver = config.driver,
        user = config.user,
        password = config.password,
    )

fun initializeAuthSchema(database: Database) {
    transaction(database) {
        SchemaUtils.create(UsersTable, AuthSessionsTable)
    }
}
