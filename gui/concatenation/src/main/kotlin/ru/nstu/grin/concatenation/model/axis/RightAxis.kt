package ru.nstu.grin.concatenation.model.axis

import javafx.scene.paint.Color
import ru.nstu.grin.concatenation.marks.MarksProvider
import ru.nstu.grin.concatenation.model.Direction

class RightAxis(
    zeroPoint: Double,
    minDelta: Double,
    marksProvider: MarksProvider,
    backGroundColor: Color,
    delimiterColor: Color
) : AbstractAxis(
    zeroPoint, minDelta, marksProvider, backGroundColor, delimiterColor
) {

    override fun getDirection(): Direction {
        return Direction.RIGHT
    }
}