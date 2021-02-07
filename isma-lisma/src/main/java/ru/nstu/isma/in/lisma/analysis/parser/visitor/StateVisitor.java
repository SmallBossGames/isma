package ru.nstu.isma.in.lisma.analysis.parser.visitor;

import ru.nstu.isma.in.lisma.analysis.gen.LismaBaseVisitor;
import ru.nstu.isma.in.lisma.analysis.gen.LismaParser;
import ru.nstu.isma.core.hsm.hybrid.HMState;
import ru.nstu.isma.core.hsm.hybrid.HMStateAutomata;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * Created by Bessonov Alex
 * Date: 29.11.13
 * Time: 0:13
 */
public class StateVisitor extends LismaBaseVisitor<HMState> {
    private final HMStateAutomata automata;

    public StateVisitor(HMStateAutomata automata) {
        this.automata = automata;
    }

    @Override
    public HMState visitState(@NotNull LismaParser.StateContext ctx) {
        automata.addState(new HMState(ctx.getChild(1).getText()));
        return super.visitState(ctx);
    }

}
