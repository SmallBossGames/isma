package ru.nstu.isma.intg.demo.app.ui.toolbar;

import ru.nstu.isma.intg.demo.app.ui.DemoFormController;
import ru.nstu.isma.intg.demo.app.ui.helpers.ButtonController;

import javax.swing.*;

/**
 * @author Mariya Nasyrova
 * @since 16.10.14
 */
public class RunButtonController extends ButtonController {

    private DemoFormController demoFormController;

    public RunButtonController(DemoFormController demoFormController, JButton runButton) {
        super(runButton);
        this.demoFormController = demoFormController;
    }

    @Override
    public void buttonClicked() {
        demoFormController.simulationStarted();
    }

}
