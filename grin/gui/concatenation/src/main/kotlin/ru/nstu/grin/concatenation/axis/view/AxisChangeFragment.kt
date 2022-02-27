package ru.nstu.grin.concatenation.axis.view

import javafx.scene.Parent
import javafx.scene.control.Tab
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.axis.controller.AxisChangeFragmentController
import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.axis.model.AxisMarkType
import tornadofx.*

class AxisChangeFragment(
    private val model: AxisChangeFragmentModel,
    private val controller: AxisChangeFragmentController,
    private val logFragment: LogarithmicTypeFragment
) : Fragment() {
    override val root: Parent = form {
        fieldset("Текст") {
            field("Расстояние между метками") {
                textfield(model.distanceBetweenMarksProperty)
            }
            field("Размер шрифта") {
                textfield(model.textSizeProperty)
            }
            field("Шрифт") {
                combobox(model.fontProperty, Font.getFamilies()).bind(model.fontProperty)
            }
            field("Цвет шрифта") {
                colorpicker().bind(model.fontColorProperty)
            }
            field("Минимум") {
                textfield(model.minProperty)
            }
            field("Максимум") {
                textfield(model.maxProperty)
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
                        text = when (it!!) {
                            AxisMarkType.LINEAR -> "Линейный"
                            AxisMarkType.LOGARITHMIC -> "Логарифмический"
                        }
                    }
                }
            }
        }
        tabpane {
            model.markTypeProperty.onChange {
                when (model.axisMarkType!!) {
                    AxisMarkType.LINEAR -> {
                        hide()
                    }
                    AxisMarkType.LOGARITHMIC -> {
                        show()
                        currentStage?.height = 600.0
                    }
                }
            }
            when (model.axisMarkType!!) {
                AxisMarkType.LINEAR -> {
                    hide()
                }
                AxisMarkType.LOGARITHMIC -> {
                    show()
                }
            }

            tabs.addAll(
                Tab(null, logFragment)
            )

            tabMaxHeight = 0.0
            tabMinHeight = 0.0
            stylesheet {
                Stylesheet.tabHeaderArea {
                    visibility = FXVisibility.HIDDEN
                }
            }
        }
        button("Сохранить") {
            action {
                controller.updateAxis()
                close()
            }
        }
    }
}