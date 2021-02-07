package com.editor.utils.generator;

import ru.nstu.isma.core.hsm.exp.*;
import ru.nstu.isma.core.hsm.service.Poliz2InfixConverter;
import ru.nstu.isma.core.hsm.var.HMDerivativeEquation;
import ru.nstu.isma.core.hsm.var.HMUnnamedConst;
import ru.nstu.isma.core.hsm.var.HMVariable;
import ru.nstu.isma.core.hsm.var.HMVariableTable;

import java.util.ArrayList;

public class ConvertVariables {

    ArrayList<ODE> odeList = new ArrayList<ODE>();

    HMVariableTable hmVariableTable;

    int index = 0;

    public ConvertVariables(HMVariableTable hmVariableTable) {
        this.hmVariableTable = hmVariableTable;
    }

    public void setODE01() {
        for (String vk : hmVariableTable.keySet()) {
            HMVariable vv = hmVariableTable.get(vk);
            if (vv instanceof HMDerivativeEquation) {
                ODE ode = new ODE();
                // отображение имен
                ode.setHsmCode(vv.getCode());
                ode.setGenCode(getNextName());
                odeList.add(ode);
            }
        }
    }

    public ArrayList<ODE> getODEList(){
        return odeList;
    }

    public void setODE02() {
        for (String vk : hmVariableTable.keySet()) {
            HMVariable vv = hmVariableTable.get(vk);
            if (vv instanceof HMDerivativeEquation) {
                ODE ode = getODE(vv.getCode());

                HMDerivativeEquation der = (HMDerivativeEquation) vv;
                ode.setInitial(der.getInitial().getValue().toString());

                StringBuilder rp = new StringBuilder();
                parseRP(der.getRightPart(), rp, true);
                ode.setRightPart(rp.toString());

                //odeList.add(dae);
            }
        }
    }


    public StringBuilder parseRP(HMExpression exp, StringBuilder sb, boolean toInfix) {

        if (toInfix == true) {
            Poliz2InfixConverter converter = new Poliz2InfixConverter(exp);
            exp = converter.convert();
        }

        for (EXPToken t : exp.getTokens()) {
            if (t instanceof EXPOperator) {
                sb.append(t.toString());

            } else if (t instanceof EXPParenthesis) {
                switch (((EXPParenthesis) t).getType()) {
                    case CLOSE:
                        sb.append(")");
                        break;
                    case OPEN:
                        sb.append("(");
                        break;
                }
            } else if (t instanceof EXPFunctionOperand) {
                sb.append(((EXPFunctionOperand) t).getName());
                sb.append("(");
                int cnt = 0;
                for (HMExpression e : ((EXPFunctionOperand) t).getArgs()) {
                    if (cnt > 0) {
                        sb.append(",");
                    }
                    StringBuilder sbArg = new StringBuilder();
                    sb.append(e.toString(sbArg, true).toString());
                    cnt++;
                }
                sb.append(")");
            } else if (t instanceof EXPPDEOperand) {
                EXPPDEOperand e = (EXPPDEOperand) t;
                sb.append("D(");
                sb.append(e.getVariable().getCode());
                sb.append(", ");
                sb.append(e.getSampledFirstSpatialVar().getCode());
                sb.append(")");
            } else if (t instanceof EXPOperand) {
                HMVariable vv = ((EXPOperand) t).getVariable();
                if (vv instanceof HMUnnamedConst) {
                    sb.append(((HMUnnamedConst) vv).getValue());
                } else sb.append(getGenName(vv.getCode()));
            }
            sb.append(" ");
        }
        return sb;
    }

    private String getNextName() {
        String str = "y [" + index + "]";
        index++;
        return str;
    }

    private ODE getODE(String hsmCode) {
        for (ODE o : odeList) {
            if (o.getHsmCode().equals(hsmCode)) {
                return o;
            }
        }
        return null;
    }

    private String getGenName(String hsmName) {
        return getODE(hsmName).getGenCode();
    }
}
