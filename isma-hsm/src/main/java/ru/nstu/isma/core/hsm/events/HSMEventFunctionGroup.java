package ru.nstu.isma.core.hsm.events;

import ru.nstu.isma.core.hsm.exp.EXPOperator;
import ru.nstu.isma.core.hsm.exp.HMExpression;

import java.util.List;

/**
 * @author Maria
 * @since 16.05.2016
 */
public class HSMEventFunctionGroup {

    /** Список выражений с событийными функциями. */
    private final List<HMExpression> expressions;

    /**
     * Оператор, соединяющий событийные функции.
     * М.б. null, если в списке только 1 функция.
     */
    private final EXPOperator.Code operatorCode;

    public HSMEventFunctionGroup(List<HMExpression> expressions) throws IllegalArgumentException {
        int size = expressions.size();
        if (size != 1) {
            throw new IllegalArgumentException("Illegal size of the expressions: expected 1 but actual was " + size +
                    ". This constructor is only used for 1 event function.");
        }

        this.expressions = expressions;
        this.operatorCode = null;
    }

    public HSMEventFunctionGroup(List<HMExpression> expressions, EXPOperator.Code operatorCode) {
        int size = expressions.size();
        /*if (size < 2) {
            throw new IllegalArgumentException("Illegal size of the expressions: expected >= 2 but actual was " + size +
                    ". This constructor is only used for 2 or more event functions.");
        }*/
        this.expressions = expressions;

        /*if (operatorCode == EXPOperator.Code.AND ^ operatorCode == EXPOperator.Code.OR) {
            throw new IllegalArgumentException("Illegal operator: expected '&&' or '||', but actual was " + operatorCode + ".");
        }*/
        this.operatorCode = operatorCode;
    }

    public List<HMExpression> getExpressions() {
        return expressions;
    }

    public EXPOperator.Code getOperatorCode() {
        return operatorCode;
    }

}
