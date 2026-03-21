package ru.nstu.isma.server.infrastructure

import org.koin.dsl.module
import ru.nstu.isma.domain.integration.IIntegrationMethodsStore
import ru.nstu.isma.server.infrastructure.integration.methods.IntegrationMethodsStore

val infrastructureModule = module {
    single<IIntegrationMethodsStore> { IntegrationMethodsStore() }
}
