package ru.nstu.isma.core.hsm.exp;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bessonov Alex on 19.02.14.
 */
public class EXPFunctionOperand extends EXPOperand implements Serializable {
    protected String name;

    protected List<HMExpression> args  = new LinkedList<HMExpression>();

    public EXPFunctionOperand(String name) {
        this.name = name;
    }

    public void addArgExpression(HMExpression a) {
        args.add(a);
    }

    public String getName() {
        return name;
    }

    public List<HMExpression> getArgs() {
        return args;
    }
}
