package ru.isma.next.math.engine.examples

import ru.isma.next.math.engine.implicitDifferentialEquations.MK22Integrator
import java.io.File
import kotlin.math.pow

fun mk22Other2Test(){
    val builder = StringBuilder()
    val solver = MK22Integrator(0.001, 0, 0.0)

    solver.addStepHandler { t, y, _, _ ->
        builder.append("${t};${y[0]};${y[1]};${y[2]} \n")
    }
    //solverRK2.enableEvaluationCountCheck(2000)

    val output = doubleArrayOf(1.0, 0.0, 0.0)
    val rVector = DoubleArray(output.size) { 1e-7 }

    solver.integrate(0.0,100.0,1e-3, output, rVector, output)
    { inY: DoubleArray, outY: DoubleArray ->
        outY[0] = -0.04*inY[0]+0.01*inY[1]*inY[2]
        outY[1] = 400*inY[0]-100*inY[1]*inY[2]-3000.0* inY[1].pow(2.0)
        outY[2] = 30.0*inY[1].pow(2)
    }

    val writingText = builder.replace(Regex("[.]"), ",")

    File("mk22Other2Test.csv ").writeText(writingText)
}

fun mk22Other3Test(){
    val builder = StringBuilder()
    val solver = MK22Integrator(0.001, 0, 0.0)

    solver.addStepHandler {t, y, _, _ ->
        builder.append("${t};${y[0]};${y[1]};${y[2]};${y[3]} \n")
    }
    //solverRK2.enableEvaluationCountCheck(2000)

    val output = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
    val rVector = DoubleArray(output.size) { 1e-7 }

    solver.integrate(0.0, 100.0, 1e-3, doubleArrayOf(1.0, 1.0, 0.0, 0.0), rVector, output)
    { i: DoubleArray, o: DoubleArray ->
        o[0] = i[2]-100.0*i[0]*i[1]
        o[1] = i[2]-2.0*i[3]-100.0*i[0]*i[1]-20000.0*i[1].pow(2)
        o[2] = -i[2]+100.0*i[0]*i[1]
        o[3] = -i[3]+10000.0*i[1].pow(2)
    }

    val writingText = builder.replace(Regex("[.]"), ",")

    File("mk22Other3Test.csv ").writeText(writingText)
}