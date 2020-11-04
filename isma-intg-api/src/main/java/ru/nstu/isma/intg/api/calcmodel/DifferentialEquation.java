package ru.nstu.isma.intg.api.calcmodel;

import java.io.Serializable;
import java.util.function.BiFunction;

public class DifferentialEquation implements Serializable {

    public interface DeFunction extends BiFunction<double[], double[][], Double>, Serializable {
    }

    private final int index;
    private final String name;
    private final String description;
    private DeFunction function;

    public DifferentialEquation(int index, DeFunction function) {
        this("", index, function, "");
    }

    public DifferentialEquation(String name, int index, DeFunction function) {
        this(name, index, function, "");
    }

    public DifferentialEquation(int index, DeFunction function, String description) {
        this("", index, function, description);
    }

    public DifferentialEquation(String name, int index, DeFunction function, String description) {
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

    public Double apply(double[] y, double[][] rhs) {
        return function.apply(y, rhs);
    }

    protected void setFunction(DeFunction function) {
        this.function = function;
    }

}
