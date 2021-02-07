package ru.nstu.isma.next.core.sim.controller.gen

import ru.nstu.isma.intg.api.calcmodel.EventFunctionGroup.StepChoiceRule
import ru.nstu.isma.intg.api.calcmodel.*
import java.util.ArrayList

/**
 * @author Maria Nasyrova
 * @since 14.07.2016
 */
class EventFunctionGroupBuilder(private val stepChoiceRule: StepChoiceRule?, private val parent: GuardBuilder) {
    private val eventFunctions = ArrayList<EventFunction>()

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
        StateBuilder.add(eventFunction, eventFunctions)
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

}