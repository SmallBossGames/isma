package ru.nstu.isma.intg.demo.app.ui.toolbar;

import ru.nstu.isma.intg.demo.app.models.ProblemType;
import ru.nstu.isma.intg.demo.app.ui.DemoFormController;
import ru.nstu.isma.intg.demo.app.ui.DemoFormControllerAdapter;
import ru.nstu.isma.intg.demo.app.ui.utils.ComboBoxItemNameTransformer;
import ru.nstu.isma.intg.demo.app.ui.utils.I18nUtils;
import ru.nstu.isma.intg.lib.IntgMethodType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mariya Nasyrova
 * @since 13.10.14
 */
public class MethodComboBoxController extends DemoFormControllerAdapter implements ActionListener {

    private DemoFormController demoFormController;
    private JComboBox methodComboBox;

    public MethodComboBoxController(DemoFormController demoFormController, JComboBox methodComboBox) {
        this.demoFormController = demoFormController;
        this.methodComboBox = methodComboBox;
        methodComboBox.addActionListener(this);
    }

    @Override
    public void formOpened(ProblemType[] problemTypes, IntgMethodType[] methodTypes) {
        methodComboBox.setModel(new DefaultComboBoxModel(methodTypes));
        methodComboBox.setRenderer(new ComboBoxItemNameTransformer<IntgMethodType>() {
            @Override
            protected String getTitleForItem(IntgMethodType item) {
                return I18nUtils.getMethodTypeName(item);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (methodComboBox.getSelectedIndex() == 0) {
            return;
        }
        IntgMethodType selected = (IntgMethodType) methodComboBox.getModel().getSelectedItem();
        demoFormController.methodSelected(selected);
    }
}
