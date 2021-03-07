package ru.nstu.isma.hsm.events

import ru.nstu.isma.print.MatrixPrint.print
import java.lang.StringBuilder
import java.lang.RuntimeException
import java.util.stream.Collectors
import javax.tools.JavaFileManager
import javax.tools.JavaFileObject
import java.text.SimpleDateFormat
import java.util.function.BiConsumer
import kotlin.jvm.JvmOverloads
import kotlin.Throws
import org.apache.commons.lang3.SerializationUtils
import org.slf4j.LoggerFactory
import ru.nstu.isma.hsm.`var`.HMUnnamedConst
import ru.nstu.isma.hsm.exp.*
import ru.nstu.isma.hsm.service.Infix2PolizConverter
import ru.nstu.isma.hsm.service.Poliz2InfixConverter
import ru.nstu.isma.print.MatrixPrint
import java.lang.IllegalArgumentException
import java.util.*
import java.util.function.Consumer

object HSMEventFunctionGroupEvaluator {
    private val logger = LoggerFactory.getLogger(HSMEventFunctionGroupEvaluator::class.java)

    /**
     * Вычисляет событийные функции по условию перехода.
     * Поддерживает следующий формат:
     * - условие перехода состоит из одного арифметического выржения (АВ),
     * например: АВ > 0;
     * - условие перехода состоит из нескольких выржений, соединенных только оператором '&&' или '||',
     * например: АВ >=0 || АВ < 0 || АВ == 0.
     *
     * Разбор основан на применении правил Де Моргана:
     * - Инверсия(АВ || АВ) => Инверсия(АВ) && Инверсия(АВ);
     * - Инверсия(АВ && АВ) => Инверсия(АВ) || Инверсия(АВ).
     *
     * Т.е. алгоритм работает следующим образом:
     * 1. Разбивает условие перехода на атомарные выражения (в каждом 1 оператор сравнения)
     * (см. [.splitByOperands] splitByOperands).
     * 2. Вычисляет для каждого выражения событийную функцию (инвертирует, приводит к каноническому виду (АВ < 0) и т.п.)
     * (см. [.evaluateAtomic] evaluateAtomic).
     * 3. Возвращает объект типа HSMEventFunctionGroup, содержащий список функций и инвертированный оператор объединения.
     *
     * @param transitionCondition - условие перехода, для которого необходимо вычислить событийную функцию.
     * @throws IllegalArgumentException - если передан неподдерживаемый формат условия перехода.
     * @return событийные функции.
     */
    @Throws(IllegalArgumentException::class)
    fun evaluate(transitionCondition: HMExpression): HSMEventFunctionGroup {
        // Копируем условие перехода, чтобы при вычислениях не затронуть исходное выражение.
        val transitionConditionClone = SerializationUtils.clone(transitionCondition)
        val eventFunctions: MutableList<HMExpression?> = ArrayList()
        val logicalOperators = findOperator(transitionConditionClone, EXPOperator.Type.LOGICAL)

        // Простое выражение без логических операторов.
        if (logicalOperators.isEmpty()) {
            val eventFunction = evaluateAtomic(transitionConditionClone)
            eventFunctions.add(eventFunction)
            return HSMEventFunctionGroup(eventFunctions)
        }
        val firstOperator = logicalOperators[0] as EXPOperator

        // Проверяем, что все операторы равны первому.
        val allTheSame =
            logicalOperators.all { t -> (t as EXPOperator).code == firstOperator.code }
        if (!allTheSame) {
            val expression = transitionConditionClone.toString(StringBuilder(), true).toString()
            logger.warn(
                "Illegal expression \"{}\" Complex boolean expressions are not supported in this revision. " +
                        "Event detection will be disabled. Please, use one expression or multiple expressions " +
                        "all connected only by '&&' or '||'.", expression
            )
        }
        val atomicGuards = splitByOperands(transitionConditionClone)
        atomicGuards.forEach(Consumer { g: HMExpression -> eventFunctions.add(evaluateAtomic(g)) })

        // Инверсия операторов (с учетом правил Де Моргана) '&&' => '||' и наоборот.
        val operatorCode = if (firstOperator.code == EXPOperator.Code.AND) EXPOperator.Code.OR else EXPOperator.Code.AND
        return HSMEventFunctionGroup(eventFunctions, operatorCode)
    }

    private fun findOperator(expression: HMExpression, operatorType: EXPOperator.Type): List<EXPToken?> {
        return expression.tokens.filter { t -> t is EXPOperator && t.type == operatorType }
    }

