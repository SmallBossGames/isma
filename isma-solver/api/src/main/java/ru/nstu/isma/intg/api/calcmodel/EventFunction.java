package ru.nstu.isma.intg.api.calcmodel;

import java.io.Serializable;
import java.util.function.BiFunction;

/**
 * @author Maria
 * @since 15.05.2016
 */
public class EventFunction implements BiFunction<double[], double[][], Double>, Serializable {

    public interface EventFunctionExpression extends BiFunction<double[], double[][], Double>, Serializable {
    }

    private final EventFunctionExpression expression;
    private final String description;

    public EventFunction(EventFunctionExpression expression, String description) {
        this.expression = expression;
        this.description = description;
    }

    public EventFunctionExpression getExpression() {
        return expression;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Double apply(double[] yForDe, double[][] rhs) {
        return expression.apply(yForDe, rhs);
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
