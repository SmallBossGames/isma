package ru.nstu.isma.intg.api.calcmodel.cauchy;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;

import java.io.Serializable;

/**
 * Задача Коши.
 */
public class CauchyProblem implements Serializable {

    private CauchyInitials cauchyInitials;
    private DaeSystem daeSystem;

    /** Возвращает начальные условия. */
    public CauchyInitials getCauchyInitials() {
        return cauchyInitials;
    }

    public void setCauchyInitials(CauchyInitials cauchyInitials) {
        this.cauchyInitials = cauchyInitials;
    }

    /** Возвращает систему дифференциально-алгебраических уравнений. */
    public DaeSystem getDaeSystem() {
        return daeSystem;
    }

    public void setDaeSystem(DaeSystem daeSystem) {
        this.daeSystem = daeSystem;
    }
}