package ru.nstu.isma.intg.demo.app.ui.problem;

import ru.nstu.isma.intg.demo.app.models.ProblemModel;

import java.util.Locale;

/**
 * @author Mariya Nasyrova
 * @since 16.10.14
 */
public interface ProblemFormControllerListener {

    void problemSaved(ProblemModel problem);

    void i18nChanged(Locale newLocale);

}
