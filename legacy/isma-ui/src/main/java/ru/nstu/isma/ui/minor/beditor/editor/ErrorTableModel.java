package ru.nstu.isma.ui.minor.beditor.editor;

import error.IsmaErrorList;

import javax.swing.table.AbstractTableModel;

/**
 * Created by Bessonov Alex
 * Date: 04.01.14
 * Time: 22:50
 */
public class ErrorTableModel extends AbstractTableModel {
//    private String[] columnNames = {"Row", "Col", "Message"};

    private IsmaErrorList errors;

    public ErrorTableModel(IsmaErrorList errors) {
        this.errors = errors;
    }

    @Override
    public int getRowCount() {
        return errors.size();
    }
    @Override
    public int getColumnCount() {
        return 1;
    }
    @Override
    public Object getValueAt(int r, int c) {
        Object res = null;
        switch (c) {
            case 0:
//                res = errors.get(r).getRow();
                res = errors.get(r).toString();
                break;
//            case 1:
//                res = errors.get(r).getRow();
//                break;
//            case 2:
//                res = errors.get(r).getMsg();
//                break;
        }
        return res;
    }

//    @Override
//    public String getColumnName(int column) {
//        return columnNames[column];
//    }
}
