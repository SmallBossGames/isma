package ru.nstu.isma.next.core.sim.controller.gen;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import common.IndexProvider;
import org.apache.commons.lang3.text.StrSubstitutor;
import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.core.hsm.var.pde.HMPartialDerivativeEquation;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;

/**
 * Генератор индексов для уравнений в расчетной модели.
 * Формирует независимые наборы индексов для дифференциальных (ДУ) и алгебраических уравнений (АУ).
 *
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
public class EquationIndexProvider implements IndexProvider {

    private static final String DE_ARRAY_NAME = "y";
    private static final String DE_RHS_ARRAY_NAME = "rhs";
    private static final String AE_RESULT_PROVIDER_NAME = "a";

    private BiMap<String, Integer> deIndeces = HashBiMap.create();      // TODO: СЛАУ
    private BiMap<String, Integer> aeIndeces = HashBiMap.create();

    public EquationIndexProvider(HSM hsm) {
        hsm.getVariableTable().getOdes().stream()
                .forEach(ode -> {
                    if (ode instanceof HMPartialDerivativeEquation) { // TODO изменить на список IsmaErrorList
                        // JUST IGNORE IT
//                        throw new RuntimeException("HMPartialDerivativeEquation not supported yet.");
                    }
                    deIndeces.putIfAbsent(ode.getCode(), deIndeces.size());
                });

        hsm.getVariableTable().getAlgs().stream()
                .forEach(ae -> aeIndeces.putIfAbsent(ae.getCode(), aeIndeces.size()));
    }

    /** Возвращает строку вида y[index] для ДУ, соответствующую уравнению по указанному код из HSM. */
    @Override
    public String getDifferentialArrayCode(String code) {
        final String template = "${deArrayName}[${deIndex}]";
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "deArrayName", DE_ARRAY_NAME,
                "deIndex", deIndeces.get(code)
        ));
    }

    /** Возвращает строку вида а.getValue(index) для АУ, соответствующей уравнению по указанному код из HSM. */
    @Override
    public String getAlgebraicArrayCode(String code) {
        final String template = "${aeArrayName}.getValue(${aeIndex})";
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "aeArrayName", AE_RESULT_PROVIDER_NAME,
                "aeIndex", aeIndeces.get(code)
        ));
    }

    /** Возвращает строку вида rhs[rhsAeIndex][index] для АУ в ДУ, соответствующей уравнению по указанному коду из HSM. */
    @Override
    public String getAlgebraicArrayCodeForDifferentialEquation(String aeCode) {
        final String template = "${deRhsArrayName}[${rhsAeIndex}][${aeIndex}]";
        return StrSubstitutor.replace(template, ImmutableMap.of(
                "deRhsArrayName", DE_RHS_ARRAY_NAME,
                "rhsAeIndex", DaeSystem.RHS_AE_PART_IDX,
                "aeIndex", aeIndeces.get(aeCode)
        ));
    }

    /** Возвращает сгенерированный индекс ДУ для указанного кода из HSM. */
    public Integer getDifferentialEquationIndex(String code) {
        return deIndeces.get(code);
    }

    /** Возвращает сгенерированный индекс АУ для указанного кода из HSM. */
    public Integer getAlgebraicEquationIndex(String code) {
        return aeIndeces.get(code);
    }

    /** Возвращает код ДУ из HSM для сгенерированного индекса. */
    public String getDifferentialEquationCode(Integer index) {
        return deIndeces.inverse().get(index);
    }

    /** Возвращает код АУ из HSM для сгенерированного индекса. */
    public String getAlgebraicEquationCode(Integer index) {
        return aeIndeces.inverse().get(index);
    }

    public int getDifferentialEquationCount() { return deIndeces.size(); }

    public int getAlgebraicEquationCount() { return aeIndeces.size(); }

}
