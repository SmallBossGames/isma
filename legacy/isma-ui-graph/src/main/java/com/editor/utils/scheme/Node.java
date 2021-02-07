package com.editor.utils.scheme;

import com.editor.utils.parser.ParseToValues;
import com.editor.utils.parser.ValuesForParametersAndConditions;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.swing.GraphEditor;

import java.util.*;

public class Node {

    private List<mxCell> nodeElems = new ArrayList<mxCell>();
    private boolean withGenerator;

    public Node(mxCell... elems) {
        Collections.addAll(nodeElems, elems);
    }

    public Node(boolean withGenerator, mxCell... elems) {
        Collections.addAll(nodeElems, elems);
        this.withGenerator = withGenerator;
    }

    /*public void printAllElements() {
        for (mxCell elem : nodeElems) {
            System.out.println("ID: " + elem.getId()
                    + " Style: " + elem.getStyle() + " Value: " + elem.getValue());
        }
    }*/

    public List<mxCell> getNodeElems() {
        return nodeElems;
    }

    public void setIsWithGenerator(boolean value) {
        withGenerator = value;
    }

    public boolean isWithGenerator() {
        return withGenerator;
    }

    public void addElement(mxICell elem) {
        nodeElems.add((mxCell) elem);
    }

    public void setGeneratorTooltip(String tooltip) {
        if (withGenerator) {
            for (mxCell elem : nodeElems) {
                if (elem.getStyle().equals("ismaGenerator")) {
                    GraphEditor.addGeneratorLabel(elem.hashCode(), tooltip);
                    return;
                }
            }
        }
    }

    /*public void printEDS() {
        for (mxCell elem : nodeElems) {
            if (elem.getStyle().equals("ismaGenerator")) {
                ParseToValues parser = new ParseToValues(elem.getValue().toString());
                HashMap<Integer, ValuesForParametersAndConditions> conds = parser.getHashMapForValuesConditions();
                double edsValue = Double.MAX_VALUE;
                for (Map.Entry<Integer, ValuesForParametersAndConditions> cond : conds.entrySet()) {
                    if (cond.getValue().getName().equals("E_q")) {
                        edsValue = cond.getValue().getValue();
                    }
                }
                System.out.println("Generator " + elem.getId() + " eds value is " +
                        edsValue);
            }
        }
    }*/

    /*public void printLoadConductivity() {
        for (mxCell elem : nodeElems) {
            if (elem.getStyle().equals("ismaCommonLoad")) {
                ParseToValues parser = new ParseToValues((String) elem.getValue());
                HashMap<Integer, ValuesForParametersAndConditions> conds = parser.getHashMapForValuesConditions();
                double g = Double.MAX_VALUE;
                double b = Double.MAX_VALUE;
                for (Map.Entry<Integer, ValuesForParametersAndConditions> cond : conds.entrySet()) {
                    if (cond.getValue().getName().equals("g")) {
                        g = cond.getValue().getValue();
                    } else if (cond.getValue().getName().equals("b")) {
                        b = cond.getValue().getValue();
                    }
                }
                System.out.println("Load " + elem.getId() + " conductivity value is " + g + " + j" + b);
            }
        }
    }*/

    public double getGeneratorValue(String valueName) {
        if (withGenerator) {
            for (mxCell elem : nodeElems) {
                if (elem.getStyle().equals("ismaGenerator")) {
                    ParseToValues parser = new ParseToValues(elem.getValue().toString());
                    HashMap<Integer, ValuesForParametersAndConditions> conds
                            = parser.getHashMapForValuesConditions();
                    double value = Double.MAX_VALUE;
                    for (Map.Entry<Integer, ValuesForParametersAndConditions> cond : conds.entrySet()) {
                        if (cond.getValue().getName().equals(valueName)) {
                            value = cond.getValue().getValue();
                            break;
                        }
                    }
                    return value;
                }
            }
        }
        return Double.MAX_VALUE;
    }
}
