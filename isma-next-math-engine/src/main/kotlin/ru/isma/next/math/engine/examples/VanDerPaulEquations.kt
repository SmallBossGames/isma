package ru.isma.next.math.engine.examples

import ru.isma.next.math.engine.explicitDifferentialEquations.*
import ru.isma.next.math.engine.implicitDifferentialEquations.ImplicitEulerIntegrator
import ru.isma.next.math.engine.implicitDifferentialEquations.MK22Integrator
import ru.isma.next.math.engine.implicitDifferentialEquations.Radau5Order3Integrator
import smallBossMathLib.implicitDifferentialEquations.exceptions.ExceedingLimitEvaluationsException
import smallBossMathLib.implicitDifferentialEquations.exceptions.ExceedingLimitStepsException
import java.io.File
import kotlin.math.pow

val references = arrayOf(
    doubleArrayOf(1.937042200, -0.702249465),
    doubleArrayOf(1.7629587057, -0.835943058),
    doubleArrayOf(1.7185581778, -0.879712258),
    doubleArrayOf(1.70840488, -0.890416),
    doubleArrayOf(1.70616743, -0.8928100),
    doubleArrayOf(1.705680, -0.893332),
    doubleArrayOf(1.70557, -0.893445))

val referencesEpsilon = DoubleArray(references.size){ i -> 1.0/10.0.pow(i+2)}

fun defaultVanDerPaul(mu: Double, inY: DoubleArray, outF:DoubleArray){
    outF[0] = inY[1]
    outF[1] = mu * (1 - inY[0] * inY[0]) * inY[1] - inY[0]
}

fun pseudoVanDerPaul(eps: Double, inY: DoubleArray, outF:DoubleArray){
    outF[0] = inY[1]
    outF[1] = ((1.0 - inY[0] * inY[0])*inY[1] - inY[0]) / eps
}

fun mk22VdPExample(mu: Double)
{
    val solver = MK22Integrator(0.01, 20, 2.0)

    File("VanDerPaul(mu = ${mu}).csv ").bufferedWriter().use { out ->
        out.appendLine("t;y1;y2;stiffnessCoefficient")
        solver.addStepHandler { t, y, state, _ ->
            val stiffness = state.jacobiMatrix.evalStiffness()
            val str = "${t};${y[0]};${y[1]};$stiffness"
                .replace('.', ',')
            out.appendLine(str)
        }

        val output = doubleArrayOf(2.0, 0.0)
        val rVector = DoubleArray(output.size) { 1e-7 }

        try {
            val result = solver.integrate(0.0, 20.0, 1e-3, output, rVector, output)
            {inY, outY ->
                outY[0] = inY[1]
                outY[1] = mu * (1 - inY[0] * inY[0]) * inY[1] - inY[0]
            }
            println(result.toString())
        } catch (ex: ExceedingLimitEvaluationsException){
            println(ex.message)
        } catch (ex: ExceedingLimitStepsException){
            println(ex.message)
        }
    }
}

fun rk2stVdPExample(mu: Double){
    val solverRK2 = RK23StabilityControlIntegrator(0.01)
    val output = doubleArrayOf(-2.0, 0.0)
    val rVector = DoubleArray(2){1e-12}

    File("VanDerPaul(mu = $mu).rk23st.csv ").bufferedWriter().use { out ->
        solverRK2.addStepHandler { t, y, _, _ ->
            val str = "${t};${y[0]};${y[1]}"
            out.appendLine(str)
        }
        //solverRK2.enableStepCountLimit(2000)

        try {
            val result = solverRK2.integrate(0.0,20.0, 1e-3, output, rVector, output)
            { _, inY, outY: DoubleArray ->
                outY[0] = inY[1]
                outY[1] = mu * (1 - inY[0] * inY[0]) * inY[1] - inY[0]
            }
            println("rk2stVdPExample: $result")
        } catch (ex: ExceedingLimitEvaluationsException){
            println(ex.message)
        } catch (ex: ExceedingLimitStepsException){
            println(ex.message)
        }
    }
}

