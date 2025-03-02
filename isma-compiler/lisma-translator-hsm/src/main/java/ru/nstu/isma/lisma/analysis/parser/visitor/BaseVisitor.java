package ru.nstu.isma.lisma.analysis.parser.visitor;

import org.antlr.v4.runtime.misc.NotNull;
import ru.nstu.isma.lisma.analysis.gen.LismaBaseVisitor;
import ru.nstu.isma.lisma.analysis.gen.LismaParser;
import ru.nstu.isma.lisma.analysis.parser.CycleIndex;
import ru.nstu.isma.lisma.analysis.parser.ParserContext;

import java.util.Objects;

/**
 * Created by Alex on 14.12.2015.
 */
public class BaseVisitor extends LismaBaseVisitor<Object> {
    protected ParserContext pc ;

    public BaseVisitor(ParserContext pc) {
        this.pc = pc;
    }

    @Override
    public Objects visitFor_cycle(@NotNull LismaParser.For_cycleContext ctx) {
        String idxName = ctx.Identifier().getText();

        CycleIndex idx = new CycleIndex(idxName);

        ctx.for_cycle_interval().forEach(i -> {
            Integer a = Integer.valueOf(i.literal(0).getText());
            if (i.literal().size() > 1) {
                Integer b = Integer.valueOf(i.literal(1).getText());
                idx.addInterval(a, b);
            } else
                idx.addInterval(a);
        });

        for (Integer i : idx) {
            pc.setIdxValue(idxName, i);
            visit(ctx.for_cycle_body());
        }

        pc.removeIdxValue(idxName);
        return null;
    }

}
