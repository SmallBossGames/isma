package ru.nstu.isma.intg.demo.app.ui.method;

import ru.nstu.isma.intg.demo.app.ui.DemoFormControllerAdapter;

import java.util.Locale;

/**
 * @author Mariya Nasyrova
 * @since 16.10.14
 */
public class DemoFormI18nChangedListener extends DemoFormControllerAdapter {

    private MethodFormController methodFormController;

    public DemoFormI18nChangedListener(MethodFormController methodFormController) {
        this.methodFormController = methodFormController;
    }

    @Override
    public void i18nChanged(Locale newLocale) {
        methodFormController.i18nChanged(newLocale);
    }
}
