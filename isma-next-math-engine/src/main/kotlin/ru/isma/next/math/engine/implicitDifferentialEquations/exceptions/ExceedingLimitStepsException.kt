package smallBossMathLib.implicitDifferentialEquations.exceptions

class ExceedingLimitStepsException : Exception() {
    override val message: String = "Maximum steps exceeded"
}