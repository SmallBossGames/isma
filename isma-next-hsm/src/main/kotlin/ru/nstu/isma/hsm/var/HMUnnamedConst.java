package ru.nstu.isma.hsm.var;

import ru.nstu.isma.hsm.exp.HMExpression;

import java.io.Serializable;
import java.util.UUID;

/**
 * by
 * Bessonov Alex.
 * Date: 14.11.13 Time: 0:23
 */
public class HMUnnamedConst extends HMConst implements Serializable {
    public HMUnnamedConst(double value) {
        super("unnamed_const@" + UUID.randomUUID().toString());
        this.value = value;
    }

    @Override
    public HMExpression getRightPart() {
        return HMExpression.getConst(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
