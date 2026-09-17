package ru.nstu.isma.domain.handlers.compileLisma

import ru.nstu.isma.domain.compiler.ICompiledModelStore
import ru.nstu.isma.domain.handlers.runSimulation.ILismaTranslator
import ru.nstu.isma.domain.handlers.runSimulation.TranslationException

class CompileLismaHandlerImpl(
    private val translator: ILismaTranslator,
    private val compiledModelStore: ICompiledModelStore,
) : ICompileLismaHandler {

    override fun handle(sourceCode: String): CompileLismaResult {
        val translationResult = translator.translate(sourceCode)

        return translationResult.fold(
            onSuccess = { hsm ->
                val modelId = compiledModelStore.create(hsm)
                CompileLismaResult(
                    compiledModelId = modelId,
                    errors = emptyList(),
                    warnings = emptyList(),
                )
            },
            onFailure = { error ->
                val compilationErrors = when (error) {
                    is TranslationException -> error.errors
                    else -> {
                        val errors = translator.validate(sourceCode)
                        if (errors.isNotEmpty()) errors
                        else listOf(CompilationError(-1, -1, error.message ?: "Unknown error"))
                    }
                }

                CompileLismaResult(
                    compiledModelId = "",
                    errors = compilationErrors,
                    warnings = emptyList(),
                )
            }
        )
    }
}
