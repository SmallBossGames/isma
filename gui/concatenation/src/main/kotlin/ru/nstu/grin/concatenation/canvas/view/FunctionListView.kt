package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.canvas.controller.FunctionListViewController
import ru.nstu.grin.concatenation.canvas.model.FunctionListViewModel
import tornadofx.Fragment
import tornadofx.listview

class FunctionListView : Fragment() {
    private val model: FunctionListViewModel by inject()
    private val controller: FunctionListViewController = find {  }

    override val root: Parent = listview(model.functions) {

        cellFormat {
            text = it.name
        }
    }
}