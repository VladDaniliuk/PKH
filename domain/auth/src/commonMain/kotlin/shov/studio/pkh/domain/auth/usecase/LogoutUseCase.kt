package shov.studio.pkh.domain.auth.usecase

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided
import shov.studio.pkh.data.auth.api.TokenStorage
import shov.studio.pkh.data.auth.api.repository.AuthRepository
import shov.studio.pkh.data.auth.contract.AuthTokens

@Factory
class LogoutUseCase(
    @Provided private val authRepository: AuthRepository,
    @Provided private val tokenStorage: TokenStorage,
) {
    suspend operator fun invoke(tokens: AuthTokens?): Result<Unit> {
        if (tokens != null) {
            authRepository.logout(tokens.refreshToken).onFailure { return Result.failure(it) }
        }

        return tokenStorage.clearTokens()
    }
}
