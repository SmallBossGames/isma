package ru.nstu.isma.intg.demo.app.ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.nstu.isma.intg.demo.app.models.ProblemType;
import ru.nstu.isma.intg.demo.app.ui.toolbar.*;
import ru.nstu.isma.intg.demo.app.ui.utils.I18nUtils;
import ru.nstu.isma.intg.demo.app.ui.utils.ViewUtils;
import ru.nstu.isma.intg.lib.IntgMethodType;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mariya Nasyrova
 * @since 02.10.14
 */
public class DemoForm implements Form {
    private JPanel mainPanel;
    private JToolBar toolBar;
    private JPanel contentPanel;
    private JComboBox problemComboBox;
    private JComboBox methodComboBox;
    private JButton runButton;
    private JPanel problemPanel;
    private JTextArea problemTextArea;
    private JPanel resultPanel;
    private JTextArea resultTextArea;
    private JButton ruButton;
    private JButton enButton;
    private JLabel problemLabel;
    private JLabel resultLabel;

    private JFrame frame;

    private DemoFormController demoFormController;

    public DemoForm() {
        setupUI();

        demoFormController = new DemoFormController(this);
        demoFormController.addListener(new ProblemComboBoxController(demoFormController, problemComboBox));
        demoFormController.addListener(new ProblemTextAreaController(demoFormController, problemTextArea));
        demoFormController.addListener(new MethodComboBoxController(demoFormController, methodComboBox));
        demoFormController.addListener(new ResultTextAreaController(demoFormController, resultTextArea));
        new RuButtonController(demoFormController, ruButton);
        new EnButtonController(demoFormController, enButton);
        new RunButtonController(demoFormController, runButton);
        demoFormController.addListener(new DemoFormI18nChangedListener(demoFormController, this));
    }

    @Override
    public DemoFormController getFormController() {
        return demoFormController;
    }

    public JComboBox getProblemComboBox() {
        return problemComboBox;
    }

    public JComboBox getMethodComboBox() {
        return methodComboBox;
    }

    public JLabel getResultLabel() {
        return resultLabel;
    }

    public JLabel getProblemLabel() {
        return problemLabel;
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            frame = createFrame();
            frame.setVisible(true);

            ProblemType[] availableProblemTypes = ProblemType.values();
            IntgMethodType[] methods = IntgMethodType.values();

            demoFormController.formOpened(availableProblemTypes, methods);
            demoFormController.i18nChanged(I18nUtils.RU);
        });
    }

    private JFrame createFrame() {
        JFrame jFrame = new JFrame("Isma 2015");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(mainPanel);

        Dimension screenSize = ViewUtils.getScreenSize();
        int height = screenSize.height / 2;
        int width = screenSize.width / 2;
        jFrame.setPreferredSize(new Dimension(width, height));

        jFrame.pack();
        jFrame.setLocation(ViewUtils.getCenterLocation(jFrame));

        ViewUtils.setWindowsLookAndFeel(jFrame);
        return jFrame;
    }

    private void setupUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        toolBar = new JToolBar();
        toolBar.setMargin(new Insets(0, 5, 0, 5));
        mainPanel.add(toolBar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
        problemComboBox = new JComboBox();
        toolBar.add(problemComboBox);
        methodComboBox = new JComboBox();
        toolBar.add(methodComboBox);
        runButton = new JButton();
        runButton.setIcon(new ImageIcon(getClass().getResource("/icons/run.png")));
        runButton.setText("");
        toolBar.add(runButton);
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        toolBar.add(toolBar$Separator1);
        ruButton = new JButton();
        ruButton.setIcon(new ImageIcon(getClass().getResource("/icons/ru.png")));
        ruButton.setLabel("");
        ruButton.setText("");
        toolBar.add(ruButton);
        enButton = new JButton();
        enButton.setIcon(new ImageIcon(getClass().getResource("/icons/en.png")));
        enButton.setText("");
        toolBar.add(enButton);
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(contentPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        contentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null));
        problemPanel = new JPanel();
        problemPanel.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 0), -1, -1));
        contentPanel.add(problemPanel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        problemPanel.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        problemTextArea = new JTextArea();
        problemTextArea.setFont(new Font("Courier New", problemTextArea.getFont().getStyle(), problemTextArea.getFont().getSize()));
        scrollPane1.setViewportView(problemTextArea);
        problemLabel = new JLabel();
        problemLabel.setText("Label");
        problemPanel.add(problemLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayoutManager(2, 1, new Insets(10, 0, 10, 10), -1, -1));
        contentPanel.add(resultPanel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        resultPanel.add(scrollPane2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        resultTextArea = new JTextArea();
        resultTextArea.setFont(new Font("Courier New", resultTextArea.getFont().getStyle(), resultTextArea.getFont().getSize()));
        scrollPane2.setViewportView(resultTextArea);
        resultLabel = new JLabel();
        resultLabel.setText("Label");
        resultPanel.add(resultLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

}
