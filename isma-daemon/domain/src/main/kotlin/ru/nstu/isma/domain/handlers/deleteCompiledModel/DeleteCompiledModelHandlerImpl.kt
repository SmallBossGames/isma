package ru.nstu.isma.domain.handlers.deleteCompiledModel

import ru.nstu.isma.domain.compiler.ICompiledModelStore

class DeleteCompiledModelHandlerImpl(
    private val compiledModelStore: ICompiledModelStore,
) : IDeleteCompiledModelHandler {

    override fun handle(compiledModelId: String): Boolean {
        return compiledModelStore.delete(compiledModelId)
    }
}
