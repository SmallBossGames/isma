package ru.nstu.isma.core.sim.fdm;

import ru.nstu.isma.core.hsm.*;
import ru.nstu.isma.core.hsm.exp.*;
import ru.nstu.isma.core.hsm.var.*;
import ru.nstu.isma.core.hsm.var.pde.HMBoundaryCondition;
import ru.nstu.isma.core.hsm.var.pde.HMPartialDerivativeEquation;
import ru.nstu.isma.core.hsm.var.pde.HMSampledSpatialVariable;
import ru.nstu.isma.core.hsm.service.PDEInitialValueCalculator;

import java.util.LinkedList;
import java.util.Set;

/**
 * Created by Bessonov Alex on 02.03.14.
 */
public class FDMNewConverter {
    private HSM model;

    private LinkedList<HMSampledSpatialVariable> approximatedVariables = new LinkedList<HMSampledSpatialVariable>();

    private LinkedList<HMEquation> approximatedEquations = new LinkedList<HMEquation>();

    private LinkedList<HMVariable> notApproximated = new LinkedList<HMVariable>();

    private FDMIndexIterator indexIterator;

    public FDMNewConverter(HSM model) {
        this.model = model;
    }

    private HMVariableTable getVT() {
        if (model == null) {
            throw new RuntimeException("Model not init!");
        }
        return model.getVariableTable();
    }

