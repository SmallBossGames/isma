package ru.nstu.isma.in.lisma.analysis.parser.visitor;

import error.IsmaError;
import error.IsmaErrorList;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import ru.nstu.isma.core.hsm.exp.*;
import ru.nstu.isma.core.hsm.service.Infix2PolizConverter;
import ru.nstu.isma.core.hsm.var.HMEquation;
import ru.nstu.isma.core.hsm.var.HMVariable;
import ru.nstu.isma.core.hsm.var.HMVariableTable;
import ru.nstu.isma.core.hsm.var.pde.HMPartialDerivativeEquation;
import ru.nstu.isma.core.hsm.var.pde.HMSpatialVariable;
import ru.nstu.isma.in.lisma.analysis.gen.LismaBaseVisitor;
import ru.nstu.isma.in.lisma.analysis.gen.LismaParser;
import ru.nstu.isma.in.lisma.analysis.parser.InternalConstUtil;
import ru.nstu.isma.in.lisma.analysis.parser.ParserContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bessonov Alex
 * Date: 02.12.13
 * Time: 15:33
 */
public class ExpressionVisitor extends LismaBaseVisitor<Object> {
    private final ParserContext pc;

    private static final Map<String, EXPOperator> unaryOperators = new HashMap<String, EXPOperator>();

    private static final Map<String, EXPOperator> binaryOperators = new HashMap<String, EXPOperator>();

    private final HMVariableTable table;

    private HMExpression infix;

    private boolean ignoreIfVarNotExist = false;

    public ExpressionVisitor(HMVariableTable table, ParserContext pc) {
        this.table = table;
        this.pc = pc;
        initOperators();
    }

    public ExpressionVisitor(HMVariableTable table, IsmaErrorList errors, boolean ignoreIfVarNotExist, ParserContext pc) {
        this.table = table;
        this.ignoreIfVarNotExist = ignoreIfVarNotExist;
        this.pc = pc;
    }

    @Override
    public HMExpression visit(@NotNull ParseTree tree) {
        return visit(tree, true);
    }

    public HMExpression visit(@NotNull ParseTree tree, boolean toPoliz) {
        if (tree == null)
            return null;
        infix = new HMExpression();
        try {
            super.visit(tree);
        } catch (Exception e) {
            IsmaError err = new IsmaError("error during analysis of the right-hand side. " + e.getMessage());
            err.setType(IsmaError.Type.SEM);
            pc.errors().push(err);
        }
        if (toPoliz) {
            Infix2PolizConverter converter = new Infix2PolizConverter(infix);
            return converter.convert();
        } else return infix;
    }

    @Override
    public Object visitPrimary_id(@NotNull LismaParser.Primary_idContext ctx) {
        String name = VisitorUtil.getVariableNameWithIndex(ctx.Identifier().getText(), ctx.cycle_index(), pc);
        Double o = InternalConstUtil.getConst(name.toUpperCase());
        if (o != null)
            infix.addUnnamedConst(o);
        else if (pc.containMacro(name)) {
            HMExpression exp = pc.getMacro(name);
            exp.getTokens().forEach(infix::add);
        } else
            infix.addOperand(getVariable(name));
        return null;
    }

    @Override
    public HMExpression visitFunc_and_math_mapping(@NotNull LismaParser.Func_and_math_mappingContext ctx) {
        String code = ctx.Identifier().getText();
        EXPFunctionOperand func;
        if (table.contain(code))
            func = new EXPMathFunction(table.get(code));
        else
            func = new EXPFunctionOperand(code);

        ctx.arg_list().expression().stream().forEach(s -> {
            ExpressionVisitor v = new ExpressionVisitor(table, pc);
            HMExpression ex = v.visit(s);
            func.addArgExpression(ex);
        });
        infix.add(func);
        return infix;
    }

    @Override
    public Object visitLiteral(@NotNull LismaParser.LiteralContext ctx) {
        double d = Double.valueOf(ctx.getText());
        infix.addUnnamedConst(d);
        return super.visitLiteral(ctx);
    }

