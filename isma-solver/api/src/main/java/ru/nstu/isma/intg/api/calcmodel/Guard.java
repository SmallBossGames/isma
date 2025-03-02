package ru.nstu.isma.intg.api.calcmodel;

import java.io.Serializable;
import java.util.function.BiFunction;

/**
 * Сторожевое условие ГС (условие перехода из состояния).
 */
public class Guard implements BiFunction<double[], double[][], Boolean>, Serializable {

    public interface GuardPredicate extends BiFunction<double[], double[][], Boolean>, Serializable {
    }

    private final String fromStateName;
    private final String toStateName;
    private final GuardPredicate predicate;
    private final String description;
    private EventFunctionGroup eventFunctionGroup;

    public Guard(String fromStateName, String toStateName, GuardPredicate predicate, String description) {
        this.fromStateName = fromStateName;
        this.toStateName = toStateName;
        this.predicate = predicate;
        this.description = description;
    }

    public String getFromStateName() {
        return fromStateName;
    }

    public String getToStateName() {
        return toStateName;
    }

    public String getDescription() {
        return description;
    }

    public EventFunctionGroup getEventFunctionGroup() {
        return eventFunctionGroup;
    }

    public void setEventFunctionGroup(EventFunctionGroup eventFunctionGroup) {
        this.eventFunctionGroup = eventFunctionGroup;
    }

    /**
     * Проверяет, изменилось ли состояние ГС, анализируя полученное приближенное решение.
     *
     * @param  yForDe приближенное решение ДУ, которое необходимо проверить
     * @param  rhs    значения правой части
     * @return true   если состояние изменилось,
     *         false  если состояние осталось прежним
     */
    @Override
    public Boolean apply(double[] yForDe, double[][] rhs) {
        return predicate.apply(yForDe, rhs);
    }

    @Override
    public String toString() {
        return getDescription();
    }

}
