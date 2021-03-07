package ru.nstu.isma.hsm.var.pde;

import ru.nstu.isma.hsm.var.HMConst;
import ru.nstu.isma.hsm.var.HMVariable;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * on 23.03.2015.
 */
public class HMSpatialVariable extends HMVariable implements Serializable {
    // Начальный край
    protected HMConst valFrom;

    // Конечный край
    protected HMConst valTo;

    public HMConst getValFrom() {
        return valFrom;
    }

    public void setValFrom(HMConst valFrom) {
        this.valFrom = valFrom;
    }

    public HMConst getValTo() {
        return valTo;
    }

    public void setValTo(HMConst valTo) {
        this.valTo = valTo;
    }
}
