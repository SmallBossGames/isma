package ru.nstu.isma.intg.api

import kotlinx.coroutines.flow.Flow
import ru.nstu.isma.intg.api.IntgResultPointProvider
import ru.nstu.isma.intg.api.IntgResultPoint
import java.lang.IllegalStateException
import ru.nstu.isma.intg.api.IntgResultPointFileReader
import java.lang.RuntimeException
import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import java.lang.AutoCloseable
import java.util.concurrent.BlockingQueue
import java.lang.InterruptedException
import java.lang.StringBuilder
import ru.nstu.isma.intg.api.IntgResultPointFileWriter
import java.io.*
import java.lang.Runnable
import java.util.concurrent.TimeUnit
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.function.Consumer

class IntgResultPointFileReader : IntgResultPointProvider {
    private var inputStream: InputStream? = null
    private var xCount: Int? = null
    private var yForDeCount: Int? = null
    private var rhsForDeCount: Int? = null
    private var rhsForAeCount: Int? = null
    fun init(inputStream: InputStream?) {
        requireNotNull(inputStream) { "inputStream can't be null" }
        this.inputStream = inputStream
    }

    override val results: Flow<IntgResultPoint> get() = TODO("Not yet implemented")

    fun read(handler: Consumer<List<IntgResultPoint>>) {
        try {
            InputStreamReader(inputStream!!).use { inputStreamReader ->
                BufferedReader(inputStreamReader).use { bufferedReader ->

                    // Считываем информацию о данных из первой строчки в файле
                    val header = bufferedReader.readLine()
                    initFromHeader(header)

                    // Считываем результаты
                    val resultPoints: MutableList<IntgResultPoint> = LinkedList()
                    var pointString: String
                    while (bufferedReader.readLine().also { pointString = it } != null) {
                        val resultPoint = buildIntgResultPoint(pointString)
                        resultPoints.add(resultPoint)
                    }
                    handler.accept(resultPoints)
                }
            }
        } catch (ex: IOException) {
            println(ex.message)
        }
    }

    private fun initFromHeader(header: String) {
        val values = header.split(COMMA).toTypedArray()
        if (values.size != 4) {
            throw RuntimeException("Header expected to be size of 4: x count, yForDe count, rhsForDe count, rhsForAe count")
        }
        xCount = Integer.valueOf(values[0])
        yForDeCount = Integer.valueOf(values[1])
        rhsForDeCount = Integer.valueOf(values[2])
        rhsForAeCount = Integer.valueOf(values[3])
    }

    private fun buildIntgResultPoint(pointString: String): IntgResultPoint {
        val values = pointString.split(COMMA).toTypedArray()
        val expectedValueCount = xCount!! + yForDeCount!! + rhsForDeCount!! + rhsForAeCount!!
        val actualValueCount = values.size
        if (actualValueCount != expectedValueCount) {
            throw RuntimeException("Expected value count per string is $expectedValueCount but was $actualValueCount")
        }
        val x = java.lang.Double.valueOf(values[0])
        val yForDe = DoubleArray(yForDeCount!!)
        val rhsForDe = DoubleArray(rhsForDeCount!!)
        val rhsForAe = DoubleArray(rhsForAeCount!!)
        var valueIndex = 1
        for (i in 0 until yForDeCount!!) {
            yForDe[i] = java.lang.Double.valueOf(values[valueIndex])
            valueIndex++
        }
        for (i in 0 until rhsForDeCount!!) {
            rhsForDe[i] = java.lang.Double.valueOf(values[valueIndex])
            valueIndex++
        }
        for (i in 0 until rhsForAeCount!!) {
            rhsForAe[i] = java.lang.Double.valueOf(values[valueIndex])
            valueIndex++
        }
        val rhs = Array(DaeSystem.RHS_PART_COUNT) { doubleArrayOf() }
        rhs[DaeSystem.RHS_DE_PART_IDX] = rhsForDe
        rhs[DaeSystem.RHS_AE_PART_IDX] = rhsForAe
        return IntgResultPoint(x, yForDe, rhs)
    }

    companion object {
        private const val COMMA = ","
    }
}