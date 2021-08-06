package ru.nstu.isma.intg.api

import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

/**
 * @author Maria
 * @since 14.03.2016
 */
class IntgResultPointFileWriter(pathname: String) : AutoCloseable {
    @Volatile
    private var opened = false

    @Volatile
    private var closed = false
    private val writer = BufferedWriter(FileWriter(File(pathname)))
    private val queue = LinkedBlockingQueue<IntgResultPoint>()
    private var workerThread: Thread? = null
    private var firstTime = true
    private fun open() {
        check(!opened) { "open() has already called" }
        opened = true
        workerThread = Thread(Worker(), "file-writer")
        workerThread!!.start()
    }

    override fun close() {
        closed = true
    }

    fun accept(intgResultPoint: IntgResultPoint) {
        /*if (!opened) {
            throw new IllegalStateException("open() call expected before accept()");
        }*/
        if (firstTime) {
            appendHeader(intgResultPoint)
            firstTime = false
            open()
        }
        try {
            queue.put(intgResultPoint)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }

    fun await() {
        try {
            workerThread!!.join()
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }

    private fun appendHeader(intgResultPoint: IntgResultPoint) {
        try {
            val header = buildCsvHeader(intgResultPoint)
            writer.append(header)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private inner class Worker : Runnable {
        override fun run() {
            // TODO: исправить
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            while (opened && !closed && !queue.isEmpty()) {
                try {
                    val intgResultPoint = queue.poll(100, TimeUnit.MICROSECONDS)
                    if (intgResultPoint != null) {
                        try {
                            writer.append(Companion.buildCsvString(intgResultPoint))
                        } catch (e: IOException) {
                            throw RuntimeException(e)
                        }
                    }
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }
            }
            try {
                writer.close()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    companion object {
        private const val COMMA = ","
        fun buildCsvString(resultPoint: IntgResultPoint): String {
            val builder = StringBuilder()
            builder.append(resultPoint.x)
            for (y in resultPoint.yForDe) {
                builder.append(COMMA).append(y)
            }
            val yForAeArray = resultPoint.rhs[DaeSystem.RHS_AE_PART_IDX]
            for (yForAe in yForAeArray) {
                builder.append(COMMA).append(yForAe)
            }
            val fArray = resultPoint.rhs[DaeSystem.RHS_DE_PART_IDX]
            for (f in fArray) {
                builder.append(COMMA).append(f)
            }
            builder.append(System.lineSeparator())
            return builder.toString()
        }

        fun buildCsvHeader(firstResultPoint: IntgResultPoint): String {
            val builder = StringBuilder()
            builder.append(1) // x count
            builder.append(COMMA).append(firstResultPoint.yForDe.size) // y count;
            builder.append(COMMA).append(firstResultPoint.rhs[DaeSystem.RHS_DE_PART_IDX].size) // rhsForDeCount;
            builder.append(COMMA).append(firstResultPoint.rhs[DaeSystem.RHS_AE_PART_IDX].size) // rhsForAeCount;
            builder.append(System.lineSeparator())
            return builder.toString()
        }
    }
}