    private fun splitByOperands(expression: HMExpression): List<HMExpression> {
        val singleExpressions: MutableList<HMExpression> = ArrayList()
        val i: Iterator<EXPToken?> = expression.tokens.iterator()
        while (i.hasNext()) {
            val singleExpression = HMExpression()
            singleExpression.type = expression.type
            var token = i.next()
            while (i.hasNext()) {
                val isLogicalOperator = token is EXPOperator && token.type == EXPOperator.Type.LOGICAL
                if (isLogicalOperator) {
                    break
                }
                singleExpression.add(token)
                val isCompareOperator = token is EXPOperator && token.type == EXPOperator.Type.COMPARE
                if (isCompareOperator) {
                    break
                }
                token = i.next()
            }
            if (!singleExpression.tokens.isEmpty()) {
                singleExpressions.add(singleExpression)
            }
        }
        return singleExpressions
    }

    private fun evaluateAtomic(atomicGuard: HMExpression): HMExpression {
        // Инвертируем оператор сравнения, он д.б. только один, поэтому будет последним, т.к. полиз.
        val tokens = atomicGuard.tokens
        val cmpOperator = tokens[tokens.size - 1] as EXPOperator
        //EXPOperator.Code originalCode = cmpOperator.getCode(); // Кэшируем исходное значение, оно передается по ссылке, потом нужно вернуть назад.
        cmpOperator.code = getInverseOperatorCode(cmpOperator)

        // Конвертируем выражение в постфиксную форму записи.
        val infixExpression =
            Poliz2InfixConverter(atomicGuard).convert()
        val infixTokens = infixExpression.tokens

        // Вычисляем позицию оператора сравнения.
        val cmpOperatorIdx = infixTokens.indexOf(cmpOperator)

        // Выбираем левую и правую части выражений.
        var leftSide: List<EXPToken?> = infixTokens.subList(0, cmpOperatorIdx)
        var rightSide: List<EXPToken?> = infixTokens.subList(cmpOperatorIdx + 1, infixTokens.size)

        // Если оператор сравнения равен > или >=, домножаем л. и п. часть на -1 и меняем знак.
        if (cmpOperator.code == EXPOperator.Code.GREATER_THAN ||
            cmpOperator.code == EXPOperator.Code.GREATER_OR_EQUAL
        ) {
            leftSide = multToMinusOne(leftSide, true)
            rightSide = multToMinusOne(rightSide, true)
            cmpOperator.code = getInverseOperatorCode(
                cmpOperator
            )
        }

        // Переносим все влево, например: ЛЧ < ПЧ => ЛЧ - ПЧ < 0.0
        val allInLeftExpression = HMExpression()
        leftSide.forEach(Consumer { t: EXPToken? ->
            allInLeftExpression.add(
                t
            )
        })
        rightSide = multToMinusOne(
            rightSide,
            false
        ) // домножаем правую часть на -1 для переноса влево.
        rightSide.forEach(Consumer { t: EXPToken? ->
            allInLeftExpression.add(
                t
            )
        })

        // Преобразуем полученное выражение обратно в полиз и возвращаем как результат.
        //cmpOperator.setCode(originalCode); // Возвращаем оператор сравнения назад, чтобы не затронуть изменение исходного выражения.
        return Infix2PolizConverter(allInLeftExpression).convert()
    }

    private fun multToMinusOne(tokens: List<EXPToken?>, unary: Boolean): List<EXPToken?> {
        val multipliedTokens = LinkedList<EXPToken?>()
        multipliedTokens.addAll(tokens)
        multipliedTokens.addFirst(EXPParenthesis(EXPParenthesis.Type.OPEN))
        multipliedTokens.addFirst(EXPOperator.mult())
        multipliedTokens.addFirst(EXPOperand(HMUnnamedConst(1.0)))
        if (unary) {
            multipliedTokens.addFirst(EXPOperator.un_minus())
        } else {
            multipliedTokens.addFirst(EXPOperator.sub())
        }
        multipliedTokens.addLast(EXPParenthesis(EXPParenthesis.Type.CLOSE))
        return multipliedTokens
    }

    private fun getInverseOperatorCode(operator: EXPOperator): EXPOperator.Code? {
        return when (operator.code) {
            EXPOperator.Code.LESS_THAN -> EXPOperator.Code.GREATER_OR_EQUAL
            EXPOperator.Code.LESS_OR_EQUAL -> EXPOperator.Code.GREATER_THAN
            EXPOperator.Code.GREATER_THAN -> EXPOperator.Code.LESS_OR_EQUAL
            EXPOperator.Code.GREATER_OR_EQUAL -> EXPOperator.Code.LESS_THAN
            else -> operator.code
        }
    }
}