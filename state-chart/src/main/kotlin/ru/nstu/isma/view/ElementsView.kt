package ru.nstu.isma.view

import javafx.scene.Parent
import ru.nstu.isma.model.ElementType
import ru.nstu.isma.model.ElementsViewModel
import tornadofx.View
import tornadofx.listview
import tornadofx.onUserSelect

class ElementsView : View() {
    private val model: ElementsViewModel by inject()

    override val root: Parent = listview(model.elementsProperty) {
        cellFormat {
            text = when (it) {
                ElementType.STATE -> "состояние" // TODO Вынести в общие константы
            }
        }
        onUserSelect {
            model.selectedElement = it
        }
    }
}