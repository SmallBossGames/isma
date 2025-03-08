package ru.nstu.isma.intg.demo.problems;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitialsLegacy;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;

public class TwoDimensionalCauchyProblem extends CauchyProblem {

    public TwoDimensionalCauchyProblem() {
        super.setCauchyInitials(createInitials());
        super.setDaeSystem(createDaeSystem());
    }

    private static CauchyInitialsLegacy createInitials() {
        CauchyInitialsLegacy cauchyInitials = new CauchyInitialsLegacy();

        cauchyInitials.setInterval(0, 50);
        cauchyInitials.setStepSize(0.5);
        cauchyInitials.setY0(new double[] { 0.1, 0 });

        return cauchyInitials;
    }

    private static DaeSystem createDaeSystem() {
        DifferentialEquation[] des = new DifferentialEquation[2];

        des[0] = new DifferentialEquation(0, (y, rhs) -> y[1], "y[1]");
        des[1] = new DifferentialEquation(1, (y, rhs) -> -y[0] - 0.1 * y[1], "-y[0] - 0.1 * y[1]");

        return new DaeSystem(des);
    }
}