package ru.nstu.isma.core.sim.controller.gen;

import ru.nstu.isma.intg.api.calcmodel.*;

import java.util.ArrayList;

/**
 * @author Maria Nasyrova
 * @since 14.07.2016
 */
public class EventFunctionGroupBuilder {
    private final GuardBuilder parent;

    private final EventFunctionGroup.StepChoiceRule stepChoiceRule;
    private final ArrayList<EventFunction> eventFunctions;

    public EventFunctionGroupBuilder(EventFunctionGroup.StepChoiceRule stepChoiceRule, GuardBuilder parent) {
        this.stepChoiceRule = stepChoiceRule;
        this.eventFunctions = new ArrayList<>();
        this.parent = parent;
    }

    public StateBuilder addState(String name) {
        return parent.addState(name);
    }

    public StateBuilder addPseudoState(String name) {
        return parent.addPseudoState(name);
    }

    public StateBuilder addDifferentialEquation(DifferentialEquation de) {
        return parent.addDifferentialEquation(de);
    }

    public StateBuilder addAlgebraicEquation(AlgebraicEquation ae) {
        return parent.addAlgebraicEquation(ae);
    }

    public GuardBuilder addGuard(Guard guard) {
        return parent.addGuard(guard);
    }

    public StateBuilder addSetter(DifferentialEquation setter) {
        return parent.addSetter(setter);
    }

    // Сознательно запрещен
    // public EventFunctionGroupBuilder addEventFunctionGroup(EventFunctionGroup.StepChoiceRule stepChoiceRule) {
    // }

    public EventFunctionGroupBuilder addEventFunction(EventFunction eventFunction) {
        StateBuilder.add(eventFunction, eventFunctions);
        return this;
    }

    public HybridSystem.State toHybridSystemState() {
        return parent.toHybridSystemState();
    }

    public HybridSystem toHybridSystem() {
        return parent.toHybridSystem();
    }

    public Guard toGuard() {
        return parent.toGuard();
    }

    public EventFunctionGroup toEventFunctionGroup() {
        return new EventFunctionGroup(stepChoiceRule, eventFunctions);
    }
}
