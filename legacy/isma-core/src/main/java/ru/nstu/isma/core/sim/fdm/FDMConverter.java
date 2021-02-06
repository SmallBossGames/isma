package ru.nstu.isma.core.sim.fdm;

import ru.nstu.isma.core.hsm.*;
import ru.nstu.isma.core.hsm.exp.*;
import ru.nstu.isma.core.hsm.var.*;
import ru.nstu.isma.core.hsm.var.pde.HMBoundaryCondition;
import ru.nstu.isma.core.hsm.var.pde.HMPartialDerivativeEquation;
import ru.nstu.isma.core.hsm.var.pde.HMSampledSpatialVariable;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by Bessonov Alex
 * Date: 12.12.13
 * Time: 1:53
 * преобразует модель с ДУЧП в модель с ОДУ
 */
public class FDMConverter {

    private HSM model;

    private HMVariableTable variableTable;

    private LinkedList<HMSampledSpatialVariable> apxVars = new LinkedList<HMSampledSpatialVariable>();

    private HashSet<HMVariable> notApx = new HashSet<HMVariable>();

    private HashSet<HMEquation> apxEq = new HashSet<HMEquation>();

    private FDMIndexIterator indexIterator;


    public FDMConverter(HSM model) {
        this.model = model;
        variableTable = model.getVariableTable();
    }

    public HSM convert() {
        // подготавливаем контекст аппроксимации - информацию для построения сетки
        prepare();

        // совершаем полный обход всех индексов
        indexIterator.start();

        processGridNode();
        do {
            indexIterator.next();
            processGridNode();
        } while (!indexIterator.atEnd());

        // убираем края и строим разностные аналоги для операторов ДУЧП
        indexIterator.start();
        completeGrid();
       do {
            indexIterator.next();
            completeGrid();
        }  while (!indexIterator.atEnd());

        // удаляем все старые аппроксимируемые переменные
        for (HMSampledSpatialVariable v : apxVars) {
            variableTable.remove(v.getCode());
        }
        // удаляем все старые аппроксимируемые уравнения
        for (HMEquation e : apxEq) {
            variableTable.remove(e.getCode());
        }
        return model;
    }

    // аппроксимируемые переменные разбивают пространство на сетку, где каждому узлу соответствует набор уравнений
    private void processGridNode() {
        // прописываем все аппроксимируемые переменные как константы
        for (FDMIndexedApxVar i : indexIterator.get()) {
            variableTable.add(i.toConst());
        }
        // генерируем для каждого уравнения аналог для сетки
        for (HMEquation eq : apxEq) {
            processEquation(eq);
        }
    }

    private HMEquation processEquation(HMEquation eq) {
        String newName = equationNameMapping(eq);
        // если таблица уже содержит такое уравнение - не будем разбирать снова
        if (variableTable.contain(newName)) {
            return (HMEquation) variableTable.get(newName);
        }
        // создаем объект нового уравнения
        HMEquation newEq = null;
        // ОДУ и ДУЧП
        if (eq instanceof HMDerivativeEquation) {
            newEq = new HMDerivativeEquation(newName);
            ((HMDerivativeEquation) newEq).setInitial(((HMDerivativeEquation) eq).getInitial().getValue());
            // алгебраические
        } else if (eq instanceof HMAlgebraicEquation) {
            newEq = new HMAlgebraicEquation(newName);
        }
        if (newEq == null) {
            throw new RuntimeException("FDMConverter -> processEquation error!");
        }

        HMExpression rp = new HMExpression();
        // пробегаем по правой части и меняем все аппроксимируемые переменные
        for (EXPToken tt : eq.getRightPart().getTokens()) {
            if (tt instanceof EXPOperand && !(tt instanceof EXPPDEOperand)) {
                EXPOperand o = (EXPOperand) tt;
                // TODO выражения могут хранить неправедбные объекты
                HMVariable mappedVar = getMappedVar(o.getVariable());
                if (mappedVar != o.getVariable()) {
                    tt = new EXPOperand(mappedVar);
                }
            }
            rp.add(tt);
        }
        newEq.setRightPart(rp);

        variableTable.add(newEq);
        return newEq;
    }

