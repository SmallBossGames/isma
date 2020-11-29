package ru.nstu.isma.next.core.common

import java.util.*
import java.util.stream.Collectors

/**
 * Created by Bessonov Alex
 * on 12.10.2014.
 */
class SimulationResult {
    protected var x = ArrayList<Double>()
    protected var y: MutableMap<String, ArrayList<Double>> = HashMap()
    fun addEquation(name: String) {
        if (y.containsKey(name)) return
        y[name] = ArrayList()
    }

    fun addX(x: Double) {
        this.x.add(x)
    }

    fun addY(name: String, y: Double) {
        assertEquationContains(name)
        this.y[name]!!.add(y)
    }

    fun getPoints(name: String): List<Point> {
        assertEquationContains(name)
        assertResultCorrect(name)
        val res: MutableList<Point> = LinkedList()
        for (i in x.indices) res.add(Point(x[i], y[name]!![i]))
        return res
    }

    fun equations(): List<String> {
        return y.keys.stream().collect(Collectors.toList())
    }

    private fun assertEquationContains(name: String) {
        if (!y.containsKey(name)) throw RuntimeException("SimulationResult don't contain equation $name")
    }

    private fun assertResultCorrect(name: String) {
        if (y[name]!!.size != x.size) throw RuntimeException("SimulationResult have different size for equation $name")
    }
}