fun rk2VdPExample(mu: Double){
    val solverRK2 = RK23Integrator(0.01)
    val output = doubleArrayOf(-2.0, 0.0)
    val rVector = DoubleArray(output.size) {1e-7}

    File("VanDerPaul(mu = $mu).rk23.csv ").bufferedWriter().use { out ->
        solverRK2.addStepHandler {t, y, _, _ ->
            val str = "${t};${y[0]};${y[1]}".replace('.', ',')
            out.appendLine(str)
        }
        //solverRK2.enableStepCountLimit(2000)


        try {
            val result = solverRK2.integrate(0.0, 2.0, 1e-3, output, rVector, output)
            {_, y, f ->
                f[0] = y[1]
                f[1] = mu * (1 - y[0] * y[0]) * y[1] - y[0]
            }
            println("rk2VdPExample: $result")
        } catch (ex: ExceedingLimitEvaluationsException){
            println(ex.message)
        } catch (ex: ExceedingLimitStepsException){
            println(ex.message)
        }
    }
}


fun rk4stVdPExample(mu: Double){
    val solverRK2 = RK4StabilityControlIntegrator(0.01)
    val output = doubleArrayOf(-2.0, 0.0)
    val rVector = DoubleArray(2){ 1e-12 }

    File("VanDerPaul(mu = $mu).rk45st.csv ").bufferedWriter().use { out ->
        solverRK2.addStepHandler { t, y, _, _ ->
            val str = "${t};${y[0]};${y[1]}".replace('.', ',')
            out.appendLine(str)
        }
        //solverRK2.enableStepCountLimit(2000)

        try {
            val result = solverRK2.integrate(0.0,20.0, 1e-3, output, rVector, output)
            { _, inY: DoubleArray, outY: DoubleArray ->
                outY[0] = inY[1]
                outY[1] = mu * (1 - inY[0] * inY[0]) * inY[1] - inY[0]
            }
            println("RK4ST: $result")
        } catch (ex: ExceedingLimitEvaluationsException){
            println(ex.message)
        } catch (ex: ExceedingLimitStepsException){
            println(ex.message)
        }
    }
}

fun rkm4VdPExample(mu: Double){
    val solverRK2 = RKM4Integrator(0.01)
    val output = doubleArrayOf(-2.0, 0.0)
    val rVector = DoubleArray(output.size) {1e-7}

    File("VanDerPaul(mu = $mu).rkm4.csv ").bufferedWriter().use { out ->
        solverRK2.addStepHandler { t, y, _, _ ->
            val str = "${t};${y[0]};${y[1]}".replace('.', ',')
            out.appendLine(str)
        }
        //solverRK2.enableStepCountLimit(2000)

        try {
            val result = solverRK2.integrate(0.0, 20.0,1e-3, output, rVector, output)
            { _, y, f ->
                f[0] = y[1]
                f[1] = mu * (1 - y[0] * y[0]) * y[1] - y[0]
            }
            println("RKM4: $result")

        } catch (ex: ExceedingLimitEvaluationsException){
            println(ex.message)
        } catch (ex: ExceedingLimitStepsException){
            println(ex.message)
        }
    }
}


fun mk22VdPAlternateExample(p: Double)
{
    val solver = MK22Integrator(0.0001, 0, 0.0)

    File("VanDerPaulStiff(p = ${p}).mk22.csv ").bufferedWriter().use { out ->
        out.appendLine("t;y1;y2;stiffnessCoefficient")
        solver.addStepHandler { t, y, state, _ ->
            val stiffness = state.jacobiMatrix.evalStiffness()
            val str = "${t};${y[0]};${y[1]};$stiffness"
                .replace('.', ',')

            out.appendLine(str)
        }
        solver.enableStepCountLimit(20000)

        val output = doubleArrayOf(2.0, -0.66)
        val rVector = DoubleArray(output.size) { 1e-7 }

        try {
            solver.integrate(0.0, 2.0, 1e-3, output, rVector, output)
            {inY: DoubleArray, outY: DoubleArray ->
                outY[0] = inY[1]
                outY[1] = ((1.0 - inY[0] * inY[0])*inY[1] - inY[0]) / p
            }
        } catch (ex: ExceedingLimitEvaluationsException){
            println(ex.message)
        } catch (ex: ExceedingLimitStepsException){
            println(ex.message)
        }
    }
}

