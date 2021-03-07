package ru.nstu.isma.hsm.var;

import ru.nstu.isma.hsm.exp.HMExpression;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:29
 */
public class HMEquation extends HMVariable implements Serializable {

    protected HMExpression rightPart;

    public HMExpression getRightPart() {
        return rightPart;
    }

    public void setRightPart(HMExpression rightPart) {
        this.rightPart = rightPart;
    }

}
