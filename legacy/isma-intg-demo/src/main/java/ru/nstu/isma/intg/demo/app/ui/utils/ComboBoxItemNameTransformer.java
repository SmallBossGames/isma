package ru.nstu.isma.intg.demo.app.ui.utils;

import javax.swing.*;
import java.awt.*;

public abstract class ComboBoxItemNameTransformer<E> extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        try {
            value = getTitleForItem((E) value);
        } catch (ClassCastException expected) {
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }

    protected abstract String getTitleForItem(E item);

}
