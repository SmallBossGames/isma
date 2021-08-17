package ru.nstu.isma.next.core.sim.controller

import kotlinx.coroutines.coroutineScope
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.intg.api.calcmodel.HybridSystem
import ru.nstu.isma.next.core.sim.controller.contracts.IHsmCompiler
import ru.nstu.isma.next.core.sim.controller.gen.AnalyzedHybridSystemClassBuilder
import ru.nstu.isma.next.core.sim.controller.gen.EquationIndexProvider
import ru.nstu.isma.next.core.sim.controller.gen.SourceCodeCompiler
import ru.nstu.isma.next.core.sim.controller.models.HsmCompilationResult

class HsmCompiler : IHsmCompiler {
    override suspend fun compile(hsm: HSM): HsmCompilationResult = coroutineScope {
        val indexProvider = EquationIndexProvider(hsm)
        val hsClassBuilder = AnalyzedHybridSystemClassBuilder(hsm, indexProvider, DEFAULT_PACKAGE_NAME, DEFAULT_CLASS_NAME)
        val hsSourceCode = hsClassBuilder.buildSourceCode()
        val hybridSystem = SourceCodeCompiler<HybridSystem>().compile(
            DEFAULT_PACKAGE_NAME, DEFAULT_CLASS_NAME, hsSourceCode
        )
        val modelClassLoader = hybridSystem.javaClass.classLoader!!

        return@coroutineScope HsmCompilationResult(indexProvider, hybridSystem, modelClassLoader)
    }

    companion object {
        private const val DEFAULT_PACKAGE_NAME = "ru.nstu.isma.core.simulation.controller"
        private const val DEFAULT_CLASS_NAME = "AnalyzedHybridSystem"
    }
}