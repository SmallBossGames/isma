package ru.nstu.grin.integration

import org.koin.dsl.module
import ru.nstu.grin.concatenation.canvas.view.ConcatenationView
import ru.nstu.grin.concatenation.canvas.view.ElementsView
import ru.nstu.grin.concatenation.file.CanvasProjectLoader
import ru.nstu.grin.concatenation.function.controller.ChangeFunctionController
import ru.nstu.grin.concatenation.function.controller.FunctionListViewController
import ru.nstu.grin.concatenation.function.model.ChangeFunctionModel
import ru.nstu.grin.concatenation.function.model.FunctionListViewModel
import ru.nstu.grin.concatenation.function.view.ChangeFunctionFragment
import ru.nstu.grin.concatenation.function.view.FunctionListView
import ru.nstu.grin.concatenation.koin.FunctionChangeModalScope
import ru.nstu.grin.concatenation.koin.MainGrinScope

val grinModule = module {
    scope<MainGrinScope> {
        scoped { tornadofx.Scope() }
        scoped { CanvasProjectLoader(get()) }

        scoped { params -> ConcatenationView(get(), get(), get(), params.getOrNull()) }
        scoped { ElementsView(get(), get()) }

        scoped { FunctionListView(get(), get(), get()) }
        scoped { FunctionListViewController(get(), get(), get()) }
        scoped { FunctionListViewModel() }

        factory {
            FunctionChangeModalScope().apply {
                scope.linkTo(get<MainGrinScope>().scope)
            }
        }
    }

    scope<FunctionChangeModalScope> {
        scoped { ChangeFunctionController(get()) }
        scoped { params -> ChangeFunctionFragment(get(), get { params }, get()) }
        scoped { params -> ChangeFunctionModel(params.get()) }
    }

    single { GrinIntegrationFacade() }
}