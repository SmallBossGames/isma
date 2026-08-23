package ru.isma.next.app.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.isma.next.app.models.CompilationErrorItem

class ModelErrorService {
    private val errorsInternal = MutableSharedFlow<List<CompilationErrorItem>>(replay = 1)

    val errors: Flow<List<CompilationErrorItem>> = errorsInternal

    fun putErrorList(errors: Iterable<CompilationErrorItem>) {
        errorsInternal.tryEmit(errors.toList())
    }
}
