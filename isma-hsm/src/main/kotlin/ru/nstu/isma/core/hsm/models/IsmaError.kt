package ru.nstu.isma.core.hsm.models

sealed interface IsmaError

class IsmaSyntaxError(var row: Int, var col: Int, var msg: String) : IsmaError {
    override fun toString(): String = ("[syntax error in $row, $col] $msg")
}

class IsmaSemanticError(val msg: String) : IsmaError {
    override fun toString(): String = "[semantic error] $msg"
}