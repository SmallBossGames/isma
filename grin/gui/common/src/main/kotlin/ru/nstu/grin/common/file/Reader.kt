package ru.nstu.grin.common.file

import java.io.ObjectInputStream

interface Reader<T> {
    fun deserialize(ois: ObjectInputStream): T
}