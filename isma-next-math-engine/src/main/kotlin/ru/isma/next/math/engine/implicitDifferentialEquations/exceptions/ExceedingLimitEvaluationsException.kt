package smallBossMathLib.implicitDifferentialEquations.exceptions

class ExceedingLimitEvaluationsException : Exception() {
    override val message: String = "Maximum evaluations exceeded"
}