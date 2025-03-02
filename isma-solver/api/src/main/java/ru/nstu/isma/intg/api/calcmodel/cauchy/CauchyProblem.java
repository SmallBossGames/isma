package ru.nstu.isma.intg.api.calcmodel.cauchy;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;

import java.io.Serializable;

/**
 * Задача Коши.
 */
public class CauchyProblem implements Serializable {

    private CauchyInitialsLegacy cauchyInitials;
    private DaeSystem daeSystem;

    /** Возвращает начальные условия. */
    public CauchyInitialsLegacy getCauchyInitials() {
        return cauchyInitials;
    }

    public void setCauchyInitials(CauchyInitialsLegacy cauchyInitials) {
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