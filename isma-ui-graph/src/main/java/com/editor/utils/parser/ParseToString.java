package com.editor.utils.parser;

import javax.swing.*;

public class ParseToString {
    private static final int FIRST_COLON = 0;
    private static final int SECOND_COLON = 1;

    public static String parsParamsToString(JTable inputTable){
       return "Parameters " + parsToString(inputTable);
    }

    public static String parsConditionsToString(JTable inputTable){
        return "Conditions " + parsToString(inputTable);
    }


    static String parsToString(JTable inputTable) {
        String result = "";
        for (int i = 0; i < inputTable.getRowCount(); i++) {
            result += inputTable.getValueAt(i, FIRST_COLON) + " = "
                    + inputTable.getValueAt(i, SECOND_COLON).toString() + " ";
        }
        return result;
    }
}
