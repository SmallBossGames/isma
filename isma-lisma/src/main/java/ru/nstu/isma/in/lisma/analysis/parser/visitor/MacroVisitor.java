package ru.nstu.isma.in.lisma.analysis.parser.visitor;

import org.antlr.v4.runtime.misc.NotNull;
import ru.nstu.isma.core.hsm.exp.HMExpression;
import ru.nstu.isma.core.hsm.var.HMVariableTable;
import ru.nstu.isma.in.lisma.analysis.gen.LismaParser;
import ru.nstu.isma.in.lisma.analysis.parser.ParserContext;

/**
 * Created by Bessonov Alex
 * Date: 28.11.13
 * Time: 23:32
 */
public class MacroVisitor extends BaseVisitor {

    protected HMVariableTable table;


    public MacroVisitor(HMVariableTable table, ParserContext pc) {
        super(pc);
        this.table = table;
        this.pc = pc;
    }

    @Override
    public Object visitMacro_item(@NotNull LismaParser.Macro_itemContext ctx) {
        String name = VisitorUtil.getVariableNameWithIndex(ctx.primary_id().Identifier().getText(), ctx.primary_id().cycle_index(), pc);
        ExpressionVisitor ev = new ExpressionVisitor(table, pc);
        HMExpression exp = ev.visit(ctx.expression(), false);
        pc.addMacro(name, exp);
        return name;
    }
}
