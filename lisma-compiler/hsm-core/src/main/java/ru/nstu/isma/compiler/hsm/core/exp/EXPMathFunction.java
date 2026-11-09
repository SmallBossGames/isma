package ru.nstu.isma.compiler.hsm.core.exp;

import ru.nstu.isma.compiler.hsm.core.var.HMVariable;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * on 24.03.2015.
 */
public class EXPMathFunction extends EXPFunctionOperand implements Serializable {
    public EXPMathFunction(HMVariable var) {
        super(var.getCode());
        this.variable = var;
    }
}
