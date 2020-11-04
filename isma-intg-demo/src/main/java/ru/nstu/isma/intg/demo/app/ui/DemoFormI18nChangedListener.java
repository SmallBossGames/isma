package ru.nstu.isma.intg.demo.app.ui;

import ru.nstu.isma.intg.demo.app.models.ProblemType;
import ru.nstu.isma.intg.demo.app.ui.utils.I18nUtils;
import ru.nstu.isma.intg.lib.IntgMethodType;

import java.util.Locale;

/**
 * @author Mariya Nasyrova
 * @since 16.10.14
 */
public class DemoFormI18nChangedListener extends DemoFormControllerAdapter {

    private DemoFormController demoFormController;
    private DemoForm demoForm;

    public DemoFormI18nChangedListener(DemoFormController demoFormController, DemoForm demoForm) {
        this.demoFormController = demoFormController;
        this.demoForm = demoForm;
    }

    @Override
    public void i18nChanged(Locale newLocale) {
        //
        demoForm.getProblemComboBox().insertItemAt(I18nUtils.getMessage("demo.problem.comboBox.first"), 0);
        if (demoForm.getProblemComboBox().getItemCount() > ProblemType.values().length+1) {
            demoForm.getProblemComboBox().removeItemAt(1);
        }
        demoForm.getProblemComboBox().setSelectedIndex(0);

        demoForm.getMethodComboBox().insertItemAt(I18nUtils.getMessage("demo.method.comboBox.first"), 0);
        if (demoForm.getMethodComboBox().getItemCount() > IntgMethodType.values().length+1) {
            demoForm.getMethodComboBox().removeItemAt(1);
        }
        demoForm.getMethodComboBox().setSelectedIndex(0);
    }

}
