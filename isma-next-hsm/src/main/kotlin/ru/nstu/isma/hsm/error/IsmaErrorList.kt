package ru.nstu.isma.hsm.error

import ru.nstu.isma.print.MatrixPrint.print
import java.lang.StringBuilder
import java.lang.RuntimeException
import java.util.stream.Collectors
import javax.tools.JavaFileManager
import javax.tools.JavaFileObject
import java.text.SimpleDateFormat
import java.util.function.BiConsumer
import kotlin.jvm.JvmOverloads
import kotlin.Throws
import org.apache.commons.lang3.SerializationUtils
import ru.nstu.isma.print.MatrixPrint
import java.util.*

/**
 * Created by Bessonov Alex
 * Date: 04.01.14
 * Time: 22:01
 */
class IsmaErrorList : LinkedList<IsmaError?>()