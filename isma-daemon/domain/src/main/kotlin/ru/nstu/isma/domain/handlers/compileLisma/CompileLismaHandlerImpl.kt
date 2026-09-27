package ru.nstu.isma.domain.handlers.compileLisma

import ru.nstu.isma.domain.compiler.CompiledModel
import ru.nstu.isma.domain.compiler.ICompiledModelStore
import ru.nstu.isma.domain.compiler.ILismaCompiler

class CompileLismaHandlerImpl(
    private val compiledModelStore: ICompiledModelStore,
    private val compiler: ILismaCompiler,
) : ICompileLismaHandler {

    override fun handle(sourceCode: String): CompileLismaResult {
        val outcome = compiler.compile(sourceCode)

        return if (outcome.isSuccess) {
            val artifact = outcome.artifact!!
            val modelId = compiledModelStore.create(CompiledModel(artifact))
            CompileLismaResult(
                compiledModelId = modelId,
                errors = emptyList(),
                warnings = emptyList(),
            )
        } else {
            CompileLismaResult(
                compiledModelId = "",
                errors = outcome.errors.ifEmpty {
                    listOf(CompilationError(-1, -1, "Compilation failed"))
                },
                warnings = emptyList(),
            )
        }
    }
}
