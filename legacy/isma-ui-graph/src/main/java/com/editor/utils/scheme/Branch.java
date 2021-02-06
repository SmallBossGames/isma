package com.editor.utils.scheme;

import com.editor.utils.parser.ParseToValues;
import com.editor.utils.parser.ValuesForParametersAndConditions;
import com.mxgraph.model.mxCell;

import java.util.HashMap;
import java.util.Map;

public class Branch {

    private mxCell branchRoot;

    public Branch(mxCell branchRoot) {
        this.branchRoot = branchRoot;
    }

    public void printConductivity() {
        ParseToValues parser = new ParseToValues(branchRoot.getValue().toString());
        HashMap<Integer, ValuesForParametersAndConditions> conds = parser.getHashMapForValuesConditions();
        double g = Double.MAX_VALUE;
        double b = Double.MAX_VALUE;
        for (Map.Entry<Integer,ValuesForParametersAndConditions> cond : conds.entrySet()){
            if (cond.getValue().getName().equals("g")) {
                g = cond.getValue().getValue();
            }
            else if (cond.getValue().getName().equals("b")) {
                b = cond.getValue().getValue();
            }
        }
        System.out.println(branchRoot.getStyle() + " id:" + branchRoot.getId() + " conductivity value is "
                + g + " + j" + b);
    }

}
