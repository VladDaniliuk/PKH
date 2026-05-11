package shov.studio.pkh.server.auth

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import shov.studio.pkh.data.auth.contract.AuthCredentialsRequest
import shov.studio.pkh.data.auth.contract.LogoutRequest
import shov.studio.pkh.data.auth.contract.RefreshSessionRequest

private const val BEARER_PREFIX = "Bearer "

@Serializable
data class ApiErrorResponse(
    val error: String,
)

fun Route.authRoutes(authService: AuthService) {
    route("/auth") {
        post("/register") {
            val request = call.receive<AuthCredentialsRequest>()
            when (val result = authService.register(request.email, request.password)) {
                is AuthServiceResult.Success -> call.respond(HttpStatusCode.Created, result.value)
                AuthServiceResult.InvalidInput -> call.respond(
                    HttpStatusCode.BadRequest,
                    ApiErrorResponse("invalid_request"),
                )
                AuthServiceResult.Conflict -> call.respond(
                    HttpStatusCode.Conflict,
                    ApiErrorResponse("account_unavailable"),
                )
                AuthServiceResult.Unauthorized -> call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiErrorResponse("invalid_credentials"),
                )
            }
        }

        post("/login") {
            val request = call.receive<AuthCredentialsRequest>()
            when (val result = authService.login(request.email, request.password)) {
                is AuthServiceResult.Success -> call.respond(HttpStatusCode.OK, result.value)
                AuthServiceResult.InvalidInput,
                AuthServiceResult.Conflict,
                AuthServiceResult.Unauthorized,
                -> call.respond(HttpStatusCode.Unauthorized, ApiErrorResponse("invalid_credentials"))
            }
        }

        post("/refresh") {
            val request = call.receive<RefreshSessionRequest>()
            when (val result = authService.refresh(request.refreshToken)) {
                is AuthServiceResult.Success -> call.respond(HttpStatusCode.OK, result.value)
                AuthServiceResult.InvalidInput,
                AuthServiceResult.Conflict,
                AuthServiceResult.Unauthorized,
                -> call.respond(HttpStatusCode.Unauthorized, ApiErrorResponse("unauthorized"))
            }
        }

        post("/logout") {
            val request = call.receive<LogoutRequest>()
            when (authService.logout(request.refreshToken)) {
                is AuthServiceResult.Success -> call.respond(HttpStatusCode.NoContent)
                AuthServiceResult.InvalidInput,
                AuthServiceResult.Conflict,
                AuthServiceResult.Unauthorized,
                -> call.respond(HttpStatusCode.NoContent)
            }
        }

        get("/me") {
            val accessToken = call.request.headers[HttpHeaders.Authorization]
                ?.takeIf { it.startsWith(BEARER_PREFIX) }
                ?.removePrefix(BEARER_PREFIX)
                ?.trim()

            if (accessToken.isNullOrBlank()) {
                call.respond(HttpStatusCode.Unauthorized, ApiErrorResponse("unauthorized"))
                return@get
            }

            when (val result = authService.currentUser(accessToken)) {
                is AuthServiceResult.Success -> call.respond(HttpStatusCode.OK, result.value)
                AuthServiceResult.InvalidInput,
                AuthServiceResult.Conflict,
                AuthServiceResult.Unauthorized,
                -> call.respond(HttpStatusCode.Unauthorized, ApiErrorResponse("unauthorized"))
            }
        }
    }
}
