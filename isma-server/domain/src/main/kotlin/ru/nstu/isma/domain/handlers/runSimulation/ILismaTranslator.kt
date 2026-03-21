package ru.nstu.isma.domain.handlers.runSimulation

import ru.nstu.isma.compiler.hsm.core.HSM

interface ILismaTranslator {
    fun translate(sourceCode: String): Result<HSM>
}
