package ru.nstu.isma.core.sim.controller;

import ru.nstu.isma.intg.api.IntgMetricData;
import ru.nstu.isma.intg.api.IntgResultPoint;
import ru.nstu.isma.intg.api.calcmodel.*;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver;
import ru.nstu.isma.intg.core.methods.EventDetectionIntgController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
public class HybridSystemSimulator {

    private List<Consumer<Double>> stepChangeListeners = new ArrayList<>();

    public void addStepChangeListener(Consumer<Double> c) {
        stepChangeListeners.add(c);
    }

    public IntgMetricData run(HybridSystem hybridSystem, DaeSystemStepSolver stepSolver,
                              SimulationInitials simulationInitials,
                              EventDetectionIntgController eventDetector, double eventDetectionStepBoundLow,
                              Consumer<IntgResultPoint> resultPointConsumer) {
        IntgMetricData metricData = new IntgMetricData();
        metricData.setStartTime(System.currentTimeMillis());

        double x = simulationInitials.getStart();
        double end = simulationInitials.getEnd();
        double step = simulationInitials.getStep();
        double[] yForDe = simulationInitials.getDifferentialEquationInitials();

        HybridSystemChangeSet changeSet;

        double[][] rhs = stepSolver.calculateRhs(yForDe);

        boolean isLastStep = false;
        while (x < end && !Thread.currentThread().isInterrupted()) {
            changeSet = hybridSystem.checkTransitions(yForDe, rhs);
            if (!changeSet.isEmpty()) {
                changeInitials(yForDe, changeSet);
                stepSolver.apply(changeSet);
                rhs = stepSolver.calculateRhs(yForDe);
            }

            resultPointConsumer.accept(new IntgResultPoint(x, Arrays.copyOf(yForDe, yForDe.length), rhs));

            IntgPoint fromPoint = new IntgPoint(step, yForDe, rhs);
            IntgPoint toPoint = stepSolver.step(fromPoint);

            // TODO: сделать правильно, чтобы первый шаг тоже оценивался.
            if (eventDetector.isEnabled()) {
                List<Guard> guards = hybridSystem.getCurrentState().getGuards();
                if (guards != null) {
                    List<EventFunctionGroup> eventFunctionGroups = guards.stream()
                            .map(Guard::getEventFunctionGroup)
                            .collect(Collectors.toList());

                    double predictedStep = eventDetector.predictNextStep(toPoint, eventFunctionGroups);
                    // TODO: если так, то шагов около 8500
                    //double nextStep = Math.min(Math.max(predictedStep, eventDetectionStepBoundLow), toPoint.getNextStep());
                    // TODO: а если так, то 170
                    double nextStep = Math.min(Math.max(predictedStep, eventDetectionStepBoundLow), simulationInitials.getStep());
                    toPoint.setNextStep(nextStep);
                }
            }

            x += toPoint.getStep();

            step = toPoint.getNextStep();
            yForDe = toPoint.getY();
            rhs = toPoint.getRhs();

            if (isLastStep) { // Если мы прошли весь интервал, то запоминаем последние значения и выходим.
                resultPointConsumer.accept(new IntgResultPoint(x, Arrays.copyOf(yForDe, yForDe.length), rhs));
                break;
            } else if (x + step > end) { // Если нам нужно сделать последний шаг, то меняем значение
                step = end - x;
                isLastStep = true;
            }

            final double finalX = x;
            stepChangeListeners.stream().forEach(l -> l.accept(finalX));
        }

        metricData.setEndTime(System.currentTimeMillis());
        return metricData;
    }

    private void changeInitials(final double[] yForDe, HybridSystemChangeSet changeSet) {
        double[][] emptyRhs = new double[0][];
        changeSet.getInitialValueSetters().entrySet().stream().forEach(entry -> {
            DifferentialEquation setter = entry.getValue().getValue();
            yForDe[entry.getKey()] = setter.apply(yForDe, emptyRhs);
        });
    }

}
