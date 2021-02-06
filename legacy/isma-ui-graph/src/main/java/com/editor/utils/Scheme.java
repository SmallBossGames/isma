package com.editor.utils;

import javax.swing.*;

/**
 * Created by Sknictik on 07.06.14.
 */
public class Scheme {

    private String name;
    private Object[][] params;
    private Object[][] conds;
    private ImageIcon schemeImg;

    public Scheme(String name, Object[][] params, Object[][] conds, ImageIcon schemeImg) {
        this.name = name;
        this.params = params;
        this.conds = conds;
        this.schemeImg = schemeImg;
    }

    public String getName() {
        return name;
    }

    public Object[][] getParams() {
        return params;
    }

    public Object[][] getConds() {
        return conds;
    }

    public ImageIcon getSchemeImg() {
        return schemeImg;
    }

}
