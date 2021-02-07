package ru.nstu.isma.intg.demo.app.ui.method;

import ru.nstu.isma.intg.demo.app.ui.utils.I18nUtils;

import javax.swing.*;
import java.util.Locale;

/**
 * @author Mariya Nasyrova
 * @since 16.10.14
 */
public class MethodFormI18nChangedListener extends MethodFormControllerAdapter {

    private MethodForm methodForm;

    public MethodFormI18nChangedListener(MethodForm methodForm) {
        this.methodForm = methodForm;
    }

    @Override
    public void i18nChanged(Locale newLocale) {
        methodForm.getAccurateCheckBox().setText(I18nUtils.getMessage("method.isAccurate.checkBox"));
        methodForm.getAccuracyLabel().setText(I18nUtils.getMessage("method.accuracy.label"));
        methodForm.getStableCheckBox().setText(I18nUtils.getMessage("method.isStable.checkBox"));
        methodForm.getParallelCheckBox().setText(I18nUtils.getMessage("method.isParallel.checkBox"));
        methodForm.getOkButton().setText(I18nUtils.getMessage("ok.button"));
        methodForm.getCancelButton().setText(I18nUtils.getMessage("cancel.button"));
        methodForm.getIntgServerPanel().setBorder(BorderFactory.createTitledBorder(I18nUtils.getMessage("method.intgServer.title")));
        methodForm.getHostLabel().setText(I18nUtils.getMessage("method.intgServer.host.label"));
        methodForm.getPortLabel().setText(I18nUtils.getMessage("method.intgServer.port.label"));
    }

}
