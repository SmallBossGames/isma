package ru.nstu.isma.hsm.`var`.pde

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
import ru.nstu.isma.hsm.`var`.HMConst
import ru.nstu.isma.hsm.`var`.HMVariable
import java.util.LinkedHashMap
import java.util.HashSet
import java.util.LinkedHashSet
import ru.nstu.isma.print.MatrixPrint
import java.io.Serializable

/**
 * Created by Bessonov Alex
 * on 23.03.2015.
 */
open class HMSpatialVariable : HMVariable(), Serializable {
    // Начальный край
    var valFrom: HMConst? = null

    // Конечный край
    var valTo: HMConst? = null
}