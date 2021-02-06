package ru.nstu.isma.next.core.sim.controller.gen

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.common.collect.ImmutableMap
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
import ru.nstu.isma.core.hsm.`var`.HMAlgebraicEquation
import ru.nstu.isma.core.hsm.`var`.HMDerivativeEquation
import ru.nstu.isma.core.hsm.`var`.pde.HMPartialDerivativeEquation
import java.util.function.Consumer

/**
 * Генератор индексов для уравнений в расчетной модели.
 * Формирует независимые наборы индексов для дифференциальных (ДУ) и алгебраических уравнений (АУ).
 *
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
class EquationIndexProvider(hsm: HSM) : IndexProvider {
    private val deIndeces: BiMap<String, Int> = HashBiMap.create() // TODO: СЛАУ
    private val aeIndeces: BiMap<String, Int> = HashBiMap.create()

    /** Возвращает строку вида y[index] для ДУ, соответствующую уравнению по указанному код из HSM.  */
    override fun getDifferentialArrayCode(code: String): String {
        val template = "\${deArrayName}[\${deIndex}]"
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "deArrayName", DE_ARRAY_NAME,
                "deIndex", deIndeces[code]
        ))
    }

    /** Возвращает строку вида а.getValue(index) для АУ, соответствующей уравнению по указанному код из HSM.  */
    override fun getAlgebraicArrayCode(code: String): String {
        val template = "\${aeArrayName}.getValue(\${aeIndex})"
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "aeArrayName", AE_RESULT_PROVIDER_NAME,
                "aeIndex", aeIndeces[code]
        ))
    }

    /** Возвращает строку вида rhs[rhsAeIndex][index] для АУ в ДУ, соответствующей уравнению по указанному коду из HSM.  */
    override fun getAlgebraicArrayCodeForDifferentialEquation(aeCode: String): String {
        val template = "\${deRhsArrayName}[\${rhsAeIndex}][\${aeIndex}]"
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "deRhsArrayName", DE_RHS_ARRAY_NAME,
                "rhsAeIndex", DaeSystem.RHS_AE_PART_IDX,
                "aeIndex", aeIndeces[aeCode]
        ))
    }

    /** Возвращает сгенерированный индекс ДУ для указанного кода из HSM.  */
    fun getDifferentialEquationIndex(code: String): Int? {
        return deIndeces[code]
    }

    /** Возвращает сгенерированный индекс АУ для указанного кода из HSM.  */
    fun getAlgebraicEquationIndex(code: String): Int? {
        return aeIndeces[code]
    }

    /** Возвращает код ДУ из HSM для сгенерированного индекса.  */
    fun getDifferentialEquationCode(index: Int): String? {
        return deIndeces.inverse()[index]
    }

    /** Возвращает код АУ из HSM для сгенерированного индекса.  */
    fun getAlgebraicEquationCode(index: Int): String? {
        return aeIndeces.inverse()[index]
    }

    fun getDifferentialEquationCount(): Int {
        return deIndeces.size
    }

    fun getAlgebraicEquationCount(): Int {
        return aeIndeces.size
    }

    companion object {
        private const val DE_ARRAY_NAME = "y"
        private const val DE_RHS_ARRAY_NAME = "rhs"
        private const val AE_RESULT_PROVIDER_NAME = "a"
    }

    init {
        hsm.variableTable.odes.stream()
                .forEach(Consumer { ode: HMDerivativeEquation ->
                    if (ode is HMPartialDerivativeEquation) { // TODO изменить на список IsmaErrorList
                        // JUST IGNORE IT
//                        throw new RuntimeException("HMPartialDerivativeEquation not supported yet.");
                    }
                    deIndeces.putIfAbsent(ode.getCode(), deIndeces.size)
                })
        hsm.variableTable.algs.stream()
                .forEach(Consumer { ae: HMAlgebraicEquation -> aeIndeces.putIfAbsent(ae.getCode(), aeIndeces.size) })
    }
}