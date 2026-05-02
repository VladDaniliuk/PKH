package shov.studio.pkh.domain.auth.usecase

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided
import shov.studio.pkh.data.auth.api.models.AuthError
import shov.studio.pkh.data.auth.api.repository.AuthRepository
import shov.studio.pkh.data.auth.contract.AuthTokens
import shov.studio.pkh.domain.auth.validator.EmailValidator
import shov.studio.pkh.domain.auth.validator.PasswordValidator

@Factory
class LoginUseCase(
    @Provided private val authRepository: AuthRepository,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator,
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthTokens> {
        if (!emailValidator.isValid(email)) return Result.failure(AuthError.InvalidEmail())
        if (!passwordValidator.isValid(password)) return Result.failure(AuthError.WeakPassword())

        return authRepository.login(email.trim(), password)
    }
}