    @Override
    public EXPPDEOperand visitPartial_operand(@NotNull LismaParser.Partial_operandContext ctx) {
        return (EXPPDEOperand) super.visitPartial_operand(ctx);
    }

    @Override
    public EXPPDEOperand visitPartial_operand_common(@NotNull LismaParser.Partial_operand_commonContext ctx) {
        // 1 достаем уравнение
        String code = ctx.partial_operand_unknown_code().getText();
        HMEquation eq = getUnknownEquation(code);

        //2 достаем переменную
        HMSpatialVariable spatVar = getSpatialVariable(ctx.partial_operand_spatial_var_code().getText());

        // 3 наполняем операнд
        EXPPDEOperand operand = new EXPPDEOperand(eq);
        operand.setFirstSpatialVariable(spatVar);

        // 4 если указан порядок - проставляем его
        if (ctx.DecimalLiteral() != null) {
            Integer type = Integer.valueOf(ctx.DecimalLiteral().getText());
            operand.setOrder(EXPPDEOperand.Order.getByCode(type));
        } else {
            operand.setOrder(EXPPDEOperand.Order.ONE);
        }
        // 5 добавляем к выражению
        if (infix != null)
            infix.add(operand);
        return operand;
    }

    // смешанный операнд
    @Override
    public Object visitPartial_operand_mixed(@NotNull LismaParser.Partial_operand_mixedContext ctx) {
        // 1 достаем уравнение
        String code = ctx.partial_operand_unknown_code().getText();
        HMEquation eq = getUnknownEquation(code);

        //2 достаем переменную
        if (ctx.partial_operand_spatial_var_code().size() != 2)
            throw new RuntimeException("Incorrect spatial var count: " + ctx.partial_operand_spatial_var_code().size());

        HMSpatialVariable sv1 = getSpatialVariable(ctx.partial_operand_spatial_var_code().get(0).getText());
        HMSpatialVariable sv2 = getSpatialVariable(ctx.partial_operand_spatial_var_code().get(1).getText());

        // 3 наполняем операнд
        EXPPDEOperand operand = new EXPPDEOperand(eq);
        operand.setFirstSpatialVariable(sv1);
        operand.setSecondSpatialVariable(sv2);

        // 4 если указан порядок - проставляем его
        if (ctx.DecimalLiteral() != null) {
            Integer type = Integer.valueOf(ctx.DecimalLiteral().getText());
            operand.setOrder(EXPPDEOperand.Order.getByCode(type));
        } else {
            operand.setOrder(EXPPDEOperand.Order.ONE);
        }

        // 5 добавляем к выражению
        if (infix != null)
            infix.add(operand);
        return operand;
    }

    private HMEquation getUnknownEquation(String code) {
        // 1 достаем уравнение
        HMVariable v = table.get(code);
        if (v == null) {
            if (!ignoreIfVarNotExist)
                throw new RuntimeException("The variable " + code + " is not defined.");
            else
                v = new HMPartialDerivativeEquation(code);
        }
        if (!(v instanceof HMEquation)) {
            /// TODO ошибка
            throw new RuntimeException("There is no corresponding equation for the variable " + code);
        }
        return (HMEquation) v;
    }

    private HMSpatialVariable getSpatialVariable(String code) {
        HMVariable v = table.get(code);
        if (v == null) {
            /// TODO ошибка
            throw new RuntimeException("The variable " + code + " is not defined");
        }
        if (!(v instanceof HMSpatialVariable)) {
            /// TODO ошибка
            throw new RuntimeException("The variable " + code + " is not spatial");
        }
        return (HMSpatialVariable) v;
    }

