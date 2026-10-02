package ru.nstu.isma.daemon.infrastructure.stores.integrationMethods

import ru.nstu.isma.daemon.infrastructure.simulation.RunnerCli
import ru.nstu.isma.domain.integration.IIntegrationMethodsStore

/**
 * Answers ListSimulationMethods by asking the batteries-included simulation-runner
 * CLI for the integration methods it bundles (`--list-methods`). The daemon holds
 * no solver jars of its own; the method list always reflects the runner.
 */
class IntegrationMethodsStore : IIntegrationMethodsStore {
    override fun getMethodNames(): List<String> = RunnerCli.listMethods()
}