    private void completeGrid() {
        for (HMEquation eq : apxEq) {
            HMEquation newEq = processEquation(eq);

            HMExpression rp = new HMExpression();

            // пробегаем по правой части и меняем все аппроксимируемые переменные
            for (EXPToken tt : eq.getRightPart().getTokens()) {
                if (tt instanceof EXPPDEOperand) {
                    EXPPDEOperand o = (EXPPDEOperand) tt;

//                    HMVariable mappedVar = getMappedVar(o.getVariable());
//                    if (mappedVar != o.getVariable()) {
//                        tt = new EXPOperand(mappedVar);
//                    }
                   // System.out.println(o.toString());
                    HMUnnamedConst uc;
                    HMPartialDerivativeEquation pde = (HMPartialDerivativeEquation) variableTable.get(o.getVariable().getCode());
                    HMSampledSpatialVariable av = o.getSampledFirstSpatialVar();
                    FDMIndexedApxVar idx = indexIterator.getIndex(av.getCode());
                    if (idx.isFirst()) {
                        pde.getBound(HMBoundaryCondition.SideType.LEFT, av).getValue().getTokens().forEach(t-> rp.add(t));
                    } else if (idx.isMax()) {
                        pde.getBound(HMBoundaryCondition.SideType.RIGHT, av).getValue().getTokens().forEach(t-> rp.add(t));
                    } else {
                        HMEquation eq_idx_plus_1 = (HMEquation) variableTable.get(equationNameMappingSpecIndex(pde, av, idx.getIndex() + 1));
                        HMEquation eq_idx_minus_1 = (HMEquation) variableTable.get(equationNameMappingSpecIndex(pde, av, idx.getIndex() - 1));
                        HMEquation eq_cur = (HMEquation) variableTable.get(equationNameMapping(pde));

                        if (eq_idx_plus_1 == null || eq_idx_minus_1 == null || eq_cur == null ) {
                            throw new RuntimeException("FDM: all is bad");
                        }
                        if (o.getOrder() == EXPPDEOperand.Order.ONE) {
                            uc = new HMUnnamedConst(o.getSampledFirstSpatialVar().getStepSize());
                            rp.add(new EXPOperand(eq_idx_plus_1));
                            rp.add(new EXPOperand(eq_idx_minus_1));
                            rp.add(EXPOperator.sub());
                            rp.add(new EXPOperand(new HMUnnamedConst(2)));
                            rp.add(new EXPOperand(uc));
                            rp.add(EXPOperator.mult());
                            rp.add(EXPOperator.div());
                        } else if (o.getOrder() == EXPPDEOperand.Order.TWO) {
                            uc = new HMUnnamedConst(Math.pow(o.getSampledFirstSpatialVar().getStepSize(), 2));
                            rp.add(new EXPOperand(eq_idx_minus_1));
                            rp.add(new EXPOperand(new HMUnnamedConst(2)));
                            rp.add(new EXPOperand(eq_cur));
                            rp.add(EXPOperator.mult());
                            rp.add(EXPOperator.sub());
                            rp.add(new EXPOperand(eq_idx_plus_1));
                            rp.add(EXPOperator.add());
                            rp.add(new EXPOperand(uc));
                            rp.add(EXPOperator.div());
                        }
                    }

                } else {
                    rp.add(tt);
                }
            }
            newEq.setRightPart(rp);
        }
    }

