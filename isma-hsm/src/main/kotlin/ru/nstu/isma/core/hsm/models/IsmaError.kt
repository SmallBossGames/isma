package ru.nstu.isma.core.hsm.models

sealed interface IsmaError

class IsmaSyntaxError(val row: Int, val col: Int, val msg: String) : IsmaError {
    override fun toString() = "[syntax error in $row, $col] $msg"
}

class IsmaSemanticError(val msg: String) : IsmaError {
    override fun toString() = "[semantic error] $msg"
}