package ru.nstu.isma.intg.demo.app.ui.problem;

import ru.nstu.isma.intg.demo.app.ui.helpers.ButtonController;

import javax.swing.*;

/**
 * @author Mariya Nasyrova
 * @since 16.10.2014
 */
public class OkButtonController extends ButtonController {

    private ProblemFormController problemFormController;

    public OkButtonController(ProblemFormController problemFormController, JButton okButton) {
        super(okButton);
        this.problemFormController = problemFormController;
    }

    @Override
    public void buttonClicked() {
        problemFormController.okButtonClicked();
    }

}
