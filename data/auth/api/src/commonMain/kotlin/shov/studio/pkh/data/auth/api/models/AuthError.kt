package shov.studio.pkh.data.auth.api.models

sealed class AuthError(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    class InvalidEmail : AuthError("Invalid email.")
    class WeakPassword : AuthError("Password does not meet local requirements.")
    class InvalidCredentials : AuthError("Invalid credentials.")
    class EmailAlreadyRegistered : AuthError("Email is already registered.")
    class Unauthorized : AuthError("Unauthorized.")
    class SessionExpired : AuthError("Session expired.")
    class TokenStorageUnavailable : AuthError("Token storage is unavailable.")

    class Network(cause: Throwable? = null) : AuthError("Network error.", cause)
    class Unknown(cause: Throwable? = null) : AuthError("Unknown authentication error.", cause)
}