package ru.nstu.grin.concatenation.view.modal

import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.controller.PointsViewController
import ru.nstu.grin.concatenation.events.FileCheckedEvent
import ru.nstu.grin.concatenation.model.PointsViewModel
import tornadofx.*

class PointsView : View() {
    private val controller: PointsViewController by inject(params = params)
    private val model: PointsViewModel by inject(params = params)

    override val root: Parent = form {
        controller.readPoints()
        tableview(model.pointsList) {
            items.first().forEachIndexed { index, list ->
                val name = if (index % 2 == 0) {
                    "x${index / 2}"
                } else {
                    "y${index / 2}"
                }
                column(name, String::class) {
                    setCellValueFactory { row ->
                        SimpleStringProperty(row.value[index])
                    }
                }
            }
        }
        button("Ok") {
            action {
                val points = model.pointsList.map {
                    it.zipWithNext { a, b ->
                        Point(a.toDouble(), b.toDouble())
                    }
                }
                fire(
                    FileCheckedEvent(points = points)
                )
                close()
            }
        }
    }
}