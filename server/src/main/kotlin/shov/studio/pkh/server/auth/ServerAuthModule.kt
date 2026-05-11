package shov.studio.pkh.server.auth

import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single
import shov.studio.pkh.server.database.DatabaseConfig
import shov.studio.pkh.server.database.connectDatabase
import shov.studio.pkh.server.database.initializeAuthSchema

@Module
class ServerAuthModule {
    @Single
    fun database(@Provided databaseConfig: DatabaseConfig): Database =
        connectDatabase(databaseConfig).also(::initializeAuthSchema)

    @Single
    fun passwordHashingService(): PasswordHashingService = Argon2PasswordHashingService()

    @Single
    fun accessTokenService(@Provided accessTokenConfig: AccessTokenConfig): AccessTokenService =
        AccessTokenService(accessTokenConfig)

    @Single
    fun refreshTokenService(@Provided refreshTokenConfig: RefreshTokenConfig): RefreshTokenService =
        RefreshTokenService(refreshTokenConfig)

    @Single
    fun authStore(database: Database): AuthStore = AuthStore(database)

    @Single
    fun authService(
        authStore: AuthStore,
        passwordHashingService: PasswordHashingService,
        accessTokenService: AccessTokenService,
        @Provided accessTokenConfig: AccessTokenConfig,
        refreshTokenService: RefreshTokenService,
    ): AuthService =
        AuthService(
            store = authStore,
            passwordHashingService = passwordHashingService,
            accessTokenService = accessTokenService,
            accessTokenConfig = accessTokenConfig,
            refreshTokenService = refreshTokenService,
        )
}
