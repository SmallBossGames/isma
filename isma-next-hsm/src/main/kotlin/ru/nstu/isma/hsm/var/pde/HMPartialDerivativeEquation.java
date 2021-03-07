package ru.nstu.isma.hsm.var.pde;

import ru.nstu.isma.hsm.var.HMConst;
import ru.nstu.isma.hsm.var.HMDerivativeEquation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Bessonov Alex.
 * Date: 04.12.13 Time: 0:25
 */
public class HMPartialDerivativeEquation extends HMDerivativeEquation implements Serializable {

    private final List<HMBoundaryCondition> boundaries = new LinkedList<HMBoundaryCondition>();

    private final Map<HMSpatialVariable, Integer> variables = new HashMap<>();

    private final Map<HMSpatialVariable, HMConst> params = new HashMap<>();

    public HMPartialDerivativeEquation(String code) {
        super(code);
    }

    public HMPartialDerivativeEquation(HMDerivativeEquation eq) {
        super(eq.getCode());
        setInitial(eq.getInitial());
        setRightPart(eq.getRightPart());
    }

    public void addBound(HMBoundaryCondition bb) {
        boundaries.add(bb);
    }

    public void addSpatialVar(HMSpatialVariable vv, Integer order) {
        variables.put(vv, order);
    }

    public List<HMBoundaryCondition> getBoundaries() {
        return boundaries;
    }

    public List<HMSpatialVariable> getVariables() {
        return variables.keySet().stream().collect(Collectors.toList());
    }

    public Integer getVariableOrder(HMSpatialVariable var) {return variables.get(var);}

    public boolean isContains(HMSpatialVariable v) {
        for (HMSpatialVariable vvv : getVariables()) {
            if (vvv.getCode().equals(v.getCode())) {
                return true;
            }
        }
        return false;
    }

    public Map<HMSpatialVariable, HMConst> getParams() {
        return params;
    }

    public void addParam(HMSpatialVariable sv, HMConst c) {
        params.put(sv, c);
    }

    public void getParam(HMSpatialVariable sv) {
        params.get(sv);
    }

    public HMBoundaryCondition getBound(HMBoundaryCondition.SideType sideType, HMSpatialVariable apx) {
        List<HMBoundaryCondition> left = new LinkedList<HMBoundaryCondition>();
        for (HMBoundaryCondition b : boundaries) {
            if (b.getSide() == sideType || b.getSide() == HMBoundaryCondition.SideType.BOTH) {
                if (b.getSpatialVar().getCode().equals(apx.getCode())) {
                    left.add(b);
                }
            }
        }
        if (left.size() != 1) {
            throw new RuntimeException("Incorrect count of boundaries for " + sideType.name() + " side(s) of " + getCode() + ": " + left.size());
        }
        return left.get(0);
    }
}
