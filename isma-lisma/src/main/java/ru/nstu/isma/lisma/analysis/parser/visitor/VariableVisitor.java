package ru.nstu.isma.lisma.analysis.parser.visitor;

import ru.nstu.isma.lisma.analysis.gen.LismaBaseVisitor;
import ru.nstu.isma.lisma.analysis.gen.LismaParser;
import ru.nstu.isma.core.hsm.var.HMAlgebraicEquation;
import ru.nstu.isma.core.hsm.var.HMDerivativeEquation;
import ru.nstu.isma.core.hsm.var.HMEquation;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import ru.nstu.isma.lisma.analysis.parser.ParserContext;

/**
 * Created by Bessonov Alex
 * Date: 28.11.13
 * Time: 23:48
 */
public class VariableVisitor extends LismaBaseVisitor<HMEquation> {
    private final ParserContext pc;

    boolean isDer = false;

    protected HMEquation equation;

    public VariableVisitor(ParserContext pc) {
        this.pc = pc;
    }

    @Override
    public HMEquation visitVariable(@NotNull LismaParser.VariableContext ctx) {
        super.visitVariable(ctx);    //To change body of overridden methods use File | Settings | File Templates.
        return equation;
    }

    @Override
    public HMEquation visitVar_ident(@NotNull LismaParser.Var_identContext ctx) {

        String name = VisitorUtil.getVariableNameWithIndex(ctx.Identifier().getText(), ctx.cycle_index(), pc) ;

        if (isDer) {
            equation = new HMDerivativeEquation(name);
        } else {
            equation = new HMAlgebraicEquation(name);
        }
        return equation;
    }

    @Override
    public HMEquation visitDerivative_ident(@NotNull LismaParser.Derivative_identContext ctx) {
        isDer = true;
        return super.visitDerivative_ident(ctx);
    }

    @Override
    public HMEquation visit(@NotNull ParseTree tree) {
        super.visit(tree);
        return equation;
    }

    @Override
    public HMEquation visitPseudo_state(@NotNull LismaParser.Pseudo_stateContext ctx) {
        return null;
    }
}
