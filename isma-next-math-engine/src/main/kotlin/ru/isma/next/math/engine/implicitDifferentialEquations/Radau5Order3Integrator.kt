package ru.isma.next.math.engine.implicitDifferentialEquations

import ru.isma.next.math.engine.shared.*
import smallBossMathLib.implicitDifferentialEquations.exceptions.ExceedingLimitEvaluationsException
import smallBossMathLib.implicitDifferentialEquations.exceptions.ExceedingLimitStepsException
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

const val uround = 1e-16
const val fac = 0.9

class Radau5Order3Integrator(
    val relativeTolerance: Double,
    val absoluteTolerance: Double,
    val maxNewtonIterations: Int
) : ImplicitIntegrator() {
    private companion object{
        val aMatrix : Matrix2D = Matrix2D(3)
        val bVector = doubleArrayOf(0.3764030627, 0.51248582618, 0.11111111111)
        val cVector = doubleArrayOf(0.15505102572, 0.64494897427, 1.0)
        val dVector = doubleArrayOf(0.0, 0.0, 1.0)
        val gamma = 3.63783

        val tMatrix = Matrix2D(3)
        val tIMatrix = Matrix2D(3)

        val e1 = gamma / 3.0 * (-13.0 - 7.0 * sqrt(6.0))
        val e2 = gamma / 3.0 * (-13.0 + 7.0 * sqrt(6.0))
        val e3 = gamma / 3.0 * (-1.0)

        init {
            aMatrix[0,0] = 0.19681547722
            aMatrix[0,1] = -0.06553542585
            aMatrix[0,2] = 0.02377097434
            aMatrix[1,0] = 0.39442431473
            aMatrix[1,1] = 0.29207341166
            aMatrix[1,2] = -0.04154875212
            aMatrix[2,0] = 0.3764030627
            aMatrix[2,1] = 0.51248582618
            aMatrix[2,2] = 0.11111111111

            tMatrix[0,0] = 9.1232394870892942792e-02
            tMatrix[0,1] = -0.14125529502095420843
            tMatrix[0,2] = -3.0029194105147424492e-02
            tMatrix[1,0] = 0.24171793270710701896
            tMatrix[1,1] = 0.20412935229379993199
            tMatrix[1,2] = 0.38294211275726193779
            tMatrix[2,0] = 0.96604818261509293619

            tIMatrix[0,0] = 4.3255798900631553510
            tIMatrix[0,1] = 0.33919925181580986954
            tIMatrix[0,2] = 0.54177053993587487119
            tIMatrix[1,0] = -4.1787185915519047273
            tIMatrix[1,1] = -0.32768282076106238708
            tIMatrix[1,2] = 0.47662355450055045196
            tIMatrix[2,0] = -0.50287263494578687595
            tIMatrix[2,1] = 2.5719269498556054292
            tIMatrix[2,2] = -0.59603920482822492497
        }
    }

    @Throws(ExceedingLimitStepsException::class, ExceedingLimitEvaluationsException::class)
    fun integrate(
        startTime: Double,
        endTime: Double,
        defaultStepSize: Double,
        y0: DoubleArray,
        rVector: DoubleArray,
        outY: DoubleArray,
        equations: NonStationaryODE
    ) : IImplicitMethodStatistic {
        if (y0.size != outY.size)
            throw IllegalArgumentException()

        y0.copyInto(outY)

        val jacobiMatrix = Matrix2D(y0.size)
        val gMatrix = Matrix2D(aMatrix.size * jacobiMatrix.size)

        val wMatrix = Matrix2D(y0.size)

        val matrixBuffer = Matrix2D(y0.size)

        val identityMatrix = Matrix2D.createUnitMatrix2D(y0.size)
        val aIdentityMatrixBuffer = Matrix2D(gMatrix.size)

        aMatrix.kroneckerMultiply(identityMatrix, aIdentityMatrixBuffer)

        val fBuffer = DoubleArray(y0.size)
        val vectorBuffer1 = DoubleArray(y0.size)
        val vectorBuffer2 = DoubleArray(y0.size)

        val zVector = DoubleArray(gMatrix.size)
        val vectorExtendedBuffer1 = DoubleArray(gMatrix.size)
        val vectorExtendedBuffer2 = DoubleArray(gMatrix.size)

        var step = defaultStepSize
        var time = startTime

        val jacobiSolver = JacobiMatrixSolver(y0.size)

        val statistic = ImplicitMethodStatistic(
            stepsCount = 0,
            evaluationsCount = 0,
            jacobiEvaluationsCount = 0,
            returnsCount = 0
        )

        val state = ImplicitMethodStepState(
            jacobiMatrix = jacobiMatrix,
            isLowStepSizeReached = isLowStepSizeReached(step),
            isHighStepSizeReached = isHighStepSizeReached(step),
            freezeJacobiStepsCount = 0
        )

        var faccon = 1.0
        var err1Old = 0.0
        var stepOld = 0.0
        var iterationsCount = 0

        mainLoop@ while (time < endTime){
            step = normalizeStep(step, time, endTime)

            jacobiSolver.solve(time, outY, jacobiMatrix, equations)
            aMatrix.kroneckerMultiply(jacobiMatrix, gMatrix)

            for (i in gMatrix.indices){
                for (j in gMatrix.indices){
                    gMatrix[i, j] = if (i == j) 1.0 else 0.0 - step * gMatrix[i, j]
                }
            }

            for (i in wMatrix.indices){
                for (j in wMatrix.indices){
                    wMatrix[i, j] = if (i == j) 1.0 else 0.0 - step * gamma * jacobiMatrix[i, j]
                }
            }

            wMatrix.makeLU()
            gMatrix.makeLU()

            for (i in zVector.indices){
                zVector[i] = 0.0
            }

            var newtonIterationsCount = 0
            var deltaZNormOld = 0.0
            var newFaccon = max(faccon, uround).pow(0.8)


            while (true){
                for (i in aMatrix.indices){
                    for (j in vectorBuffer1.indices){
                        vectorBuffer1[j] = outY[j] + zVector[i * vectorBuffer1.size + j]
                    }
                    equations(cVector[i] + time, vectorBuffer1, fBuffer)
                    for (j in fBuffer.indices){
                        vectorExtendedBuffer1[i * vectorBuffer1.size + j] = step * fBuffer[j]
                    }
                }

                aIdentityMatrixBuffer.multiply(vectorExtendedBuffer1, vectorExtendedBuffer2)

                for (i in vectorExtendedBuffer2.indices){
                    vectorExtendedBuffer2[i] = vectorExtendedBuffer2[i] - zVector[i]
                }

                gMatrix.solveLU(vectorExtendedBuffer2, vectorExtendedBuffer1)

                for (i in zVector.indices){
                    zVector[i] += vectorExtendedBuffer1[i]
                }

                val deltaZNorm = zeroSafetyNorm(vectorExtendedBuffer1, zVector, uround)

                if(newtonIterationsCount > 0){
                    val theta = deltaZNorm / deltaZNormOld
                    if(theta >= 1.0){
                        step /= 2.0
                        continue@mainLoop
                    } else{
                        newFaccon = theta / (1.0 - theta)
                    }
                }

                newtonIterationsCount++

                if(newFaccon * deltaZNorm <= relativeTolerance * 1e-2)
                    break

                deltaZNormOld = deltaZNorm
            }

            wMatrix.inverseLU(matrixBuffer)

            equations(time, outY, fBuffer)
            for (i in fBuffer.indices){
                fBuffer[i] = gamma *step*fBuffer[i] +
                        e1 * zVector[fBuffer.size * 0 + i] +
                        e2 * zVector[fBuffer.size * 1 + i] +
                        e3 * zVector[fBuffer.size * 2 + i]
            }

            matrixBuffer.multiply(fBuffer, vectorBuffer1)

            for (i in vectorBuffer1.indices){
                vectorBuffer1[i] = vectorBuffer1[i] + outY[i]
            }

            equations(time, vectorBuffer1, fBuffer)

            for (i in fBuffer.indices){
                fBuffer[i] = gamma *step*fBuffer[i] +
                        e1 * zVector[i] +
                        e2 * zVector[fBuffer.size + i] +
                        e3 * zVector[fBuffer.size * 2 + i]
            }

            matrixBuffer.multiply(fBuffer, vectorBuffer1)

            outY.copyInto(vectorBuffer2)

            for (i in zVector.indices){
                vectorBuffer2[i % vectorBuffer2.size] += dVector[i % dVector.size] * zVector[i]
            }

            val err1 = radauErrorNorm(
                outY,
                vectorBuffer2,
                vectorBuffer1,
                relativeTolerance,
                absoluteTolerance
            )

            val hNew1 = (fac * step) / err1.pow(1.0/4.0)

            val hNew2 = if (iterationsCount > 0) {
                fac * step * (1.0 / err1).pow(1.0/4.0) * (step / stepOld) * (err1Old / err1).pow(1.0/4.0)
            } else {
                hNew1
            }

            if(err1 > 1.0){
                step = min(hNew1, hNew2)
            }

            for (i in outY.indices){
                outY[i] = vectorBuffer2[i]
            }

            time += step

            executeStepHandlers(time, outY, state, statistic)

            stepOld = step
            err1Old = err1
            faccon = newFaccon
            iterationsCount++

            step = min(hNew1, hNew2)
        }
        return statistic
    }

    fun findError1(
        t: Double,
        y: DoubleArray,
        fBuffer: DoubleArray,
        zVector:DoubleArray,
        inverseWMatrix: Matrix2D,
        step: Double,
        outError: DoubleArray
    ) {
        for (i in fBuffer.indices){
            fBuffer[i] = gamma *step*fBuffer[i] +
                    e1 * zVector[i] +
                    e2 * zVector[fBuffer.size + i] +
                    e3 * zVector[fBuffer.size * 2 + i]
        }

        inverseWMatrix.multiply(fBuffer, outError)
    }
}