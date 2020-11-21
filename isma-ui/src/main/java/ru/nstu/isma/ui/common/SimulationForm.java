package ru.nstu.isma.ui.common;


import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import ru.nstu.isma.core.sim.controller.SimulationCoreController;
import ru.nstu.isma.core.sim.fdm.FDMNewConverter;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials;
import ru.nstu.isma.intg.api.methods.AccuracyIntgController;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.lib.IntgMethodLibrary;
import ru.nstu.isma.ui.i18n.I18ChangedListener;
import ru.nstu.isma.ui.i18n.I18nUtils;
import ru.nstu.isma.ui.minor.beditor.editor.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by Bessonov Alex
 * on 10.01.15.
 */
public class SimulationForm<W extends OutputAware> extends JFrame implements I18ChangedListener {

    static final String RESULT_FILE_NAME = "result.csv";

    private W mw;

    private AppData data;

    private JTextField start;
    private JTextField end;
    private JTextField step;

    private JComboBox<String> method;
    private JCheckBox accurate;
    private JTextField accuracy;
    private JCheckBox stable;
    private JCheckBox parallel;

    private JTextField intgServer;
    private JTextField intgPort;

    private JProgressBar progressBar;

    private JButton run;
    private JButton show;
    private JButton export;

    private ButtonGroup resultStorageRadioGroup;
    private JRadioButton memoryStorageRadio;
    private JRadioButton fileStorageRadio;

    private JCheckBox simplifyCheck;
    private JComboBox<String> simplifyAlgorithmCombo;
    private JTextField simplifyToleranceField;

    private JCheckBox eventDetectionCheck;
    private JTextField eventDetectionGammaField;
    private JCheckBox eventDetectionStepBoundCheck;
    private JTextField eventDetectionStepBoundLowField;

    private SimulationTask task;

    private LinkedList<String> toShow = new LinkedList<>();

    public SimulationForm(AppData data, W mainWindow) throws HeadlessException {
        this.mw = mainWindow;
        this.data = data;
        if (data.getModel().isPDE()) {
            FDMNewConverter converter = new FDMNewConverter(data.getModel());
            data.setModel(converter.convert());
        }

        init();

        ((MainWindow) mainWindow).addI18NChangedListener(this);
    }

    private void init() {
        JComponent panel = buildPanel();
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        p.add(panel);
        p.add(buildToShow());
        //        getContentPane().add(panel);
        getContentPane().add(p);
        pack();
        //setSize(870, 400);
        centerWindowLocation();
        setVisible(true);
    }

