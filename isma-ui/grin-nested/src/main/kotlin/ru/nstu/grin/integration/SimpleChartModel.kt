package ru.nstu.grin.integration

data class PointModel(val x: Double, val y: Double){
    companion object {
        val ZERO = PointModel(0.0, 0.0)
    }
}

class FunctionModel(val name: String, val points: Iterable<PointModel>)