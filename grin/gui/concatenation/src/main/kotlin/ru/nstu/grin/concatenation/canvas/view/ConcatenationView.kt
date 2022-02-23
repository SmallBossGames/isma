package ru.nstu.grin.concatenation.canvas.view

import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.stage.FileChooser
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.model.InitCanvasData
import ru.nstu.grin.concatenation.file.CanvasProjectLoader
import tornadofx.*

class ConcatenationView(
    override val scope: Scope,
    private val canvasProjectLoader: CanvasProjectLoader,
    initData: InitCanvasData?
) : View() {
    private val concatenationCanvasController: ConcatenationCanvasController by inject()

    override val root: Parent = borderpane {
        top {
            vbox {
                menubar {
                    menu("Файл") {
                        item("Сохранить как").action {
                            val file = FileChooser().run {
                                title = "Save Chart"
                                extensionFilters.addAll(grinChartDataFileFilters)
                                return@run showSaveDialog(currentWindow)
                            } ?: return@action

                            canvasProjectLoader.save(file)
                        }
                        item("Загрузить").action {
                            val file = FileChooser().run {
                                title = "Load Chart"
                                extensionFilters.addAll(grinChartDataFileFilters)
                                return@run showOpenDialog(currentWindow)
                            } ?: return@action

                            canvasProjectLoader.load(file)
                        }
                    }
                    menu("Полотно") {
                        item("Очистить все").action {
                            concatenationCanvasController.clearCanvas()
                        }
                    }
                }
                add<CanvasWorkPanel>()
            }
        }
        center {
            add(
                find<ConcatenationCanvas>(
                    mapOf(
                        ConcatenationCanvas::initData to initData
                    )
                )
            )
        }
        right {
            add<ElementsView>() {
                padding = Insets(0.0, 0.0, 0.0, 2.0)
            }
        }
    }

    private companion object {
        val grinChartDataFileFilters = arrayOf(
            FileChooser.ExtensionFilter("Grin Chart Data", "*.chart.json")
        )
    }
}