    private JComponent buildPanel() {
        initComponents();
        setTitle(I18nUtils.getMessage("simulationForm.title"));

        FormLayout layout = new FormLayout(
                "right:max(40dlu;pref), 3dlu, 70dlu, 7dlu, "
                        + "right:max(40dlu;pref), 3dlu, 70dlu",
                "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();

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

        builder.appendSeparator("");

        builder.append(run);
        builder.append(show);
        builder.append(export);
        builder.nextLine();

        progressBar = new JProgressBar();

        progressBar.setStringPainted(true);
        builder.append(progressBar, 7);
        builder.nextLine();

        return builder.getPanel();
    }

    private JComponent buildToShow() {
        JScrollPane panel = new JScrollPane();
        panel.setAutoscrolls(true);
        panel.getVerticalScrollBar().setUnitIncrement(16);
        panel.setLayout(new ScrollPaneLayout());
        panel.setBorder(BorderFactory.createTitledBorder(I18nUtils.getMessage("simulationForm.variablesToShow")));
        panel.setPreferredSize(new Dimension(250, 500));

        JPanel pp = new JPanel();
        pp.setLayout(new BoxLayout(pp, BoxLayout.PAGE_AXIS));
        panel.getViewport().add(pp);

        pp.add(new Label("=[ODE]="));
        data.getModel().getVariableTable().getOdes().stream()
                .sorted((a, b) -> a.getCode().compareTo(b.getCode()))
                .forEach(ode -> pp.add(makeCheckBox(ode.getCode())));

        pp.add(new Label("=[ALG]="));
        data.getModel().getVariableTable().getAlgs().stream()
                .sorted((a, b) -> a.getCode().compareTo(b.getCode()))
                .forEach(alg -> pp.add(makeCheckBox(alg.getCode())));

        pp.add(new Label("=[LS]="));
        data.getModel().getLinearSystem().getVars().values().stream()
                .sorted((a, b) -> a.getCode().compareTo(b.getCode()))
                .forEach(v -> pp.add(makeCheckBox(v.getCode())));

        return panel;
    }

    private JCheckBox makeCheckBox(String code) {
        JCheckBox jCheckBox = new JCheckBox(code);
        jCheckBox.addActionListener(e -> {
            if (jCheckBox.isSelected()) {
                toShow.add(code);
            } else {
                toShow.remove(code);
            }
        });
        return jCheckBox;
    }

    private void initComponents() {
        start = new JTextField("0");
        end = new JTextField("10");
        step = new JTextField("0.1");
        run = new JButton("start");
        run.addActionListener(e -> runSim());

        show = new JButton("show");
        show.setEnabled(false);
        show.addActionListener(e -> {
            boolean simplify = simplifyCheck.isSelected();
            // TODO: отрефакторить
            if (simplify) {
                boolean highQuality = simplifyAlgorithmCombo.getSelectedIndex() == 1; // Douglas-Peucker
                double tolerance = Double.valueOf(simplifyToleranceField.getText());
                showSim(true, highQuality, tolerance);
            } else {
                showSim(false, false, 0.0);
            }

        });

        export = new JButton("export");
        export.setEnabled(false);
        export.addActionListener(e -> exportSim());

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
        accuracy = new JTextField("0.1");
        accuracy.setEnabled(false);

        stable = new JCheckBox();
        stable.setEnabled(false);

        parallel = new JCheckBox();
        intgServer = new JTextField("localhost");
        intgPort = new JTextField("7890");

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
        simplifyAlgorithmCombo = new JComboBox<>(simplifyAlgorithms);
        simplifyAlgorithmCombo.setEnabled(false);

        simplifyToleranceField = new JTextField();
        simplifyToleranceField.setEnabled(false);

        eventDetectionCheck = new JCheckBox();
        eventDetectionCheck.addActionListener(e -> {
            boolean selected = eventDetectionCheck.isSelected();
            eventDetectionGammaField.setEnabled(selected);
            eventDetectionStepBoundCheck.setEnabled(selected);
            eventDetectionStepBoundLowField.setEnabled(selected);
        });

        eventDetectionGammaField = new JTextField("0.8");
        eventDetectionGammaField.setEnabled(false);

        eventDetectionStepBoundCheck = new JCheckBox();
        eventDetectionStepBoundCheck.setSelected(true);
        eventDetectionStepBoundCheck.setEnabled(false);

        eventDetectionStepBoundLowField = new JTextField("0.001");
        eventDetectionStepBoundLowField.setEnabled(false);
    }

    private void runSim() {
        Double start = null;
        Double end = null;
        Double step = null;
        Boolean isAccurate;
        Double accuracy;
        Boolean isStable;

        try {
            start = getValOrDie(this.start);
            end = getValOrDie(this.end);
            step = getValOrDie(this.step);

            isAccurate = accurate.isSelected();
            accuracy = getValOrDie(this.accuracy);
            isStable = stable.isSelected();

            progressBar.setMinimum(0);
            progressBar.setMaximum(100);
            progressBar.setValue(0);

            CauchyInitials cauchyInitials = new CauchyInitials();
            cauchyInitials.setStart(start);
            cauchyInitials.setEnd(end);
            cauchyInitials.setStepSize(step);

            IntgMethod intgMethod = IntgMethodLibrary.getIntgMethod((String) method.getSelectedItem());

            AccuracyIntgController accuracyController = intgMethod.getAccuracyController();
            if (accuracyController != null) {
                accuracyController.setEnabled(isAccurate);
                if (isAccurate) {
                    accuracyController.setAccuracy(accuracy);
                }
            }

            if (intgMethod.getStabilityController() != null) {
                intgMethod.getStabilityController().setEnabled(isStable);
            }

            boolean isParallel = parallel.isSelected();
            String server = intgServer.getText();
            int port = Integer.valueOf(intgPort.getText());

            data.getModel().initTimeEquation(start);

            String resultFileName = null;
            if (fileStorageRadio.isSelected()) {
                resultFileName = RESULT_FILE_NAME;
            }

            boolean eventDetectionEnabled = eventDetectionCheck.isSelected();
            double eventDetectionGamma = eventDetectionEnabled ? Double.valueOf(eventDetectionGammaField.getText()) : 0.0;
            double eventDetectionStepBoundLow = Double.MIN_VALUE;
            if (eventDetectionStepBoundCheck.isEnabled() && eventDetectionStepBoundCheck.isSelected()) {
                eventDetectionStepBoundLow = Double.valueOf(eventDetectionStepBoundLowField.getText());
            }

            SimulationCoreController isma = new SimulationCoreController(data.getModel(), cauchyInitials, intgMethod,
                    isParallel, server, port,
                    resultFileName,
                    eventDetectionEnabled,
                    eventDetectionGamma,
                    eventDetectionStepBoundLow);
            double interval = cauchyInitials.getEnd() - cauchyInitials.getStart();
            task = new SimulationTask(progressBar, isma, mw, toShow, interval, start);
            task.execute();
            show.setEnabled(true);
            export.setEnabled(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showSim(boolean simplify, boolean highQuality, double tolerance) {
        if (task != null && task.getResult() != null) {
            task.show(simplify, highQuality, tolerance);
        }
    }

    private void exportSim() {
        if (task != null && task.getResult() != null) {
            try {
                task.export();

                String msg = String.format(I18nUtils.getMessage("simulationForm.export.success"), ExportService.EXPORT_FILEPATH);
                String title = I18nUtils.getMessage("simulationForm.export");
                JOptionPane.showMessageDialog((Component) mw, msg, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (RuntimeException e) {
                String msg = I18nUtils.getMessage("simulationForm.export.failed");
                String title = I18nUtils.getMessage("simulationForm.export");
                JOptionPane.showMessageDialog((Component) mw, msg, title, JOptionPane.INFORMATION_MESSAGE);

                e.printStackTrace();
            }
        }
    }

    private Double getValOrDie(JTextField source) {
        if (source.getText().length() == 0)
            throw new RuntimeException("empty field " + source.getName());
        return Double.valueOf(source.getText());
    }

    private void centerWindowLocation() {
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        // Move the window
        this.setLocation(x, y);
    }

    @Override
    public void i18Changed(Locale newLocale) {
        setTitle(I18nUtils.getMessage("simulationForm.title"));

        // TODO: реализовать, но для этого необходимо переделать интерфейс.
    }
}
