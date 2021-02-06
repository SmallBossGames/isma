package ru.nstu.isma.app.ui.windows.simulation;

import com.alee.laf.panel.WebPanel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import ru.nstu.isma.app.env.project.RunConfiguration;
import ru.nstu.isma.app.ui.common.rsyntaxtextarea.TitledBorderPanel;
import ru.nstu.isma.app.ui.windows.simulation.ConfigurationFieldVerifie.FieldType;
import ru.nstu.isma.app.util.Consts;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.lib.IntgMethodLibrary;
import ru.nstu.isma.ui.i18n.I18nUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.util.*;

import static ru.nstu.isma.app.ui.windows.simulation.ConfigurationFieldVerifie.FieldType.*;

/**
 * Created by Bessonov Alex
 * on 30.07.2016.
 */
public class ConfigurationFields extends WebPanel {

    JTextField configName;

    JTextField start;
    JTextField end;
    JTextField step;

    JComboBox method;
    JCheckBox accurate;
    JTextField accuracy;
    JCheckBox stable;
    JCheckBox parallel;

    JTextField intgServer;
    JTextField intgPort;

    ButtonGroup resultStorageRadioGroup;
    JRadioButton memoryStorageRadio;
    JRadioButton fileStorageRadio;

    JCheckBox simplifyCheck;
    JComboBox simplifyAlgorithmCombo;
    JTextField simplifyToleranceField;

    JCheckBox eventDetectionCheck;
    JTextField eventDetectionGammaField;
    JCheckBox eventDetectionStepBoundCheck;
    JTextField eventDetectionStepBoundLowField;

    public java.util.List<JComponent> fields = new LinkedList<>();

    public ConfigurationFields() {

        setMargin(0, 10, 0, 0);

        TitledBorderPanel content = new TitledBorderPanel(I18nUtils.getMessage("simulationForm.sim.parameters"), Consts.PANELS_ROUND);

        content.add(buildPanel());

        add(content);
    }

