package ru.nstu.grin.integration

import org.koin.dsl.module
import ru.nstu.grin.concatenation.axis.controller.AxisChangeFragmentController
import ru.nstu.grin.concatenation.axis.controller.AxisListViewController
import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.axis.model.AxisListViewModel
import ru.nstu.grin.concatenation.axis.model.LogarithmicFragmentModel
import ru.nstu.grin.concatenation.axis.view.AxisChangeFragment
import ru.nstu.grin.concatenation.axis.view.AxisListView
import ru.nstu.grin.concatenation.axis.view.LogarithmicTypeFragment
import ru.nstu.grin.concatenation.canvas.view.ConcatenationView
import ru.nstu.grin.concatenation.canvas.view.ElementsView
import ru.nstu.grin.concatenation.cartesian.controller.CartesianListViewController
import ru.nstu.grin.concatenation.cartesian.model.CartesianListViewModel
import ru.nstu.grin.concatenation.cartesian.view.CartesianListView
import ru.nstu.grin.concatenation.description.controller.DescriptionListViewController
import ru.nstu.grin.concatenation.description.model.DescriptionListViewModel
import ru.nstu.grin.concatenation.description.view.DescriptionListView
import ru.nstu.grin.concatenation.file.CanvasProjectLoader
import ru.nstu.grin.concatenation.function.controller.ChangeFunctionController
import ru.nstu.grin.concatenation.function.controller.CopyFunctionController
import ru.nstu.grin.concatenation.function.controller.FunctionListViewController
import ru.nstu.grin.concatenation.function.model.ChangeFunctionModel
import ru.nstu.grin.concatenation.function.model.CopyFunctionModel
import ru.nstu.grin.concatenation.function.model.FunctionListViewModel
import ru.nstu.grin.concatenation.function.view.ChangeFunctionFragment
import ru.nstu.grin.concatenation.function.view.CopyFunctionFragment
import ru.nstu.grin.concatenation.function.view.FunctionListView
import ru.nstu.grin.concatenation.koin.AxisChangeModalScope
import ru.nstu.grin.concatenation.koin.FunctionChangeModalScope
import ru.nstu.grin.concatenation.koin.FunctionCopyModalScope
import ru.nstu.grin.concatenation.koin.MainGrinScope

val grinModule = module {
    scope<MainGrinScope> {
        scoped { tornadofx.Scope() }
        scoped { CanvasProjectLoader(get()) }

        scoped { params -> ConcatenationView(get(), get(), get(), params.getOrNull()) }
        scoped { ElementsView(get(), get(), get(), get()) }

        scoped { FunctionListView(get(), get()) }
        scoped { FunctionListViewController(get(), get()) }
        scoped { FunctionListViewModel(get()) }

        scoped { AxisListView(get(), get()) }
        scoped { AxisListViewController(get()) }
        scoped { AxisListViewModel(get()) }

        scoped { CartesianListView(get(), get()) }
        scoped { CartesianListViewController(get()) }
        scoped { CartesianListViewModel(get()) }

        scoped { DescriptionListView(get(), get()) }
        scoped { DescriptionListViewController(get()) }
        scoped { DescriptionListViewModel(get()) }

        factory {
            FunctionChangeModalScope().apply {
                scope.linkTo(get<MainGrinScope>().scope)
            }
        }

        factory {
            AxisChangeModalScope().apply {
                scope.linkTo(get<MainGrinScope>().scope)
            }
        }

        factory {
            FunctionCopyModalScope().apply {
                scope.linkTo(get<MainGrinScope>().scope)
            }
        }
    }

    scope<FunctionChangeModalScope> {
        scoped { ChangeFunctionController(get()) }
        scoped { params -> ChangeFunctionFragment(get { params }, get()) }
        scoped { params -> ChangeFunctionModel(params.get()) }
    }

    scope<AxisChangeModalScope> {
        scoped { params -> AxisChangeFragmentController(get(), get { params }, get { params }) }
        scoped { params -> AxisChangeFragment(get { params }, get { params }, get { params }) }
        scoped { params -> LogarithmicTypeFragment(get { params }) }
        scoped { params -> AxisChangeFragmentModel(params.get()) }
        scoped { params -> LogarithmicFragmentModel(params.get()) }
    }

    scope<FunctionCopyModalScope> {
        scoped { CopyFunctionController(get()) }
        scoped { params -> CopyFunctionFragment(get { params }, get()) }
        scoped { params -> CopyFunctionModel(params.get()) }
    }

    single { GrinIntegrationFacade() }
}