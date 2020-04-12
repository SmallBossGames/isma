package ru.nstu.grin.simple.view.modal

import javafx.scene.Parent
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.simple.controller.PointsViewController
import ru.nstu.grin.simple.events.FileCheckedEvent
import ru.nstu.grin.simple.model.PointsViewModel
import tornadofx.*

class PointsView : View() {
    private val controller: PointsViewController by inject(params = params)
    private val model: PointsViewModel by inject(params = params)

    override val root: Parent = form {
        controller.readPoints()
        tableview(model.pointsProperty) {
            readonlyColumn("x", Point::x)
            readonlyColumn("y", Point::y)
        }
        button("Ok") {
            action {
                val event = FileCheckedEvent(
                    points = model.points
                )
                fire(event)
                close()
            }
        }
    }
}