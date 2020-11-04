package com.editor.utils.parser;

public class ValuesForParametersAndConditions {

    String name;
    double value;

    public ValuesForParametersAndConditions(String name, double value){
        this.name=name;
        this.value=value;
    }

    public String getName() {
        return name;
    }
    public double getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
