package shov.studio.pkh.data.auth.api.repository

import shov.studio.pkh.data.auth.contract.AuthTokens

interface AuthRepository {
    suspend fun register(email: String, password: String): Result<AuthTokens>

    suspend fun login(email: String, password: String): Result<AuthTokens>

    suspend fun refreshSession(refreshToken: String): Result<AuthTokens>

    suspend fun logout(refreshToken: String): Result<Unit>
}