package ru.nstu.isma.intg.api.calcmodel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Система дифференциально-алгебраических уравнений.
 */
public class DaeSystem implements Serializable {
    /**
     * Количество фрагментов правой части:
     * - для дифференциальных уравнений,
     * - для алгебраических.
     */
    public static final int RHS_PART_COUNT = 2;
    public static final int RHS_DE_PART_IDX = 0;
    public static final int RHS_AE_PART_IDX = 1;

    /**
     * Количество дифференциальных переменных.
     * Используется в распределенных вычислениях, поскольку там система становится не симметричной,
     * т.е. кол-во переменных != кол-ву уравнений.
     */
    private final int deVarCount;

    /** Дифференциальные уравнения */
    private final DifferentialEquation[] differentialEquations;

    /** Алгебраические уравнения */
    private final AlgebraicEquation[] algebraicEquations;

    private Map<Integer, Integer> deIndices;

    public DaeSystem(DifferentialEquation[] differentialEquations) {
        this.differentialEquations = differentialEquations;
        this.deVarCount = differentialEquations.length;
        this.algebraicEquations = new AlgebraicEquation[0];
    }

    public DaeSystem(DifferentialEquation[] differentialEquations, AlgebraicEquation[] algebraicEquations) {
        this.differentialEquations = differentialEquations;
        this.deVarCount = differentialEquations.length;
        this.algebraicEquations = algebraicEquations;
    }

    /** Используется только методом getSubsystem(...) */
    private DaeSystem(DifferentialEquation[] differentialEquations, int deVarCount, AlgebraicEquation[] algebraicEquations) {
        this.differentialEquations = differentialEquations;
        this.deVarCount = deVarCount;
        this.algebraicEquations = algebraicEquations;
    }

    /** Количество фрагментов правой части */
    public int getRhsPartCount() {
        return RHS_PART_COUNT;
    }

    /** Создает структуру, которую можно использовать при вычислении правой части. */
    public double[][] createEmptyRhs() {
        return new double[getRhsPartCount()][];
    }

    public DifferentialEquation[] getDifferentialEquations() {
        return differentialEquations;
    }

    public AlgebraicEquation[] getAlgebraicEquations() {
        return algebraicEquations;
    }

    public int getDifferentialEquationCount() {
        return differentialEquations.length;
    }

    public int getAlgebraicEquationCount() {
        return algebraicEquations.length;
    }

    public int getDifferentialVariableCount() {
        return deVarCount;
    }

    /**
     * Возвращает подсистему уравнений, содержащую ДУ заданного диапазона и все АУ.
     *
     * @param fromDeIdx начальный индекс ДУ, включающий.
     * @param toDeIdx   конечный индекс ДУ, невключающий.
     */
    public DaeSystem getSubsystem(int fromDeIdx, int toDeIdx) {
        return new DaeSystem(Arrays.copyOfRange(getDifferentialEquations(), fromDeIdx, toDeIdx), getDifferentialVariableCount(), getAlgebraicEquations());
    }

    public DaeSystem copy() {
        return new DaeSystem(
                Arrays.copyOf(getDifferentialEquations(), getDifferentialEquationCount()),
                getDifferentialVariableCount(),
                Arrays.copyOf(getAlgebraicEquations(), getAlgebraicEquationCount())
        );
    }

    public int mapDeIndexToArrayIndex(Integer deIndex) {
        if (deIndices == null) {
            deIndices = new HashMap<>(differentialEquations.length);
            for (int i = 0; i < differentialEquations.length; i++) {
                deIndices.put(differentialEquations[i].getIndex(), i);
            }
        }
        Integer arrayIndex = deIndices.get(deIndex);
        return arrayIndex != null ? arrayIndex : -1;
    }

    public boolean containsDifferentialEquation(Integer index) {
        return mapDeIndexToArrayIndex(index) >= 0;
    }

    public boolean containsAlgebraicEquation(Integer index) {
        return index >= 0 && index < algebraicEquations.length;
    }

}
