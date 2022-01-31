package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import ru.nstu.grin.common.events.ConcatenationClearCanvasEvent
import ru.nstu.grin.concatenation.canvas.events.LoadEvent
import ru.nstu.grin.concatenation.canvas.events.SaveEvent
import ru.nstu.grin.concatenation.canvas.model.ConcatenationViewModel
import ru.nstu.grin.concatenation.canvas.model.InitCanvasData
import tornadofx.*

class ConcatenationView : View() {
    private val model: ConcatenationViewModel by inject()

    override val root: Parent = vbox {
        menubar {
            menu("Файл") {
                item("Сохранить как").action {
                    val file = chooseFile(title="File", filters = arrayOf(), mode = FileChooserMode.Save).first()
                    fire(SaveEvent(file))
                }
                item("Загрузить").action {
                    val file = chooseFile(title="File", filters = arrayOf(), mode = FileChooserMode.Single).first()
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

    fun addConcatenationCanvas(initCanvasData: InitCanvasData){
        val concatenationCanvas = find<ConcatenationCanvas>(
            mapOf(
                ConcatenationCanvas::initData to initCanvasData
            )
        ) { }
        root.add(concatenationCanvas)
    }
}