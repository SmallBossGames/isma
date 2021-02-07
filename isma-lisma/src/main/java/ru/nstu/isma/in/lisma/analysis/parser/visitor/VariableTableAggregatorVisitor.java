package ru.nstu.isma.in.lisma.analysis.parser.visitor;

import org.antlr.v4.runtime.misc.NotNull;
import ru.nstu.isma.core.hsm.exp.EXPPDEOperand;
import ru.nstu.isma.core.hsm.exp.HMExpression;
import ru.nstu.isma.core.hsm.hybrid.HMState;
import ru.nstu.isma.core.hsm.hybrid.HMStateAutomata;
import ru.nstu.isma.core.hsm.linear.HMLinearSystem;
import ru.nstu.isma.core.hsm.var.HMConst;
import ru.nstu.isma.core.hsm.var.HMUnnamedConst;
import ru.nstu.isma.core.hsm.var.HMVariableTable;
import ru.nstu.isma.core.hsm.var.pde.HMPartialDerivativeEquation;
import ru.nstu.isma.core.hsm.var.pde.HMSampledSpatialVariable;
import ru.nstu.isma.core.hsm.var.pde.HMSpatialVariable;
import ru.nstu.isma.in.lisma.analysis.gen.LismaParser;
import ru.nstu.isma.in.lisma.analysis.parser.InternalConstUtil;
import ru.nstu.isma.in.lisma.analysis.parser.ParserContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bessonov Alex
 * Date: 28.11.13
 * Time: 23:32
 */
public class VariableTableAggregatorVisitor extends BaseVisitor {

    protected HMVariableTable table;

    private final HMStateAutomata automata;

    private final HMLinearSystem linearSystem;


    public VariableTableAggregatorVisitor(HMVariableTable table, HMStateAutomata automata, HMLinearSystem linearSystem, ParserContext pc) {
        super(pc);
        this.table = table;
        this.automata = automata;
        this.linearSystem = linearSystem;
        this.pc = pc;
    }

    @Override
    public Object visitConstant_body(@NotNull LismaParser.Constant_bodyContext ctx) {
        VariableVisitor vv = new VariableVisitor(pc);
        ctx.var_ident().stream()
                .forEach(vi -> table.add(new HMConst(vv.visitVar_ident(vi).getCode())));
        return null;
    }

    @Override
    public Object visitInit_const(@NotNull LismaParser.Init_constContext ctx) {
        Double value = Double.valueOf(ctx.literal().getText());
        pc.setDefaultConstInitialValue(value);
        return null;
    }

    @Override
    public Object visitOde_equation(@NotNull LismaParser.Ode_equationContext ctx) {
        VariableVisitor vv = new VariableVisitor(pc);
        table.add(vv.visit(ctx.variable()));
        return super.visitOde_equation(ctx);
    }

    @Override
    public Object visitPde_equation(@NotNull LismaParser.Pde_equationContext ctx) {
        ExpressionVisitor expressionVisitor = new ExpressionVisitor(table, null, true, pc);
        List<EXPPDEOperand> pdes = ctx.partial_operand().stream().map(po -> expressionVisitor.visitPartial_operand(po)).collect(Collectors.toList());
        prepearePde(pdes, ctx.getText());
        return null;
    }

    @Override
    public Object visitPde_equation_param(@NotNull LismaParser.Pde_equation_paramContext ctx) {
        ExpressionVisitor expressionVisitor = new ExpressionVisitor(table, null, true, pc);
        List<EXPPDEOperand> pdes = ctx.partial_operand_with_param().stream().map(po -> expressionVisitor.visitPartial_operand(po.partial_operand())).collect(Collectors.toList());
        prepearePde(pdes, ctx.getText());
        return super.visitPde_equation_param(ctx);
    }

