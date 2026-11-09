package ru.nstu.isma.compiler.hsm.core.linear;

import ru.nstu.isma.compiler.hsm.core.var.HMVariable;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * on 13.03.2015.
 */
public class HMLinearVar extends HMVariable implements Serializable {
    private Integer columnIndex;

    public HMLinearVar() {
    }

    public HMLinearVar(String code) {
        super(code);
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }
}
