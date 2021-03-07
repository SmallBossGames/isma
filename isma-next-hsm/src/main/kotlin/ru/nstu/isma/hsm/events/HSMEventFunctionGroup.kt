package ru.nstu.isma.hsm.events

import ru.nstu.isma.hsm.exp.EXPOperator
import ru.nstu.isma.hsm.exp.HMExpression

/**
 * @author Maria
 * @since 16.05.2016
 */
class HSMEventFunctionGroup {
    /** Список выражений с событийными функциями.  */
    val expressions: List<HMExpression?>

    /**
     * Оператор, соединяющий событийные функции.
     * М.б. null, если в списке только 1 функция.
     */
    val operatorCode: EXPOperator.Code?

    constructor(expressions: List<HMExpression?>) {
        val size = expressions.size
        require(size == 1) {
            "Illegal size of the expressions: expected 1 but actual was " + size +
                    ". This constructor is only used for 1 event function."
        }
        this.expressions = expressions
        operatorCode = null
    }

    constructor(expressions: List<HMExpression?>, operatorCode: EXPOperator.Code?) {
        val size = expressions.size
        /*if (size < 2) {
            throw new IllegalArgumentException("Illegal size of the expressions: expected >= 2 but actual was " + size +
                    ". This constructor is only used for 2 or more event functions.");
        }*/this.expressions = expressions

        /*if (operatorCode == EXPOperator.Code.AND ^ operatorCode == EXPOperator.Code.OR) {
            throw new IllegalArgumentException("Illegal operator: expected '&&' or '||', but actual was " + operatorCode + ".");
        }*/this.operatorCode = operatorCode
    }
}