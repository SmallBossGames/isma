package models

class SyntaxErrorModel(val startPosition: Int, val endPosition: Int, val message: String) {
    val interval get() = "${startPosition}:${endPosition}"
}