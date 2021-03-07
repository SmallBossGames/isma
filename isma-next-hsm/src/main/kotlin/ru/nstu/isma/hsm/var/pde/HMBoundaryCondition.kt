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
import ru.nstu.isma.hsm.exp.HMExpression
import java.util.LinkedHashMap
import java.util.HashSet
import java.util.LinkedHashSet
import ru.nstu.isma.print.MatrixPrint
import java.io.Serializable

/**
 * by
 * Bessonov Alex.
 * Date: 04.12.13 Time: 0:26
 */
class HMBoundaryCondition : Serializable {
    var equation: HMPartialDerivativeEquation? = null
    var derOrder = 0
    var spatialVar = HMSpatialVariable()
    var side: SideType? = null
    var value: HMExpression? = null
    var type: Type? = null

    enum class SideType : Serializable {
        LEFT, RIGHT, BOTH
    }

    enum class Type : Serializable {
        DIRICHLET, NEUMANN
    }
}