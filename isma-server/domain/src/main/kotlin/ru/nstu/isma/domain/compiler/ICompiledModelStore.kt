package ru.nstu.isma.domain.compiler

import ru.nstu.isma.compiler.hsm.core.IHSM

interface ICompiledModelStore {
    fun create(hsm: IHSM): String
    fun get(id: String): IHSM?
    fun delete(id: String): Boolean
    fun exists(id: String): Boolean
}
