package ru.nstu.isma.intg.demo.app.ui.problem;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import ru.nstu.isma.intg.demo.app.models.ProblemModel;
import ru.nstu.isma.intg.demo.app.models.ProblemType;
import ru.nstu.isma.intg.demo.app.ui.DemoFormController;
import ru.nstu.isma.intg.demo.app.ui.Form;
import ru.nstu.isma.intg.demo.app.ui.utils.I18nUtils;
import ru.nstu.isma.intg.demo.app.ui.utils.ViewUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mariya Nasyrova
 * @since 13.10.14
 */
public class ProblemForm implements Form {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel buttonPanel;
    private JLabel intervalLabel;
    private JTextField intervalStartTextField;
    private JTextField intervalEndTextField;
    private JLabel stepLabel;
    private JTextField stepTextField;
    private JLabel accuracyLabel;
    private JTextField accuracyTextField;
    private JButton okButton;
    private JButton cancelButton;
    private JSpinner paramJSpinner;
    private JSpinner paramKSpinner;
    private JLabel paramJLabel;
    private JLabel paramKLabel;
    private JPanel reactionDiffusionPanel;

    private JFrame frame;

    private ProblemFormController problemFormController;
    private ProblemModel problemModel;

    public ProblemForm(Form parent) {
        setupUI();

        paramJSpinner.setModel(new SpinnerNumberModel(0, null, null, 1));
        paramKSpinner.setModel(new SpinnerNumberModel(0, null, null, 1));

        problemFormController = new ProblemFormController(this);
        new OkButtonController(problemFormController, okButton);
        new CancelButtonController(problemFormController, cancelButton);
        problemFormController.addListener(new ProblemFormI18nChangedListener(problemFormController, this));
        DemoFormController demoFormController = (DemoFormController) parent.getFormController();
        demoFormController.addListener(new DemoFormI18nChangedListener(problemFormController));
        frame = createFrame();
    }

    @Override
    public ProblemFormController getFormController() {
        return problemFormController;
    }

    public JLabel getIntervalLabel() {
        return intervalLabel;
    }

    public JTextField getIntervalStartTextField() {
        return intervalStartTextField;
    }

    public JTextField getIntervalEndTextField() {
        return intervalEndTextField;
    }

    public JLabel getStepLabel() {
        return stepLabel;
    }

    public JTextField getStepTextField() {
        return stepTextField;
    }

    public JLabel getAccuracyLabel() {
        return accuracyLabel;
    }

    public JTextField getAccuracyTextField() {
        return accuracyTextField;
    }

    public ProblemModel getProblemModel() {
        return problemModel;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JSpinner getParamJSpinner() {
        return paramJSpinner;
    }

    public JSpinner getParamKSpinner() {
        return paramKSpinner;
    }

    public JLabel getParamJLabel() {
        return paramJLabel;
    }

    public JLabel getParamKLabel() {
        return paramKLabel;
    }

    public void show(final ProblemModel problemModel) {
        this.problemModel = new ProblemModel(problemModel);

        intervalEndTextField.setText(Double.toString(problemModel.getIntervalEnd()));
        intervalStartTextField.setText(Double.toString(problemModel.getIntervalStart()));
        stepTextField.setText(Double.toString(problemModel.getStep()));

        reactionDiffusionPanel.setVisible(ProblemType.REACTION_DIFFUSION.equals(problemModel.getType()));
        paramJSpinner.setValue(problemModel.getReactionDiffusionParamJ());
        paramKSpinner.setValue(problemModel.getReactionDiffusionParamK());

        problemFormController.i18nChanged(I18nUtils.getLocale());
        frame.setTitle(I18nUtils.getProblemTypeName(problemModel.getType()));
        frame.setVisible(true);
    }

    public void close() {
        frame.setVisible(false);
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.add(mainPanel);

        Dimension screenSize = ViewUtils.getScreenSize();
        int height = screenSize.height / 4;
        int width = screenSize.width / 4;
        frame.setPreferredSize(new Dimension(width, height));

        frame.pack();
        frame.setLocation(ViewUtils.getCenterLocation(frame));

        ViewUtils.setWindowsLookAndFeel(frame);
        return frame;
    }

    private void setupUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(4, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(contentPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        intervalLabel = new JLabel();
        intervalLabel.setText("Label");
        contentPanel.add(intervalLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        intervalStartTextField = new JTextField();
        contentPanel.add(intervalStartTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        intervalEndTextField = new JTextField();
        contentPanel.add(intervalEndTextField, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        stepLabel = new JLabel();
        stepLabel.setText("Label");
        contentPanel.add(stepLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stepTextField = new JTextField();
        contentPanel.add(stepTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(buttonPanel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setIcon(new ImageIcon(getClass().getResource("/icons/cancel.png")));
        cancelButton.setText("Button");
        buttonPanel.add(cancelButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        buttonPanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        okButton = new JButton();
        okButton.setIcon(new ImageIcon(getClass().getResource("/icons/ok.png")));
        okButton.setText("Button");
        buttonPanel.add(okButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        reactionDiffusionPanel = new JPanel();
        reactionDiffusionPanel.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        reactionDiffusionPanel.setEnabled(true);
        reactionDiffusionPanel.setVisible(true);
        mainPanel.add(reactionDiffusionPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        paramJLabel = new JLabel();
        paramJLabel.setText("Label");
        reactionDiffusionPanel.add(paramJLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        reactionDiffusionPanel.add(spacer3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        reactionDiffusionPanel.add(spacer4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        paramJSpinner = new JSpinner();
        reactionDiffusionPanel.add(paramJSpinner, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        paramKLabel = new JLabel();
        paramKLabel.setText("Label");
        reactionDiffusionPanel.add(paramKLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        paramKSpinner = new JSpinner();
        reactionDiffusionPanel.add(paramKSpinner, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

}
