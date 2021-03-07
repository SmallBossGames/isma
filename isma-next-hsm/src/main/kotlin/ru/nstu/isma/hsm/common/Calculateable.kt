package ru.nstu.isma.hsm.common

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
 * on 13.03.2015.
 */
interface Calculateable {
    fun prepareForCalculation(indexMapper: IndexMapper?)
    fun calculate(y: DoubleArray): DoubleArray?
}