package ru.nstu.isma.domain.handlers.deleteCompiledModel

interface IDeleteCompiledModelHandler {
    fun handle(compiledModelId: String): Boolean
}
