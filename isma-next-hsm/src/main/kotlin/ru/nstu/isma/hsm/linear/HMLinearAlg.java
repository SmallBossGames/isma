package ru.nstu.isma.hsm.linear;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Created by Bessonov Alex
 * on 25.03.2015.
 */
public class HMLinearAlg implements Serializable {
    private final int index;

    private final Function<double[], Double> function;

    private String description;

    public HMLinearAlg(int index, Function<double[], Double> function, String desc) {
        this.index = index;
        this.function = function;
        this.description = desc;
    }

    public int getIndex() {
        return index;
    }

    public double calculateRightMember(double[] y) {
        return function.apply(y);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