    // Парсинг таблицы переменных и наполнение предварительных данных
    public void prepare() {

        // запоминаем все апроксимируемые переменные и уравнения
        for (String s : variableTable.keySet()) {
            HMVariable vv = variableTable.get(s);
            if (vv instanceof HMPartialDerivativeEquation) {
                apxEq.add((HMEquation) vv);
            } else if (vv instanceof HMSampledSpatialVariable) {
                apxVars.add((HMSampledSpatialVariable) vv);
            }
        }

        boolean isReady = false;
        boolean eqNeedApx;

        while (!isReady) {
            // пробегаем все уравнения в которых есть другие аппроксимируемые уравнения или переменные
            isReady = true;
            for (String s : variableTable.keySet()) {
                HMVariable var = variableTable.get(s);

                if (var instanceof HMEquation
                        && !(var instanceof HMConst)
                        && !apxEq.contains(var)) {
                    // проверяем правую часть на аппроксимируемые элементы
                    eqNeedApx = false;
                    HMExpression right = ((HMEquation) var).getRightPart();
                    for (EXPToken t : right.getTokens()) {
                        // проверяем только уравнения и только если ранее не было найдено (eqNeedApx)
                        if (t instanceof EXPOperand && !eqNeedApx) {
                            HMVariable operVar = ((EXPOperand) t).getVariable();
                            if (apxEq.contains(operVar) || apxVars.contains(operVar)) {
                                isReady = false;
                                apxEq.add((HMEquation) var);
                                eqNeedApx = true;
                            }
                        }
                    }
                    if (!eqNeedApx) {
                        notApx.add(var);
                    }
                } else {
                    if (!apxVars.contains(var) && !apxEq.contains(var))
                        notApx.add(var);
                }
            }
        }
        // подготовим итератор по индексам - полный перебор всех индексов
        indexIterator = getIterator();
    }

    // инициализировать итератор индексов
    private FDMIndexIterator getIterator() {
        FDMIndexIterator iterator = new FDMIndexIterator();
        for (HMSampledSpatialVariable av : apxVars) {
            iterator.addIndex(new FDMIndexedApxVar(av));
        }
        return iterator;
    }


    // TODO рефакторинг
    private HMVariable getMappedVar(HMVariable v) {
        if (notApx.contains(v) || v instanceof HMUnnamedConst) {
            return v;
        } else if (v instanceof HMSampledSpatialVariable) {
            return variableTable.get(apxVarNameMapping((HMSampledSpatialVariable) v));
        } else if (v instanceof HMEquation) {
            String code = equationNameMapping(v);
            if (variableTable.contain(code)) {
                return variableTable.get(code);
            } else {
                return processEquation((HMEquation) v);
            }
        }
        throw new RuntimeException("Cant find mapped variable: " + v.getCode());
//
//        if (!context.containsApxVar(v.getCode())) {
//            return v;
//        }
//        String newName = nameMapping(v);
//        if (variables.contain(newName)) {
//            return variables.get(newName);
//        }
//        if (v instanceof HMEquation) {
//            processEquation((HMEquation) v);
//        }
//        throw new RuntimeException("Cant find mapped variable: " + newName);
    }

    // TODO apxLinkTable
    protected String equationNameMapping(HMVariable equation) {
        if (!apxEq.contains(equation)) {
            throw new RuntimeException("FDM converter doesn't contain " + equation.getCode());
        }
        StringBuilder newName = new StringBuilder(equation.getCode());
        newName.append(FDMStatic.APX_PREFIX);
        for (HMSampledSpatialVariable v : apxVars) {
            newName.append("_");
            newName.append(indexIterator.getIndex(v.getCode()).getIndex());
//            if (isEqContains(equation, v)) {
//                newName.append(indexes.get(v.getCode()).getIndex());
//            } else if (!indexes.containsKey(v.getCode())) {
//                newName.append("E");
//            } else {
//                newName.append("0");
//            }
        }
        return newName.toString();
    }

    protected String equationNameMappingSpecIndex(HMVariable equation, HMSampledSpatialVariable av, Integer specValue) {
        if (!apxEq.contains(equation)) {
            throw new RuntimeException("FDM converter doesn't contain " + equation.getCode());
        }
        StringBuilder newName = new StringBuilder(equation.getCode());
        newName.append(FDMStatic.APX_PREFIX);
        for (HMSampledSpatialVariable v : apxVars) {
            newName.append("_");
            if (v.getCode().equals(av.getCode())) {
                newName.append(specValue);
            } else {
                newName.append(indexIterator.getIndex(v.getCode()).getIndex());
            }
        }
        return newName.toString();
    }

    private String apxVarNameMapping(HMSampledSpatialVariable variable) {
        return indexIterator.getIndex(variable.getCode()).getConstCode();
    }

}
