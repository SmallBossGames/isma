package ru.nstu.isma.in.lisma.analysis.parser.visitor;

import error.IsmaError;
import error.IsmaErrorList;
import org.antlr.v4.runtime.misc.NotNull;
import ru.nstu.isma.core.hsm.exp.EXPPDEOperand;
import ru.nstu.isma.core.hsm.exp.HMExpression;
import ru.nstu.isma.core.hsm.var.HMVariable;
import ru.nstu.isma.core.hsm.var.HMVariableTable;
import ru.nstu.isma.core.hsm.var.pde.HMBoundaryCondition;
import ru.nstu.isma.core.hsm.var.pde.HMPartialDerivativeEquation;
import ru.nstu.isma.core.hsm.var.pde.HMSpatialVariable;
import ru.nstu.isma.in.lisma.analysis.gen.LismaBaseVisitor;
import ru.nstu.isma.in.lisma.analysis.gen.LismaParser;
import ru.nstu.isma.in.lisma.analysis.parser.ParserContext;

/**
 * Created by Bessonov Alex
 * Date: 04.12.13 Time: 1:07
 */
public class BoundInfoVisitor extends LismaBaseVisitor<HMBoundaryCondition> {
    private HMVariableTable table;

    private ParserContext pc;

    public BoundInfoVisitor(HMVariableTable table, ParserContext pc) {
        this.table = table;
        this.pc = pc;
    }

    @Override
    public HMBoundaryCondition visitEdge(@NotNull LismaParser.EdgeContext ctx) {
        HMBoundaryCondition bound = new HMBoundaryCondition();

        HMVariable v = null;
        // 1 ищем ДУЧП для края
        if (ctx.edge_eq().Identifier() != null) {
            bound.setType(HMBoundaryCondition.Type.DIRICHLET);
            String code = ctx.edge_eq().Identifier().getText();
            v = table.get(code);

        } else if (ctx.edge_eq().partial_operand() != null) {
            bound.setType(HMBoundaryCondition.Type.NEUMANN);
            ExpressionVisitor visitor = new ExpressionVisitor(table, pc);
            EXPPDEOperand o = visitor.visitPartial_operand(ctx.edge_eq().partial_operand());
            v = o.getVariable();
            bound.setDerOrder(o.getOrder().getI());
        }

        if (v == null || !(v instanceof HMPartialDerivativeEquation)) {
            IsmaError error = new IsmaError("error in the definition of boundary conditions. Unknown PDE " + v.getCode());
            error.setType(IsmaError.Type.SEM);
            pc.errors().push(error);
            return null;
        }

        HMPartialDerivativeEquation derEquation = (HMPartialDerivativeEquation) v;
        bound.setEquation(derEquation);

        // 2 ставим значение края
        ExpressionVisitor expressionVisitor = new ExpressionVisitor(table, pc);
        HMExpression val = expressionVisitor.visit(ctx.edge_eq().expression());
        bound.setValue(val);

        // 3 ищем переменную
        String avCode = ctx.Identifier().getText();
        if (table.get(avCode) == null || !(table.get(avCode) instanceof HMSpatialVariable)) {
            IsmaError error = new IsmaError("error in the definition of boundary conditions. Unknown variable " + avCode);
            error.setType(IsmaError.Type.SEM);
            pc.errors().push(error);
            return null;
        }
        HMSpatialVariable av = (HMSpatialVariable) table.get(avCode);
        bound.setSpatialVar(av);

        //4 смотрим тип края
        String type = ctx.edge_side().getText();
        if (type.equals("left")) {
            bound.setSide(HMBoundaryCondition.SideType.LEFT);
        } else if (type.equals("right")) {
            bound.setSide(HMBoundaryCondition.SideType.RIGHT);
        } else if (type.equals("both")) {
            bound.setSide(HMBoundaryCondition.SideType.BOTH);
        }
        // добавим край
        derEquation.addBound(bound);
        return bound;    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public HMBoundaryCondition visitEdge_eq(@NotNull LismaParser.Edge_eqContext ctx) {
        return super.visitEdge_eq(ctx);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
