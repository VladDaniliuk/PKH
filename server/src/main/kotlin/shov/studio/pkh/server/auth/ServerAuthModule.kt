package shov.studio.pkh.server.auth

import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Module
class ServerAuthModule {
    @Single(binds = [PasswordHashingService::class])
    fun passwordHashingService(): Argon2PasswordHashingService = Argon2PasswordHashingService()

    @Single
    fun accessTokenService(@Provided accessTokenConfig: AccessTokenConfig): AccessTokenService =
        AccessTokenService(accessTokenConfig)

    @Single
    fun refreshTokenService(@Provided refreshTokenConfig: RefreshTokenConfig): RefreshTokenService =
        RefreshTokenService(refreshTokenConfig)
}
