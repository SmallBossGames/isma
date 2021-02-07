package ru.nstu.isma.in.lisma.analysis;

import org.antlr.v4.runtime.tree.ParseTree;
import error.IsmaErrorList;
import ru.nstu.isma.core.hsm.*;
import ru.nstu.isma.core.hsm.hybrid.HMState;
import ru.nstu.isma.core.hsm.linear.HMLinearSystem;
import ru.nstu.isma.core.hsm.var.*;
import ru.nstu.isma.core.hsm.exp.EXPPDEOperand;
import ru.nstu.isma.core.hsm.exp.EXPToken;
import ru.nstu.isma.core.hsm.exp.HMExpression;
import ru.nstu.isma.core.hsm.hybrid.HMStateAutomata;
import ru.nstu.isma.core.hsm.var.pde.HMPartialDerivativeEquation;
import ru.nstu.isma.in.lisma.analysis.parser.ParserContext;
import ru.nstu.isma.in.lisma.analysis.parser.visitor.*;

import java.util.HashSet;

/**
 * Created by Bessonov Alex
 * Date: 28.11.13
 * Time: 23:24
 */
public class ASTTreeTranslator {
    private final ParserContext pc;

    private final ParseTree tree;

    private HMStateAutomata automata;

    private HMVariableTable variables;

    private HMLinearSystem linearSystem;

    private IsmaErrorList errors;

    public ASTTreeTranslator(ParseTree tree, IsmaErrorList errors) {
        this.tree = tree;
        this.errors = errors;
        pc = new ParserContext(errors);
    }

    public HSM parse() {
        if (errors == null) {
            errors = new IsmaErrorList();
        }
        HSM model = new HSM();

        automata = model.getAutomata();
        variables = model.getVariableTable();
        linearSystem = model.getLinearSystem();

        // 1 Парсим все состояния
        StateVisitor stateNameVisitor = new StateVisitor(automata);
        stateNameVisitor.visit(tree);

        // 2 Парсим все имена переменных
        //   с учетом пеменных СЛАУ
        VariableTableAggregatorVisitor aggregatorVisitor = new VariableTableAggregatorVisitor(variables, automata, linearSystem, pc);
        aggregatorVisitor.visit(tree);
        model.initTimeEquation(0d);

        // Макроподстановка
        MacroVisitor macroVisitor = new MacroVisitor(variables, pc);
        macroVisitor.visit(tree);

        // 3 постоить для каждого уравения (как глобального так и состояния), константы и начального значения
        // полиз правой части и записать
        PopulateExpressionVisitor expressionVisitor = new PopulateExpressionVisitor(model, variables, automata, pc);
        expressionVisitor.visit(tree);

        // TODO
        // написать полиз для логики
        // применить на condition
        // парсить from
        // заполнять переходами

        // ОДУ -> ДУЧП
        HashSet<HMEquation> updated = new HashSet<HMEquation>();
        for (String key : variables.keySet()) {
            HMVariable v = variables.get(key);
            if (v instanceof HMDerivativeEquation) {
                HMExpression exp = ((HMDerivativeEquation) v).getRightPart();
                boolean isPartial = false;
                for (EXPToken t : exp.getTokens()) {
                    if (t instanceof EXPPDEOperand) {
                        isPartial = true;
                    }
                }

                if (isPartial) {
                    HMPartialDerivativeEquation partialDerEquation
                            = new HMPartialDerivativeEquation((HMDerivativeEquation) v);
                    updated.add(partialDerEquation);
                }
            }
        }
        HashSet<String> updateCodes = new HashSet<String>();
        for (HMEquation e : updated) {
            variables.remove(e.getCode());
            variables.add(e);
            updateCodes.add(e.getCode());
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

        PopulateStateInfoVisitor stateInfoVisitor = new PopulateStateInfoVisitor(variables, automata, pc);
        stateInfoVisitor.visit(tree);

        // TODO
        // создать сущности ДУЧП
        // заполнить данные аппроксимации

        // смотрим края
        BoundInfoVisitor boundInfoVisitor = new BoundInfoVisitor(variables, pc);
        boundInfoVisitor.visit(tree);

        // 4.1 разобрать выражение (ПОЛИЗ) (ограничение - константы)
        // 4.2 написать небольшой вычислитель для констант
        // 4.3 продумать механизм блокировки зацикливания
        // 4.4 посчитать и записать
        CalcConstVisitor calcConstVisitor = new CalcConstVisitor(variables, errors, pc);
        calcConstVisitor.visit(tree);

        PseudoStateVisitor pseudoStateVisitor = new PseudoStateVisitor(model, variables, automata, linearSystem, pc);
        pseudoStateVisitor.visit(tree);

        model = VisitorUtil.parseNotInitEquations(model);

        return model;
    }

    public IsmaErrorList getErrors() {
        return errors;
    }

    public void setErrors(IsmaErrorList errors) {
        this.errors = errors;
    }



}
