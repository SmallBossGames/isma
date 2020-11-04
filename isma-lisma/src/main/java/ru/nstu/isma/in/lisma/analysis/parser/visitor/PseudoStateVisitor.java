package ru.nstu.isma.in.lisma.analysis.parser.visitor;

import error.IsmaErrorList;
import org.antlr.v4.runtime.misc.NotNull;
import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.core.hsm.exp.EXPOperator;
import ru.nstu.isma.core.hsm.exp.HMExpression;
import ru.nstu.isma.core.hsm.hybrid.HMPseudoState;
import ru.nstu.isma.core.hsm.hybrid.HMStateAutomata;
import ru.nstu.isma.core.hsm.linear.HMLinearSystem;
import ru.nstu.isma.core.hsm.var.HMVariableTable;
import ru.nstu.isma.in.lisma.analysis.gen.LismaParser;
import ru.nstu.isma.in.lisma.analysis.parser.ParserContext;

/**
 * Created by Bessonov Alex
 * Date: 03.12.13
 * Time: 1:18
 */
public class PseudoStateVisitor extends BaseVisitor {

    private HMVariableTable table;

    private HMStateAutomata automata;

    private HMPseudoState current;

    private HMLinearSystem linearSystem;

    private HMVariableTable baseTable;

    private HSM hsm;

    public PseudoStateVisitor(HSM hsm, HMVariableTable table, HMStateAutomata automata, HMLinearSystem linearSystem, ParserContext pc) {
        super(pc);
        this.table = table;
        this.baseTable = table;
        this.automata = automata;
        this.linearSystem = linearSystem;
        this.hsm = hsm;
    }

    @Override
    public Object visitPseudo_state(@NotNull LismaParser.Pseudo_stateContext ctx) {
        HMPseudoState elseState = null;

        // Парсится тело состояния
        // if
        current = new HMPseudoState();
        automata.addPseudoState(current);
        table = current.getVariables();
        visitPseudo_state_body(ctx.pseudo_state_body());
        VisitorUtil.parseNotInitEquationsWithState(baseTable, current);

        // else
        if (ctx.pseudo_state_else() != null) {
            elseState = new HMPseudoState();
            automata.addPseudoState(elseState);
            table = elseState.getVariables();
            visitPseudo_state_body(ctx.pseudo_state_else().pseudo_state_body());
            VisitorUtil.parseNotInitEquationsWithState(baseTable, elseState);
        }

        // Парсится условие состояния
        // парсится в таком порядке из-за того что в теле (как if так и else) может быть первое объявление переменной
        // if
        ExpressionVisitor ev = new ExpressionVisitor(baseTable, pc);
        HMExpression expr = ev.visit(ctx.expression());
        current.setCondition(expr);

        // else
        if (ctx.pseudo_state_else() != null) {
            expr = ev.visit(ctx.expression());
            expr.add(EXPOperator.not());
            elseState.setCondition(expr);
        }
        return current;
    }

    @Override
    public Object visitPseudo_state_elem(@NotNull LismaParser.Pseudo_state_elemContext ctx) {
        if (ctx.setter() != null) {
            String name = VisitorUtil.getVariableNameWithIndex(ctx.setter().var_ident().Identifier().getText(), ctx.setter().var_ident().cycle_index(), pc);

            ExpressionVisitor ev = new ExpressionVisitor(table, pc);
            HMExpression expression = ev.visit(ctx.setter().expression());
            table.getSetters().put(name, expression);
        } else if (ctx.equation() != null) {
            VariableTableAggregatorVisitor variableTableAggregatorVisitor = new VariableTableAggregatorVisitor(table, automata, linearSystem, pc);
            variableTableAggregatorVisitor.visit(ctx.equation());

            PopulateExpressionVisitor populateExpressionVisitor = new PopulateExpressionVisitor(hsm, table, automata, pc);
            populateExpressionVisitor.visit(ctx.equation());
        }
        return super.visitPseudo_state_elem(ctx);
    }
}