fun eulerVdPAlternateExample(p: Double)
{
    val solver = EulerIntegrator(1e-3)
    val output = doubleArrayOf(2.0, 0.0)
    val rVector = DoubleArray(output.size) { 1e-7 }

    File("VanDerPaul(p = ${p}).euler.csv ").bufferedWriter().use { out ->
        out.appendLine("t;y1;y2")
        solver.addStepHandler { t, y, _, _ ->
            val str = "${t};${y[0]};${y[1]}"
            out.appendLine(str)
        }
        solver.enableStepCountLimit(2000000)

        try {
            val result = solver.integrate(0.0, 2.0, 1e-3, output, rVector, output)
            { _, y, f -> pseudoVanDerPaul(p, y, f) }
            println("EulerVdPAlternate: $result")
        } catch (ex: ExceedingLimitEvaluationsException){
            println(ex.message)
        } catch (ex: ExceedingLimitStepsException){
            println(ex.message)
        }
    }
}

fun eulerVdPExample(mu: Double)
{
    val solver = EulerIntegrator(1e-3)
    val output = doubleArrayOf(2.0, 0.0)
    val rVector = DoubleArray(output.size) { 1e-7 }

    File("VanDerPaul(mu = ${mu}).euler.csv ").bufferedWriter().use { out ->
        out.appendLine("t;y1;y2")
        solver.addStepHandler { t, y, _, _ ->
            val str = "${t};${y[0]};${y[1]}"
            out.appendLine(str)
        }

        try {
            val result = solver.integrate(0.0, 20.0, 1e-3, output, rVector, output)
            { _, y, f ->  defaultVanDerPaul(mu, y, f) }
            println("eulerVdPExample: $result")
        } catch (ex: ExceedingLimitEvaluationsException){
            println(ex.message)
        } catch (ex: ExceedingLimitStepsException){
            println(ex.message)
        }
    }
}

fun implicitEulerVdPExample(mu: Double)
{
    val solver = ImplicitEulerIntegrator(0.01)

    File("VanDerPaul(mu = ${mu}).implicitEuler.csv ").bufferedWriter().use { out ->
        out.appendLine("t;y1;y2")
        solver.addStepHandler { t, y, _, _ ->
            val str = "${t};${y[0]};${y[1]}".replace('.', ',')
            out.appendLine(str)
        }
        solver.enableStepCountLimit(20000)

        val output = doubleArrayOf(2.0, 0.0)
        val rVector = DoubleArray(2){1e-7}

        try {
            val result = solver.integrate(0.0, 20.0, 1e-3, output, rVector, output)
            {inY: DoubleArray, outY: DoubleArray ->
                outY[0] = inY[1]
                outY[1] = mu * (1 - inY[0] * inY[0]) * inY[1] - inY[0]
            }
            println("eulerVdPExample: $result")
        } catch (ex: ExceedingLimitEvaluationsException){
            println(ex.message)
        } catch (ex: ExceedingLimitStepsException){
            println(ex.message)
        }
    }
}

fun radau5Order3VdPExample(mu: Double)
{
    val solver = Radau5Order3Integrator(0.01, 1e-10, 10)

    File("VanDerPaul(mu = ${mu}).radau5Order5.csv ").bufferedWriter().use { out ->
        out.appendLine("t;y1;y2")
        solver.addStepHandler { t, y, _, _ ->
            val str = "${t};${y[0]};${y[1]}".replace('.', ',')
            out.appendLine(str)
        }
        solver.enableStepCountLimit(20000)

        val output = doubleArrayOf(2.0, 0.0)
        val rVector = DoubleArray(2){1e-7}

        try {
            val result = solver.integrate(0.0, 20.0, 1e-2, output, rVector, output)
            { _, inY: DoubleArray, outY: DoubleArray ->
                outY[0] = inY[1]
                outY[1] = mu * (1 - inY[0] * inY[0]) * inY[1] - inY[0]
            }
            println("eulerVdPExample: $result")
        } catch (ex: ExceedingLimitEvaluationsException){
            println(ex.message)
        } catch (ex: ExceedingLimitStepsException){
            println(ex.message)
        }
    }
}

