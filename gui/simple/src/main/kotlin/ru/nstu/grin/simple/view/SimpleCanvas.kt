package ru.nstu.grin.simple.view

import javafx.collections.ListChangeListener
import javafx.scene.Parent
import javafx.scene.paint.Color
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.converters.model.ArrowConverter
import ru.nstu.grin.common.converters.model.DescriptionConverter
import ru.nstu.grin.common.events.SimpleArrowEvent
import ru.nstu.grin.common.events.SimpleDescriptionEvent
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.simple.converters.model.SimpleFunctionConverter
import ru.nstu.grin.simple.events.SimpleFunctionEvent
import ru.nstu.grin.simple.events.TurnLogarithmScaleEvent
import ru.nstu.grin.simple.model.LogarithmAxis
import ru.nstu.grin.simple.model.SimpleFunction
import ru.nstu.grin.simple.model.view.SimpleCanvasViewModel
import tornadofx.View
import tornadofx.canvas
import tornadofx.stackpane
import java.lang.Math.log10

class SimpleCanvas : View() {
    private val model: SimpleCanvasViewModel by inject()
    private lateinit var chainDrawer: SimpleChainDrawer

    override val root: Parent = stackpane {
        canvas(SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()) {
            chainDrawer = SimpleChainDrawer(this, model)
            model.arrowsProperty.addListener { _: ListChangeListener.Change<out Arrow> -> chainDrawer.draw() }
            model.descriptions.addListener { _: ListChangeListener.Change<out Description> -> chainDrawer.draw() }
            model.functions.addListener { _: ListChangeListener.Change<out SimpleFunction> -> chainDrawer.draw() }

            model.functions.add(
                SimpleFunction(
                    name = "test",
                    points = listOf(
                        Point(1.0, 0.0001),
                        Point(2.0, 0.001),
                        Point(3.0, 0.01),
                        Point(4.0, 0.1),
                        Point(6.0, 3.0),
                        Point(8.0, 5.0),
                        Point(20.0, 20.0)
                    ),
                    color = Color.BLACK,
                    step = 1
                )
            )

            onScroll = ScrollableHandler(model, chainDrawer)
            onMouseDragged = DraggedHandler(model, chainDrawer)
            onMousePressed = ShowPointHandler(model, chainDrawer)
            onMouseReleased = ReleaseMouseHandler(model, chainDrawer)
            chainDrawer.draw()
        }
    }

    init {
        subscribe<SimpleFunctionEvent> {
            val function = SimpleFunctionConverter.convert(it.function)
            model.functions.add(function)
        }
        subscribe<SimpleDescriptionEvent> {
            val description = DescriptionConverter.convert(it.description)
            model.descriptions.add(description)
        }
        subscribe<SimpleArrowEvent> {
            val arrow = ArrowConverter.convert(it.arrow)
            model.arrows.add(arrow)
        }
        subscribe<TurnLogarithmScaleEvent> {
            when (it.axis) {
                LogarithmAxis.X -> model.settings.isXLogarithmic = !model.settings.isXLogarithmic
                LogarithmAxis.Y -> model.settings.isYLogarithmic = !model.settings.isYLogarithmic
            }
            chainDrawer.draw()
        }
    }
}