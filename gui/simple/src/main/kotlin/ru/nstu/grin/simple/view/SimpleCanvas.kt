package ru.nstu.grin.simple.view

import javafx.collections.ListChangeListener
import javafx.scene.Parent
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.converters.model.ArrowConverter
import ru.nstu.grin.common.converters.model.DescriptionConverter
import ru.nstu.grin.common.events.SimpleArrowEvent
import ru.nstu.grin.common.events.SimpleDescriptionEvent
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.simple.converters.model.SimpleFunctionConverter
import ru.nstu.grin.simple.events.SimpleFunctionEvent
import ru.nstu.grin.simple.model.SimpleFunction
import ru.nstu.grin.simple.model.view.SimpleCanvasViewModel
import tornadofx.View
import tornadofx.canvas
import tornadofx.stackpane

class SimpleCanvas : View() {
    private val model: SimpleCanvasViewModel by inject()
    private lateinit var chainDrawer: SimpleChainDrawer

    override val root: Parent = stackpane {
        canvas(SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()) {
            chainDrawer = SimpleChainDrawer(this, model)
            model.arrowsProperty.addListener { _: ListChangeListener.Change<out Arrow> -> chainDrawer.draw() }
            model.descriptions.addListener { _: ListChangeListener.Change<out Description> -> chainDrawer.draw() }
            model.functions.addListener { _: ListChangeListener.Change<out SimpleFunction> -> chainDrawer.draw() }

            onScroll = ScrollableHandler(model, chainDrawer)
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
    }
}