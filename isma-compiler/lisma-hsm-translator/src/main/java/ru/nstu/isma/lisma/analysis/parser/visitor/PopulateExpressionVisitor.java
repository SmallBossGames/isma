package ru.nstu.isma.lisma.analysis.parser.visitor;

import org.antlr.v4.runtime.misc.NotNull;
import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.core.hsm.exp.EXPOperand;
import ru.nstu.isma.core.hsm.exp.EXPPDEOperand;
import ru.nstu.isma.core.hsm.exp.HMExpression;
import ru.nstu.isma.core.hsm.hybrid.HMState;
import ru.nstu.isma.core.hsm.hybrid.HMStateAutomata;
import ru.nstu.isma.core.hsm.linear.HMLinearEquation;
import ru.nstu.isma.core.hsm.linear.HMLinearVar;
import ru.nstu.isma.core.hsm.models.IsmaSemanticError;
import ru.nstu.isma.core.hsm.var.*;
import ru.nstu.isma.core.hsm.var.pde.HMPartialDerivativeEquation;
import ru.nstu.isma.lisma.analysis.gen.LismaParser;
import ru.nstu.isma.lisma.analysis.parser.ParserContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bessonov Alex
 * Date: 03.12.13
 * Time: 0:35
 */
public class PopulateExpressionVisitor extends BaseVisitor {

    private final HMVariableTable table;

    private final HMStateAutomata automata;

    private HMLinearEquation currentEquation;

    private final HSM hsm;

    public PopulateExpressionVisitor(HSM hsm, HMVariableTable table, HMStateAutomata automata, ParserContext pc) {
        super(pc);
        this.table = table;
        this.automata = automata;
        this.hsm = hsm;
    }

    @Override
    public Object visitConstant_body(@NotNull LismaParser.Constant_bodyContext ctx) {
        ExpressionVisitor ev = new ExpressionVisitor(table, pc);
        HMExpression expr = ev.visit(ctx.expression());
        VariableVisitor vv = new VariableVisitor(pc);

        ctx.var_ident().stream()
                .forEach(vi -> {
                    HMConst c = (HMConst) table.get(vv.visitVar_ident(vi).getCode());
                    c.setRightPart(expr);
                    if (expr == null || expr.getTokens().size() == 0) {
                        Double val = pc.getDefaultConstInitialValue();
                        c.setValue(val);
                        HMExpression ex = new HMExpression();
                        ex.add(new EXPOperand(new HMUnnamedConst(val)));
                        c.setRightPart(ex);
                    }
                });

        return null;
    }

    @Override
    public Object visitInit_cond(@NotNull LismaParser.Init_condContext ctx) {
        if (ctx.variable() != null)
            populateInitCond(ctx.variable(), ctx.expression());
        ctx.init_cond_body().forEach(cc -> populateInitCond(cc.variable(), cc.expression()));
        return null;
    }

    private void populateInitCond(LismaParser.VariableContext variable, LismaParser.ExpressionContext expression) {
        VariableVisitor vv = new VariableVisitor(pc);
        String code = vv.visit(variable).getCode();
        if (!(table.get(code) instanceof HMDerivativeEquation)) {
            var e = new IsmaSemanticError("error during initilizing initial conditions. There is no corresponding ODE for the variable " + code);
            pc.errors().push(e);
            return;
        }
        HMDerivativeEquation equation = (HMDerivativeEquation) table.get(code);

        ExpressionVisitor ev = new ExpressionVisitor(table, pc);
        HMExpression expr = ev.visit(expression);

        HMConst initConst = new HMConst(code + "@init_cond");
        initConst.setRightPart(expr);
        equation.setInitial(initConst);
    }


