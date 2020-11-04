package com.editor.utils.values;

import com.mxgraph.model.mxGraphModel;

import java.io.Serializable;

/**
 * Created by sknictik on 16.04.15.
 */
public class MacroMxCellValue implements Serializable {

    private String macroDisplayName;
    private mxGraphModel model;

    public MacroMxCellValue(String name, mxGraphModel model) {
       this.macroDisplayName = name;
        this.model = model;
    }

    public String getMacroDisplayName() {
        return macroDisplayName;
    }

    public void setMacroDisplayName(String macroDisplayName) {
        this.macroDisplayName = macroDisplayName;
    }

    public mxGraphModel getModel() {
        return model;
    }

    public void setModel(mxGraphModel model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return macroDisplayName;
    }
}
