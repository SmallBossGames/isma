package ru.nstu.isma.intg.api.calcmodel;

import java.io.Serializable;
import java.util.function.BiFunction;

public class AlgebraicEquation implements BiFunction<double[], AlgebraicEquationResultProvider, Double>, Serializable {

    public interface AeFunction extends BiFunction<double[], AlgebraicEquationResultProvider, Double>, Serializable {
    }

    private final int index;
    private final String name;
    private final String description;
    private AeFunction function;

    public AlgebraicEquation(int index, AeFunction function) {
        this("", index, function, "");
    }

    public AlgebraicEquation(String name, int index, AeFunction function) {
        this(name, index, function, "");
    }

    public AlgebraicEquation(int index, AeFunction function, String description) {
        this("", index, function, description);
    }

    public AlgebraicEquation(String name, int index, AeFunction function, String description) {
        this.index = index;
        this.name = name;
        this.function = function;
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Double apply(double[] y, AlgebraicEquationResultProvider resultProvider) {
        return function.apply(y, resultProvider);
    }

}
