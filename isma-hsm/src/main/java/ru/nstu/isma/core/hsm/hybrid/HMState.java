package ru.nstu.isma.core.hsm.hybrid;

import ru.nstu.isma.core.hsm.var.HMVariableTable;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:32
 */
public class HMState implements Serializable {
    protected String code;

    protected HMVariableTable variables;

    public HMState(String code) {
        variables = new HMVariableTable();
        this.code = code;
    }

    public HMVariableTable getVariables() {
        return variables;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HMState hmState = (HMState) o;

        return code.equals(hmState.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
