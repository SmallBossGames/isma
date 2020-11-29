package ru.nstu.isma.next.core.sim.controller.gen;

import ru.nstu.isma.intg.api.calcmodel.*;

/**
 * @author Maria Nasyrova
 * @since 14.07.2016
 */
public class GuardBuilder {
    private final StateBuilder parent;
    private final Guard guard;
    private EventFunctionGroupBuilder eventFunctionGroupBuilder;

    public GuardBuilder(Guard guard, StateBuilder parent) {
        this.parent = parent;
        this.guard = guard;
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

    public EventFunctionGroupBuilder addEventFunctionGroup(EventFunctionGroup.StepChoiceRule stepChoiceRule) {
        if (eventFunctionGroupBuilder != null) {
            throw new IllegalStateException("you can call addEventFunctionGroup for guard only once");
        }
        eventFunctionGroupBuilder = new EventFunctionGroupBuilder(stepChoiceRule, this);
        return eventFunctionGroupBuilder;
    }

    public HybridSystem.State toHybridSystemState() {
        return parent.toHybridSystemState();
    }

    public HybridSystem toHybridSystem() {
        return parent.toHybridSystem();
    }

    public Guard toGuard() {
        EventFunctionGroup eventFunctionGroup = eventFunctionGroupBuilder.toEventFunctionGroup();
        guard.setEventFunctionGroup(eventFunctionGroup);
        return guard;
    }
}
