package ru.nstu.grin.integration

import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.nstu.grin.common.draw.elements.ArrowDrawElement
import ru.nstu.grin.common.draw.elements.DescriptionDrawElement
import ru.nstu.grin.concatenation.axis.controller.AxisChangeFragmentController
import ru.nstu.grin.concatenation.axis.controller.AxisListViewController
import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.axis.model.AxisListViewModel
import ru.nstu.grin.concatenation.axis.model.LogarithmicFragmentModel
import ru.nstu.grin.concatenation.axis.service.AxisCanvasService
import ru.nstu.grin.concatenation.axis.view.*
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.controller.MatrixTransformer
import ru.nstu.grin.concatenation.canvas.handlers.DraggedHandler
import ru.nstu.grin.concatenation.canvas.handlers.PressedMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ReleaseMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ScalableScrollHandler
import ru.nstu.grin.concatenation.canvas.model.CanvasViewModel
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
import ru.nstu.grin.concatenation.file.options.view.FileOptionsView
import ru.nstu.grin.concatenation.function.controller.*
import ru.nstu.grin.concatenation.function.model.*
import ru.nstu.grin.concatenation.function.service.FunctionsOperationsService
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.function.view.*
import ru.nstu.grin.concatenation.koin.*
import ru.nstu.grin.concatenation.points.view.PointTooltipsDrawElement
import tornadofx.Scope
import tornadofx.find
import tornadofx.setInScope

val grinGuiModule = module {
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
        scopedOf(::ConcatenationViewModel)
        scopedOf(::CanvasViewModel)
        scopedOf(::ConcatenationCanvasController)
        scopedOf(::ConcatenationCanvasModel)

        scopedOf(::ConcatenationChainDrawer)
        scopedOf(::AxisDrawElement)
        scopedOf(::VerticalAxisDrawStrategy)
        scopedOf(::HorizontalAxisDrawStrategy)
        scopedOf(::HorizontalPixelMarksArrayBuilder)
        scopedOf(::HorizontalValueMarksArrayBuilder)
        scopedOf(::PointTooltipsDrawElement)
        scopedOf(::ConcatenationFunctionDrawElement)
        scopedOf(::MatrixTransformer)
        scopedOf(::CartesianCanvasContextMenuController)

        scoped { DescriptionDrawElement(get<ConcatenationCanvasModel>().descriptions) }
        scoped { SelectionDrawElement(get<ConcatenationCanvasModel>().selectionSettings) }
        scoped { ArrowDrawElement(get<ConcatenationCanvasModel>().arrows, 1.0) }

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
        scopedOf(::FunctionCanvasService)
        scopedOf(::FunctionsOperationsService)

        scopedOf(::ScalableScrollHandler)
        scopedOf(::DraggedHandler)
        scopedOf(::PressedMouseHandler)
        scopedOf(::ReleaseMouseHandler)

        scopedOf(::SpacesTransformationController)

        scoped{ FileFragmentController(lazy { get() }, lazy {  get() }) }

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

        factory {
            AddFunctionModalScope().apply {
                scope.linkTo(get<MainGrinScope>().scope)
            }
        }

        // Access to the TornadoFX world. Should be removed later.
        scoped { Scope() }

        scoped { find<FileModel>(get<Scope>()) }
        scoped { find<FileOptionsView>(get<Scope>()) }
    }

    scope<FunctionChangeModalScope> {
        scopedOf(::ChangeFunctionController)
        scopedOf(::ChangeFunctionFragment)
        scopedOf(::ChangeFunctionModel)
    }

    scope<AxisChangeModalScope> {
        scopedOf(::AxisChangeFragmentController)
        scopedOf(::AxisChangeFragment)
        scopedOf(::LogarithmicTypeFragment)
        scopedOf(::AxisChangeFragmentModel)
        scopedOf(::LogarithmicFragmentModel)
    }

    scope<FunctionCopyModalScope> {
        scopedOf(::CopyFunctionController)
        scopedOf(::CopyFunctionFragment)
        scopedOf(::CopyFunctionModel)
    }

    scope<DescriptionChangeModalScope> {
        scoped { ChangeDescriptionController(get()) }
        scoped { params -> ChangeDescriptionView(get(), get{ params }) }
        scoped { params -> ChangeDescriptionViewModel(params.getOrNull(), params.getOrNull()) }
    }

    scope<CartesianCopyModalScope> {
        scopedOf(::CopyCartesianController)
        scopedOf(::CopyCartesianFragment)
        scopedOf(::CopyCartesianModel)
    }

    scope<CartesianChangeModalScope> {
        scopedOf(::ChangeCartesianController)
        scopedOf(::ChangeCartesianFragment)
        scopedOf(::ChangeCartesianSpaceModel)
    }

    scope<AddFunctionModalScope> {
        scopedOf(::AddFunctionModalView)
        scopedOf(::AddFunctionController)
        scopedOf(::AddFunctionModel)

        scopedOf(::FileFunctionFragment)
        scopedOf(::FileFunctionModel)

        scopedOf(::AnalyticFunctionFragment)
        scopedOf(::AnalyticFunctionModel)

        scopedOf(::ManualFunctionFragment)
        scopedOf(::ManualFunctionModel)
    }
}

val grinIntegrationModule = module {
    single { GrinIntegrationFacade() }
}