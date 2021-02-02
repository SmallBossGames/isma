package ru.nstu.isma.intg.lib;

@Deprecated
public enum IntgMethodType {
    EULER("Euler"),
    RUNGE_KUTTA_2("Runge-Kutta (2)"),
    RUNGE_KUTTA_3("Runge-Kutta (3)"),
    RUNGE_KUTTA_31("Runge-Kutta (RK31)"),
    RUNGE_KUTTA_MERSON("Runge-Kutta-Merson");

    private final String name;

    IntgMethodType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
