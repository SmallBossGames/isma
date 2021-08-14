package ru.isma.next.math.engine.shared

import smallBossMathLib.implicitDifferentialEquations.exceptions.ExceedingLimitEvaluationsException
import smallBossMathLib.implicitDifferentialEquations.exceptions.ExceedingLimitStepsException

abstract class Integrator {
    var isStepCountLimitEnabled: Boolean = false
        private set

    var isEvaluationsCountLimitEnabled: Boolean = false
        private set

    var isMinStepControlEnabled: Boolean = false
        private set

    var isMaxStepControlEnabled: Boolean =  false
        private set

    var maxStepCount: Int = 0
        private set
    
    var maxEvaluationsCount: Int = 0
        private set

    var maxStepValue = 0.0
        private set

    var minStepValue = 0.0
        private set

    /*private val stepHandlers = HashSet<(info:StepInfo) -> Unit>()

    fun addStepHandler(handler: (info:StepInfo) -> Unit) {
        stepHandlers += handler
    }

    fun removeStepHandler(handler: (info:StepInfo) -> Unit) {
        stepHandlers -= handler
    }

    fun clearStepHandlers() {
        stepHandlers.clear()
    }

    protected fun executeStepHandlers(info: StepInfo) {
        for (handler in stepHandlers)
            handler(info)
    }*/

    //Evaluation control
    fun enableStepCountLimit(maxSteps: Int){
        isStepCountLimitEnabled = true
        maxStepCount = maxSteps
    }

    fun disableStepCountLimit(){
        isStepCountLimitEnabled = false
        maxStepCount = 0
    }

    @Throws(ExceedingLimitStepsException::class)
    fun checkStepCount(steps: Int){
        if(isStepCountLimitEnabled && steps >= maxStepCount){
            throw ExceedingLimitStepsException()
        }
    }

    //Right part evaluations control

    fun enableEvaluationsCountLimit(maxEvaluations: Int){
        isEvaluationsCountLimitEnabled = true
        maxEvaluationsCount = maxEvaluations
    }

    fun disableEvaluationsCountLimit(){
        isEvaluationsCountLimitEnabled = false
        maxEvaluationsCount = 0
    }

    fun enableHighStepLimit(max: Double){
        isMaxStepControlEnabled = true
        maxStepValue = max
    }

    fun enableLowStepLimit(min: Double){
        isMinStepControlEnabled = true
        minStepValue = min
    }

    fun enableStepLimits(min: Double, max: Double){
        isMinStepControlEnabled = true
        isMaxStepControlEnabled = true
        minStepValue = min
        maxStepValue = max
    }

    fun disableHighStepLimit(){
        isMaxStepControlEnabled = false
    }

    fun disableLowStepLimit(){
        isMinStepControlEnabled = false
    }

    fun disableStepLimits(){
        isMinStepControlEnabled = false
        isMaxStepControlEnabled = false
    }

    fun isLowStepSizeReached(step: Double) = isMinStepControlEnabled && step <= minStepValue

    fun isHighStepSizeReached(step: Double) = isMaxStepControlEnabled && step >= maxStepValue

    @Throws(ExceedingLimitEvaluationsException::class)
    fun checkEvaluationCount(evaluations: Int){
        if(isEvaluationsCountLimitEnabled && evaluations >= maxEvaluationsCount) {
            throw ExceedingLimitEvaluationsException()
        }
    }

    protected fun normalizeStep(step: Double, t: Double, endT: Double) : Double{
        val maxByLimit = if (t + step > endT) endT - t else step
        return when {
            isMinStepControlEnabled && maxByLimit < minStepValue -> minStepValue
            isMaxStepControlEnabled && maxByLimit > maxStepValue -> maxStepValue
            else -> maxByLimit
        }
    }
}