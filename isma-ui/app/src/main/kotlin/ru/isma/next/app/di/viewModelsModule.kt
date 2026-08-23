package ru.isma.next.app.di

import org.koin.dsl.module
import org.koin.dsl.onClose
import ru.isma.next.app.viewmodels.EditorTabPaneViewModel
import ru.isma.next.app.viewmodels.ErrorListViewModel
import ru.isma.next.app.viewmodels.MainCommandsViewModel
import ru.isma.next.app.viewmodels.SimulationParametersViewModel
import ru.isma.next.app.viewmodels.TasksViewModel
import ru.isma.next.app.viewmodels.WindowViewModel

val viewModelsModule = module {
    single { SimulationParametersViewModel(get()) }
    single { EditorTabPaneViewModel(get(), get()) } onClose { it?.close() }
    single { TasksViewModel(get(), get()) } onClose { it?.close() }
    single { ErrorListViewModel(get()) } onClose { it?.close() }
    single { WindowViewModel(get(), get()) }
    single { MainCommandsViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
}
