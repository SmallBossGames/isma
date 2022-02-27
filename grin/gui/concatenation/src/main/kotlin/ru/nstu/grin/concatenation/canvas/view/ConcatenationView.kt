package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import ru.nstu.grin.concatenation.canvas.controller.ConcatenationCanvasController
import ru.nstu.grin.concatenation.canvas.model.InitCanvasData
import ru.nstu.grin.concatenation.file.CanvasProjectLoader
import tornadofx.*

class ConcatenationView(
    private val scope: Scope,
    private val canvasProjectLoader: CanvasProjectLoader,
    private val elementsView: ElementsView,
    private val concatenationCanvasController: ConcatenationCanvasController,
    private val canvasWorkPanel: CanvasWorkPanel,
    initData: InitCanvasData?
) : BorderPane() {
    init {
        top {
            vbox {
                menubar {
                    menu("Файл") {
                        item("Сохранить как").action {
                            val file = FileChooser().run {
                                title = "Save Chart"
                                extensionFilters.addAll(grinChartDataFileFilters)
                                return@run showSaveDialog(scene.window)
                            } ?: return@action

                            canvasProjectLoader.save(file)
                        }
                        item("Загрузить").action {
                            val file = FileChooser().run {
                                title = "Load Chart"
                                extensionFilters.addAll(grinChartDataFileFilters)
                                return@run showOpenDialog(scene.window)
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
                add(canvasWorkPanel.root)
            }
        }
        center {
            add(
                find<ConcatenationCanvas>(scope, mapOf(ConcatenationCanvas::initData to initData)).root
            )
        }
        right {
            add(elementsView)
        }
    }

    private companion object {
        val grinChartDataFileFilters = arrayOf(
            FileChooser.ExtensionFilter("Grin Chart Data", "*.chart.json")
        )
    }
}
