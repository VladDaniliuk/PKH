package shov.studio.pkh.data.auth.api

import shov.studio.pkh.data.auth.contract.AuthTokens

interface TokenStorage {
    suspend fun readTokens(): Result<StoredAuthTokens>

    suspend fun saveTokens(tokens: AuthTokens): Result<Unit>

    suspend fun clearTokens(): Result<Unit>
}

sealed interface StoredAuthTokens {
    data object Empty : StoredAuthTokens
    data class Available(val tokens: AuthTokens) : StoredAuthTokens
}
