package ru.nstu.isma.intg.demo.app.ui.problem;

import ru.nstu.isma.intg.demo.app.models.ProblemModel;
import ru.nstu.isma.intg.demo.app.ui.FormController;

import java.util.Locale;

/**
 * @author Mariya Nasyrova
 * @since 16.10.2014
 */
public class ProblemFormController extends FormController<ProblemFormControllerListener> {

    private ProblemForm form;

    public ProblemFormController(ProblemForm problemForm) {
        this.form = problemForm;
    }

    public void okButtonClicked() {
        ProblemModel problem = new ProblemModel(form.getProblemModel());

        problem.setIntervalStart(Double.valueOf(form.getIntervalStartTextField().getText()));
        problem.setIntervalEnd(Double.valueOf(form.getIntervalEndTextField().getText()));
	    problem.setStep(Double.valueOf(form.getStepTextField().getText()));

        problem.setReactionDiffusionParamJ((Integer) form.getParamJSpinner().getValue());
        problem.setReactionDiffusionParamK((Integer) form.getParamKSpinner().getValue());

        for (ProblemFormControllerListener listener : getListeners()) {
            listener.problemSaved(problem);
        }
        form.close();
    }

    public void cancelButtonClicked() {
        form.close();
    }

    public void i18nChanged(Locale newLocale) {
        for (ProblemFormControllerListener listener : getListeners()) {
            listener.i18nChanged(newLocale);
        }
    }

}
