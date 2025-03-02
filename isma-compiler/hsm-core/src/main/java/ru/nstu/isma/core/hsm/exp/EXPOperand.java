package ru.nstu.isma.core.hsm.exp;

import ru.nstu.isma.core.hsm.var.HMVariable;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * Date: 29.11.13
 * Time: 0:57
 */
public class EXPOperand extends EXPToken implements Serializable {
    protected HMVariable variable;

    public EXPOperand() {
    }

    public EXPOperand(HMVariable variable) {
        this.variable = variable;
    }

    public HMVariable getVariable() {
        return variable;
    }

    public void setVariable(HMVariable variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return variable.toString();
    }
}
