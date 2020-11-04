package ru.nstu.isma.core.sim.fdm;

import ru.nstu.isma.core.hsm.var.HMConst;
import ru.nstu.isma.core.hsm.var.pde.HMSampledSpatialVariable;

/**
 * Created by Bessonov Alex
 * Date: 12.12.13
 * Time: 1:02
 * Класс индекса апроксимируемой переменной
 */
public class FDMIndexedApxVar extends HMSampledSpatialVariable {
    protected Integer index;

    public FDMIndexedApxVar(HMSampledSpatialVariable v) {
        linkWithHMApproximateVariable(v);
    }

    // связываем текущий объект с объектом HMApproximateVariable
    // все изменения в базовых полях индекса дожны отразиьться в предке
    public void linkWithHMApproximateVariable(HMSampledSpatialVariable v) {
        valFrom = v.getValFrom();
        valTo = v.getValTo();
        type = v.getType();
        apxVal = v.getApxVal();
        code = v.getCode();
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        if (!validate(index)) {
            throw new RuntimeException("index is not valid! index ="
                    + index.toString() + " but range = [1; " + getPointsCount().toString() + "].");
        }
        this.index = index;
    }

    // индекс может быть в диапазоне [1; PointsCount]
    public boolean isValid() {
        return validate(index);
    }

    private boolean validate(int idx) {
        return (idx > 0 && idx <= getPointsCount());
    }

    public boolean isMax() {
        return index.equals(getPointsCount());
    }

    public boolean isFirst() {
        return index.equals(1);
    }

    public void inc() {
        index++;
    }

    public double getValue() {
        return getValFrom().getValue() + (index-1)*getStepSize();
    }

    public HMConst toConst() {
        HMConst hmConst = new HMConst(getConstCode());
        hmConst.setValue(getValue());
        return hmConst;
    }

    public String getConstCode() {
        return code + FDMStatic.APX_PREFIX + "_" + index;
    }
}