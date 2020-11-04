package ru.nstu.isma.intg.demo.app.ui.method;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author Maria Nasyrova
 * @since 03.12.2014
 */
public class AccurateCheckBoxController {

    private MethodFormController methodFormController;
    private JCheckBox accurateCheckBox;

    public AccurateCheckBoxController(MethodFormController methodFormController, JCheckBox accurateCheckBox) {
        this.methodFormController = methodFormController;
        this.accurateCheckBox = accurateCheckBox;
        accurateCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                AccurateCheckBoxController.this.methodFormController.accurateCheckBoxChanged(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
    }
}
