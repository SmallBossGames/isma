package ru.nstu.isma.core.sim.engine0;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Алексей on 03.10.14.
 */

public class E0SimulationContext {
    private double time;

    private double step;

    private double timeStart;

    private double timeStop;

    List<String> out = new LinkedList<>();

    private Map<String, E0SimulationVariable> consts = new HashMap<>();

    private Map<String, E0SimulationVariable> eqCurrent = new HashMap<>();

    private Map<String, E0SimulationVariable> eqNext = new HashMap<>();


    public void start() {
        time = timeStart;
        eqCurrent.values().stream().forEach(e -> eqNext.put(e.getName(), new E0SimulationVariable(e)));

    }

    public void next() {
        time += step;
        eqNext.values().stream().forEach(e -> eqCurrent.get(e.getName()).setValue(e.getValue()));
    }

    public double time() {
        return time;
    }

    public boolean isEnd() {
        return time >= timeStop;
    }


    public void addConst(String name, double val) {
        consts.put(name, new E0SimulationVariable(name, val));
    }

    public void addEq(String name, double val) {
        eqCurrent.put(name, new E0SimulationVariable(name, val));
        eqNext.put(name, new E0SimulationVariable(name, val));
    }

    public void setValue(String name, Double val) {
        getSimVar(name, false).setValue(val);
    }

    public Double getValue(String name,  boolean isCurrent) {
        return getSimVar(name, isCurrent).getValue();
    }

    private E0SimulationVariable getSimVar(String name, boolean isCurrent) {
        E0SimulationVariable c = consts.get(name);
        E0SimulationVariable e = isCurrent ? eqCurrent.get(name) : eqNext.get(name);
        if (c == null && e == null)
            throw new RuntimeException("Variable " + name + " not in context table");
        return c != null ? c : e;
    }

    public List<E0SimulationVariable> equations() {
        return eqCurrent.values().stream().collect(Collectors.toList());
    }


    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public double getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(double timeStart) {
        this.timeStart = timeStart;
    }

    public double getTimeStop() {
        return timeStop;
    }

    public void setTimeStop(double timeStop) {
        this.timeStop = timeStop;
    }

    public List<String> getOut() {
        return out;
    }

    public void setOut(List<String> out) {
        this.out = out;
    }
}
