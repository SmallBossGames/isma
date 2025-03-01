package ru.nstu.isma.next.core.simulation.gen

import common.ClassBuilder
import common.IndexMapper
import common.JavaClassBuilder
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.intg.api.calcmodel.HybridSystem

/**
 * Created by Bessonov Alex on 07.01.15.
 */
class OdeSystemBuilder(private val modelContext: IndexMapper?) : ClassBuilder<HybridSystem?>() {

    fun build(name: String?, printJava: Boolean): HybridSystem {
        return super.build(name, "ru.nstu.isma.core.simulation.controller.", printJava)!!
    }

    override fun getJavaString(name: String): String {
        val javaClassBuilder = JavaClassBuilder(modelContext)
        return javaClassBuilder.buildHybridOdeSystem(name)
    }
}