package ru.nstu.isma.intg.demo.app.ui.problem;

import ru.nstu.isma.intg.demo.app.ui.DemoFormControllerAdapter;

import java.util.Locale;

/**
 * @author Mariya Nasyrova
 * @since 16.10.14
 */
public class DemoFormI18nChangedListener extends DemoFormControllerAdapter {

    private ProblemFormController problemFormController;

    public DemoFormI18nChangedListener(ProblemFormController problemFormController) {
        this.problemFormController = problemFormController;
    }

    @Override
    public void i18nChanged(Locale newLocale) {
        problemFormController.i18nChanged(newLocale);
    }
}
