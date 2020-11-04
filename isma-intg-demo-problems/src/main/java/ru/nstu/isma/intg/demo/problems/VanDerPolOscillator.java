package ru.nstu.isma.intg.demo.problems;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;

/**
 * Created by Dmitry Dostovalov on 07.09.2015.
 */
public class VanDerPolOscillator extends CauchyProblem {

    public VanDerPolOscillator() {
        super.setCauchyInitials(createInitials());
        super.setDaeSystem(createDaeSystem());
    }

    private static CauchyInitials createInitials() {
        CauchyInitials cauchyInitials = new CauchyInitials();

        cauchyInitials.setInterval(0, 10);
        cauchyInitials.setStepSize(0.01);
        cauchyInitials.setY0(new double[] { 2.0, 0.0 });

        return cauchyInitials;
    }

    private static DaeSystem createDaeSystem() {
        DifferentialEquation[] des = new DifferentialEquation[2];

        des[0] = new DifferentialEquation(0, (y, rhs) -> y[1], "y[1]");
        des[1] = new DifferentialEquation(1, (y, rhs) -> 333.3 * (1 - y[0] * y[0]) * y[1] - y[0], "333.3*(1-y[0]*y[0])*y[1]-y[0]");

        return new DaeSystem(des);
    }
}
