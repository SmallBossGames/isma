package ru.isma.next.math.engine.examples

import ru.isma.next.math.engine.implicitDifferentialEquations.ImplicitEulerIntegrator
import ru.isma.next.math.engine.implicitDifferentialEquations.MK22Integrator
import ru.isma.next.math.engine.implicitDifferentialEquations.Radau5Order3Integrator
import smallBossMathLib.implicitDifferentialEquations.exceptions.ExceedingLimitEvaluationsException
import smallBossMathLib.implicitDifferentialEquations.exceptions.ExceedingLimitStepsException
import java.io.File
import kotlin.math.*
import kotlin.system.measureTimeMillis

private const val C = 1.6e-8
private const val Cs = 2e-12
private const val Cp = 1e-8
private const val Lh = 4.45
private const val Ls1 = 0.002
private const val Ls2 = 5e-4
private const val Ls3 = 5e-4
private const val gamma = 40.67286402e-9
private const val R = 25000
private const val Rp = 50
private const val Rg1 = 36.3
private const val Rg2 = 17.3
private const val Rg3 = 17.3
private const val Ri = 50
private const val Rc = 600
private const val delta = 17.7493332

fun Uin1(t:Double) = 0.5 * sin(2000.0 * PI * t)

fun Uin2(t:Double) = 2.0 * sin(20000.0 * PI * t)

fun q(u: Double) = gamma * (exp(delta *u) - 1.0)

fun mainFunction(y: DoubleArray, outF: DoubleArray){
    val t = y[0]

    val ud1 = y[3] - y[5] - y[7] - Uin2(t)
    val ud2 = -y[4] + y[6] - y[7] - Uin2(t)
    val ud3 = y[4] + y[5] + y[7] + Uin2(t)
    val ud4 = -y[3] - y[6] + y[7] + Uin2(t)

    outF[0] = 1.0

    outF[1] = (1.0/ C) * (y[8] - 0.5 * y[10] + 0.5 * y[11] + y[14] - (1.0 / R) * y[1])
    outF[2] = (1.0/ C) * (y[9] - 0.5 * y[12] + 0.5 * y[13] + y[15] - (1.0 / R) * y[2])
    outF[3] = (1.0/ Cs) * (y[10] - q(ud1) + q(ud4))
    outF[4] = (1.0/ Cs) * (-y[11] + q(ud2) - q(ud3))
    outF[5] = (1.0/ Cs) * (y[12] + q(ud1) - q(ud3))
    outF[6] = (1.0/ Cs) * (-y[13] - q(ud2) + q(ud4))
    outF[7] = (1.0/ Cp) * (-(1.0/ Rp) * y[7] + q(ud1) + q(ud2) - q(ud3) - q(ud4))
    outF[8] = -(1.0 / Lh) * y[1]
    outF[9] = -(1.0 / Lh) * y[2]
    outF[10] = (1.0 / Ls2) * (0.5 * y[1] - y[3] - Rg2 * y[10])
    outF[11] = (1.0 / Ls3) * (-0.5 * y[1] + y[4] - Rg3 * y[11])
    outF[12] = (1.0 / Ls2) * (0.5 * y[2] - y[5] - Rg2 * y[12])
    outF[13] = (1.0 / Ls3) * (-0.5 * y[2] + y[6] - Rg3 * y[13])
    outF[14] = (1.0 / Ls1) * (-y[1] + Uin1(t) - (Ri + Rg1) * y[14])
    outF[15] = (1.0 / Ls1) * (-y[2] - (Rc + Rg1) * y[15])
}

fun mainFunction(t: Double, y: DoubleArray, outF: DoubleArray){
    val ud1 = y[3] - y[5] - y[7] - Uin2(t)
    val ud2 = -y[4] + y[6] - y[7] - Uin2(t)
    val ud3 = y[4] + y[5] + y[7] + Uin2(t)
    val ud4 = -y[3] - y[6] + y[7] + Uin2(t)

    outF[0] = 0.0

    outF[1] = (1.0/ C) * (y[8] - 0.5 * y[10] + 0.5 * y[11] + y[14] - (1.0 / R) * y[1])
    outF[2] = (1.0/ C) * (y[9] - 0.5 * y[12] + 0.5 * y[13] + y[15] - (1.0 / R) * y[2])
    outF[3] = (1.0/ Cs) * (y[10] - q(ud1) + q(ud4))
    outF[4] = (1.0/ Cs) * (-y[11] + q(ud2) - q(ud3))
    outF[5] = (1.0/ Cs) * (y[12] + q(ud1) - q(ud3))
    outF[6] = (1.0/ Cs) * (-y[13] - q(ud2) + q(ud4))
    outF[7] = (1.0/ Cp) * (-(1.0/ Rp) * y[7] + q(ud1) + q(ud2) - q(ud3) - q(ud4))
    outF[8] = -(1.0 / Lh) * y[1]
    outF[9] = -(1.0 / Lh) * y[2]
    outF[10] = (1.0 / Ls2) * (0.5 * y[1] - y[3] - Rg2 * y[10])
    outF[11] = (1.0 / Ls3) * (-0.5 * y[1] + y[4] - Rg3 * y[11])
    outF[12] = (1.0 / Ls2) * (0.5 * y[2] - y[5] - Rg2 * y[12])
    outF[13] = (1.0 / Ls3) * (-0.5 * y[2] + y[6] - Rg3 * y[13])
    outF[14] = (1.0 / Ls1) * (-y[1] + Uin1(t) - (Ri + Rg1) * y[14])
    outF[15] = (1.0 / Ls1) * (-y[2] - (Rc + Rg1) * y[15])
}

