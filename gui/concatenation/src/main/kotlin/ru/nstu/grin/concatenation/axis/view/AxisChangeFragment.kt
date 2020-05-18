package ru.nstu.grin.concatenation.axis.view

import javafx.scene.Parent
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.axis.controller.AxisChangeFragmentController
import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.axis.events.AxisQuery
import ru.nstu.grin.concatenation.axis.events.UpdateAxisEvent
import ru.nstu.grin.concatenation.axis.model.AxisMarkType
import tornadofx.*
import java.util.*

class AxisChangeFragment : Fragment() {
    val axisId: UUID by param()
    private val model: AxisChangeFragmentModel by inject()
    private val controller: AxisChangeFragmentController by inject(params = params)

    override val root: Parent = form {
        println(controller.params)
        fieldset("Текст") {
            field("Расстояние между метками") {
                textfield().bind(model.distanceBetweenMarksProperty)
            }
            field("Размер шрифта") {
                textfield().bind(model.textSizeProperty)
            }
            field("Шрифт") {
                combobox(model.fontProperty, Font.getFamilies()).bind(model.fontProperty)
            }
            field("Цвет шрифта") {
                colorpicker().bind(model.fontColorProperty)
            }
        }
        fieldset {
            field("Цвет оси") {
                colorpicker().bind(model.axisColorProperty)
            }
            field("Спрятать ось") {
                checkbox().bind(model.isHideProperty)
            }
            field("Режим масштабирования") {
                combobox(model.markTypeProperty, AxisMarkType.values().toList()) {
                    cellFormat {
                        text = when (it) {
                            AxisMarkType.LINEAR -> "Линейный"
                            AxisMarkType.LOGARITHMIC -> "Логарифмический"
                        }
                    }
                }
            }
        }
        tabpane {
            model.markTypeProperty.onChange {
                when (model.axisMarkType) {
                    AxisMarkType.LINEAR -> {
                        hide()
                    }
                    AxisMarkType.LOGARITHMIC -> {
                        show()
                        println("Show blya")
                        currentStage?.height = 470.0
                    }
                }
            }
            when (model.axisMarkType) {
                AxisMarkType.LINEAR -> {
                    hide()
                }
                AxisMarkType.LOGARITHMIC -> {
                    show()
                    println("Hehe")
                }
            }
            val logFragment = find<LogarithmicTypeFragment>(params = params)
            tab(logFragment)

            tabMaxHeight = 0.0
            tabMinHeight = 0.0
            stylesheet {
                Stylesheet.tabHeaderArea {
                    visibility = FXVisibility.HIDDEN
                }
            }
        }
        button("Ок") {
            action {
                controller.updateAxis()
                close()
            }
        }
    }

    init {
        fire(AxisQuery(axisId))
    }
}