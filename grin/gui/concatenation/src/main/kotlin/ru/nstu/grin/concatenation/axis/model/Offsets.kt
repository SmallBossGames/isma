package ru.nstu.grin.concatenation.axis.model

data class Offsets(
    var top: Double = 0.0,
    var right: Double = 0.0,
    var bottom: Double = 0.0,
    var left: Double = 0.0,
) {
    fun increase(height: Double, direction: Direction){
        when(direction){
            Direction.LEFT -> left += height
            Direction.RIGHT -> right += height
            Direction.TOP -> top += height
            Direction.BOTTOM -> bottom += height
        }
    }

    fun getByDirection(direction: Direction) = when(direction){
        Direction.LEFT -> left
        Direction.RIGHT -> right
        Direction.TOP -> top
        Direction.BOTTOM -> bottom
    }
}