package ru.nstu.isma.intg.demo.app.ui.toolbar;

import ru.nstu.isma.intg.demo.app.ui.DemoFormController;
import ru.nstu.isma.intg.demo.app.ui.helpers.ButtonController;
import ru.nstu.isma.intg.demo.app.ui.utils.I18nUtils;

import javax.swing.*;

/**
 * @author Mariya Nasyrova
 * @since 16.10.14
 */
public class RuButtonController extends ButtonController {

    private DemoFormController demoFormController;

    public RuButtonController(DemoFormController demoFormController, JButton okButton) {
        super(okButton);
        this.demoFormController = demoFormController;
    }

    @Override
    public void buttonClicked() {
        I18nUtils.setLocale(I18nUtils.RU);
        demoFormController.i18nChanged(I18nUtils.RU);
    }

}
