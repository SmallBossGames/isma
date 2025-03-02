package ru.nstu.isma.lisma.analysis.parser;

import ru.nstu.isma.core.hsm.exp.EXPParenthesis;
import ru.nstu.isma.core.hsm.exp.HMExpression;
import ru.nstu.isma.core.hsm.models.IsmaErrorList;
import ru.nstu.isma.core.hsm.models.IsmaSemanticError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 08.12.2015.
 */
public class ParserContext {
    private final Map<String, Integer> indexMap = new HashMap<>();

    private final IsmaErrorList errors;

    private Double defaultConstInitialValue = 0.0;

    public ParserContext(IsmaErrorList errors) {
        this.errors = errors;
    }

    public void clearIndexMap() {
        indexMap.clear();
    }

    public void setIdxValue(String idxName, Integer value) {
        indexMap.put(idxName, value);
    }

    private final Map<String, HMExpression> macro = new HashMap<>();

    public Integer getIdxValue(String idxName) {
        if (!indexMap.containsKey(idxName)) {
            var err = new IsmaSemanticError("The index " + idxName + " does not have the for part.");
            errors.push(err);
        }
        return indexMap.get(idxName);
    }

    public void removeIdxValue(String idxName) {
        indexMap.remove(idxName);
    }

    public Double getDefaultConstInitialValue() {
        return defaultConstInitialValue;
    }

    public void setDefaultConstInitialValue(Double defaultConstInitialValue) {
        this.defaultConstInitialValue = defaultConstInitialValue;
    }

    public IsmaErrorList errors() {
        return errors;
    }

    public void addMacro(String key, HMExpression expression) {
        HMExpression exp = new HMExpression();
        exp.add(new EXPParenthesis(EXPParenthesis.Type.OPEN));
        expression.getTokens().forEach(t -> exp.add(t));
        exp.add(new EXPParenthesis(EXPParenthesis.Type.CLOSE));
        macro.put(key, exp);
    }

    public HMExpression getMacro(String key) {
        return macro.get(key);
    }

    public boolean containMacro(String key) {
        return macro.containsKey(key);
    }

}
