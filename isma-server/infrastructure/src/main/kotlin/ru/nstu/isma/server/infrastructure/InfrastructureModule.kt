package ru.nstu.isma.server.infrastructure

import org.koin.dsl.module
import ru.nstu.isma.domain.integration.IIntegrationMethodsStore
import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.server.infrastructure.stores.integrationMethods.IntegrationMethodsStore
import ru.nstu.isma.server.infrastructure.stores.simulationSessions.SimulationSessionStore

val infrastructureModule = module {
    single<IIntegrationMethodsStore> { IntegrationMethodsStore() }
    single<ISimulationSessionStore> { SimulationSessionStore() }
}
