package ru.isma.next.app.viewmodels

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.isma.next.app.models.CompilationErrorItem
import ru.isma.next.app.services.ModelErrorService

class ErrorListViewModel(
    private val modelErrorService: ModelErrorService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.JavaFx,
) : AutoCloseable {
    val errors: ObservableList<CompilationErrorItem> = FXCollections.observableArrayList()

    private val scope = CoroutineScope(dispatcher + SupervisorJob())

    init {
        scope.launch {
            modelErrorService.errors.collect { list ->
                errors.setAll(list)
            }
        }
    }

    override fun close() {
        scope.cancel()
    }
}
