package ru.nstu.isma.core.hsm.var;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:18
 */
public class HMConst extends HMEquation implements Serializable {

    protected Double value;

    public HMConst(String code) {
        this.code = code;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    // в случае если константа посчитана происходит проставление значения через setValue, иначе по умполчанию value = null
    public boolean isCalulated() {
        return value != null;
    }

}
