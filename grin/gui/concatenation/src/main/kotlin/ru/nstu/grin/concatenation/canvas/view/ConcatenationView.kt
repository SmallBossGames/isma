package ru.nstu.grin.concatenation.canvas.view

import javafx.geometry.Insets
import javafx.scene.Parent
import ru.nstu.grin.common.events.ConcatenationClearCanvasEvent
import ru.nstu.grin.concatenation.canvas.events.LoadEvent
import ru.nstu.grin.concatenation.canvas.events.SaveEvent
import ru.nstu.grin.concatenation.canvas.model.InitCanvasData
import tornadofx.*

class ConcatenationView(fxScope: Scope, initData: InitCanvasData?) : View() {
    override val scope: Scope = fxScope

    override val root: Parent = borderpane {
        top {
            vbox {
                menubar {
                    menu("Файл") {
                        item("Сохранить как").action {
                            val file =
                                chooseFile(title = "File", filters = arrayOf(), mode = FileChooserMode.Save).first()
                            fire(SaveEvent(file))
                        }
                        item("Загрузить").action {
                            val file = chooseFile(
                                title = "File",
                                filters = arrayOf(),
                                mode = FileChooserMode.Single
                            ).first()
                            fire(LoadEvent(file))
                        }
                    }
                    menu("Полотно") {
                        item("Очистить все").action {
                            fire(ConcatenationClearCanvasEvent)
                        }
                    }
                    menu("Элементы") {
                        item("Открыть окно с элементами").action {
                            find<ElementsView>().openModal()
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
}
