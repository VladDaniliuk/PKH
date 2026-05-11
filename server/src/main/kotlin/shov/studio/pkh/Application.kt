package shov.studio.pkh

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module as koinModule
import org.koin.ktor.ext.getKoin
import org.koin.ktor.plugin.Koin
import shov.studio.pkh.server.auth.AccessTokenConfig
import shov.studio.pkh.server.auth.AuthService
import shov.studio.pkh.server.auth.RefreshTokenConfig
import shov.studio.pkh.server.auth.ServerAuthModule
import shov.studio.pkh.server.auth.authRoutes
import shov.studio.pkh.server.auth.module as generatedKoinModule
import shov.studio.pkh.server.database.DatabaseConfig

private const val SERVER_PORT = 8080
private const val ACCESS_TOKEN_TTL_SECONDS = 900L
private const val REFRESH_TOKEN_TTL_SECONDS = 2_592_000L

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(
    databaseConfig: DatabaseConfig = DatabaseConfig(),
    accessTokenConfig: AccessTokenConfig = defaultAccessTokenConfig(),
    refreshTokenConfig: RefreshTokenConfig = RefreshTokenConfig(ttlSeconds = REFRESH_TOKEN_TTL_SECONDS),
) {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            },
        )
    }

    install(Koin) {
        modules(
            koinModule {
                single { databaseConfig }
                single { accessTokenConfig }
                single { refreshTokenConfig }
            },
            ServerAuthModule().generatedKoinModule(),
        )
    }

    val authService = getKoin().get<AuthService>()

    routing {
        get("/") {
            call.respondText("Ktor: Text")
        }
        authRoutes(authService)
    }
}

private fun defaultAccessTokenConfig(): AccessTokenConfig =
    AccessTokenConfig(
        issuer = System.getenv("PKH_ACCESS_TOKEN_ISSUER") ?: "pkh-server",
        audience = System.getenv("PKH_ACCESS_TOKEN_AUDIENCE") ?: "pkh-client",
        secret = System.getenv("PKH_ACCESS_TOKEN_SECRET") ?: "pkh-dev-access-token-secret-change-me",
        ttlSeconds = ACCESS_TOKEN_TTL_SECONDS,
    )
