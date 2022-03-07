package ru.nstu.grin.integration

import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.nstu.grin.concatenation.axis.controller.AxisChangeFragmentController
import ru.nstu.grin.concatenation.axis.controller.AxisListViewController
import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.axis.model.AxisListViewModel
import ru.nstu.grin.concatenation.axis.model.LogarithmicFragmentModel
import ru.nstu.grin.concatenation.axis.service.AxisCanvasService
import ru.nstu.grin.concatenation.axis.view.AxisChangeFragment
import ru.nstu.grin.concatenation.axis.view.AxisDrawElement
import ru.nstu.grin.concatenation.axis.view.AxisListView
import ru.nstu.grin.concatenation.axis.view.LogarithmicTypeFragment
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformerController
import ru.nstu.grin.concatenation.canvas.handlers.DraggedHandler
import ru.nstu.grin.concatenation.canvas.handlers.PressedMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ReleaseMouseHandler
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationViewModel
import ru.nstu.grin.concatenation.canvas.model.InitCanvasData
import ru.nstu.grin.concatenation.canvas.view.*
import ru.nstu.grin.concatenation.cartesian.controller.CartesianListViewController
import ru.nstu.grin.concatenation.cartesian.controller.ChangeCartesianController
import ru.nstu.grin.concatenation.cartesian.controller.CopyCartesianController
import ru.nstu.grin.concatenation.cartesian.model.CartesianListViewModel
import ru.nstu.grin.concatenation.cartesian.model.ChangeCartesianSpaceModel
import ru.nstu.grin.concatenation.cartesian.model.CopyCartesianModel
import ru.nstu.grin.concatenation.cartesian.service.CartesianCanvasService
import ru.nstu.grin.concatenation.cartesian.view.CartesianListView
import ru.nstu.grin.concatenation.cartesian.view.ChangeCartesianFragment
import ru.nstu.grin.concatenation.cartesian.view.CopyCartesianFragment
import ru.nstu.grin.concatenation.description.controller.ChangeDescriptionController
import ru.nstu.grin.concatenation.description.controller.DescriptionListViewController
import ru.nstu.grin.concatenation.description.model.ChangeDescriptionViewModel
import ru.nstu.grin.concatenation.description.model.DescriptionListViewModel
import ru.nstu.grin.concatenation.description.service.DescriptionCanvasService
import ru.nstu.grin.concatenation.description.view.ChangeDescriptionView
import ru.nstu.grin.concatenation.description.view.DescriptionListView
import ru.nstu.grin.concatenation.file.CanvasProjectLoader
import ru.nstu.grin.concatenation.function.controller.ChangeFunctionController
import ru.nstu.grin.concatenation.function.controller.CopyFunctionController
import ru.nstu.grin.concatenation.function.controller.FunctionListViewController
import ru.nstu.grin.concatenation.function.model.ChangeFunctionModel
import ru.nstu.grin.concatenation.function.model.CopyFunctionModel
import ru.nstu.grin.concatenation.function.model.FunctionListViewModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.canvas.handlers.ScalableScrollHandler
import ru.nstu.grin.concatenation.canvas.model.CanvasViewModel
import ru.nstu.grin.concatenation.function.view.*
import ru.nstu.grin.concatenation.koin.*
import ru.nstu.grin.concatenation.points.view.PointTooltipsDrawElement
import tornadofx.Scope
import tornadofx.find
import tornadofx.setInScope

val grinModule = module {
    scope<MainGrinScope> {
        scoped { params ->
            // Access from the TornadoFx world. Should be removed later.
            setInScope(MainGrinScopeWrapper(get()), get())

            val initData = params.getOrNull<InitCanvasData>()

            if(initData!=null){
                get<ConcatenationCanvasController>().replaceAll(
                    initData.cartesianSpaces,
                    initData.arrows,
                    initData.descriptions
                )
            }

            ConcatenationView(get(), get(), get(), get())
        }

        scopedOf(::CanvasProjectLoader)

        scopedOf(::CanvasMenuBar)
        scopedOf(::CanvasToolBar)
        scopedOf(::ElementsView)

        scopedOf(::ConcatenationCanvas)
        scopedOf(::ConcatenationChainDrawer)

        scopedOf(::FunctionListView)
        scopedOf(::FunctionListViewController)
        scopedOf(::FunctionListViewModel)

        scopedOf(::AxisListView)
        scopedOf(::AxisListViewController)
        scopedOf(::AxisListViewModel)

        scopedOf(::CartesianListView)
        scopedOf(::CartesianListViewController)
        scopedOf(::CartesianListViewModel)

        scopedOf(::DescriptionListView)
        scopedOf(::DescriptionListViewController)
        scopedOf(::DescriptionListViewModel)

        scopedOf(::ChartToolBar)
        scopedOf(::ModesToolBar)
        scopedOf(::MathToolBar)
        scopedOf(::TransformToolBar)

        scopedOf(::CartesianCanvasService)
        scopedOf(::DescriptionCanvasService)
        scopedOf(::AxisCanvasService)

        scopedOf(::ScalableScrollHandler)
        scopedOf(::DraggedHandler)
        scopedOf(::PressedMouseHandler)
        scopedOf(::ReleaseMouseHandler)

        scopedOf(::SpacesTransformationController)

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

        factory {
            DescriptionChangeModalScope().apply {
                scope.linkTo(get<MainGrinScope>().scope)
            }
        }

        factory {
            CartesianCopyModalScope().apply {
                scope.linkTo(get<MainGrinScope>().scope)
            }
        }

        factory {
            CartesianChangeModalScope().apply {
                scope.linkTo(get<MainGrinScope>().scope)
            }
        }

        // Access to the TornadoFX world. Should be removed later.
        scoped { Scope() }

        scoped { find<FunctionCanvasService>(get<Scope>()) }

        scoped { find<ConcatenationCanvasModel>(get<Scope>()) }
        scoped { find<ConcatenationCanvasController>(get<Scope>()) }

        scoped { find<ConcatenationViewModel>(get<Scope>()) }

        scoped { find<CanvasViewModel>(get<Scope>()) }
        scoped { find<MatrixTransformerController>(get<Scope>()) }

        scoped { find<ConcatenationFunctionDrawElement>(get<Scope>()) }
        scoped { find<PointTooltipsDrawElement>(get<Scope>()) }

        scoped { find<AxisDrawElement>(get<Scope>()) }
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

    scope<DescriptionChangeModalScope> {
        scoped { ChangeDescriptionController(get()) }
        scoped { params -> ChangeDescriptionView(get(), get{ params }) }
        scoped { params -> ChangeDescriptionViewModel(params.getOrNull(), params.getOrNull()) }
    }

    scope<CartesianCopyModalScope> {
        scoped { CopyCartesianController(get()) }
        scoped { params -> CopyCartesianFragment(get(), get { params }) }
        scoped { params -> CopyCartesianModel(params.get()) }
    }

    scope<CartesianChangeModalScope> {
        scoped { ChangeCartesianController(get()) }
        scoped { params -> ChangeCartesianFragment(get { params }, get()) }
        scoped { params -> ChangeCartesianSpaceModel(params.get()) }
    }

    single { GrinIntegrationFacade() }
}