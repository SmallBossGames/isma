package ru.nstu.isma.print

import javax.tools.StandardJavaFileManager
import javax.tools.ForwardingJavaFileManager
import javax.tools.JavaFileManager
import java.security.SecureClassLoader
import kotlin.Throws
import java.lang.ClassNotFoundException
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.tools.JavaFileObject
import javax.tools.FileObject
import javax.tools.SimpleJavaFileObject

/**
 * Created by Bessonov Alex
 * on 14.03.2015.
 */
object MatrixPrint {
    @JvmStatic
    fun print(label: String, matrix: Array<DoubleArray>, b: DoubleArray) {
        println(" =====  Matrix: $label ========")
        for (i in matrix.indices) {
            for (element in matrix[i]) {
                print("$element   \t")
            }
            print("\t   =   \t")
            println(b[i])
        }
    }

    @JvmStatic
    fun print(label: String, matrix: Array<DoubleArray>) {
        println(" =====  Matrix: $label ========")
        for (i in matrix.indices) {
            for (element in matrix[i]) {
                print("$element   \t")
            }
            print("\n")
        }
    }
}