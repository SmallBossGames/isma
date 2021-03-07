package ru.nstu.isma.hsm.exp;

import ru.nstu.isma.hsm.var.HMVariable;
import ru.nstu.isma.hsm.var.pde.HMSampledSpatialVariable;
import ru.nstu.isma.hsm.var.pde.HMSpatialVariable;

import java.io.Serializable;

/**
 * by
 * Bessonov Alex.
 * Date: 04.12.13 Time: 1:51
 */
public class EXPPDEOperand extends EXPOperand implements Serializable {
    private HMSpatialVariable firstSpatialVariable;

    private HMSpatialVariable secondSpatialVariable;

    private Order order = Order.ONE;

    public EXPPDEOperand(HMVariable variable) {
        super(variable);
    }

    public HMSampledSpatialVariable getSampledFirstSpatialVar() {
        if (firstSpatialVariable instanceof HMSampledSpatialVariable)
            return (HMSampledSpatialVariable) firstSpatialVariable;
        else throw new RuntimeException("Variable " + firstSpatialVariable.getCode() + " can't be sampled!");
    }

    public HMSampledSpatialVariable getSampledSecondSpatialVar() {
        if (secondSpatialVariable instanceof HMSampledSpatialVariable)
            return (HMSampledSpatialVariable) secondSpatialVariable;
        else throw new RuntimeException("Variable " + secondSpatialVariable.getCode() + " can't be sampled!");
    }

    public HMSpatialVariable getFirstSpatialVariable() {
        return firstSpatialVariable;
    }

    public void setFirstSpatialVariable(HMSpatialVariable firstSpatialVariable) {
        this.firstSpatialVariable = firstSpatialVariable;
    }

    public HMSpatialVariable getSecondSpatialVariable() {
        return secondSpatialVariable;
    }

    public void setSecondSpatialVariable(HMSpatialVariable secondSpatialVariable) {
        this.secondSpatialVariable = secondSpatialVariable;
    }

    public boolean isMixedPDEOperand() {
        return firstSpatialVariable != null && secondSpatialVariable != null;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public enum Order implements Serializable {

        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);
        int i;

        Order(int i) {
            this.i = i;
        }

        public static Order getByCode(Integer code) {
            switch (code) {
                case 1:
                    return ONE;
                case 2:
                    return TWO;
                case 3:
                    return THREE;
                case 4:
                    return FOUR;
                case 5:
                    return FIVE;
                default:
                    return null;

            }
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getVariable().getCode());
        sb.append(" BY ");
        sb.append(getSampledFirstSpatialVar().getCode());
        sb.append(" ON ");
        sb.append(order.name());
        return sb.toString();
    }
}
