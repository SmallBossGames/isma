package common;

import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.core.hsm.var.pde.HMPartialDerivativeEquation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bessonov Alex
 * on 04.01.2015.
 */
public class IndexMapper implements IndexProvider, Serializable {
    static final String odeArrayName = "y";
    // HSM equation name -> ODE index
    private final Map<String, Integer> indexMap = new HashMap<>();

    private final Map<Integer ,String> codeMap = new HashMap<>();

    private final HSM hsm;

    public IndexMapper(HSM hsm) {
        this.hsm = hsm;
        prepeare();
    }

    private void prepeare() {
        hsm.getVariableTable().getOdes()
                .forEach(de -> {
                    if (de instanceof HMPartialDerivativeEquation) {
                        throw new RuntimeException("not supported"); // todo изменить на списщк IsmaErrorList
                    }
                    addCode(de.getCode());
                });
        hsm.getVariableTable().getAlgs()
                .forEach(a -> addCode(a.getCode()));
        hsm.getLinearSystem().getVars().values()
                .forEach(v-> addCode(v.getCode()));
    }

    private void addCode(String code) {
        if (!indexMap.containsKey(code)) {
            int idx =  indexMap.size();
            indexMap.put(code, idx);
            codeMap.put(idx, code);
        }
    }

    @Override
    public String getDifferentialArrayCode(String code) {
        return odeArrayName + "[" + indexMap.get(code) + "]";
    }

    @Override
    public String getAlgebraicArrayCode(String code) {
        return "a.Y(" + indexMap.get(code) + ")";
    }

    @Override
    public String getAlgebraicArrayCodeForDifferentialEquation(String aeCode) {
        return getAlgebraicArrayCode(aeCode);
    }

    public HSM getHsm() {
        return hsm;
    }

    public Map<String, Integer> getIndexMap() {
        return indexMap;
    }

    public Integer index(String code) {
        return indexMap.get(code);
    }

    public String code(Integer idx) {
        return codeMap.get(idx);
    }
}
