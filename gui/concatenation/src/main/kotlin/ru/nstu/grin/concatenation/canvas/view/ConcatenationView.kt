package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.common.events.ConcatenationClearCanvasEvent
import ru.nstu.grin.concatenation.canvas.events.LoadEvent
import ru.nstu.grin.concatenation.canvas.events.SaveEvent
import ru.nstu.grin.concatenation.canvas.model.ConcatenationViewModel
import ru.nstu.grin.concatenation.canvas.model.EditMode
import ru.nstu.grin.concatenation.canvas.model.InitCanvasData
import tornadofx.*

class ConcatenationView : View() {
    private val model: ConcatenationViewModel by inject()
    val initData: InitCanvasData? by param()

    override val root: Parent = vbox {
        menubar {
            menu("Файл") {
                item("Сохранить как").action {
                    val file = chooseFile("Файл", arrayOf(), FileChooserMode.Save).first()
                    fire(SaveEvent(file))
                }
                item("Загрузить").action {
                    val file = chooseFile("Файл", arrayOf(), FileChooserMode.Single).first()
                    fire(LoadEvent(file))
                }
            }
            menu("Канвас") {
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
        toolbar {
            button {
                val image = Image("view-tool.png")
                val imageView = ImageView(image)
                imageView.fitWidth = 20.0
                imageView.fitHeight = 20.0
                graphic = imageView
                tooltip = Tooltip("Просмотр")

                action {
                    model.currentEditMode = EditMode.VIEW
                }
            }
            button {
                val image = Image("scale-tool.png")
                val imageView = ImageView(image)
                imageView.fitHeight = 20.0
                imageView.fitWidth = 20.0
                graphic = imageView
                tooltip = Tooltip("Скалирование")

                action {
                    model.currentEditMode = EditMode.SCALE
                }
            }
            button {
                val image = Image("edit-tool.png")
                val imageView = ImageView(image)
                imageView.setFitHeight(20.0)
                imageView.setFitWidth(20.0)
                graphic = imageView
                tooltip = Tooltip("Редактирование")

                action {
                    model.currentEditMode = EditMode.EDIT
                }
            }
            button {
                val image = Image("window-tool.png")
                val imageView = ImageView(image)
                imageView.fitHeight = 20.0
                imageView.fitWidth = 20.0
                graphic = imageView
                tooltip = Tooltip("Открытие новых окон")

                action {
                    model.currentEditMode = EditMode.WINDOWED
                }
            }
        }
        val concatenationCanvas = find<ConcatenationCanvas>(
            mapOf(
                ConcatenationCanvas::initData to initData
            )
        ) { }
        add(concatenationCanvas)
    }
}