package ru.nstu.isma.core.hsm.events;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.core.hsm.exp.*;
import ru.nstu.isma.core.hsm.service.Infix2PolizConverter;
import ru.nstu.isma.core.hsm.service.Poliz2InfixConverter;
import ru.nstu.isma.core.hsm.var.HMUnnamedConst;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HSMEventFunctionGroupEvaluator {

    private static Logger logger = LoggerFactory.getLogger(HSMEventFunctionGroupEvaluator.class);

    /**
     * Вычисляет событийные функции по условию перехода.
     * Поддерживает следующий формат:
     * - условие перехода состоит из одного арифметического выржения (АВ),
     *   например: АВ > 0;
     * - условие перехода состоит из нескольких выржений, соединенных только оператором '&&' или '||',
     *   например: АВ >=0 || АВ < 0 || АВ == 0.
     *
     * Разбор основан на применении правил Де Моргана:
     * - Инверсия(АВ || АВ) => Инверсия(АВ) && Инверсия(АВ);
     * - Инверсия(АВ && АВ) => Инверсия(АВ) || Инверсия(АВ).
     *
     * Т.е. алгоритм работает следующим образом:
     * 1. Разбивает условие перехода на атомарные выражения (в каждом 1 оператор сравнения)
     *    (см. {@link #splitByOperands(HMExpression)} splitByOperands).
     * 2. Вычисляет для каждого выражения событийную функцию (инвертирует, приводит к каноническому виду (АВ < 0) и т.п.)
     *    (см. {@link #evaluateAtomic(HMExpression)} evaluateAtomic).
     * 3. Возвращает объект типа HSMEventFunctionGroup, содержащий список функций и инвертированный оператор объединения.
     *
     * @param transitionCondition - условие перехода, для которого необходимо вычислить событийную функцию.
     * @throws IllegalArgumentException - если передан неподдерживаемый формат условия перехода.
     * @return событийные функции.
     */
    public static HSMEventFunctionGroup evaluate(HMExpression transitionCondition) throws IllegalArgumentException {
        // Копируем условие перехода, чтобы при вычислениях не затронуть исходное выражение.
        HMExpression transitionConditionClone = SerializationUtils.clone(transitionCondition);

        List<HMExpression> eventFunctions = new ArrayList<>();

        List<EXPToken> logicalOperators = findOperator(transitionConditionClone, EXPOperator.Type.LOGICAL);

        // Простое выражение без логических операторов.
        if (logicalOperators.isEmpty()) {
            HMExpression eventFunction = evaluateAtomic(transitionConditionClone);
            eventFunctions.add(eventFunction);
            return new HSMEventFunctionGroup(eventFunctions);
        }

        EXPOperator firstOperator = (EXPOperator) logicalOperators.get(0);

        // Проверяем, что все операторы равны первому.
        boolean allTheSame = logicalOperators.stream().allMatch(t -> ((EXPOperator) t).getCode() == firstOperator.getCode());
        if (!allTheSame) {
            String expression = transitionConditionClone.toString(new StringBuilder(), true).toString();
            logger.warn("Illegal expression \"{}\" Complex boolean expressions are not supported in this revision. " +
                            "Event detection will be disabled. Please, use one expression or multiple expressions " +
                            "all connected only by '&&' or '||'.", expression);
        }

        List<HMExpression> atomicGuards = splitByOperands(transitionConditionClone);
        atomicGuards.forEach(g -> eventFunctions.add(evaluateAtomic(g)));

        // Инверсия операторов (с учетом правил Де Моргана) '&&' => '||' и наоборот.
        EXPOperator.Code operatorCode = firstOperator.getCode() == EXPOperator.Code.AND ? EXPOperator.Code.OR : EXPOperator.Code.AND;

        return new HSMEventFunctionGroup(eventFunctions, operatorCode);
    }

    private static List<EXPToken> findOperator(HMExpression expression, EXPOperator.Type operatorType) {
        return expression.getTokens()
                .stream()
                .filter(t -> t instanceof EXPOperator && ((EXPOperator) t).getType() == operatorType)
                .collect(Collectors.toList());
    }

    private static List<HMExpression> splitByOperands(HMExpression expression) {
        List<HMExpression> singleExpressions = new ArrayList<>();

        Iterator<EXPToken> i = expression.getTokens().iterator();
        while (i.hasNext()) {
            HMExpression singleExpression = new HMExpression();
            singleExpression.setType(expression.getType());

            EXPToken token = i.next();

            while (i.hasNext()) {
                boolean isLogicalOperator = token instanceof EXPOperator && ((EXPOperator) token).getType() == EXPOperator.Type.LOGICAL;
                if (isLogicalOperator) {
                    break;
                }

                singleExpression.add(token);

                boolean isCompareOperator = token instanceof EXPOperator && ((EXPOperator) token).getType() == EXPOperator.Type.COMPARE;
                if (isCompareOperator) {
                    break;
                }

                token = i.next();
            }

            if (!singleExpression.getTokens().isEmpty()) {
                singleExpressions.add(singleExpression);
            }
        }

        return singleExpressions;
    }

    private static HMExpression evaluateAtomic(HMExpression atomicGuard) {
        // Инвертируем оператор сравнения, он д.б. только один, поэтому будет последним, т.к. полиз.
        List<EXPToken> tokens = atomicGuard.getTokens();
        EXPOperator cmpOperator = (EXPOperator) tokens.get(tokens.size() - 1);
        //EXPOperator.Code originalCode = cmpOperator.getCode(); // Кэшируем исходное значение, оно передается по ссылке, потом нужно вернуть назад.
        cmpOperator.setCode(getInverseOperatorCode(cmpOperator));

        // Конвертируем выражение в постфиксную форму записи.
        HMExpression infixExpression = new Poliz2InfixConverter(atomicGuard).convert();
        List<EXPToken> infixTokens = infixExpression.getTokens();

        // Вычисляем позицию оператора сравнения.
        int cmpOperatorIdx = infixTokens.indexOf(cmpOperator);

        // Выбираем левую и правую части выражений.
        List<EXPToken> leftSide = infixTokens.subList(0, cmpOperatorIdx);
        List<EXPToken> rightSide = infixTokens.subList(cmpOperatorIdx + 1, infixTokens.size());

        // Если оператор сравнения равен > или >=, домножаем л. и п. часть на -1 и меняем знак.
        if (cmpOperator.getCode() == EXPOperator.Code.GREATER_THAN ||
                cmpOperator.getCode() == EXPOperator.Code.GREATER_OR_EQUAL) {
            leftSide = multToMinusOne(leftSide, true);
            rightSide = multToMinusOne(rightSide, true);
            cmpOperator.setCode(getInverseOperatorCode(cmpOperator));
        }

        // Переносим все влево, например: ЛЧ < ПЧ => ЛЧ - ПЧ < 0.0
        HMExpression allInLeftExpression = new HMExpression();
        leftSide.forEach(allInLeftExpression::add);

        rightSide = multToMinusOne(rightSide, false); // домножаем правую часть на -1 для переноса влево.
        rightSide.forEach(allInLeftExpression::add);

        // Преобразуем полученное выражение обратно в полиз и возвращаем как результат.
        HMExpression allInLeftExpressionPoliz = new Infix2PolizConverter(allInLeftExpression).convert();
        //cmpOperator.setCode(originalCode); // Возвращаем оператор сравнения назад, чтобы не затронуть изменение исходного выражения.
        return allInLeftExpressionPoliz;
    }

    private static List<EXPToken> multToMinusOne(List<EXPToken> tokens, boolean unary) {
        LinkedList<EXPToken> multipliedTokens = new LinkedList<>();

        multipliedTokens.addAll(tokens);

        multipliedTokens.addFirst(new EXPParenthesis(EXPParenthesis.Type.OPEN));
        multipliedTokens.addFirst(EXPOperator.mult());
        multipliedTokens.addFirst(new EXPOperand(new HMUnnamedConst(1.0)));
        if (unary)
            multipliedTokens.addFirst(EXPOperator.un_minus());
        else
            multipliedTokens.addFirst(EXPOperator.sub());

        multipliedTokens.addLast(new EXPParenthesis(EXPParenthesis.Type.CLOSE));

        return multipliedTokens;
    }

    private static EXPOperator.Code getInverseOperatorCode(EXPOperator operator) {
        switch (operator.getCode()) {
            case LESS_THAN:
                return EXPOperator.Code.GREATER_OR_EQUAL;
            case LESS_OR_EQUAL:
                return EXPOperator.Code.GREATER_THAN;
            case GREATER_THAN:
                return EXPOperator.Code.LESS_OR_EQUAL;
            case GREATER_OR_EQUAL:
                return EXPOperator.Code.LESS_THAN;
            default:
                return operator.getCode();
        }
    }

}
