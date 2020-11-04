package ru.nstu.isma.core.sim.controller;

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
public class SimulationInitials {

    private final double start;
    private final double end;
    private final double step;
    private final double[] differentialEquationInitials;

    public SimulationInitials(double[] differentialEquationInitials, double step, double start, double end) {
        this.differentialEquationInitials = differentialEquationInitials;
        this.step = step;
        this.end = end;
        this.start = start;
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    public double getStep() {
        return step;
    }

    public double[] getDifferentialEquationInitials() {
        return differentialEquationInitials;
    }

}
