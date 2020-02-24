package ru.nstu.grin.view.simple

import javafx.scene.Parent
import ru.nstu.grin.converters.model.ArrowConverter
import ru.nstu.grin.converters.model.DescriptionConverter
import ru.nstu.grin.converters.model.SimpleFunctionConverter
import ru.nstu.grin.events.common.ArrowEvent
import ru.nstu.grin.events.common.DescriptionEvent
import ru.nstu.grin.events.common.SimpleArrowEvent
import ru.nstu.grin.events.common.SimpleDescriptionEvent
import ru.nstu.grin.events.simple.SimpleFunctionEvent
import ru.nstu.grin.model.SimpleType
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