package ru.nstu.grin.file

interface Reader<T> {
    fun deserialize(): T
}