package ru.nstu.isma.hsm.exp.cond;

import ru.nstu.isma15.hsm.exp.EXPOperator;
import ru.nstu.isma15.hsm.var.HMVariable;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:56
 */
public class HMAtom extends HMVariable implements Serializable {

    protected HMVariable left;

    protected HMVariable right;

    protected EXPOperator op;

    public HMVariable getLeft() {
        return left;
    }

    public void setLeft(HMVariable left) {
        this.left = left;
    }

    public HMVariable getRight() {
        return right;
    }

    public void setRight(HMVariable right) {
        this.right = right;
    }

    public EXPOperator getOp() {
        return op;
    }

    public void setOp(EXPOperator op) {
        this.op = op;
    }
}
