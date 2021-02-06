package ru.nstu.isma.next.core.sim.controller.gen

import ru.nstu.isma.intg.api.calcmodel.EventFunctionGroup.StepChoiceRule
import java.lang.IllegalStateException
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
import common.IndexProvider
import org.apache.commons.lang3.text.StrSubstitutor
import ru.nstu.isma.intg.api.calcmodel.*
import java.util.ArrayList

/**
 * @author Maria Nasyrova
 * @since 14.07.2016
 */
class EventFunctionGroupBuilder(private val stepChoiceRule: StepChoiceRule?, parent: GuardBuilder) {
    private val parent: GuardBuilder
    private val eventFunctions: ArrayList<EventFunction>
    fun addState(name: String?): StateBuilder? {
        return parent.addState(name)
    }

    fun addPseudoState(name: String?): StateBuilder? {
        return parent.addPseudoState(name)
    }

    fun addDifferentialEquation(de: DifferentialEquation?): StateBuilder? {
        return parent.addDifferentialEquation(de)
    }

    fun addAlgebraicEquation(ae: AlgebraicEquation?): StateBuilder? {
        return parent.addAlgebraicEquation(ae)
    }

    fun addGuard(guard: Guard): GuardBuilder? {
        return parent.addGuard(guard)
    }

    fun addSetter(setter: DifferentialEquation?): StateBuilder? {
        return parent.addSetter(setter)
    }

    // Сознательно запрещен
    // public EventFunctionGroupBuilder addEventFunctionGroup(EventFunctionGroup.StepChoiceRule stepChoiceRule) {
    // }
    fun addEventFunction(eventFunction: EventFunction): EventFunctionGroupBuilder {
        StateBuilder.Companion.add<EventFunction>(eventFunction, eventFunctions)
        return this
    }

    fun toHybridSystemState(): HybridSystem.State? {
        return parent.toHybridSystemState()
    }

    fun toHybridSystem(): HybridSystem? {
        return parent.toHybridSystem()
    }

    fun toGuard(): Guard? {
        return parent.toGuard()
    }

    fun toEventFunctionGroup(): EventFunctionGroup {
        return EventFunctionGroup(stepChoiceRule, eventFunctions)
    }

    init {
        eventFunctions = ArrayList<EventFunction>()
        this.parent = parent
    }
}