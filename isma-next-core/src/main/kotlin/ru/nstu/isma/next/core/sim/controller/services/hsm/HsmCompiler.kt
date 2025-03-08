package ru.nstu.isma.next.core.sim.controller.services.hsm

import ru.nstu.isma.compiler.hsm.jvm.AnalyzedHybridSystemClassBuilder
import ru.nstu.isma.compiler.hsm.jvm.EquationIndexProvider
import ru.nstu.isma.compiler.hsm.jvm.SourceCodeCompiler
import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.intg.api.calcmodel.HybridSystem
import ru.nstu.isma.next.core.sim.controller.models.HsmCompilationResult

class HsmCompiler : IHsmCompiler {
    override fun compile(hsm: HSM): HsmCompilationResult {
        val indexProvider = EquationIndexProvider(hsm)
        val hsClassBuilder = AnalyzedHybridSystemClassBuilder(hsm, indexProvider, DEFAULT_PACKAGE_NAME, DEFAULT_CLASS_NAME)
        val hsSourceCode = hsClassBuilder.buildSourceCode()
        val hybridSystem = SourceCodeCompiler<HybridSystem>().compile(
            DEFAULT_PACKAGE_NAME, DEFAULT_CLASS_NAME, hsSourceCode
        )
        val modelClassLoader = hybridSystem.javaClass.classLoader!!

        return HsmCompilationResult(indexProvider, hybridSystem, modelClassLoader)
    }

    companion object {
        private const val DEFAULT_PACKAGE_NAME = "ru.nstu.isma.core.simulation.controller"
        private const val DEFAULT_CLASS_NAME = "AnalyzedHybridSystem"
    }
}