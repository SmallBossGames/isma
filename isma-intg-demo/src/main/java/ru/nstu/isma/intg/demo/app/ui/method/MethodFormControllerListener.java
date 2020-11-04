package ru.nstu.isma.intg.demo.app.ui.method;

import ru.nstu.isma.intg.demo.app.models.MethodModel;

import java.util.Locale;

/**
 * @author Mariya Nasyrova
 * @since 16.10.14
 */
public interface MethodFormControllerListener {

    void methodSaved(MethodModel method);

    void i18nChanged(Locale newLocale);

}
