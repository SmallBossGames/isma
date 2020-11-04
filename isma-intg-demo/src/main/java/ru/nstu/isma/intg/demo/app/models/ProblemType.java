package ru.nstu.isma.intg.demo.app.models;

public enum ProblemType {
    TWO_DIMENSIONAL("Two-dimensional problem"),
    FOUR_DIMENSIONAL("Four-dimensional problem"),
    REACTION_DIFFUSION("Reaction-diffusion problem"),
    LORENZ_SYSTEM("Lorenz system"),
    VAN_DER_POL("Van der Pol oscillator");

    private final String name;

    private ProblemType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
