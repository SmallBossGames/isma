package ru.nstu.isma.hsm.linear

import ru.nstu.isma.hsm.HSM
import ru.nstu.isma.hsm.common.ClassBuilder
import ru.nstu.isma.hsm.common.IndexMapper
import ru.nstu.isma.hsm.common.JavaClassBuilder

/**
 * Created by Bessonov Alex
 * on 14.03.2015.
 */
class LinearSystemMatrixBuilder : ClassBuilder<LinearSystemMatrix?> {
    constructor(modelContext: IndexMapper?) : super(modelContext) {}
    constructor(hsm: HSM) : super(hsm) {}

    fun build(name: String?, printJava: Boolean): LinearSystemMatrix? {
        return super.build(name!!, "ru.nstu.isma15.hsm.linear.", printJava)
    }

    override fun getJavaString(name: String): String? {
        val javaClassBuilder = JavaClassBuilder(modelContext!!)
        return javaClassBuilder.printLinearSystemMatrix(name)
    }
}