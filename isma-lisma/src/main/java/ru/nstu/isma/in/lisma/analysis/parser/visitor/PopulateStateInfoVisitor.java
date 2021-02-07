package ru.nstu.isma.in.lisma.analysis.parser.visitor;

import error.IsmaErrorList;
import ru.nstu.isma.in.lisma.analysis.gen.LismaBaseVisitor;
import ru.nstu.isma.in.lisma.analysis.gen.LismaParser;
import ru.nstu.isma.core.hsm.hybrid.HMState;
import ru.nstu.isma.core.hsm.hybrid.HMStateAutomata;
import ru.nstu.isma.core.hsm.var.HMVariableTable;
import ru.nstu.isma.core.hsm.exp.HMExpression;
import org.antlr.v4.runtime.misc.NotNull;
import ru.nstu.isma.in.lisma.analysis.parser.ParserContext;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bessonov Alex
 * Date: 03.12.13
 * Time: 1:18
 */
public class PopulateStateInfoVisitor extends LismaBaseVisitor<Object> {
    private final HMVariableTable table;

    private final HMStateAutomata automata;

    private final ParserContext pc;

    public PopulateStateInfoVisitor(HMVariableTable table, HMStateAutomata automata, ParserContext pc) {
        this.table = table;
        this.automata = automata;
        this.pc = pc;
    }

    @Override
    public Object visitState(@NotNull LismaParser.StateContext ctx) {
        // 1 ловим текущее состояние
        HMState to = automata.getState(ctx.getChild(1).getText());

        // 2 парсим условие перехода
        ExpressionVisitor ev = new ExpressionVisitor(table, pc);
        HMExpression cond = ev.visit(ctx.getChild(3));

        // 3 делаем список состояний из которых выходим в текщее
        List<HMState> statesFrom = new LinkedList<HMState>();

        if (ctx.getChild(6) instanceof LismaParser.State_fromContext) {
            LismaParser.State_fromContext from = (LismaParser.State_fromContext) ctx.getChild(6);
            HMState sttemp;
            for (int i = 0; i < from.getChildCount(); i++) {
                if (from.getChild(i) instanceof LismaParser.State_nameContext) {
                    sttemp = automata.getState(from.getChild(i).getText());
                    statesFrom.add(sttemp);
                }
            }
        }
        if (statesFrom.size() == 0) {
            // TODO преупреждение семантики - висячее состояние
        }
        // проставляем переходы
        for (HMState from : statesFrom) {
            automata.addTransaction(from, to, cond);
        }

        return null;
    }

    @Override
    public HMExpression visitPseudo_state(@NotNull LismaParser.Pseudo_stateContext ctx) {
        return null;
    }
}
