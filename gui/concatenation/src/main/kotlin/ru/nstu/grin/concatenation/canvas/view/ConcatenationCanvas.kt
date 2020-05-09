package ru.nstu.grin.concatenation.canvas.view

import javafx.collections.ListChangeListener
import javafx.scene.Parent
import javafx.scene.layout.Priority
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.concatenation.canvas.model.CartesianSpace
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModelViewModel
import ru.nstu.grin.concatenation.canvas.handlers.DraggedHandler
import ru.nstu.grin.concatenation.canvas.handlers.ReleaseMouseHandler
import ru.nstu.grin.concatenation.canvas.handlers.ScalableScrollHandler
import ru.nstu.grin.concatenation.canvas.handlers.ShowPointHandler
import ru.nstu.grin.concatenation.canvas.model.CanvasModel
import tornadofx.*

class ConcatenationCanvas : View() {
    private val model: ConcatenationCanvasModelViewModel by inject()
    private val controller: ConcatenationCanvasController= find {  }
    private val canvasModel: CanvasModel by inject()
    private var chainDrawer: ConcatenationChainDrawer = find {  }
    private val scalableScrollHandler: ScalableScrollHandler by inject()
    private val draggedHandler: DraggedHandler by inject()
    private val showPointHandler: ShowPointHandler by inject()
    private val releaseMouseHandler: ReleaseMouseHandler by inject()


    override val root: Parent = stackpane {
        canvas(SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()) {
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS
            canvasModel.canvas = this

            model.arrowsProperty.addListener { _: ListChangeListener.Change<out Arrow> -> chainDrawer.draw() }
            model.cartesianSpaces.addListener { _: ListChangeListener.Change<out CartesianSpace> -> chainDrawer.draw() }
            model.descriptionsProperty.addListener { _: ListChangeListener.Change<out Description> -> chainDrawer.draw() }

            onScroll = scalableScrollHandler

            onMouseDragged = draggedHandler

            onMousePressed = showPointHandler

            onMouseReleased = releaseMouseHandler
        }
    }

    fun redraw() {
        chainDrawer.draw()
    }
}