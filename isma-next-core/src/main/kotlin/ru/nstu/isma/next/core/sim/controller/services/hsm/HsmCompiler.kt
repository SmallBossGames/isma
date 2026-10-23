package ru.nstu.isma.next.core.sim.controller.services.hsm

import ru.nstu.isma.compiler.hsm.jvm.AnalyzedHybridSystemClassBuilder
import ru.nstu.isma.compiler.hsm.jvm.EquationIndexProvider
import ru.nstu.isma.compiler.hsm.jvm.SourceCodeCompiler
import ru.nstu.isma.compiler.hsm.jvm.calcmodel.HybridSystem
import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.next.core.sim.controller.models.HsmCompilationResult

class HsmCompiler : IHsmCompiler {
    private val sourceCodeCompiler: SourceCodeCompiler<HybridSystem> by lazy {
        SourceCodeCompiler()
    }

    override fun compile(hsm: HSM): HsmCompilationResult {
        val hsClassBuilder = AnalyzedHybridSystemClassBuilder(hsm, DEFAULT_PACKAGE_NAME, DEFAULT_CLASS_NAME)
        val hsSourceCode = hsClassBuilder.buildSourceCode()
        val hybridSystem = sourceCodeCompiler.compile(
            DEFAULT_PACKAGE_NAME, DEFAULT_CLASS_NAME, hsSourceCode
        )

        return HsmCompilationResult(hsClassBuilder.indexProvider, hybridSystem)
    }

    companion object {
        private const val DEFAULT_PACKAGE_NAME = "ru.nstu.isma.core.simulation.controller"
        private const val DEFAULT_CLASS_NAME = "AnalyzedHybridSystem"
    }
}