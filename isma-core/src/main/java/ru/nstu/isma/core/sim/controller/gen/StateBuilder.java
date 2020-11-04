package ru.nstu.isma.core.sim.controller.gen;

import ru.nstu.isma.intg.api.calcmodel.AlgebraicEquation;
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.api.calcmodel.Guard;
import ru.nstu.isma.intg.api.calcmodel.HybridSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maria Nasyrova
 * @since  14.07.2016
 */
public class StateBuilder {

    /** Название */
    private final String name;

    /** HybridSystemBuilder, создавший состояние */
    private HybridSystemBuilder parent;

    /** ОДУ */
    private List<DifferentialEquation> differentialEquations;

    /** Алгебраические функции*/
    private List<AlgebraicEquation> algebraicEquations;

    /** Условия перехода из этого состояния */
    private List<GuardBuilder> guardBuilders;

    /** ОДУ для выражений "set" */
    private final List<DifferentialEquation> setters;

    /** Добавляет элемент element в коллекцию to, если такого там еще нет. */
    static <T> void add(T element, List<T> to) {
        if (!to.contains(element)){
            to.add(element);
        }
    }

    public StateBuilder(String name, HybridSystemBuilder parent) {
        this.name = name;
        this.parent = parent;
        this.differentialEquations = new ArrayList<>();
        this.algebraicEquations = new ArrayList<>();
        this.guardBuilders = new ArrayList<>();
        this.setters = new ArrayList<>();
    }

    /** Добавляет новое локальное состояние в ГС. */
    public StateBuilder addState(String name) {
        return parent.addState(name);
    }

    /** Добавляет новое псевдосостояние в ГС. */
    public StateBuilder addPseudoState(String name) {
        return parent.addPseudoState(name);
    }

    /** Добавляет ОДУ. */
    public StateBuilder addDifferentialEquation(DifferentialEquation de) {
        add(de, differentialEquations);
        return this;
    }

    /** Добавляет алгебраическую функцию. */
    public StateBuilder addAlgebraicEquation(AlgebraicEquation algebraicEquation) {
        add(algebraicEquation, algebraicEquations);
        return this;
    }

    /** Добавляет условие перехода в это состояние. */
    public GuardBuilder addGuard(Guard guard) {
        GuardBuilder guardBuilder = new GuardBuilder(guard, this);
        add(guardBuilder, guardBuilders);
        return guardBuilder;
    }

    /** Добавляет алгебраическую функцию для выражений "set". */
    public StateBuilder addSetter(DifferentialEquation setter) {
        add(setter, setters);
        return this;
    }

    /** Компонует локальное состояние ГС по сформированному описанию. */
    public HybridSystem.State toHybridSystemState() {
        List<Guard> guards = guardBuilders.stream()
                .map(GuardBuilder::toGuard)
                .collect(Collectors.toList());
        return new HybridSystem.State(name, differentialEquations, algebraicEquations, guards, setters);
    }

    /** Компонует ГС по сформированному описанию. */
    public HybridSystem toHybridSystem() {
        return parent.toHybridSystem();
    }
}
