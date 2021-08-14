package ru.isma.next.math.engine.shared

//Non-concurrent
class NewtonRaphsonSolver(size: Int, val accuracy: Double, val rVector: DoubleArray) {
    val jacobiSolver = JacobiMatrixSolver(size)
    val jacobiMatrix = Matrix2D(size)
    val bufferMatrix = Matrix2D(size)
    val vectorBuffer1 = DoubleArray(size)
    val vectorBuffer2 = DoubleArray(size)

    inline fun solve(
        startValue: DoubleArray,
        outValue: DoubleArray,
        equations: StationaryODE
    ){
        startValue.copyInto(outValue)
        while (true){
            equations(outValue, vectorBuffer1)

            jacobiSolver.solve(outValue, jacobiMatrix, equations)
            jacobiMatrix.makeLU()
            jacobiMatrix.solveLU(vectorBuffer1, vectorBuffer2)

            for (i in outValue.indices){
                outValue[i] = outValue[i] - vectorBuffer2[i]
            }

            if(zeroSafetyNorm(vectorBuffer2, outValue, rVector) <= accuracy)
                return
        }
    }

    inline fun solveStaticJacobi(
        startValue: DoubleArray,
        outValue: DoubleArray,
        equations: StationaryODE
    ){
        startValue.copyInto(outValue)

        jacobiSolver.solve(outValue, jacobiMatrix, equations)
        jacobiMatrix.makeLU()
        jacobiMatrix.inverseLU(bufferMatrix)

        while (true){
            equations(outValue, vectorBuffer1)

            bufferMatrix.multiply(vectorBuffer1, vectorBuffer2)

            for (i in outValue.indices){
                outValue[i] = outValue[i] - vectorBuffer2[i]
            }

            if(zeroSafetyNorm(vectorBuffer2, outValue, rVector) <= accuracy)
                return
        }
    }
}