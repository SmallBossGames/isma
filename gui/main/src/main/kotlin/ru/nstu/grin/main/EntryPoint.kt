package ru.nstu.grin.main

import javafx.stage.Stage
import jwave.Transform
import jwave.tools.MathToolKit
import jwave.transforms.AncientEgyptianDecomposition
import jwave.transforms.FastWaveletTransform
import jwave.transforms.wavelets.haar.Haar1
import ru.nstu.grin.concatenation.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.view.ConcatenationView
import ru.nstu.grin.simple.view.SimpleCanvasView
import tornadofx.App
import tornadofx.UIComponent
import tornadofx.launch
import tornadofx.reloadStylesheetsOnFocus
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.reflect.KClass

class MyApp : App() {
    override val primaryView: KClass<out UIComponent> = ConcatenationView::class

    init {
        reloadStylesheetsOnFocus()
    }

    override fun start(stage: Stage) {
        stage.isResizable = false
        super.start(stage)
    }
}

fun main(args: Array<String>) {
    launch<MyApp>(args = args)
}
//
//fun main() {
//    val transform = AncientEgyptianDecomposition(FastWaveletTransform(Haar1()))
//
//    val array = doubleArrayOf(
//        0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0,
//        0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0
//    )
//    val test = arrayOf(
//        doubleArrayOf(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0),
//        doubleArrayOf(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0)
//    )
//
//    println(egyptianTransform(array).size)

//    val pow = getPow(array.size)
//    2.0.pow(pow)
//    val transformed = array.toList().subList(0, 2.0.pow(pow).toInt())
//
//    println(getPow(array.size))
//    println(transformed.size)
//    val result = transform.forward(test)
//    println(result.toList())
//}
//
//fun getPow(size: Int): Int {
//    var temp = size
//    var pow = 0
//    while (temp != 0) {
//        temp = temp / 2
//        pow++
//    }
//    return pow - 1
//}
//
//fun egyptianTransform(array: DoubleArray): List<Double> {
//    val ancientEgyptianMultipliers = MathToolKit.decompose(array.size);
//
//    val result = mutableListOf<Double>()
//    var offSet = 0
//    for (m in 0 until ancientEgyptianMultipliers.size) {
//        val ancientEgyptianMultiplier = ancientEgyptianMultipliers[m];
//
//        val arrTimeSubLength = MathToolKit.scalb(1.0, ancientEgyptianMultiplier).toInt()
//
//        val arrTimeSub = DoubleArray(arrTimeSubLength)
//
//        for (i in 0 until arrTimeSub.size) {
//            arrTimeSub[i] = array[i + offSet]
//        }
//        result.addAll(arrTimeSub.toList())
//        offSet += arrTimeSub.size;
//    }
//
//    return result
//}

