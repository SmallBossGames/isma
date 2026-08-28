package ru.nstu.isma.intg.demo.problems;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitialsLegacy;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;

public class FourDimensionalCauchyProblem extends CauchyProblem {

    public FourDimensionalCauchyProblem() {
        super.setCauchyInitials(createInitials());
        super.setDaeSystem(createDaeSystem());
    }

    private static CauchyInitialsLegacy createInitials() {
        CauchyInitialsLegacy cauchyInitials = new CauchyInitialsLegacy();

        cauchyInitials.setInterval(0, 4.5);
        cauchyInitials.setStepSize(0.3);
        cauchyInitials.setY0(new double[] { 0.95, 0.3, 0.5, 0 });

        return cauchyInitials;
    }

    private static DaeSystem createDaeSystem() {
        DifferentialEquation[] des = new DifferentialEquation[4];

        des[0] = new DifferentialEquation(0, (y, rhs) -> -1.2 * y[0], "-1.2 * y[0]");
        des[1] = new DifferentialEquation(1, (y, rhs) -> 0.6 * y[0] - 0.2 * y[1] + 0.4 * y[2] * y[3] * y[3],
                "0.6 * y[0] - 0.2 * y[1] + 0.4 * y[2] * y[3] * y[3]");
        des[2] = new DifferentialEquation(2, (y, rhs) -> 0.2 * y[1] - 0.4 * y[2] * y[3] * y[3],
                "0.2 * y[1] - 0.4 * y[2] * y[3] * y[3]");
        des[3] = new DifferentialEquation(3, (y, rhs) -> 0.4 * y[1] - 0.8 * y[2] * y[3] * y[3],
                "0.4 * y[1] - 0.8 * y[2] * y[3] * y[3]");

        return new DaeSystem(des);
    }
}
