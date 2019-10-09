package ru.nstu.grin

import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle
import javax.swing.JFrame

class Present : JFrame() {
    init {
        bounds = Rectangle(100, 100, 1200, 1000)
        isResizable = false
        defaultCloseOperation = EXIT_ON_CLOSE
    }

    private fun drawTree(g: Graphics, x1: Int, y1: Int, angle: Double, depth: Int) {
        if (depth == 0) return
        val x2 = x1 + (Math.cos(Math.toRadians(angle)) * depth.toDouble() * 10.0).toInt()
        val y2 = y1 + (Math.sin(Math.toRadians(angle)) * depth.toDouble() * 10.0).toInt()
        g.drawLine(x1, y1, x2, y2)
        drawTree(g, x2, y2, angle - 20, depth - 1)
        drawTree(g, x2, y2, angle + 20, depth - 1)
    }

    override fun paint(g: Graphics) {
        g.color = Color.BLACK
        drawTree(g, 600, 800, -90.0, 12)
    }
}

fun main(args: Array<String>) {
    Present().isVisible = true
}