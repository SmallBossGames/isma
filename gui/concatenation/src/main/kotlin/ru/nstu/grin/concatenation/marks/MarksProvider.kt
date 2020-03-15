package ru.nstu.grin.concatenation.marks

interface MarksProvider {
    fun getMark(current: Double, step: Double): String
}