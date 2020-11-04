package ru.nstu.isma.intg.demo.app.ui.method;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.demo.app.models.MethodModel;
import ru.nstu.isma.intg.demo.app.ui.DemoFormController;
import ru.nstu.isma.intg.demo.app.ui.Form;
import ru.nstu.isma.intg.demo.app.ui.utils.I18nUtils;
import ru.nstu.isma.intg.demo.app.ui.utils.ViewUtils;
import ru.nstu.isma.intg.lib.IntgMethodLibrary;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @author Mariya Nasyrova
 * @since 16.10.14
 */
public class MethodForm implements Form {

    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel buttonPanel;
    private JButton cancelButton;
    private JButton okButton;
    private JPanel accuracyPanel;
    private JLabel accuracyLabel;
    private JTextField accuracyTextField;
    private JCheckBox accurateCheckBox;
    private JCheckBox stableCheckBox;
    private JCheckBox parallelCheckBox;
    private JPanel intgServerPanel;
    private JLabel hostLabel;
    private JTextField hostTextField;
    private JLabel portLabel;
    private JTextField portTextField;

    private JFrame frame;

    private MethodFormController methodFormController;
    private MethodModel methodModel;

    public MethodForm(Form parent) {
        setupUI();

        boolean isParallel = parallelCheckBox.isSelected();
        intgServerPanel.setEnabled(isParallel);
        hostLabel.setEnabled(isParallel);
        hostTextField.setEnabled(isParallel);
        portLabel.setEnabled(isParallel);
        portTextField.setEnabled(isParallel);

        parallelCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                boolean isParallel = parallelCheckBox.isSelected();
                intgServerPanel.setEnabled(isParallel);
                hostLabel.setEnabled(isParallel);
                hostTextField.setEnabled(isParallel);
                portLabel.setEnabled(isParallel);
                portTextField.setEnabled(isParallel);
            }
        });

        methodFormController = new MethodFormController(this);
        new OkButtonController(methodFormController, okButton);
        new CancelButtonController(methodFormController, cancelButton);
        methodFormController.addListener(new MethodFormI18nChangedListener(this));

        new AccurateCheckBoxController(methodFormController, accurateCheckBox);

        // TODO: развязать ProblemForm и DemoForm
        DemoFormController demoFormController = (DemoFormController) parent.getFormController();
        demoFormController.addListener(new DemoFormI18nChangedListener(methodFormController));
        frame = createFrame();
    }

    public String getName() {
        return frame.getTitle();
    }

    public JCheckBox getAccurateCheckBox() {
        return accurateCheckBox;
    }

    public JLabel getAccuracyLabel() {
        return accuracyLabel;
    }

    public JTextField getAccuracyTextField() {
        return accuracyTextField;
    }

    public JCheckBox getStableCheckBox() {
        return stableCheckBox;
    }

    public JCheckBox getParallelCheckBox() {
        return parallelCheckBox;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JPanel getIntgServerPanel() {
        return intgServerPanel;
    }

    public JLabel getHostLabel() {
        return hostLabel;
    }

    public JTextField getHostTextField() {
        return hostTextField;
    }

    public JLabel getPortLabel() {
        return portLabel;
    }

    public JTextField getPortTextField() {
        return portTextField;
    }

    public MethodModel getMethodModel() {
        return methodModel;
    }

    @Override
    public MethodFormController getFormController() {
        return methodFormController;
    }

    public void show(final MethodModel method) {
        this.methodModel = method;
        methodFormController.i18nChanged(I18nUtils.getLocale());
        frame.setTitle(I18nUtils.getMethodTypeName(method.getType()));
        frame.setVisible(true);

        IntgMethod intgMethod = IntgMethodLibrary.createMethod(method.getType());
        accurateCheckBox.setEnabled(intgMethod.getAccuracyController() != null);
        accuracyLabel.setEnabled(accurateCheckBox.isEnabled() && accurateCheckBox.isSelected());
        accuracyTextField.setEnabled(accurateCheckBox.isEnabled() && accurateCheckBox.isSelected());
        accuracyTextField.setText(Double.toString(method.getAccuracy()));
        stableCheckBox.setEnabled(intgMethod.getStabilityController() != null);

        hostTextField.setText(method.getIntgServerHost());
        portTextField.setText(String.valueOf(method.getIntgServerPort()));
    }

    public void close() {
        frame.setVisible(false);
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.add(mainPanel);

        Dimension screenSize = ViewUtils.getScreenSize();
        int height = screenSize.height / 3;
        int width = screenSize.width / 4;
        frame.setPreferredSize(new Dimension(width, height));

        frame.pack();
        frame.setLocation(ViewUtils.getCenterLocation(frame));

        ViewUtils.setWindowsLookAndFeel(frame);
        return frame;
    }

    private void setupUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(3, 2, new Insets(10, 10, 10, 10), -1, -1));
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 10, 0), -1, -1));
        mainPanel.add(contentPanel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        stableCheckBox = new JCheckBox();
        stableCheckBox.setText("CheckBox");
        contentPanel.add(stableCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        parallelCheckBox = new JCheckBox();
        parallelCheckBox.setText("CheckBox");
        contentPanel.add(parallelCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        accuracyPanel = new JPanel();
        accuracyPanel.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(accuracyPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        accurateCheckBox = new JCheckBox();
        accurateCheckBox.setText("CheckBox");
        accuracyPanel.add(accurateCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        accuracyPanel.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        accuracyLabel = new JLabel();
        accuracyLabel.setText("Label");
        accuracyPanel.add(accuracyLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        accuracyTextField = new JTextField();
        accuracyPanel.add(accuracyTextField, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 20, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        intgServerPanel = new JPanel();
        intgServerPanel.setLayout(new GridLayoutManager(1, 2, new Insets(10, 25, 10, 10), -1, -1));
        panel1.add(intgServerPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        intgServerPanel.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        hostLabel = new JLabel();
        hostLabel.setText("Label");
        panel2.add(hostLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        hostTextField = new JTextField();
        panel2.add(hostTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        intgServerPanel.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        portLabel = new JLabel();
        portLabel.setText("Label");
        panel3.add(portLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        portTextField = new JTextField();
        panel3.add(portTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(buttonPanel, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setIcon(new ImageIcon(getClass().getResource("/icons/cancel.png")));
        cancelButton.setText("Button");
        buttonPanel.add(cancelButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        buttonPanel.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        okButton = new JButton();
        okButton.setIcon(new ImageIcon(getClass().getResource("/icons/ok.png")));
        okButton.setText("Button");
        buttonPanel.add(okButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

}
