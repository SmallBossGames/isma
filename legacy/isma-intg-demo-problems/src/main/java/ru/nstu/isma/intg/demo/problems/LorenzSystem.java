package ru.nstu.isma.intg.demo.problems;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitialsLegacy;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;

/**
 * Created by Dmitry Dostovalov on 07.09.2015.
 */

public class LorenzSystem extends CauchyProblem {

    public LorenzSystem() {
        super.setCauchyInitials(createInitials());
        super.setDaeSystem(createDaeSystem());
    }

    private static CauchyInitialsLegacy createInitials() {
        CauchyInitialsLegacy cauchyInitials = new CauchyInitialsLegacy();

        cauchyInitials.setInterval(0, 10);
        cauchyInitials.setStepSize(0.01);
        cauchyInitials.setY0(new double[] { 5.0, 5.0, 5.0 });

        return cauchyInitials;
    }

    private static DaeSystem createDaeSystem() {
        DifferentialEquation[] des = new DifferentialEquation[3];

        des[0] = new DifferentialEquation(0, (y, rhs) -> 10.0 * (y[1] - y[0]), "10*(y[1]-y[0])");
        des[1] = new DifferentialEquation(1, (y, rhs) -> y[0] * (28.0 - y[2]) - y[1], "y[0]*(28-y[2])-y[1]");
        des[2] = new DifferentialEquation(2, (y, rhs) -> y[0] * y[1] - 8.0 * y[2] / 3.0, "y[0]*y[1]-8*y[2]/3");

        return new DaeSystem(des);
    }

}
