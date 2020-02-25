package ru.nstu.grin.view.simple

import javafx.collections.ListChangeListener
import javafx.scene.Parent
import ru.nstu.grin.converters.model.ArrowConverter
import ru.nstu.grin.converters.model.DescriptionConverter
import ru.nstu.grin.converters.model.SimpleFunctionConverter
import ru.nstu.grin.events.common.SimpleArrowEvent
import ru.nstu.grin.events.common.SimpleDescriptionEvent
import ru.nstu.grin.events.simple.SimpleFunctionEvent
import ru.nstu.grin.model.drawable.Arrow
import ru.nstu.grin.model.drawable.Description
import ru.nstu.grin.model.drawable.SimpleFunction
import ru.nstu.grin.model.view.SimpleCanvasViewModel
import ru.nstu.grin.settings.SettingsProvider
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