package ru.nstu.isma.intg.demo.app.ui.method;

import ru.nstu.isma.intg.demo.app.ui.helpers.ButtonController;

import javax.swing.*;

/**
 * @author Mariya Nasyrova
 * @since 16.10.2014
 */
public class CancelButtonController extends ButtonController {

    private MethodFormController methodFormController;

    public CancelButtonController(MethodFormController methodFormController, JButton okButton) {
        super(okButton);
        this.methodFormController = methodFormController;
    }

    @Override
    public void buttonClicked() {
        methodFormController.cancelButtonClicked();
    }
}