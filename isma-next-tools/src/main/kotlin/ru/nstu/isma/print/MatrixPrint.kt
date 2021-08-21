package ru.nstu.isma.print

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