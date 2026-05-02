package shov.studio.pkh

import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import shov.studio.pkh.domain.auth.AuthDomainModule

@KoinApplication
@Module(includes = [AuthDomainModule::class])
class PkhSharedApplication
