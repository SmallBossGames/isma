package ru.nstu.isma.core.sim.engine0;

/**
 * Created by Алексей on 03.10.14.
 */
public class E0SimulationVariable {
    private String name;

    private Double value;

    public E0SimulationVariable(String name, Double initial) {
        this.name = name;
        this.value = initial;
    }

    public E0SimulationVariable(E0SimulationVariable var) {
        this.name = var.getName();
        this.value = var.getValue();
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