    @Override
    public Object visitOde_equation(@NotNull LismaParser.Ode_equationContext ctx) {
        VariableVisitor vv = new VariableVisitor(pc);
        String code = vv.visit(ctx.variable()).getCode();
        HMEquation equation = (HMEquation) table.get(code);

        HMExpression expr = null;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof LismaParser.ExpressionContext) {
                ExpressionVisitor ev = new ExpressionVisitor(table, pc);
                expr = ev.visit(ctx.getChild(i));
            }
        }
        equation.setRightPart(expr);
        return null;
    }

    @Override
    public Object visitPde_equation(@NotNull LismaParser.Pde_equationContext ctx) {
        ExpressionVisitor expressionVisitor = new ExpressionVisitor(table, pc);
        List<EXPPDEOperand> pdes = ctx.partial_operand().stream().map(po -> expressionVisitor.visitPartial_operand(po)).collect(Collectors.toList());
        if (pdes.size() == 0)
            throw new RuntimeException("left part empty: " + ctx.getText());
        String code = pdes.get(0).getVariable().getCode();
        pdes.stream().forEach(p -> {
            if (!p.getVariable().getCode().equals(code))
                throw new RuntimeException("Not supported type of PDE: " + ctx.getText());
        });

        HMPartialDerivativeEquation pde = (HMPartialDerivativeEquation) table.get(code);

        HMExpression expr = null;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof LismaParser.ExpressionContext) {
                ExpressionVisitor ev = new ExpressionVisitor(table, pc);
                expr = ev.visit(ctx.getChild(i));
            }
        }
        pde.setRightPart(expr);
        return super.visitPde_equation(ctx);
    }

    @Override
    public Object visitPde_equation_param(@NotNull LismaParser.Pde_equation_paramContext ctx) {
        ExpressionVisitor expressionVisitor = new ExpressionVisitor(table, null, true, pc);
        String code = null;
        for (LismaParser.Partial_operand_with_paramContext pop : ctx.partial_operand_with_param()) {
            EXPPDEOperand op = expressionVisitor.visitPartial_operand(pop.partial_operand());
            code = op.getVariable().getCode();
            HMVariable ref = table.get(code);
            if (!(ref instanceof HMPartialDerivativeEquation))
                throw new RuntimeException("Incorrect variable");
            HMPartialDerivativeEquation pde = (HMPartialDerivativeEquation) ref;
            HMConst c = new HMConst("p");
            c.setRightPart(getExpressiontFromPde_param_atom(expressionVisitor, pop.pde_param().pde_param_atom()));
            pde.addParam(op.getFirstSpatialVariable(), c);
        }

        HMPartialDerivativeEquation pde = (HMPartialDerivativeEquation) table.get(code);

        HMExpression expr = null;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof LismaParser.ExpressionContext) {
                ExpressionVisitor ev = new ExpressionVisitor(table, pc);
                expr = ev.visit(ctx.getChild(i));
            }
        }
        pde.setRightPart(expr);
        return null;
    }

    // разбираем параметр производной в левой части
    public HMExpression getExpressiontFromPde_param_atom(ExpressionVisitor expressionVisitor, LismaParser.Pde_param_atomContext ctx) {
        if (ctx.expression() != null)
            return expressionVisitor.visit(ctx.expression());
        else if (ctx.Identifier() != null) {
            HMVariable var = table.get(ctx.Identifier().getText());
            if (var == null || !(var instanceof HMConst))
                throw new RuntimeException("Incorrect variable:" + ctx.Identifier().getText());
            HMExpression expression = new HMExpression();
            expression.addOperand(var);
            return expression;
        } else if (ctx.literal() != null) {
            Double var = Double.valueOf(ctx.literal().getText());
            HMExpression expression = new HMExpression();
            expression.addUnnamedConst(var);
            return expression;
        }
        throw new RuntimeException("Unexpected pde param atom: " + ctx.getText());
    }

    @Override
    public Object visitState(@NotNull LismaParser.StateContext ctx) {
        // меняем таблицу переменных на ту что в состоянии
        HMState st = automata.getState(ctx.getChild(1).getText());
        PopulateExpressionVisitor visitor = new PopulateExpressionVisitor(hsm, st.getVariables(), automata, pc);
        visitor.visit(ctx.getChild(5));
        return null;
    }

    @Override
    public Object visitSetter(@NotNull LismaParser.SetterContext ctx) {
        String name = ctx.var_ident().getText();
        ExpressionVisitor ev = new ExpressionVisitor(table, pc);
        HMExpression expression = ev.visit(ctx.expression());
        table.getSetters().put(name, expression);
        return super.visitSetter(ctx);
    }

    @Override
    public HMExpression visitPseudo_state(@NotNull LismaParser.Pseudo_stateContext ctx) {
        return null;
    }

    @Override
    public Object visitLinear_eq(@NotNull LismaParser.Linear_eqContext ctx) {
        currentEquation = hsm.getLinearSystem().createEquation();
        // правая часть
        ExpressionVisitor expressionVisitor = new ExpressionVisitor(table, pc);
        currentEquation.setRightPart(expressionVisitor.visit(ctx.linear_eq_b()));
        return super.visitLinear_eq(ctx);
    }


    @Override
    public Object visitLinear_eq_A(@NotNull LismaParser.Linear_eq_AContext ctx) {
        return super.visitLinear_eq_A(ctx);
    }

    @Override
    public Object visitLinear_eq_A_elem(@NotNull LismaParser.Linear_eq_A_elemContext ctx) {
        HMLinearVar v = hsm.getLinearSystem().getLinearVariable(ctx.var_ident().getText());
        HMExpression expression = visitLinear_eq_A_elem_expr(ctx.linear_eq_A_elem_expr());
        currentEquation.setEquationElem(v, expression);
        return null;
    }

    @Override
    public HMExpression visitLinear_eq_A_elem_expr(@NotNull LismaParser.Linear_eq_A_elem_exprContext ctx) {
        ExpressionVisitor expressionVisitor = new ExpressionVisitor(table, pc);
        return expressionVisitor.visit(ctx);
    }
}
