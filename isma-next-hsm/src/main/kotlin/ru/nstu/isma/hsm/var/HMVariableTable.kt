package ru.nstu.isma.hsm.var;

import ru.nstu.isma.hsm.exp.HMExpression;
import ru.nstu.isma.hsm.var.pde.HMPartialDerivativeEquation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:33
 */
public class HMVariableTable implements Serializable {
    protected HMVariableTable parent;

    protected Map<String, HMVariable> variables = new HashMap<>();

    protected Map<String, HMExpression> setters = new HashMap<>();

    public HMVariable get(String key) {
        if (!variables.containsKey(key) && parent != null) {
            return parent.get(key);
        }
        return variables.get(key);
    }

    public boolean contain(String key) {
        return variables.containsKey(key);
    }

    public boolean empty() {
        return variables.size() == 0;
    }

    public void clear() {
        variables.clear();
    }

    public boolean add(HMVariable item) {
        if (contain(item.getCode())) {
            return false;
        }
        variables.put(item.getCode(), item);
        return true;
    }

    public boolean remove(String key) {
        if (!contain(key)) {
            return false;
        }
        variables.remove(key);
        return true;
    }

    public Set<String> keySet() {
        return variables.keySet();
    }

    public List<HMVariable> variables() {
        return variables.values().stream().collect(Collectors.toList());
    }

    public List<HMDerivativeEquation> getOdes() {
        return variables.values().stream()
                .filter(t -> t instanceof HMDerivativeEquation)
                .map(t -> (HMDerivativeEquation) t)
                .collect(Collectors.toList());
    }

    public List<HMAlgebraicEquation> getAlgs() {
        return variables.values().stream()
                .filter(t -> t instanceof HMAlgebraicEquation)
                .map(t -> (HMAlgebraicEquation) t)
                .collect(Collectors.toList());
    }

    public List<HMPartialDerivativeEquation> getPdes() {
        return variables.values().stream()
                .filter(t -> t instanceof HMPartialDerivativeEquation)
                .map(t -> (HMPartialDerivativeEquation) t)
                .collect(Collectors.toList());
    }


    public HMVariableTable getParent() {
        return parent;
    }

    public void setParent(HMVariableTable parent) {
        this.parent = parent;
    }

    public Map<String, HMExpression> getSetters() {
        return setters;
    }

    public void setSetters(Map<String, HMExpression> setters) {
        this.setters = setters;
    }
}
