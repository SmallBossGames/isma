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
public class ProblemComboBoxController extends DemoFormControllerAdapter implements ActionListener {

    private DemoFormController demoFormController;
    private JComboBox problemComboBox;

    public ProblemComboBoxController(DemoFormController demoFormController, JComboBox problemComboBox) {
        this.demoFormController = demoFormController;
        this.problemComboBox = problemComboBox;
        problemComboBox.addActionListener(this);
    }

    @Override
    public void formOpened(ProblemType[] problemTypes, IntgMethodType[] methodTypes) {
        problemComboBox.setModel(new DefaultComboBoxModel(problemTypes));
        problemComboBox.setRenderer(new ComboBoxItemNameTransformer<ProblemType>() {
            @Override
            protected String getTitleForItem(ProblemType item) {
                return I18nUtils.getProblemTypeName(item);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (problemComboBox.getSelectedIndex() == 0) {
            return;
        }
        ProblemType selectedType = (ProblemType) problemComboBox.getSelectedItem();
        demoFormController.problemSelected(selectedType);
    }

}
