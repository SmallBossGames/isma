package ru.nstu.grin.concatenation.axis.view

import javafx.scene.Parent
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.axis.controller.AxisChangeFragmentController
import ru.nstu.grin.concatenation.axis.model.AxisChangeFragmentModel
import ru.nstu.grin.concatenation.axis.events.AxisQuery
import ru.nstu.grin.concatenation.axis.model.AxisMarkType
import ru.nstu.grin.concatenation.axis.model.LogarithmicTypeModel
import tornadofx.*
import java.util.*

class AxisChangeFragment : Fragment() {
    val axisId: UUID by param()
    private val model: AxisChangeFragmentModel by inject()
    private val controller: AxisChangeFragmentController by inject(params = params)
    private val logFragment = find<LogarithmicTypeFragment>(params = params)
    private val logFragmentModel: LogarithmicTypeModel by inject()

    override val root: Parent = form {
        println(controller.params)
        fieldset("Текст") {
            field("Расстояние между метками") {
                textfield(model.distanceBetweenMarksProperty) {
                    validator {
                        if (it?.toDoubleOrNull() == null || it?.toDoubleOrNull() ?: -1.0 < 0.0) {
                            error("Число должно быть плавающим 20,0 и больше нуля")
                        } else {
                            null
                        }
                    }
                }
            }
            field("Размер шрифта") {
                textfield(model.textSizeProperty) {
                    validator {
                        if (it?.toDoubleOrNull() == null || it?.toDoubleOrNull() ?: -1.0 < 0.0) {
                            error("Число должно быть плавающим 20,0 и больше нуля")
                        } else {
                            null
                        }
                    }
                }
            }
            field("Шрифт") {
                combobox(model.fontProperty, Font.getFamilies()).bind(model.fontProperty)
            }
            field("Цвет шрифта") {
                colorpicker().bind(model.fontColorProperty)
            }
            field("Минимум") {
                textfield(model.minProperty) {
                    validator {
                        if (it?.toDoubleOrNull() == null || it?.toDoubleOrNull() ?: -1.0 < 0.0) {
                            error("Число должно быть плавающим 20,0 и больше нуля")
                        } else {
                            null
                        }
                    }
                }
            }
            field("Максимум") {
                textfield(model.maxProperty) {
                    validator {
                        if (it?.toDoubleOrNull() == null || it?.toDoubleOrNull() ?: -1.0 < 0.0) {
                            error("Число должно быть плавающим 20,0 и больше нуля")
                        } else {
                            null
                        }
                    }
                }
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
                        currentStage?.height = 600.0
                    }
                }
            }
            when (model.axisMarkType) {
                AxisMarkType.LINEAR -> {
                    hide()
                }
                AxisMarkType.LOGARITHMIC -> {
                    show()
                }
            }
            tab(logFragment)

            tabMaxHeight = 0.0
            tabMinHeight = 0.0
            stylesheet {
                Stylesheet.tabHeaderArea {
                    visibility = FXVisibility.HIDDEN
                }
            }
        }
        button("Сохранить") {
            enableWhen {
                model.valid.and(logFragmentModel.valid)
            }
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