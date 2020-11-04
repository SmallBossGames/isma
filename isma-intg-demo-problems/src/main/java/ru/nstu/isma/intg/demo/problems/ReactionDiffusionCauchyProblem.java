package ru.nstu.isma.intg.demo.problems;

import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;
import ru.nstu.isma.intg.demo.problems.lotkaVolterra.*;
import ru.nstu.isma.intg.demo.problems.lotkaVolterra.utils.Pair;

public class ReactionDiffusionCauchyProblem extends CauchyProblem {

    public ReactionDiffusionCauchyProblem(int J, int K) {
        ReactionDiffusionLVProblemTemplate template = new ReactionDiffusionLVProblemTemplate();
        LVProblem lvProblem = template.generateLVProblem(J, K);

        setDaeSystem(lvProblem.getDaeSystem());

        CauchyInitials cauchyInitials = new CauchyInitials();
        cauchyInitials.setInterval(0, 1.0);
        cauchyInitials.setStepSize(0.001);
        cauchyInitials.setY0(lvProblem.getY0());
        setCauchyInitials(cauchyInitials);
    }

    private class ReactionDiffusionLVProblemTemplate extends LVProblemTemplate {
        @Override
        public LVHabitatArea getHabitatArea() {
            Pair<Double, Double> onX = new Pair<Double, Double>(0.0, 1.0);
            Pair<Double, Double> onZ = new Pair<Double, Double>(0.0, 1.0);
            return new LVHabitatArea(onX, onZ);
        }

        @Override
        public double getD1() {
            return 0.05;
        }

        @Override
        public double getD2() {
            return 1;
        }

        @Override
        public LVFunction getF1() {
            return new LVFunction() {
                @Override
                public double calculate(double arg1, double arg2) {
                    double b1 = 1;
                    double a12 = 0.1;
                    double f = arg1 * (b1 - a12 * arg2);
                    double roundedF = Math.round(f * 100000) / 100000.0; // Округляем все числа до 5 знаков. Отрефакторить!
                    return roundedF;
                }
            };
        }

        @Override
        public LVFunction getF2() {
            return new LVFunction() {
                @Override
                public double calculate(double arg1, double arg2) {
                    double b2 = 1000;
                    double a21 = 100;
                    double f = arg2 * (- b2 + a21 * arg1);
                    double roundedF = Math.round(f * 100000) / 100000.0; // Округляем все числа до 5 знаков. Отрефакторить!
                    return roundedF;
                }
            };
        }

        @Override
        public LVFunction getC1() {
            return new LVFunction() {
                @Override
                public double calculate(double arg1, double arg2) {
                    double c = 10 - 5 * Math.cos(Math.PI * arg1) * Math.cos(10 * Math.PI * arg2);
                    return c;
                }
            };
        }

        @Override
        public LVFunction getC2() {
            return new LVFunction() {
                @Override
                public double calculate(double arg1, double arg2) {
                    double c = 17 + 5 * Math.cos(10 * Math.PI * arg1) * Math.cos(Math.PI * arg2);
                    return c;
                }
            };
        }

        @Override
        public LVBoundaryCondition getBoundaryCondition1() {
            int J = getJ();
            return new LVBoundaryCondition(-1, 1, J, J - 2);
        }

        @Override
        public LVBoundaryCondition getBoundaryCondition2() {
            int K = getK();
            return new LVBoundaryCondition(-1, 1, K, K - 2);
        }
    }
}
