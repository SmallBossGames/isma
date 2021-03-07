package ru.nstu.isma.hsm.error

import ru.nstu.isma.print.MatrixPrint.print
import java.lang.StringBuilder
import java.lang.RuntimeException
import java.util.HashMap
import java.util.stream.Collectors
import java.util.UUID
import javax.tools.JavaFileManager
import java.util.Arrays
import javax.tools.JavaFileObject
import java.text.SimpleDateFormat
import java.util.function.BiConsumer
import kotlin.jvm.JvmOverloads
import kotlin.Throws
import org.apache.commons.lang3.SerializationUtils
import java.util.LinkedHashMap
import java.util.HashSet
import java.util.LinkedHashSet
import ru.nstu.isma.print.MatrixPrint

/**
 * Created by Bessonov Alex
 * Date: 04.01.14
 * Time: 22:00
 */
class IsmaError {
    var row: Int? = null
        private set
    var col: Int? = null
        private set
    var type: Type? = null
    var msg: String

    constructor(row: Int, col: Int, msg: String) {
        this.row = row
        this.col = col
        this.msg = msg
    }

    constructor(msg: String) {
        this.msg = msg
    }

    fun setRow(row: Int) {
        this.row = row
    }

    fun setCol(col: Int) {
        this.col = col
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (type != null) {
            when (type) {
                Type.SYNTAX -> {
                    sb.append("[cинтаксическая ошибка ")
                    if (row != null && col != null) {
                        sb.append("в ")
                        sb.append(row)
                        sb.append(", ")
                        sb.append(col)
                        sb.append(" ")
                    }
                    sb.append("] ")
                }
                Type.SEM -> sb.append("[семантическая ошибка] ")
            }
        }
        sb.append(msg)
        return sb.toString()
    }

    enum class Type {
        SYNTAX, SEM
    }
}