    @Override
    public EXPPDEOperand visitPartial_operand_spatial_common(@NotNull LismaParser.Partial_operand_spatial_commonContext ctx) {
        // 1 достаем уравнение
        String code = ctx.partial_operand_unknown_code().getText();
        HMVariable v = table.get(code);
        if (v == null) {
            /// TODO ошибка
            if (!ignoreIfVarNotExist)
                throw new RuntimeException("The variable " + code + " is not defined.");
            else
                v = new HMPartialDerivativeEquation(code);
        }
        if (!(v instanceof HMEquation)) {
            /// TODO ошибка
            throw new RuntimeException("There is no corresponding equation for the varible " + code);
        }
        HMEquation eq = (HMEquation) v;

        //2 достаем переменную
        String funcName = ctx.partial_operand_func_spatial_common().getText();
        if (funcName.contains("dx"))
            code = "x";
        else if (funcName.contains("dy"))
            code = "y";
        else if (funcName.contains("dz"))
            code = "z";
        v = table.get(code);
        if (v == null) {
            /// TODO ошибка
            throw new RuntimeException("The variable " + code + " is not defined");
        }
        if (!(v instanceof HMSpatialVariable)) {
            /// TODO ошибка
            throw new RuntimeException("The variable " + code + " is not spatial");
        }
        HMSpatialVariable spatVar = (HMSpatialVariable) v;

        // 3 наполняем операнд
        EXPPDEOperand operand = new EXPPDEOperand(eq);
        operand.setFirstSpatialVariable(spatVar);

        // 4 если указан порядок - проставляем его
        if (ctx.DecimalLiteral() != null) {
            Integer type = Integer.valueOf(ctx.DecimalLiteral().getText());
            operand.setOrder(EXPPDEOperand.Order.getByCode(type));
        } else {
            operand.setOrder(EXPPDEOperand.Order.ONE);
        }
        if (infix != null)
            infix.add(operand);
        return operand;
    }

    @Override
    public EXPPDEOperand visitPartial_operand_spatial_N(@NotNull LismaParser.Partial_operand_spatial_NContext ctx) {
        // 1 достаем уравнение
        String code = ctx.partial_operand_unknown_code().getText();
        HMVariable v = table.get(code);
        if (v == null) {
            if (!ignoreIfVarNotExist)
                throw new RuntimeException("The variable " + code + " is not defined.");
            else
                v = new HMPartialDerivativeEquation(code);
        }
        if (!(v instanceof HMEquation)) {
            /// TODO ошибка
            throw new RuntimeException("There is no corresponding equation for the variable " + code);
        }
        HMEquation eq = (HMEquation) v;

        //2 достаем переменную
        Integer type = null;
        String funcName = ctx.partial_operand_func_spatial().getText();
        if (ctx.partial_operand_func_spatial().partial_operand_func_spatial_2() != null)
            type = 2;
        else if (ctx.partial_operand_func_spatial().partial_operand_func_spatial_3() != null)
            type = 3;
        else if (ctx.partial_operand_func_spatial().partial_operand_func_spatial_4() != null)
            type = 4;

        if (funcName.contains("dx"))
            code = "x";
        else if (funcName.contains("dy"))
            code = "y";
        else if (funcName.contains("dz"))
            code = "z";
        v = table.get(code);
        if (v == null) {
            /// TODO ошибка
            throw new RuntimeException("The variable " + code + " is not defined");
        }
        if (!(v instanceof HMSpatialVariable)) {
            /// TODO ошибка
            throw new RuntimeException("The variable " + code + " is not defined");
        }
        HMSpatialVariable spatVar = (HMSpatialVariable) v;

        // 3 наполняем операнд
        EXPPDEOperand operand = new EXPPDEOperand(eq);
        operand.setFirstSpatialVariable(spatVar);

        // 4 если указан порядок - проставляем его
        if (type != null) {
            operand.setOrder(EXPPDEOperand.Order.getByCode(type));
        } else operand.setOrder(EXPPDEOperand.Order.ONE);
        if (infix != null)
            infix.add(operand);
        return operand;
    }

    // + -  (binary)
    @Override
    public Object visitAdditiveExpressionOperator(@NotNull LismaParser.AdditiveExpressionOperatorContext ctx) {
        addOperator(ctx.getText(), true);
        return super.visitAdditiveExpressionOperator(ctx);
    }

