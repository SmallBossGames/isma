package ru.nstu.isma.hsm.linear;

import ru.nstu.isma.hsm.exp.HMExpression;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Bessonov Alex
 * on 13.03.2015.
 */
public class HMLinearEquation implements Serializable {
    private final ArrayList<HMExpression> leftPart;
    private HMExpression rightPart;
    private final Integer size;

    public HMLinearEquation(Integer size) {
        this.size = size;
        leftPart = new ArrayList<>(size);
    }

    public HMExpression getRightPart() {
        return rightPart;
    }

    public void setRightPart(HMExpression rightPart) {
        this.rightPart = rightPart;
    }

    public void setEquationElem(HMLinearVar var, HMExpression expression) {
        leftPart.add(var.getColumnIndex(), expression);
    }

    public ArrayList<HMExpression> getLeftPart() {
        return leftPart;
    }
}
