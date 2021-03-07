package ru.nstu.isma.hsm.var.pde;

import ru.nstu.isma.hsm.exp.HMExpression;

import java.io.Serializable;

/**
 * by
 * Bessonov Alex.
 * Date: 04.12.13 Time: 0:26
 */
public class HMBoundaryCondition implements Serializable {
    private HMPartialDerivativeEquation equation;

    private Integer derOrder = 0;

    private HMSpatialVariable atApxVar = new HMSpatialVariable();

    private SideType side;

    private HMExpression value;

    private Type type;

    public void setEquation(HMPartialDerivativeEquation equation) {
        this.equation = equation;
    }

    public HMPartialDerivativeEquation getEquation() {
        return equation;
    }

    public HMSpatialVariable getSpatialVar() {
        return atApxVar;
    }

    public void setSpatialVar(HMSpatialVariable atApxVar) {
        this.atApxVar = atApxVar;
    }

    public SideType getSide() {
        return side;
    }

    public void setSide(SideType side) {
        this.side = side;
    }

    public HMExpression getValue() {
        return value;
    }

    public void setValue(HMExpression value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getDerOrder() {
        return derOrder;
    }

    public void setDerOrder(Integer derOrder) {
        this.derOrder = derOrder;
    }

    public enum SideType implements Serializable {
        LEFT, RIGHT, BOTH
    }

    public enum Type  implements Serializable {
        DIRICHLET, NEUMANN
    }
}
