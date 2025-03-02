package ru.nstu.isma.next.core.simulation.gen

import ru.nstu.isma.intg.api.calcmodel.EventFunctionGroup.StepChoiceRule
import ru.nstu.isma.intg.api.calcmodel.*

/**
 * @author Maria Nasyrova
 * @since 14.07.2016
 */
class GuardBuilder(private val guard: Guard, private val parent: StateBuilder) {
    private var eventFunctionGroupBuilder: EventFunctionGroupBuilder? = null
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

    fun addEventFunctionGroup(stepChoiceRule: StepChoiceRule?): EventFunctionGroupBuilder {
        check(eventFunctionGroupBuilder == null) { "you can call addEventFunctionGroup for guard only once" }
        eventFunctionGroupBuilder = EventFunctionGroupBuilder(stepChoiceRule, this)
        return eventFunctionGroupBuilder!!
    }

    fun toHybridSystemState(): HybridSystem.State? {
        return parent.toHybridSystemState()
    }

    fun toHybridSystem(): HybridSystem? {
        return parent.toHybridSystem()
    }

    fun toGuard(): Guard {
        val eventFunctionGroup = eventFunctionGroupBuilder!!.toEventFunctionGroup()
        guard.eventFunctionGroup = eventFunctionGroup
        return guard
    }
}