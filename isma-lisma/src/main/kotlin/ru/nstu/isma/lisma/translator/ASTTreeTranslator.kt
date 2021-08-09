package ru.nstu.isma.lisma.translator

import ru.nstu.isma.core.hsm.models.IsmaErrorList
import org.antlr.v4.runtime.tree.ParseTree
import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.core.hsm.`var`.HMDerivativeEquation
import ru.nstu.isma.core.hsm.`var`.HMEquation
import ru.nstu.isma.core.hsm.`var`.pde.HMPartialDerivativeEquation
import ru.nstu.isma.core.hsm.exp.EXPPDEOperand
import ru.nstu.isma.lisma.analysis.parser.ParserContext
import ru.nstu.isma.lisma.analysis.parser.visitor.*

/**
 * Created by Bessonov Alex
 * Date: 28.11.13
 * Time: 23:24
 */
class ASTTreeTranslator(private val tree: ParseTree, var errors: IsmaErrorList) {
    private val pc = ParserContext(errors)

    fun parse(): HSM {
        var model = HSM()
        val automata = model.automata
        val variables = model.variableTable
        val linearSystem = model.linearSystem

        // 1 Парсим все состояния
        val stateNameVisitor = StateVisitor(automata)
        stateNameVisitor.visit(tree)

        // 2 Парсим все имена переменных
        //   с учетом пеменных СЛАУ
        val aggregatorVisitor = VariableTableAggregatorVisitor(variables, automata, linearSystem, pc)
        aggregatorVisitor.visit(tree)
        model.initTimeEquation(0.0)

        // Макроподстановка
        val macroVisitor = MacroVisitor(variables, pc)
        macroVisitor.visit(tree)

        // 3 постоить для каждого уравения (как глобального так и состояния), константы и начального значения
        // полиз правой части и записать
        val expressionVisitor = PopulateExpressionVisitor(model, variables, automata, pc)
        expressionVisitor.visit(tree)

        // TODO
        // написать полиз для логики
        // применить на condition
        // парсить from
        // заполнять переходами

        // ОДУ -> ДУЧП
        val updated = HashSet<HMEquation>()
        for (key in variables.keySet()) {
            val v = variables.get(key)
            if (v is HMDerivativeEquation) {
                val exp = v.rightPart
                var isPartial = false
                for (t in exp.tokens) {
                    if (t is EXPPDEOperand) {
                        isPartial = true
                    }
                }
                if (isPartial) {
                    val partialDerEquation = HMPartialDerivativeEquation(v)
                    updated.add(partialDerEquation)
                }
            }
        }
        val updateCodes = HashSet<String>()
        for (e in updated) {
            variables.remove(e.code)
            variables.add(e)
            updateCodes.add(e.code)
        }


        // надо пробежаться по правым частям и заменить на новый объект
//        for (String varKey : variables.keySet()) {
//            HMVariable var = variables.get(varKey);
//            if (var instanceof HMEquation) {
//                HMEquation eq = (HMEquation) var;
//                HMExpression expr = eq.getRightPart();
//                if (expr != null) {
//                    for (EXPToken tt : expr.getTokens()) {
//                        if (tt instanceof EXPOperand && !(tt instanceof EXPPDEOperand)) {
//                            String operandVarCode = ((EXPOperand)tt).getVariable().getCode();
//                            if (updateCodes.contains(operandVarCode)) {
//                                ((EXPOperand) tt).setVariable(variables.get(operandVarCode));
//                            }
//                        } else if (tt instanceof EXPPDEOperand) {
//                            String operandVarCode = ((EXPPDEOperand)tt).getVariable().getCode();
//                            if (updateCodes.contains(operandVarCode)) {
//                                ((EXPPDEOperand) tt).setVariable(variables.get(operandVarCode));
//                            }
//                        }
//                    }
//                }
//            }
//        }
        val stateInfoVisitor = PopulateStateInfoVisitor(variables, automata, pc)
        stateInfoVisitor.visit(tree)

        // TODO
        // создать сущности ДУЧП
        // заполнить данные аппроксимации

        // смотрим края
        val boundInfoVisitor = BoundInfoVisitor(variables, pc)
        boundInfoVisitor.visit(tree)

        // 4.1 разобрать выражение (ПОЛИЗ) (ограничение - константы)
        // 4.2 написать небольшой вычислитель для констант
        // 4.3 продумать механизм блокировки зацикливания
        // 4.4 посчитать и записать
        val calcConstVisitor = CalcConstVisitor(variables, errors, pc)
        calcConstVisitor.visit(tree)
        val pseudoStateVisitor = PseudoStateVisitor(model, variables, automata, linearSystem, pc)
        pseudoStateVisitor.visit(tree)
        model = VisitorUtil.parseNotInitEquations(model)
        return model
    }

}