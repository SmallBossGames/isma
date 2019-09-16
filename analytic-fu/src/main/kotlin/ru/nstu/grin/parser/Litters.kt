package ru.nstu.grin.parser

enum class Litters(val ch: Char) {
    DEL('\\'),
    MULTIPLY('*'),
    MINUS('-'),
    PLUS('+'),
    LEFT_BRACKET('('),
    RIGHT_BRACKET(')')
}