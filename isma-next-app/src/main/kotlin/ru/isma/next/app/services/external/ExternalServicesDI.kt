package ru.isma.next.app.services.external

import org.koin.dsl.module
import ru.isma.next.editor.text.services.TextEditorService
import ru.isma.next.editor.text.services.contracts.ITextEditorService
import ru.nstu.grin.integration.IntegrationController
import ru.nstu.isma.next.core.sim.controller.HsmCompiler
import ru.nstu.isma.next.core.sim.controller.HybridSystemSimulator
import ru.nstu.isma.next.core.sim.controller.SimulationCoreController
import ru.nstu.isma.next.core.sim.controller.contracts.IHsmCompiler
import ru.nstu.isma.next.core.sim.controller.contracts.IHybridSystemSimulator
import ru.nstu.isma.next.core.sim.controller.contracts.ISimulationCoreController
import ru.nstu.isma.next.integration.services.IntegrationMethodLibraryLoader

val externalServicesModule = module {
    single { IntegrationController() }
    single { IntegrationMethodLibraryLoader("methods/").load() }
    single<ITextEditorService> { TextEditorService() }
    single<ISimulationCoreController> { SimulationCoreController(get(), get()) }
    single<IHybridSystemSimulator> { HybridSystemSimulator() }
    single<IHsmCompiler> { HsmCompiler() }
}