fun ringModulatorMK22Example(){
    val integrator = MK22Integrator(1e-2, 0, 2.0)
    integrator.enableLowStepLimit(1e-30)
    integrator.enableHighStepLimit(0.001)

    File("RingModulator.csv ").bufferedWriter().use { out ->
        val output = DoubleArray(16)
        val rVector = DoubleArray(output.size) { 1e-7 }

        var counter = 0

        out.append("stiffness;t;")

        for (i in 1..15)
            out.append("y$i;")

        out.appendLine()

        integrator.addStepHandler(){ _, y, state, _ ->
            if(counter % 20 == 0){
                val stiffness = state.jacobiMatrix.evalStiffness()
                out.append("$stiffness;".replace('.',','))
                for (item in y)
                    out.append("$item;".replace('.',','))
                out.appendLine()
            }
           counter++
        }

        try {
            val time = measureTimeMillis {
                val stat = integrator
                    .integrate(0.0, 0.0012, 1e-30, output, rVector, output, ::mainFunction)
                println("MK22: $stat")
            }
            println("Time: $time")
        } catch (ex: ExceedingLimitEvaluationsException){
            println(ex.message)
        } catch (ex: ExceedingLimitStepsException){
            println(ex.message)
        }
    }
}

fun ringModulatorImplicitEulerExample(){
    val integrator = ImplicitEulerIntegrator(1e-3)

    File("RingModulator.ImplicitEuler.csv ").bufferedWriter().use { out ->
        val output = DoubleArray(16)
        val rVector = DoubleArray(output.size) { 1e-7 }

        var counter = 0

        out.append("stiffness;t;")

        for (i in 1..15)
            out.append("y$i;")

        out.appendLine()

        integrator.addStepHandler(){ _, y, state, _ ->
            if(counter % 20 == 0){
                val stiffness = state.jacobiMatrix.evalStiffness()
                out.append("$stiffness;".replace('.',','))
                for (item in y)
                    out.append("$item;".replace('.',','))
                out.appendLine()
            }
            counter++
        }

        try {
            val time = measureTimeMillis {
                val stat = integrator.integrate(
                    0.0,
                    0.0012,
                    1e-30,
                    output,
                    rVector,
                    output,
                    ::mainFunction
                )
                println("ImplicitEuler: $stat")
            }
            println("Time: $time")
        } catch (ex: ExceedingLimitEvaluationsException){
            println(ex.message)
        } catch (ex: ExceedingLimitStepsException){
            println(ex.message)
        }
    }
}

/*fun ringModulatorRK23ST() {
    val integrator = RK23StabilityControlIntegrator(1e-5, 0.001)
    integrator.enableStepCountLimit(50000)

    File("RingModulator.rk23st.csv").bufferedWriter().use { out ->
        val output = DoubleArray(16)
        val rVector = DoubleArray(output.size) { 1e-7 }

        var counter = 0

        out.append("t;nothing;")

        for (i in 1..15)
            out.append("y$i;")

        out.appendLine()

        integrator.addStepHandler(){ t, y, state, _ ->
            if(counter % 5 == 0){
                out.append("$t;")
                for (item in y)
                    out.append("$item;".replace('.',','))
                out.appendLine()
            }
            counter++
        }

        try {
            integrator.integrate(0.0, output, 0.001, output, ::mainFunction)
        } catch (ex: ExceedingLimitEvaluationsException){
            println(ex.message)
        } catch (ex: ExceedingLimitStepsException){
            println(ex.message)
        }
    }
}*/

fun ringModulatorRadau5Example(){
    val integrator = Radau5Order3Integrator(1e-4, 1e-10, 10)

    File("RingModulator.Radau5.csv ").bufferedWriter().use { out ->
        val output = DoubleArray(16)
        val rVector = DoubleArray(output.size) { 1e-10 }

        var counter = 0

        out.append("stiffness;t;")

        for (i in 0..15)
            out.append("y$i;")

        out.appendLine()

        integrator.addStepHandler(){ t, y, state, _ ->
            if(counter % 20 == 0){
                val stiffness = state.jacobiMatrix.evalStiffness()
                out.append("$stiffness;$t;".replace('.',','))
                for (item in y)
                    out.append("$item;".replace('.',','))
                out.appendLine()
            }
            counter++
        }

        try {
            val time = measureTimeMillis {
                val stat = integrator.integrate(
                    0.0,
                    0.0012,
                    1e-30,
                    output,
                    rVector,
                    output,
                    ::mainFunction
                )
                println("ImplicitEuler: $stat")
            }
            println("Time: $time")
        } catch (ex: ExceedingLimitEvaluationsException){
            println(ex.message)
        } catch (ex: ExceedingLimitStepsException){
            println(ex.message)
        }
    }
}