    private void prepearePde(List<EXPPDEOperand> pdes, String text) {
        if (pdes.size() == 0)
            throw new RuntimeException("left part empty: " + text);
        String code = pdes.get(0).getVariable().getCode();
        pdes.stream().forEach(p -> {
            if (!p.getVariable().getCode().equals(code))
                throw new RuntimeException("Not supported type of PDE: " + text);
        });
        HMPartialDerivativeEquation pde = new HMPartialDerivativeEquation(code);
        pdes.stream().forEach(p -> pde.addSpatialVar(p.getFirstSpatialVariable(), p.getOrder().getI()));
        table.add(pde);
    }

    @Override
    public Object visitSpatial_var(@NotNull LismaParser.Spatial_varContext ctx) {
        String code = ctx.var_ident().getText();
        if (ctx.spatial_var_bound().size() != 2)
            throw new RuntimeException("Not correct spatial var bound count");

        double from = InternalConstUtil.getSpatialBound(ctx.spatial_var_bound().get(0));
        double to = InternalConstUtil.getSpatialBound(ctx.spatial_var_bound().get(1));

        HMUnnamedConst cfrom = new HMUnnamedConst(from);
        HMUnnamedConst cto = new HMUnnamedConst(to);

        table.add(cfrom);
        table.add(cto);


        HMSpatialVariable variable;

        if (ctx.spatial_var_tail() != null) {
            HMSampledSpatialVariable tmpVar = new HMSampledSpatialVariable();
            HMConst apxVal = null;
            if (ctx.spatial_var_tail().spatial_var_tail_APX() != null) {
                tmpVar.setType(HMSampledSpatialVariable.ApproximateType.BY_NUMBER_OF_PIECES);
                apxVal = new HMUnnamedConst(Double.valueOf(ctx.spatial_var_tail().DecimalLiteral().getText()));
            } else if (ctx.spatial_var_tail().spatial_var_tail_STEP() != null) {
                tmpVar.setType(HMSampledSpatialVariable.ApproximateType.BY_STEP);
                apxVal = new HMUnnamedConst(Double.valueOf(ctx.spatial_var_tail().literal().getText()));
            }
            tmpVar.setApxVal(apxVal);
            variable = tmpVar;
        } else
            variable = new HMSpatialVariable();

        variable.setCode(code);
        variable.setValFrom(cfrom);
        variable.setValTo(cto);

        table.add(variable);
        return null;
    }

    @Override
    public Object visitState(@NotNull LismaParser.StateContext ctx) {
        // меняем таблицу переменных на ту что в состоянии
        HMState st = automata.getState(ctx.state_name().getText());
        VariableTableAggregatorVisitor aggregatorVisitor = new VariableTableAggregatorVisitor(st.getVariables(), automata, linearSystem, pc);
        aggregatorVisitor.visit(ctx.state_body());
        VisitorUtil.parseNotInitEquationsWithState(table, st);
        return null;
    }

    @Override
    public HMExpression visitPseudo_state(@NotNull LismaParser.Pseudo_stateContext ctx) {
        // Парсинг исключительно для поиска переменных, объявленных в событийном управлении
        // if
        HMState st = new HMState("temp");
        VariableTableAggregatorVisitor aggregatorVisitor = new VariableTableAggregatorVisitor(st.getVariables(), automata, linearSystem, pc);
        if (ctx.pseudo_state_body() != null)
            aggregatorVisitor.visit(ctx.pseudo_state_body());
        VisitorUtil.parseNotInitEquationsWithState(table, st);
        // else
        if (ctx.pseudo_state_else() != null) {
            st = new HMState("temp_else");
            aggregatorVisitor = new VariableTableAggregatorVisitor(st.getVariables(), automata, linearSystem, pc);
            aggregatorVisitor.visit(ctx.pseudo_state_else().pseudo_state_body());
            VisitorUtil.parseNotInitEquationsWithState(table, st);
        }
        return null;
    }

    @Override
    public Object visitLinear_vars(@NotNull LismaParser.Linear_varsContext ctx) {
        ctx.var_ident().stream().forEach(vi -> linearSystem.addVar(vi.getText()));
        return null;
    }
}
