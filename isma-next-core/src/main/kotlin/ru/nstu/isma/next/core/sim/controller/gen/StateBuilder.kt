package ru.nstu.isma.next.core.sim.controller.gen

import ru.nstu.isma.intg.api.calcmodel.AlgebraicEquation
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation
import ru.nstu.isma.intg.api.calcmodel.Guard
import ru.nstu.isma.intg.api.calcmodel.HybridSystem
import java.util.*
import java.util.stream.Collectors

/**
 * @author Maria Nasyrova
 * @since  14.07.2016
 */
class StateBuilder(
        /** Название  */
        private val name: String?,
        /** HybridSystemBuilder, создавший состояние  */
        private val parent: HybridSystemBuilder) {
    /** ОДУ  */
    private val differentialEquations: MutableList<DifferentialEquation?>

    /** Алгебраические функции */
    private val algebraicEquations: MutableList<AlgebraicEquation?>

    /** Условия перехода из этого состояния  */
    private val guardBuilders: MutableList<GuardBuilder>

    /** ОДУ для выражений "set"  */
    private val setters: MutableList<DifferentialEquation?>

    /** Добавляет новое локальное состояние в ГС.  */
    fun addState(name: String?): StateBuilder? {
        return parent.addState(name)
    }

    /** Добавляет новое псевдосостояние в ГС.  */
    fun addPseudoState(name: String?): StateBuilder? {
        return parent.addPseudoState(name)
    }

    /** Добавляет ОДУ.  */
    fun addDifferentialEquation(de: DifferentialEquation?): StateBuilder {
        add(de, differentialEquations)
        return this
    }

    /** Добавляет алгебраическую функцию.  */
    fun addAlgebraicEquation(algebraicEquation: AlgebraicEquation?): StateBuilder {
        add(algebraicEquation, algebraicEquations)
        return this
    }

    /** Добавляет условие перехода в это состояние.  */
    fun addGuard(guard: Guard): GuardBuilder {
        val guardBuilder = GuardBuilder(guard, this)
        add(guardBuilder, guardBuilders)
        return guardBuilder
    }

    /** Добавляет алгебраическую функцию для выражений "set".  */
    fun addSetter(setter: DifferentialEquation?): StateBuilder {
        add(setter, setters)
        return this
    }

    /** Компонует локальное состояние ГС по сформированному описанию.  */
    fun toHybridSystemState(): HybridSystem.State {
        val guards = guardBuilders.stream()
                .map { obj: GuardBuilder -> obj.toGuard() }
                .collect(Collectors.toList())
        return HybridSystem.State(name, differentialEquations, algebraicEquations, guards, setters)
    }

    /** Компонует ГС по сформированному описанию.  */
    fun toHybridSystem(): HybridSystem? {
        return parent.toHybridSystem()
    }

    companion object {
        /** Добавляет элемент element в коллекцию to, если такого там еще нет.  */
        fun <T> add(element: T, to: MutableList<T>) {
            if (!to.contains(element)) {
                to.add(element)
            }
        }
    }

    init {
        differentialEquations = ArrayList()
        algebraicEquations = ArrayList()
        guardBuilders = ArrayList()
        setters = ArrayList()
    }
}