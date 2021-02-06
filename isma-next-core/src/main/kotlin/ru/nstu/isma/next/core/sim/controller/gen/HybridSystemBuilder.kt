package ru.nstu.isma.next.core.sim.controller.gen

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
 * Компоновщик гибридной системы.
 *
 * Фрагмент кода на примере ГС "мячик": см. AnalyzedHybridSystemClassBuilderTest.
 *
 * Поскольку для расчетной модели удобнее указывать исходящие связи, то для псевдосостояний необходимо создавать
 * дополнительное начальное псевдосостояние с именем [HybridSystem.INIT_PSEUDO_STATE].
 *
 * Основная система ДАУ формируется по состоянию (@link ru.nstu.isma.core.hsm.HSM.INIT_STATE}.
 * В остальных состояниях уравнения только переопределяют первоначальные. Т.е. дублировать их необязательно.
 *
 * // TODO: алгебраические и СЛАУ.
 *
 * @author Maria Nasyrova
 * @since  05.10.2015
 */
class HybridSystemBuilder {
    /** Компоновщики состояний гибридной системы  */
    private val stateBuilders: MutableMap<String?, StateBuilder>

    /** Компоновщики псевдосостояний гибридной системы (например, предикаты условных блоков)  */
    private val pseudoStateBuilders: MutableMap<String?, StateBuilder>

    /**
     * Добавляет новое состояние.
     * @param  name имя состояния
     * @return      добавленное состояние
     */
    fun addState(name: String?): StateBuilder {
        val stateBuilder = StateBuilder(name, this)
        stateBuilders[name] = stateBuilder
        return stateBuilder
    }

    /**
     * Добавляет новое псевдосостояние.
     * @param  name имя псевдосостояния
     * @return      добавленное псевдосостояние
     */
    fun addPseudoState(name: String?): StateBuilder {
        val pseudoStateBuilder = StateBuilder(name, this)
        pseudoStateBuilders[name] = pseudoStateBuilder
        return pseudoStateBuilder
    }

    /** Компонует ГС по сформированному описанию.  */
    fun toHybridSystem(): HybridSystem {
        // Компонуем локальные состояния ГС.
        val states: MutableMap<String, HybridSystem.State?> = HashMap()
        for (stateBuilder in stateBuilders.values) {
            val state = stateBuilder.toHybridSystemState()
            states[state!!.name] = state
        }

        // Компонуем псевдосостояния ГС.
        val pseudoStates: MutableMap<String, HybridSystem.State?> = HashMap()
        for (stateBuilder in pseudoStateBuilders.values) {
            val pseudoState = stateBuilder.toHybridSystemState()
            pseudoStates[pseudoState!!.name] = pseudoState
        }

        // Компонуем основную систему ДАУ.
        val initState = states[HSM.INIT_STATE]

        // Получаем сформированнный набор ОДУ
        val initStateDes = initState!!.differentialEquations
        val des = initStateDes.toTypedArray()

        // Получаем сформированный набор алгебраических функций
        val initStateAes = initState.algebraicEquations
        val aes = initStateAes.toTypedArray()
        val daeSystem = DaeSystem(des, aes)
        return HybridSystem(daeSystem, states, pseudoStates)
    }

    init {
        stateBuilders = HashMap()
        pseudoStateBuilders = HashMap()
    }
}