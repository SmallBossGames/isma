package ru.nstu.isma.intg.demo.app.ui.method;

import ru.nstu.isma.intg.demo.app.models.MethodModel;
import ru.nstu.isma.intg.demo.app.ui.FormController;

import java.util.Locale;

/**
 * @author Mariya Nasyrova
 * @since 16.10.14
 */
public class MethodFormController extends FormController<MethodFormControllerListener> {

    private MethodForm methodForm;

    public MethodFormController(MethodForm methodForm) {
        this.methodForm = methodForm;
    }

    public void okButtonClicked() {
        MethodModel method = new MethodModel(methodForm.getMethodModel());
        method.setAccurate(methodForm.getAccurateCheckBox().isSelected());
        method.setAccuracy(Double.valueOf(methodForm.getAccuracyTextField().getText()));
        method.setStable(methodForm.getStableCheckBox().isSelected());
        method.setParallel(methodForm.getParallelCheckBox().isSelected());
        method.setIntgServerHost(methodForm.getHostTextField().getText());
        method.setIntgServerPort(Integer.valueOf(methodForm.getPortTextField().getText()));
        for (MethodFormControllerListener listener : getListeners()) {
            listener.methodSaved(method);
        }
        methodForm.close();
    }

    public void cancelButtonClicked() {
        methodForm.close();
    }

    public void i18nChanged(Locale newLocale) {
        for (MethodFormControllerListener listener : getListeners()) {
            listener.i18nChanged(newLocale);
        }
    }

    public void accurateCheckBoxChanged(boolean accurate) {
        methodForm.getAccuracyLabel().setEnabled(accurate);
        methodForm.getAccuracyTextField().setEnabled(accurate);
    }
}
