package ru.nstu.grin.model.drawable.axis

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import ru.nstu.grin.model.Drawable

abstract class AbstractAxis(
    private val startPoint: Double,
    private val minDelta: Double,
    private val deltaMarks: List<Double>,
    private val backGroundColor: Color,
    private val delimiterColor: Color
) : Drawable {

    override fun draw(context: GraphicsContext) {
        drawRectangle(context)
        drawMinorDelimiters(context)
        drawDeltaMarks(context)
    }

    protected abstract fun drawRectangle(graphicsContext: GraphicsContext)

    protected abstract fun drawMinorDelimiters(graphicsContext: GraphicsContext)

    protected abstract fun drawDeltaMarks(graphicsContext: GraphicsContext)


    internal companion object {
        const val WIDTH_AXIS = 50.0 // 100 px in default
        const val WIDTH_DELIMITER = 10.0
        const val TEXT_ALIGN = 30.0
        const val DEFAULT_DELTA_SPACE = 5
    }
}