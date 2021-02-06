package ru.nstu.isma.intg.demo.problems.lotkaVolterra;

import java.io.Serializable;

public abstract class LVFunction implements Serializable {
    public abstract double calculate(double arg1, double arg2);
}
