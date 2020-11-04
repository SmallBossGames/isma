package ru.nstu.isma.core.hsm.var;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:31
 */
public class HMDerivativeEquation extends HMEquation implements Serializable {
    protected HMConst initial;

    public HMDerivativeEquation(String code) {
        this.code = code;
        initial = new HMUnnamedConst(0);
    }

    public HMConst getInitial() {
        return initial;
    }

    public Double getInitialValue() {
        if (initial == null)
            return 0d;

        if (initial.getValue() == null)
            return 0d;

        return initial.getValue();
    }

    public void setInitial(HMConst initial) {
        this.initial = initial;
    }

    public void setInitial(double initial) {
        this.initial = new HMUnnamedConst(initial);
    }
}