    // * /
    @Override
    public Object visitMultiplicativeExpressionOperator(@NotNull LismaParser.MultiplicativeExpressionOperatorContext ctx) {
        addOperator(ctx.getText(), true);
        return super.visitMultiplicativeExpressionOperator(ctx);
    }

    // not NOT !
    @Override
    public Object visitNot_operator(@NotNull LismaParser.Not_operatorContext ctx) {
        addOperator(ctx.getText(), false);
        return super.visitNot_operator(ctx);    //To change body of overridden methods use File | Settings | File Templates.
    }

    // +  -  (unary)
    @Override
    public Object visitUnaryExpressionOperator(@NotNull LismaParser.UnaryExpressionOperatorContext ctx) {
        addOperator(ctx.getText(), false);
        return super.visitUnaryExpressionOperator(ctx);
    }

    // == !=
    @Override
    public Object visitEqualityExpressionOperator(@NotNull LismaParser.EqualityExpressionOperatorContext ctx) {
        addOperator(ctx.getText(), true);
        return super.visitEqualityExpressionOperator(ctx);
    }

    // < <= > >=
    @Override
    public Object visitRelationalOp(@NotNull LismaParser.RelationalOpContext ctx) {
        addOperator(ctx.getText(), true);
        return super.visitRelationalOp(ctx);
    }

    // or OR ||
    @Override
    public Object visitOr_operator(@NotNull LismaParser.Or_operatorContext ctx) {
        addOperator(ctx.getText(), true);
        return super.visitOr_operator(ctx);
    }

    // and AND &&
    @Override
    public Object visitAnd_operator(@NotNull LismaParser.And_operatorContext ctx) {
        addOperator(ctx.getText(), true);
        return super.visitAnd_operator(ctx);
    }

    // (
    @Override
    public Object visitParExpressionLeftPar(@NotNull LismaParser.ParExpressionLeftParContext ctx) {
        infix.openParenthesis();
        return super.visitParExpressionLeftPar(ctx);
    }

    // )
    @Override
    public Object visitParExpressionRightPar(@NotNull LismaParser.ParExpressionRightParContext ctx) {
        infix.closeParenthesis();
        return super.visitParExpressionRightPar(ctx);
    }

    private void addOperator(String code, boolean binary) {
        EXPOperator o = null;
        if (binary) {
            o = binaryOperators.get(code);
        } else {
            o = unaryOperators.get(code);
        }
        if (o == null) {
            throw new RuntimeException("unknown operator " + code);

        } else {
            infix.add(o);
        }
    }

    private HMVariable getVariable(String name) {
        HMVariable v = table.get(name);
        if (v == null) {
            throw new RuntimeException("The variable " + name + " is not defined.");
        }
        return v;
    }

    private void initOperators() {
        unaryOperators.put("!", EXPOperator.not());
        unaryOperators.put("not", EXPOperator.not());
        unaryOperators.put("NOT", EXPOperator.not());
        unaryOperators.put("+", EXPOperator.un_plus());
        unaryOperators.put("-", EXPOperator.un_minus());

        binaryOperators.put("+", EXPOperator.add());
        binaryOperators.put("-", EXPOperator.sub());
        binaryOperators.put("*", EXPOperator.mult());
        binaryOperators.put("/", EXPOperator.div());

        binaryOperators.put("<", EXPOperator.less());
        binaryOperators.put(">", EXPOperator.greater());
        binaryOperators.put("==", EXPOperator.equal());
        binaryOperators.put("!=", EXPOperator.not_equal());
        binaryOperators.put("<=", EXPOperator.less_equal());
        binaryOperators.put(">=", EXPOperator.greater_equal());

        binaryOperators.put("and", EXPOperator.and());
        binaryOperators.put("AND", EXPOperator.and());
        binaryOperators.put("&&", EXPOperator.and());

        binaryOperators.put("or", EXPOperator.or());
        binaryOperators.put("OR", EXPOperator.or());
        binaryOperators.put("||", EXPOperator.or());
    }

    @Override
    public HMExpression visitPseudo_state(@NotNull LismaParser.Pseudo_stateContext ctx) {
        return null;
    }
}