fun mk22VdPAbsoluteAccuracyTestStage(accuracy: Double)
{
    val solver = MK22Integrator(accuracy, 0, 0.0)
    val input = doubleArrayOf(2.0, -0.66)
    val output = DoubleArray(2)
    val rVector = DoubleArray(input.size) { 1e-7 }

    println("Accuracy: $accuracy")

    for (i in references.indices){
        solver.integrate(0.0, 2.0, 1e-3, input, rVector, output)
        {y: DoubleArray, f: DoubleArray -> pseudoVanDerPaul(referencesEpsilon[i], y, f) }
        println("Eps: ${referencesEpsilon[i]}")
        printResultWithDelta(output, references[i])
    }
}


fun mk22VdPAbsoluteAccuracyTest(){
    absoluteAccuracyTest {
            tol, eps, input, r, out ->
        val solver = MK22Integrator(tol, 0, 0.0)
        solver.integrate(0.0, 2.0, 1e-3, input, r, out)
        {y: DoubleArray, f: DoubleArray -> pseudoVanDerPaul(eps, y, f) }
    }
}

fun eulerAbsoluteAccuracyTest(){
    absoluteAccuracyTest {
            tol, eps, input, r, out ->
        val solver = EulerIntegrator(tol)
        solver.integrate(0.0, 2.0, 1e-3, input, r, out)
        {_, y, f -> pseudoVanDerPaul(eps, y, f) }
    }
}

fun impEulerAbsoluteAccuracyTest(){
    absoluteAccuracyTest {
            tol, eps, input, r, out ->
        val solver = ImplicitEulerIntegrator(tol)
        solver.integrate(0.0, 2.0, 1e-3, input, r, out)
        {y: DoubleArray, f: DoubleArray -> pseudoVanDerPaul(eps, y, f) }
    }
}

fun rk23AbsoluteAccuracyTest(){
    absoluteAccuracyTest {
            tol, eps, input, r, out ->
        val solver = RK23Integrator(tol)
        solver.integrate(0.0, 2.0, 1e-3, input, r, out)
        {_, y: DoubleArray, f: DoubleArray -> pseudoVanDerPaul(eps, y, f) }
    }
}

fun rkm45AbsoluteAccuracyTest(){
    absoluteAccuracyTest {
            tol, eps, input, r, out ->
        val solver = RKM4Integrator(tol)
        solver.integrate(0.0, 2.0, 1e-3, input, r, out)
        {_, y: DoubleArray, f: DoubleArray -> pseudoVanDerPaul(eps, y, f) }
    }
}

fun rk23stAbsoluteAccuracyTest(){
    absoluteAccuracyTest {
            tol, eps, input, r, out ->
        val solver = RK23StabilityControlIntegrator(tol)
        solver.integrate(0.0, 2.0, 1e-3, input, r, out)
        {_, y: DoubleArray, f: DoubleArray -> pseudoVanDerPaul(eps, y, f) }
    }
}

fun rk45stAbsoluteAccuracyTest(){
    absoluteAccuracyTest {
            tol, eps, input, r, out ->
        val solver = RK4StabilityControlIntegrator(tol)
        solver.integrate(0.0, 2.0, 1e-3, input, r, out)
        {_, y: DoubleArray, f: DoubleArray -> pseudoVanDerPaul(eps, y, f) }
    }
}

inline fun absoluteAccuracyTest(
    integrate: (tol: Double, eps: Double, input: DoubleArray, r:DoubleArray, out: DoubleArray) -> Unit
){
    val input = doubleArrayOf(2.0, -0.66)
    val output = DoubleArray(2)
    val rVector = DoubleArray(input.size) { 1e-12 }

    print("aTol/eps;")
    for (i in referencesEpsilon)
        print("$i;")
    println()
    for (i in 2..12 step 2){
        val accuracy =  1.0/10.0.pow(i)
        print("$accuracy;")
        for (j in referencesEpsilon.indices){
            val eps = referencesEpsilon[j]
            val reference = references[j]
            integrate(accuracy, eps, input, rVector, output)
            val delta = findDelta(reference, output)
            print("$delta;")
        }
        println()
    }
}

fun eulerBreakpointTest(){
    for (i in 2..10)
        eulerVdPAlternateExample(1.0/10.0.pow(i))
}

