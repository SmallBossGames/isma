package ru.nstu.isma.core.sim.fdm;

import ru.nstu.isma.core.hsm.var.HMConst;
import ru.nstu.isma.core.hsm.var.HMEquation;
import ru.nstu.isma.core.hsm.var.HMVariable;
import ru.nstu.isma.core.hsm.var.HMVariableTable;
import ru.nstu.isma.core.hsm.exp.EXPOperand;
import ru.nstu.isma.core.hsm.exp.EXPToken;
import ru.nstu.isma.core.hsm.exp.HMExpression;
import ru.nstu.isma.core.hsm.var.pde.HMPartialDerivativeEquation;
import ru.nstu.isma.core.hsm.var.pde.HMSampledSpatialVariable;
import ru.nstu.isma.core.hsm.var.pde.HMSpatialVariable;

import java.util.*;

/**
 * Created by Bessonov Alex
 * Date: 12.12.13
 * Time: 0:49
 */
public class FDMContext {
    private LinkedList<HMSampledSpatialVariable> variables = new LinkedList<HMSampledSpatialVariable>();

    private HashSet<HMEquation> equations = new HashSet<HMEquation>();

    // маппинг (отображение) имен - проекция имени уравнение и набора индексов для аппроксимируемых переменных на новое имя
    // например уравнение F(x,y,t) и xi=5; hi = 35;  yi=23; ->  F___apx_5_0_23;
    protected String variableNameMapping(HMVariable equation, Map<String, FDMIndexedApxVar> indexes) {
        if (!contains(equation.getCode())) {
            throw new RuntimeException("FDMContext doesn't contain " + equation.getCode());
        }
        StringBuilder newName = new StringBuilder(equation.getCode());
        newName.append(FDMStatic.APX_PREFIX);
        for (HMSampledSpatialVariable v : variables) {
            newName.append("_");
            if (indexes.containsKey(v.getCode()) && isEqContains(equation, v)) {
                newName.append(indexes.get(v.getCode()).getIndex());
            } else if (!indexes.containsKey(v.getCode())) {
                newName.append("E");
            } else {
                newName.append("0");
            }
        }
        return newName.toString();
    }

    protected String variableNameMapping(HMVariable equation, List<FDMIndexedApxVar> indexes) {
        Map<String, FDMIndexedApxVar> mVars = new HashMap<String, FDMIndexedApxVar>();
        for (FDMIndexedApxVar vv : indexes) {
            mVars.put(vv.getCode(), vv);
        }
        return variableNameMapping(equation, mVars);
    }

    // Добавление новой аппроксимируеммой переменной
    // примеч.: от порядка вставки зависит порядок индексации при маппинге имен
    public boolean addVariable(HMSampledSpatialVariable apx) {
        variables.add(apx);
        return true;
    }

    // Добавление нового уравнения контекста
    public boolean addEquation(HMEquation eq) {
        if (containsEquation(eq.getCode())) {
            return false;
        }
        equations.add(eq);
        return true;
    }

    public LinkedList<HMSampledSpatialVariable> getVariables() {
        return variables;
    }

    public HashSet<HMEquation> getEquations() {
        return equations;
    }

    public boolean containsEquation(String eqCode) {
        for (HMEquation e : equations) {
            if(e.getCode().equals(eqCode)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(String code) {
        return containsApxVar(code) || containsEquation(code);
    }

    public boolean containsApxVar(String vCode) {
        for (HMSampledSpatialVariable v : variables) {
            if(v.getCode().equals(vCode)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEqContains(HMVariable eq, HMSpatialVariable v) {
        if (eq instanceof HMPartialDerivativeEquation) {
            return ((HMPartialDerivativeEquation) eq).isContains(v);
        }
        return false;
    }

    // Парсинг таблицы переменных и наполнение контекста
    public static FDMContext prepare(HMVariableTable variables) {
        FDMContext context = new FDMContext();
        Set<String> keys = variables.keySet();

        for (String s : keys) {
            HMVariable vv = variables.get(s);
            if (vv instanceof HMPartialDerivativeEquation) {
                context.addEquation((HMPartialDerivativeEquation) vv);
            } else if (vv instanceof HMSampledSpatialVariable) {
                context.addVariable((HMSampledSpatialVariable) vv);
            }
        }

        boolean contextReady = false;
        boolean eqNeedApx;

        while (!contextReady) {
            // пробегаем все уравнения в которых есть другие аппроксимируемые уравнения или переменные
            contextReady = true;
            for (String s : keys) {
                HMVariable var = variables.get(s);
                if (var instanceof HMEquation
                        && !(var instanceof HMConst)
                        && !context.containsEquation(var.getCode())) {
                    // проверяем правую часть на аппроксимируемые элементы
                    eqNeedApx = false;
                    HMExpression right = ((HMEquation) var).getRightPart();
                    for (EXPToken t : right.getTokens()) {
                        // проверяем только уравнения и только если ранее не было найдено (eqNeedApx)
                        if (t instanceof EXPOperand && !eqNeedApx) {
                            String code = ((EXPOperand) t).getVariable().getCode();
                            if (context.containsEquation(code) || context.containsApxVar(code)) {
                                contextReady = false;
                                context.addEquation((HMEquation) var);
                                eqNeedApx = true;
                            }
                        }
                    }
                }

            }
        }
        return context;
    }

}
