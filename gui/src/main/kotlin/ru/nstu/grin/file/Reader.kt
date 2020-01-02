package ru.nstu.grin.file

import java.io.ObjectInputStream

interface Reader<T> {
    fun deserialize(ois: ObjectInputStream): T
}