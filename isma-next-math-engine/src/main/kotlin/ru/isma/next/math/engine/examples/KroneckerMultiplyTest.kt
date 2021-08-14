package ru.isma.next.math.engine.examples

import ru.isma.next.math.common.Matrix2D

fun kroneckerMultiplyTest1(){
    val matrix = Matrix2D(4)
    matrix[0,0] = 1.0
    matrix[0, 1] = 2.0
    matrix[0, 2] = 4.0
    matrix[0, 3] = 1.0
    matrix[1,0] = 2.0
    matrix[1, 1] = 8.0
    matrix[1, 2] = 6.0
    matrix[1, 3] = 4.0
    matrix[2,0] = 3.0
    matrix[2, 1] = 10.0
    matrix[2, 2] = 8.0
    matrix[2, 3] = 8.0

    val matrix2 = Matrix2D(3)
    matrix2[0,0] = 2.0
    matrix2[0, 1] = 1.0
    matrix2[0, 2] = 6.0
    matrix2[1,0] = 4.0
    matrix2[1, 1] = 8.0
    matrix2[1, 2] = 2.0
    matrix2[2,0] = 3.0
    matrix2[2, 1] = 10.0
    matrix2[2, 2] = 8.0

    val result = Matrix2D(matrix.size * matrix2.size)

    matrix.kroneckerMultiply(matrix2, result)

    println(result.toString())
}

fun kroneckerMultiplyTest2(){
    val matrix = Matrix2D(6)
    matrix[0, 0] = 1.0
    matrix[1, 1] = 1.0
    matrix[2, 2] = 1.0
    matrix[3, 3] = 1.0
    matrix[4, 4] = 1.0
    matrix[5, 5] = 1.0


    val matrix2 = Matrix2D(2)
    matrix2[0,0] = 2.0
    matrix2[0, 1] = 1.0
    matrix2[1,0] = 4.0
    matrix2[1, 1] = 8.0

    val result = Matrix2D(matrix.size * matrix2.size)

    matrix.kroneckerMultiply(matrix2, result)

    println(result.toString())
}