    public HSM convert() {
        try {
            healModelPhase();
            preparePhase();
            generateObjectPhase();
            correctRightPartsPhase();
            initialConditionPhase();
            boundsPhase();
            finishPhase();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return model;
    }

    public void healModelPhase() {
        for (String k : getVT().keySet()) {
            HMVariable vv = getVT().get(k);
            if (vv instanceof HMEquation) {
                HMEquation eq = (HMEquation) vv;
                if (eq.getRightPart() != null) {
                    healModelEquation(eq.getRightPart());
                }
            }
        }
    }

    public void healModelEquation(HMExpression expr) {
        for (EXPToken tt : expr.getTokens()) {
            if (tt instanceof EXPFunctionOperand) {
                EXPFunctionOperand f = (EXPFunctionOperand) tt;
                f.getArgs().stream().forEach(e -> healModelEquation(e));
            } else if (tt instanceof EXPOperand) {
                EXPOperand o = (EXPOperand) tt;
                if (o.getVariable() != null && getVT().contain((o.getVariable().getCode()))) {
                    o.setVariable(getVT().get(o.getVariable().getCode()));
                }
            }
        }
    }

    public void preparePhase() {
        Set<String> variableCodes = getVT().keySet();

        // запоминаем все апроксимируемые переменные и ДУЧП
        for (String varCode : variableCodes) {
            HMVariable variable = getVT().get(varCode);
            if (variable instanceof HMPartialDerivativeEquation) {
                approximatedEquations.add((HMEquation) variable);
            } else if (variable instanceof HMSampledSpatialVariable) {
                approximatedVariables.add((HMSampledSpatialVariable) variable);
            }
        }

        // выявляем пассивные апроксимируемые элементы
        boolean isReady = false;
        while (!isReady) {
            isReady = true;
            for (String varCode : variableCodes) {
                HMVariable equation = getVT().get(varCode);
                if (isNeedToAddIntoApproximateEq(equation)) {
                    approximatedEquations.add((HMEquation) equation);
                    isReady = false;
                }
            }
        }

        // запоминаем все не апроксимируемые элементы
        for (String varCode : variableCodes) {
            HMVariable var = getVT().get(varCode);
            if (!approximatedEquations.contains(var) && !approximatedVariables.contains(var))
                notApproximated.add(var);
        }

        // создаем итератор
        indexIterator = getNewIterator();
    }

    public void generateObjectPhase() {
        indexIterator.start();
        doGenerateObjectPhase();
        if (!indexIterator.atEnd())
            do {
                indexIterator.next();
                doGenerateObjectPhase();
            } while (!indexIterator.atEnd());
    }

    public void doGenerateObjectPhase() {
        // прописываем все аппроксимируемые переменные как константы
        for (FDMIndexedApxVar i : indexIterator.get()) {
            getVT().add(i.toConst());
        }
        // генерируем для каждого уравнения аналог для сетки
        for (HMEquation equation : approximatedEquations) {
            String newName = equationNameMapping(equation);
            if (getVT().contain(newName)) {
                continue;
            }
            // создаем объект нового уравнения
            HMEquation newEq = null;
            // ОДУ и ДУЧП
            if (equation instanceof HMDerivativeEquation) {
                newEq = new HMDerivativeEquation(newName);
                ((HMDerivativeEquation) newEq).setInitial(((HMDerivativeEquation) equation).getInitial());
                // алгебраические
            } else if (equation instanceof HMAlgebraicEquation) {
                newEq = new HMAlgebraicEquation(newName);
            }
            if (newEq == null) {
                throw new RuntimeException("FDMConverter -> processEquation error!");
            }
            newEq.setRightPart(equation.getRightPart());
            getVT().add(newEq);
        }
    }

    public void correctRightPartsPhase() {
        indexIterator.start();
        doCorrectRightPartsPhase();
        if (!indexIterator.atEnd())
            do {
                indexIterator.next();
                doCorrectRightPartsPhase();
            } while (!indexIterator.atEnd());
    }

    public void doCorrectRightPartsPhase() {
        for (HMEquation eq : approximatedEquations) {
            HMEquation appEq = (HMEquation) getMappedVar(eq);
            appEq.setRightPart(correctRightParts(appEq.getRightPart()));
            if (appEq instanceof HMDerivativeEquation) {
                HMDerivativeEquation der = (HMDerivativeEquation) appEq;
                HMConst initial = new HMConst(der.getInitial().getCode());
                initial.setRightPart(correctRightParts(der.getInitial().getRightPart()));
                der.setInitial(initial);
            }
        }
    }

    private HMExpression correctRightParts(HMExpression oldRP) {
        HMExpression newRP = new HMExpression();
        newRP.setType(oldRP.getType());
        // пробегаем по правой части и меняем все аппроксимируемые переменные
        for (EXPToken tt : oldRP.getTokens()) {
            if (tt instanceof EXPPDEOperand) {
                EXPPDEOperand o = (EXPPDEOperand) tt;
                HMUnnamedConst uc;
                HMPartialDerivativeEquation pde = (HMPartialDerivativeEquation) getVT().get(o.getVariable().getCode());
                HMSampledSpatialVariable av = o.getSampledFirstSpatialVar();
                FDMIndexedApxVar idx = indexIterator.getIndex(av.getCode());
                addSubst(pde, av, idx, o, newRP);
                /*
                    HMEquation eq_idx_plus_1 = (HMEquation) getVT().get(equationNameMappingSpecIndex(pde, av, idx.getIndex() + 1));
                    HMEquation eq_idx_minus_1 = (HMEquation) getVT().get(equationNameMappingSpecIndex(pde, av, idx.getIndex() - 1));
                    HMEquation eq_cur = (HMEquation) getVT().get(equationNameMapping(pde));

                    if (eq_idx_plus_1 == null || eq_idx_minus_1 == null || eq_cur == null) {
                        throw new RuntimeException("FDM: all is bad");
                    }
                    if (o.getOrder() == EXPPDEOperand.Order.ONE) {
                        uc = new HMUnnamedConst(o.getSampledFirstSpatialVar().getStepSize());
                        newRP.add(new EXPOperand(eq_idx_plus_1));
                        newRP.add(new EXPOperand(eq_idx_minus_1));
                        newRP.add(EXPOperator.sub());
                        newRP.add(new EXPOperand(new HMUnnamedConst(2)));
                        newRP.add(new EXPOperand(uc));
                        newRP.add(EXPOperator.mult());
                        newRP.add(EXPOperator.div());
                    } else if (o.getOrder() == EXPPDEOperand.Order.TWO) {
                        uc = new HMUnnamedConst(Math.pow(o.getSampledFirstSpatialVar().getStepSize(), 2));
                        newRP.add(new EXPOperand(eq_idx_minus_1));
                        newRP.add(new EXPOperand(new HMUnnamedConst(2)));
                        newRP.add(new EXPOperand(eq_cur));
                        newRP.add(EXPOperator.mult());
                        newRP.add(EXPOperator.sub());
                        newRP.add(new EXPOperand(eq_idx_plus_1));
                        newRP.add(EXPOperator.add());
                        newRP.add(new EXPOperand(uc));
                        newRP.add(EXPOperator.div());
                    }*/
            } else if (tt instanceof EXPFunctionOperand) {
                EXPFunctionOperand func = new EXPFunctionOperand(((EXPFunctionOperand) tt).getName());
                for (HMExpression exp : ((EXPFunctionOperand) tt).getArgs()) {
                    func.addArgExpression(correctRightParts(exp));
                }
                newRP.add(func);
            } else if (tt instanceof EXPOperand && !(tt instanceof EXPPDEOperand)) {
                EXPOperand o = (EXPOperand) tt;
                // TODO выражения могут хранить неправедбные объекты
                HMVariable mappedVar = getMappedVar(o.getVariable());
                if (mappedVar != o.getVariable()) {
                    tt = new EXPOperand(mappedVar);
                }
                newRP.add(tt);
            } else {
                newRP.add(tt);
            }
        }
        return newRP;
    }

    private void addSubst(HMPartialDerivativeEquation pde, HMSampledSpatialVariable av, FDMIndexedApxVar idx, EXPPDEOperand o,
                          HMExpression newRP) {
        HMEquation eq_idx_plus_1 = (HMEquation) getVT().get(equationNameMappingSpecIndex(pde, av, idx.getIndex() + 1));
        HMEquation eq_idx_minus_1 = (HMEquation) getVT().get(equationNameMappingSpecIndex(pde, av, idx.getIndex() - 1));
        HMEquation eq_cur = (HMEquation) getVT().get(equationNameMapping(pde));
        HMUnnamedConst uc;

        if (idx.isMax() && pde.getBound(HMBoundaryCondition.SideType.RIGHT, av).getDerOrder() > 0) {
            pde.getBound(HMBoundaryCondition.SideType.RIGHT, av).getValue().getTokens().forEach(t -> newRP.add(t));
        } else if (idx.isFirst() && pde.getBound(HMBoundaryCondition.SideType.LEFT, av).getDerOrder() > 0) {
            pde.getBound(HMBoundaryCondition.SideType.LEFT, av).getValue().getTokens().forEach(t -> newRP.add(t));
        } else if (o.getOrder() == EXPPDEOperand.Order.ONE) {
//            if (eq_idx_plus_1 == null || eq_idx_minus_1 == null || eq_cur == null) {
//                throw new RuntimeException("FDM: all is bad");
//            }

            uc = new HMUnnamedConst(o.getSampledFirstSpatialVar().getStepSize());

            if (idx.isMax())
                pde.getBound(HMBoundaryCondition.SideType.RIGHT, av).getValue().getTokens().forEach(t -> newRP.add(t));
            else
                newRP.add(new EXPOperand(eq_idx_plus_1));

            if (idx.isFirst())
                pde.getBound(HMBoundaryCondition.SideType.LEFT, av).getValue().getTokens().forEach(t -> newRP.add(t));
            else
                newRP.add(new EXPOperand(eq_idx_minus_1));

            newRP.add(EXPOperator.sub());
            newRP.add(new EXPOperand(new HMUnnamedConst(2)));
            newRP.add(new EXPOperand(uc));
            newRP.add(EXPOperator.mult());
            newRP.add(EXPOperator.div());
        } else if (o.getOrder() == EXPPDEOperand.Order.TWO) {
            uc = new HMUnnamedConst(Math.pow(o.getSampledFirstSpatialVar().getStepSize(), 2));
            if (idx.isFirst())
                pde.getBound(HMBoundaryCondition.SideType.LEFT, av).getValue().getTokens().forEach(t -> newRP.add(t));
            else
                newRP.add(new EXPOperand(eq_idx_minus_1));
            newRP.add(new EXPOperand(new HMUnnamedConst(2)));
            newRP.add(new EXPOperand(eq_cur));
            newRP.add(EXPOperator.mult());
            newRP.add(EXPOperator.sub());
            if (idx.isMax())
                pde.getBound(HMBoundaryCondition.SideType.RIGHT, av).getValue().getTokens().forEach(t -> newRP.add(t));
            else
                newRP.add(new EXPOperand(eq_idx_plus_1));
            newRP.add(EXPOperator.add());
            newRP.add(new EXPOperand(uc));
            newRP.add(EXPOperator.div());
        }
    }


    public void initialConditionPhase() {
    }


    public void boundsPhase() {
    }


    public void finishPhase() {
        // удаляем все старые аппроксимируемые переменные
        for (HMSampledSpatialVariable v : approximatedVariables) {
            getVT().remove(v.getCode());
        }
        // удаляем все старые аппроксимируемые уравнения
        for (HMEquation e : approximatedEquations) {
            getVT().remove(e.getCode());
        }

        // высчитываем значения НУ для ОДУ
        for (String str : getVT().keySet()) {
            HMVariable v = getVT().get(str);
            if (v instanceof HMDerivativeEquation) {
                HMDerivativeEquation der = (HMDerivativeEquation) v;
                PDEInitialValueCalculator.calculate(der.getInitial());
            }
        }
    }

    // -------------------- ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ------------------

    private boolean isNeedToAddIntoApproximateEq(HMVariable var) {
        boolean eqNeedApx;
        boolean res = false;
        // проверяем уравенения (не константы) не содержащиеся в списке апроксимации
        if (var instanceof HMEquation
                && !(var instanceof HMConst)
                && !approximatedEquations.contains(var)) {
            res = checkExpression(((HMEquation) var).getRightPart());
        } else {
            res = false;
        }
        return res;
    }

    private boolean checkExpression(HMExpression right) {
        boolean eqNeedApx = false;

        for (EXPToken t : right.getTokens()) {
            // если есть оператор ДУЧП - автоматом попадает в апроксимируемые
            if (t instanceof EXPPDEOperand) {
                eqNeedApx = true;
                // проверяем фунции
            } else if (t instanceof EXPFunctionOperand && !eqNeedApx) {
                for (HMExpression expr : ((EXPFunctionOperand) t).getArgs()) {
                    if (checkExpression(expr)) {
                        eqNeedApx = true;
                    }
                }
                // проверяем только уравнения и только если ранее не было найдено (eqNeedApx)
            } else if (t instanceof EXPOperand && !eqNeedApx) {
                HMVariable operandVarisable = ((EXPOperand) t).getVariable();
                if (approximatedEquations.contains(operandVarisable) || approximatedVariables.contains(operandVarisable)) {
                    eqNeedApx = true;
                }
            }

        }
        return eqNeedApx;
    }

    // инициализировать итератор индексов
    private FDMIndexIterator getNewIterator() {
        FDMIndexIterator iterator = new FDMIndexIterator();
        for (HMSampledSpatialVariable av : approximatedVariables) {
            iterator.addIndex(new FDMIndexedApxVar(av));
        }
        return iterator;
    }

    protected String equationNameMapping(HMVariable equation) {
        if (!approximatedEquations.contains(equation)) {
            throw new RuntimeException("FDM converter doesn't contain " + equation.getCode());
        }
        StringBuilder newName = new StringBuilder(equation.getCode());
        newName.append(FDMStatic.APX_PREFIX);
        for (HMSampledSpatialVariable v : approximatedVariables) {
            newName.append("_");
            newName.append(indexIterator.getIndex(v.getCode()).getIndex());
        }
        return newName.toString();
    }

    protected String equationNameMappingSpecIndex(HMVariable equation, HMSampledSpatialVariable av, Integer specValue) {
        if (!approximatedEquations.contains(equation)) {
            throw new RuntimeException("FDM converter doesn't contain " + equation.getCode());
        }
        StringBuilder newName = new StringBuilder(equation.getCode());
        newName.append(FDMStatic.APX_PREFIX);
        for (HMSampledSpatialVariable v : approximatedVariables) {
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

    // TODO рефакторинг
    private HMVariable getMappedVar(HMVariable v) {
        if (v == null) {
            throw new RuntimeException("mappedVariable can't be NULL");
        }
        if (notApproximated.contains(v) || v instanceof HMUnnamedConst) {
            return v;
        } else if (v instanceof HMSampledSpatialVariable) {
            return getVT().get(apxVarNameMapping((HMSampledSpatialVariable) v));
        } else if (v instanceof HMEquation) {
            return getVT().get(equationNameMapping(v));
        }
        throw new RuntimeException("Cant find mapped variable: " + v.getCode());
    }


}