    private JComponent buildPanel() {
        initComponents();

        FormLayout layout = new FormLayout(
                "right:max(40dlu;pref), 3dlu, 70dlu, 7dlu, "
                        + "right:max(40dlu;pref), 3dlu, 70dlu",
                "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();

        builder.append(I18nUtils.getMessage("simulationForm.configName"), configName);
        builder.nextLine();

        builder.appendSeparator(I18nUtils.getMessage("simulationForm.parameters"));
        builder.nextLine();
        builder.append(I18nUtils.getMessage("simulationForm.start"), start);
        builder.append(I18nUtils.getMessage("simulationForm.end"), end);
        builder.nextLine();
        builder.append(I18nUtils.getMessage("simulationForm.step"), step);
        builder.nextLine();

        builder.appendSeparator(I18nUtils.getMessage("simulationForm.method"));

        builder.append(I18nUtils.getMessage("simulationForm.method"), method);
        builder.nextLine();
        builder.append(I18nUtils.getMessage("simulationForm.accurate"), accurate);
        builder.append(I18nUtils.getMessage("simulationForm.accuracy"), accuracy);
        builder.nextLine();
        builder.append(I18nUtils.getMessage("simulationForm.stable"), stable);
        builder.nextLine();
        builder.append(I18nUtils.getMessage("simulationForm.parallel"), parallel);
        builder.nextLine();
        builder.append(I18nUtils.getMessage("simulationForm.intgHost"), intgServer);
        builder.append(I18nUtils.getMessage("simulationForm.intgPort"), intgPort);
        builder.nextLine();

        builder.appendSeparator(I18nUtils.getMessage("simulationForm.storage"));
        builder.append(memoryStorageRadio);
        builder.append("", fileStorageRadio);
        builder.nextLine();

        resultStorageRadioGroup = new ButtonGroup();
        resultStorageRadioGroup.add(memoryStorageRadio);
        resultStorageRadioGroup.add(fileStorageRadio);

        builder.appendSeparator(I18nUtils.getMessage("simulationForm.intgResults"));
        builder.append(I18nUtils.getMessage("simulationForm.simplifyCheck"), simplifyCheck);
        builder.nextLine();
        builder.append(I18nUtils.getMessage("simulationForm.simplifyAlgorithmCombo"), simplifyAlgorithmCombo);
        builder.append(I18nUtils.getMessage("simulationForm.simplifyToleranceField"), simplifyToleranceField);
        builder.nextLine();

        builder.appendSeparator(I18nUtils.getMessage("simulationForm.eventDetection"));
        builder.append(I18nUtils.getMessage("simulationForm.eventDetectionCheck"), eventDetectionCheck);
        builder.append(I18nUtils.getMessage("simulationForm.eventDetectionGammaField"), eventDetectionGammaField);
        builder.nextLine();
        builder.append(I18nUtils.getMessage("simulationForm.eventDetectionStepBoundCheck"), eventDetectionStepBoundCheck);
        builder.append(I18nUtils.getMessage("simulationForm.eventDetectionStepBoundLowField"), eventDetectionStepBoundLowField);
        builder.nextLine();


        return builder.getPanel();
    }

    private void initComponents() {

        configName = getTextField("Unnamed 1", STRING, false);

        start = getTextField("0", NUMBER, false);

        end = getTextField("10", NUMBER, false);

        step = getTextField("0.1", NUMBER, false);

        java.util.List<String> intgMethodNames = IntgMethodLibrary.getIntgMethodNames();
        String[] methodNames = new String[intgMethodNames.size()];
        method = new JComboBox<>(intgMethodNames.toArray(methodNames));

        method.addActionListener(l -> {
            IntgMethod selectedMethod = IntgMethodLibrary.getIntgMethod((String) method.getSelectedItem());
            accurate.setSelected(false);
            accurate.setEnabled(selectedMethod.getAccuracyController() != null);
            stable.setSelected(false);
            stable.setEnabled(false);
        });


        accurate = new JCheckBox();
        accurate.setEnabled(false);
        accurate.addChangeListener(e -> {
            accuracy.setEnabled(accurate.isSelected());
            if (accurate.isSelected()) {
                IntgMethod selectedMethod = IntgMethodLibrary.getIntgMethod((String) method.getSelectedItem());
                stable.setEnabled(selectedMethod.getStabilityController() != null);
            } else {
                stable.setSelected(false);
                stable.setEnabled(false);
            }
        });
        accuracy = getTextField("0.1", NUMBER, false);
        accuracy.setEnabled(false);

        stable = new JCheckBox();
        stable.setEnabled(false);

        parallel = new JCheckBox();
        intgServer = getTextField("localhost");
        intgPort = getTextField("7890");

        memoryStorageRadio = new JRadioButton(I18nUtils.getMessage("simulationForm.memoryStorageLabel"), true);
        fileStorageRadio = new JRadioButton(I18nUtils.getMessage("simulationForm.fileStorageLabel"));

        simplifyCheck = new JCheckBox();
        simplifyCheck.addActionListener(e -> {
            boolean selected = simplifyCheck.isSelected();
            simplifyAlgorithmCombo.setEnabled(selected);
            simplifyToleranceField.setEnabled(selected);
            if (selected) {
                simplifyAlgorithmCombo.setSelectedIndex(0);
                simplifyToleranceField.setText("20");
            }
        });

        String[] simplifyAlgorithms = {
                "Radial-Distance",
                "Douglas-Peucker"
        };
        simplifyAlgorithmCombo = new JComboBox(simplifyAlgorithms);
        simplifyAlgorithmCombo.setEnabled(false);

        simplifyToleranceField = getTextField("20", NUMBER, false);
        simplifyToleranceField.setEnabled(false);

        eventDetectionCheck = new JCheckBox();
        eventDetectionCheck.addActionListener(e -> {
            boolean selected = eventDetectionCheck.isSelected();
            eventDetectionGammaField.setEnabled(selected);
            eventDetectionStepBoundCheck.setEnabled(selected);
            eventDetectionStepBoundLowField.setEnabled(selected);
        });

        eventDetectionGammaField = getTextField("0.8", NUMBER, false);
        ;
        eventDetectionGammaField.setEnabled(false);

        eventDetectionStepBoundCheck = new JCheckBox();
        eventDetectionStepBoundCheck.setSelected(true);
        eventDetectionStepBoundCheck.setEnabled(false);

        eventDetectionStepBoundLowField = getTextField("0.001", NUMBER, false);
        ;
        eventDetectionStepBoundLowField.setEnabled(false);
    }


    private JTextField getTextField(String value) {
        JTextField field = new JTextField(value);
        fields.add(field);
        return field;
    }

    private JTextField getTextField(String value, ConfigurationFieldVerifie.FieldType fieldType, boolean canBeEmpty) {
        JTextField field = new JTextField(value);
        field.setInputVerifier(new ConfigurationFieldVerifie(fieldType, canBeEmpty));
        fields.add(field);
        return field;
    }

    public RunConfiguration populateRunConfig() {
        RunConfiguration conf = new RunConfiguration();
        conf.setName(configName.getText());

        conf.setStartTime(Double.parseDouble(start.getText()));
        conf.setStep(Double.parseDouble(step.getText()));
        conf.setEndTime(Double.parseDouble(end.getText()));

        conf.setMethod(method.getSelectedItem().toString());
        conf.setAccurate(accurate.isSelected());
        conf.setAccuracy(Double.parseDouble(accuracy.getText()));
        conf.setStable(stable.isSelected());
        conf.setParallel(parallel.isSelected());

        conf.setIntgServer(intgServer.getText());
        conf.setIntgPort(Integer.parseInt(intgPort.getText()));

        if (memoryStorageRadio.isSelected())
            conf.setResultStorage(RunConfiguration.ResultStorageType.MEMORY);

        if (fileStorageRadio.isSelected())
            conf.setResultStorage(RunConfiguration.ResultStorageType.FILE);

        conf.setSimplifyCheck(simplifyCheck.isSelected());
        conf.setSimplifyAlgorithmCombo(simplifyAlgorithmCombo.getSelectedItem().toString());
        conf.setSimplifyToleranceField(Double.parseDouble(simplifyToleranceField.getText()));

        conf.setEventDetectionCheck(eventDetectionCheck.isSelected());
        conf.setEventDetectionGammaField(Double.parseDouble(eventDetectionGammaField.getText()));

        conf.setEventDetectionStepBoundCheck(eventDetectionStepBoundCheck.isSelected());
        conf.setEventDetectionStepBoundLowField(Double.parseDouble(eventDetectionStepBoundLowField.getText()));

        return conf;
    }

    public boolean validateFields() {
        for (JComponent c : fields)
            if (!c.isValid())
                return false;
        return true;
    }
}
