package ru.nstu.isma.intg.demo.app.ui.problem;

import ru.nstu.isma.intg.demo.app.ui.utils.I18nUtils;

import java.util.Locale;

/**
 * @author Mariya Nasyrova
 * @since 16.10.14
 */
public class ProblemFormI18nChangedListener extends ProblemFormControllerAdapter {

    private ProblemFormController problemFormController;
    private ProblemForm problemForm;

    public ProblemFormI18nChangedListener(ProblemFormController problemFormController, ProblemForm problemForm) {
        this.problemFormController = problemFormController;
        this.problemForm = problemForm;
    }

    @Override
    public void i18nChanged(Locale newLocale) {
        problemForm.getIntervalLabel().setText(I18nUtils.getMessage("problem.interval.label"));
        problemForm.getStepLabel().setText(I18nUtils.getMessage("problem.step.label"));
        problemForm.getOkButton().setText(I18nUtils.getMessage("ok.button"));
        problemForm.getCancelButton().setText(I18nUtils.getMessage("cancel.button"));

        problemForm.getParamJLabel().setText(I18nUtils.getMessage("problem.paramJ.label"));
        problemForm.getParamKLabel().setText(I18nUtils.getMessage("problem.paramK.label"));
    }

}
