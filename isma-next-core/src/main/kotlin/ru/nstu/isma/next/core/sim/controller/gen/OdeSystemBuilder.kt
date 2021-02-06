package ru.nstu.isma.next.core.sim.controller.gen

import common.ClassBuilder
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation
import ru.nstu.isma.intg.api.calcmodel.AlgebraicEquation
import ru.nstu.isma.intg.api.calcmodel.EventFunctionGroup.StepChoiceRule
import java.lang.IllegalStateException
import ru.nstu.isma.intg.api.calcmodel.HybridSystem
import ru.nstu.isma.intg.api.calcmodel.EventFunctionGroup
import java.util.stream.Collectors
import common.IndexMapper
import ru.nstu.isma.core.hsm.HSM
import common.JavaClassBuilder
import javax.tools.JavaFileManager
import java.util.Arrays
import javax.tools.JavaFileObject
import java.lang.RuntimeException
import java.lang.ClassNotFoundException
import java.lang.IllegalAccessException
import java.util.HashMap
import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import common.IndexProvider
import org.apache.commons.lang3.text.StrSubstitutor

/**
 * Created by Bessonov Alex on 07.01.15.
 */
class OdeSystemBuilder : ClassBuilder<HybridSystem?> {
    constructor(modelContext: IndexMapper?) : super(modelContext) {}
    constructor(hsm: HSM?) : super(hsm) {}

    fun build(name: String?, printJava: Boolean): HybridSystem {
        return super.build(name, "ru.nstu.isma.core.simulation.controller.", printJava)!!
    }

    override fun getJavaString(name: String): String {
        val javaClassBuilder = JavaClassBuilder(modelContext)
        return javaClassBuilder.buildHybridOdeSystem(name)
    }
}