package ru.nstu.isma.domain.compiler

import ru.nstu.isma.compiler.hsm.core.HSM

interface ICompiledModelStore {
    fun create(hsm: HSM): String
    fun get(id: String): HSM?
    fun delete(id: String): Boolean
    fun exists(id: String): Boolean
}
