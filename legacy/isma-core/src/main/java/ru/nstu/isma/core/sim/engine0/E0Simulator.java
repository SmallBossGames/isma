package ru.nstu.isma.core.sim.engine0;

import error.IsmaErrorList;
import ru.nstu.isma.core.hsm.*;
import ru.nstu.isma.core.hsm.hybrid.HMState;
import ru.nstu.isma.core.hsm.var.*;
import ru.nstu.isma.core.sim.IsmaSimulator;
import ru.nstu.isma.core.common.SimulationResult;

/**
 * Created by Алексей on 03.10.14.
 */
public class E0Simulator implements IsmaSimulator {
    private HSM model;

    private E0SimulationContext context;

    private IsmaErrorList errors;

    private HMState currentState;

    public E0Simulator(HSM model, E0SimulationContext context, IsmaErrorList errors) {
        this.model = model;
        this.context = context;
        this.errors = errors;
    }

    @Override
    public SimulationResult simulate() {
        currentState = model.getAutomata().getInit();
        prepeareValueTable();
        SimulationResult res = new SimulationResult();
        context.equations().stream().forEach(e -> res.addEquation(e.getName()));

        context.start();

        E0Calculator calc = new E0Calculator(errors);

        while (!context.isEnd()) {
            res.addX(context.time());
            context.equations().stream()
                    .forEach(eq -> res.addY(eq.getName(), context.getValue(eq.getName(), true)));

            context.equations().stream().forEach(e -> {
                HMEquation eq = (HMEquation) currentState.getVariables().get(e.getName());
                if (eq == null) {
                    System.out.println("NPE");
                }
                context.setValue(eq.getCode(), calc.calculate(eq, context, false));
            });
            context.next();


            E0StateChangeController log = new E0StateChangeController(errors, context);
            model.getAutomata().getTransactions().stream()
                    .filter(e -> e.getSource().getCode().equals(currentState.getCode()))
                    .forEach(e -> {
                        if ((Boolean) log.calculate(e.getCondition())) {
                            currentState = e.getTarget();
                            currentState.getVariables().getSetters().keySet().stream()
                                    .forEach(ee -> {
                                        Double set = calc.calculate(currentState.getVariables().getSetters().get(ee), false);
                                        context.setValue(ee, set);
                                    });
                        }
                    });

        }
        return res;
    }


    private void prepeareValueTable() {
        model.getVariables().stream()
                .filter(e -> e instanceof HMConst)
                .filter(e -> !(e instanceof HMUnnamedConst))
                .forEach(e -> context.addConst(e.getCode(), ((HMConst) e).getValue()));

        model.getVariables().stream()
                .filter(e -> e instanceof HMDerivativeEquation)
                .forEach(e -> context.addEq(e.getCode(), ((HMDerivativeEquation) e).getInitialValue()));

        E0Calculator calc = new E0Calculator(errors);

        model.getVariables().stream()
                .filter(e -> e instanceof HMAlgebraicEquation)
                .forEach(e -> context.addEq(e.getCode(), calc.calculate((HMEquation) e, context, true)));

    }


    private double f(double x) {
        return Math.sin(x);
    }

}
