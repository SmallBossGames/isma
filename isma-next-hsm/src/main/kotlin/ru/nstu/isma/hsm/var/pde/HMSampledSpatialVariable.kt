package ru.nstu.isma.hsm.var.pde;

import ru.nstu.isma.hsm.var.HMConst;

import java.io.Serializable;

/**
 * Bessonov Alex.
 * Date: 04.12.13 Time: 0:17
 */
public class HMSampledSpatialVariable extends HMSpatialVariable implements Serializable {

    // апроксимировать по шагу или количеству точек
    protected ApproximateType type;

    // в зависимости от типа - шаг или количество точек
    protected HMConst apxVal;

    public Double getStepSize() {
        Double out = null;
        if (type == ApproximateType.BY_NUMBER_OF_PIECES) {
            out = (valTo.getValue() - valFrom.getValue()) / apxVal.getValue();
        } else if (type == ApproximateType.BY_STEP) {
           out = apxVal.getValue();
        }
        return out;
    }

    public Integer getPointsCount() {
        Double out = null;
        if (type == ApproximateType.BY_NUMBER_OF_PIECES) {
            out = apxVal.getValue();
        } else if (type == ApproximateType.BY_STEP) {
            out = (valTo.getValue() - valFrom.getValue()) / apxVal.getValue();
        }
        out = Math.ceil(out);
        return out.intValue();
    }

    public ApproximateType getType() {
        return type;
    }

    public void setType(ApproximateType type) {
        this.type = type;
    }

    public HMConst getApxVal() {
        return apxVal;
    }

    public void setApxVal(HMConst apxVal) {
        this.apxVal = apxVal;
    }

    public enum ApproximateType implements Serializable {
        BY_STEP, BY_NUMBER_OF_PIECES
    }
}
