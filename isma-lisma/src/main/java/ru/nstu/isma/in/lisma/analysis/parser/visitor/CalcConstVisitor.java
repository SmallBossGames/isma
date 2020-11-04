package ru.nstu.isma.in.lisma.analysis.parser.visitor;

import error.IsmaError;
import error.IsmaErrorList;
import ru.nstu.isma.core.hsm.var.HMConst;
import ru.nstu.isma.core.hsm.var.HMDerivativeEquation;
import ru.nstu.isma.core.hsm.var.HMVariableTable;
import ru.nstu.isma.core.hsm.var.pde.HMPartialDerivativeEquation;
import ru.nstu.isma.core.hsm.service.ConstValueCalculator;
import ru.nstu.isma.in.lisma.analysis.gen.LismaParser;
import org.antlr.v4.runtime.misc.NotNull;
import ru.nstu.isma.in.lisma.analysis.parser.ParserContext;

/**
 * Created by Bessonov Alex
 * Date: 03.12.13
 * Time: 0:35
 */
public class CalcConstVisitor extends BaseVisitor {
    private HMVariableTable table;

    public CalcConstVisitor(HMVariableTable table, IsmaErrorList errors, ParserContext pc) {
        super(pc);
        this.table = table;

        // вычисляем значения параметров в левых частях ДУЧП
        table.getPdes().forEach(p ->
                p.getParams().values().forEach(c -> {
                    ConstValueCalculator calculator = new ConstValueCalculator(errors);
                    c.setValue(calculator.calculateConst(c));
                }));
    }

    @Override
    public Object visitConstant_body(@NotNull LismaParser.Constant_bodyContext ctx) {
        VariableVisitor vv = new VariableVisitor(pc);
        ConstValueCalculator calculator = new ConstValueCalculator(pc.errors());
        ctx.var_ident().stream()
                .forEach(vi -> {
                    HMConst c = (HMConst) table.get(vv.visitVar_ident(vi).getCode());
                    c.setValue(calculator.calculateConst(c));
                });

        return null;
    }

    @Override
    public Object visitInit_cond(@NotNull LismaParser.Init_condContext ctx) {
        VariableVisitor vv = new VariableVisitor(pc);
        if (ctx.variable() != null)
            calcInitCond(vv.visit(ctx.variable()).getCode());
        ctx.init_cond_body().forEach(cc -> calcInitCond(vv.visit(cc.variable()).getCode()));
        return null;
    }

    private void calcInitCond(String code) {
        if (!(table.get(code) instanceof HMDerivativeEquation)) {
            IsmaError e = new IsmaError("error while computing boundary condtions. There is no corresponding ODE for the variable " + code);
            e.setType(IsmaError.Type.SEM);
            pc.errors().push(e);
        }

        HMDerivativeEquation equation = (HMDerivativeEquation) table.get(code);
        // TODO временный костыль - не подсчитываем значения у PDE поскольку на данном этапе это не возможно
        if (!(equation instanceof HMPartialDerivativeEquation)) {
            HMConst initConst = equation.getInitial();
            ConstValueCalculator calculator = new ConstValueCalculator(pc.errors());
            initConst.setValue(calculator.calculateConst(initConst));
        }
    }


}
