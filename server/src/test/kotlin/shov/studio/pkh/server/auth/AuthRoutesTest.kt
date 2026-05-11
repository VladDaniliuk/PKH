package shov.studio.pkh.server.auth

import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import shov.studio.pkh.data.auth.contract.AuthCredentialsRequest
import shov.studio.pkh.data.auth.contract.AuthResponse
import shov.studio.pkh.data.auth.contract.AuthTokens
import shov.studio.pkh.data.auth.contract.LogoutRequest
import shov.studio.pkh.data.auth.contract.RefreshSessionRequest
import shov.studio.pkh.data.auth.contract.User
import shov.studio.pkh.module
import shov.studio.pkh.server.database.AuthSessionsTable
import shov.studio.pkh.server.database.DatabaseConfig
import shov.studio.pkh.server.database.connectDatabase
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class AuthRoutesTest {
    @Test
    fun registerReturnsUserAndTokensWithoutStoringRawRefreshToken() = testApplication {
        val databaseConfig = testDatabaseConfig()
        application { module(databaseConfig = databaseConfig, accessTokenConfig = testAccessTokenConfig()) }
        val client = createJsonClient()

        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(AuthCredentialsRequest(email = "User@Example.com", password = "password1"))
        }

        assertEquals(HttpStatusCode.Created, response.status)
        val body = response.body<AuthResponse>()
        assertEquals("user@example.com", body.user.email)
        assertTrue(body.tokens.accessToken.isNotBlank())
        assertTrue(body.tokens.refreshToken.isNotBlank())

        val database = connectDatabase(databaseConfig)
        val storedRefreshTokenHash = transaction(database) {
            AuthSessionsTable.selectAll().single()[AuthSessionsTable.refreshTokenHash]
        }
        assertNotEquals(body.tokens.refreshToken, storedRefreshTokenHash)
    }

    @Test
    fun loginSucceedsForValidCredentialsAndFailsGenericallyOtherwise() = testApplication {
        application { module(databaseConfig = testDatabaseConfig(), accessTokenConfig = testAccessTokenConfig()) }
        val client = createJsonClient()

        client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(AuthCredentialsRequest(email = "user@example.com", password = "password1"))
        }

        val loginResponse = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(AuthCredentialsRequest(email = "user@example.com", password = "password1"))
        }
        assertEquals(HttpStatusCode.OK, loginResponse.status)
        assertTrue(loginResponse.body<AuthResponse>().tokens.accessToken.isNotBlank())

        val unknownEmailResponse = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(AuthCredentialsRequest(email = "missing@example.com", password = "password1"))
        }
        assertEquals(HttpStatusCode.Unauthorized, unknownEmailResponse.status)
        assertEquals("invalid_credentials", unknownEmailResponse.body<ApiErrorResponse>().error)

        val wrongPasswordResponse = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(AuthCredentialsRequest(email = "user@example.com", password = "password2"))
        }
        assertEquals(HttpStatusCode.Unauthorized, wrongPasswordResponse.status)
        assertEquals("invalid_credentials", wrongPasswordResponse.body<ApiErrorResponse>().error)
    }

    @Test
    fun refreshRotatesTokensAndRejectsReuse() = testApplication {
        application { module(databaseConfig = testDatabaseConfig(), accessTokenConfig = testAccessTokenConfig()) }
        val client = createJsonClient()

        val registered = register(client)

        val refreshResponse = client.post("/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(RefreshSessionRequest(refreshToken = registered.tokens.refreshToken))
        }
        assertEquals(HttpStatusCode.OK, refreshResponse.status)
        val rotatedTokens = refreshResponse.body<AuthTokens>()
        assertTrue(rotatedTokens.accessToken.isNotBlank())
        assertNotEquals(registered.tokens.refreshToken, rotatedTokens.refreshToken)

        val reuseResponse = client.post("/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(RefreshSessionRequest(refreshToken = registered.tokens.refreshToken))
        }
        assertEquals(HttpStatusCode.Unauthorized, reuseResponse.status)
    }

    @Test
    fun logoutInvalidatesRefreshSession() = testApplication {
        application { module(databaseConfig = testDatabaseConfig(), accessTokenConfig = testAccessTokenConfig()) }
        val client = createJsonClient()

        val registered = register(client)

        val logoutResponse = client.post("/auth/logout") {
            contentType(ContentType.Application.Json)
            setBody(LogoutRequest(refreshToken = registered.tokens.refreshToken))
        }
        assertEquals(HttpStatusCode.NoContent, logoutResponse.status)

        val refreshResponse = client.post("/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(RefreshSessionRequest(refreshToken = registered.tokens.refreshToken))
        }
        assertEquals(HttpStatusCode.Unauthorized, refreshResponse.status)
    }

    @Test
    fun meReturnsUserForValidBearerTokenOnly() = testApplication {
        application { module(databaseConfig = testDatabaseConfig(), accessTokenConfig = testAccessTokenConfig()) }
        val client = createJsonClient()

        val registered = register(client)

        val meResponse = client.get("/auth/me") {
            bearerAuth(registered.tokens.accessToken)
        }
        assertEquals(HttpStatusCode.OK, meResponse.status)
        assertEquals(registered.user, meResponse.body<User>())

        val missingTokenResponse = client.get("/auth/me")
        assertEquals(HttpStatusCode.Unauthorized, missingTokenResponse.status)

        val invalidTokenResponse = client.get("/auth/me") {
            bearerAuth("not-a-jwt")
        }
        assertEquals(HttpStatusCode.Unauthorized, invalidTokenResponse.status)
    }

    private fun testDatabaseConfig(): DatabaseConfig =
        DatabaseConfig(url = "jdbc:h2:mem:${UUID.randomUUID()};DB_CLOSE_DELAY=-1;MODE=PostgreSQL")

    private fun testAccessTokenConfig(): AccessTokenConfig =
        AccessTokenConfig(
            issuer = "pkh-test",
            audience = "pkh-client-test",
            secret = "test-secret-that-is-long-enough-for-hmac",
            ttlSeconds = 900,
        )

    private fun ApplicationTestBuilder.createJsonClient() =
        createClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    },
                )
            }
        }

    private suspend fun register(client: io.ktor.client.HttpClient): AuthResponse {
        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(AuthCredentialsRequest(email = "user@example.com", password = "password1"))
        }
        assertEquals(HttpStatusCode.Created, response.status)
        return response.body()
    }
}
