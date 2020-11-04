package ru.nstu.isma.app.ui.windows.simulation;

import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.label.WebLabel;
import org.springframework.util.Assert;
import ru.nstu.isma.ui.i18n.I18nUtils;

import javax.swing.*;

/**
 * Created by Bessonov Alex
 * on 30.07.2016.
 */
public class ConfigurationFieldVerifie extends InputVerifier {
    private FieldType fieldType;

    private boolean canBeEmpty;


    public ConfigurationFieldVerifie(FieldType fieldType, boolean canBeEmpty) {
        Assert.notNull(fieldType);
        this.fieldType = fieldType;
        this.canBeEmpty = canBeEmpty;
    }

    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText();

        if (!canBeEmpty && (text == null || text.length() == 0)) {
            popup(I18nUtils.getMessage("simulationForm.validation.empty"), input);
            return false;
        }

        try {
            if (fieldType == FieldType.NUMBER) {
                Double value = new Double(text);
            }
            return true;
        } catch (NumberFormatException e) {
            popup(I18nUtils.getMessage("simulationForm.validation.number"), input);
            return false;
        }
    }

    public void popup(String message, JComponent component) {

        final WebPopOver popOver = new WebPopOver(component);
        popOver.setCloseOnFocusLoss(true);
        popOver.setMargin(10);
        popOver.setLayout(new VerticalFlowLayout());
        popOver.add(new WebLabel(message));
        popOver.add(new WebLabel(I18nUtils.getMessage("simulationForm.validation.retry")));
        popOver.show(component);

    }

    public enum FieldType {
        STRING,
        NUMBER
    }

}
