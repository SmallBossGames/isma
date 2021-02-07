package ru.nstu.isma.app.ui.windows.simulation;

import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.progress.WebProgressOverlay;
import com.alee.global.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.utils.swing.ComponentUpdater;
import ru.nstu.isma.app.env.IsmaEnvironment;
import ru.nstu.isma.app.env.project.RunConfiguration;
import ru.nstu.isma.ui.common.ExportService;
import ru.nstu.isma.ui.i18n.I18nUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Bessonov Alex
 * on 30.07.2016.
 */

public class SimulationToolbar extends WebPanel {

    private IsmaEnvironment env;

    private SimulationWindowController controller;

    private WebProgressBar progressBar;

    private WebButton runButton;

    private WebButton showButton;

    private WebButton exportButton;

    private WebProgressOverlay progressOverlay;

    public SimulationToolbar(IsmaEnvironment env, SimulationWindowController controller) {
        super(new VerticalFlowLayout(10, 10));

        this.env = env;
        this.controller = controller;

        setMargin(10, 10, 5, 10);

        add(tools(), BorderLayout.WEST);
        add(simulation(), BorderLayout.CENTER);
    }

    private WebPanel tools() {
        WebPanel toolsPanel = new WebPanel(new HorizontalFlowLayout(10));
        showButton = new WebButton(I18nUtils.getMessage("simulationForm.buttons.show"), env.loadIcon("toolbar/settings.png"));
        showButton.setEnabled(false);
        showButton.addActionListener(e -> {
            RunConfiguration conf = controller.getRunConfiguration();
            boolean simplify = conf.getSimplifyCheck();
            // TODO: отрефакторить
            if (simplify) {
                boolean highQuality = conf.getSimplifyAlgorithmCombo().equals("Douglas-Peucker");
                double tolerance = conf.getSimplifyToleranceField();
                controller.showSim(true, highQuality, tolerance);
            } else {
                controller.showSim(false, false, 0.0);
            }
        });
        toolsPanel.add(showButton);

        exportButton = new WebButton(I18nUtils.getMessage("simulationForm.buttons.export"), env.loadIcon("toolbar/settings.png"));
        exportButton.setEnabled(false);
        subscribeOnExportButton();

        toolsPanel.add(exportButton);

        toolsPanel.setPreferredSize(new Dimension(365, 25));
        return toolsPanel;
    }

    private WebPanel simulation() {
        WebPanel simPanel = new WebPanel(new BorderLayout());
        simPanel.add(makeStartButton(), BorderLayout.WEST);
        simPanel.add(progressBar(), BorderLayout.CENTER);
        return simPanel;
    }

    private WebProgressBar progressBar() {
        progressBar = new WebProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setPreferredWidth(450);
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(true);
        return progressBar;
    }

    private Component makeStartButton() {

        final ImageIcon start = env.loadIcon("rocket.png");

        // Progress overlay
        progressOverlay = new WebProgressOverlay();
        progressOverlay.setConsumeEvents(false);

        // Progress state change button
        runButton = new WebButton(I18nUtils.getMessage("simulationForm.sim.start"), start);
        runButton.setRound(9);
        progressOverlay.setComponent(runButton);

        // Progress switch
        runButton.addActionListener(e -> {

            boolean showLoad = !progressOverlay.isShowLoad();

            if (showLoad) {
                setCancelButtonView();
                controller.runSim(controller.getModel());
            } else {
                setRunButtonView();
                controller.stopSim();
            }

        });

        return new GroupPanel(5, progressOverlay);
    }

    public void setCancelButtonView() {
        final ImageIcon stop = env.loadIcon("unchecked.png");
        progressOverlay.setShowLoad(true);
        runButton.setText(I18nUtils.getMessage("simulationForm.sim.cancel"));
        runButton.setIcon(stop);
    }

    public void setRunButtonView() {
        final ImageIcon start = env.loadIcon("rocket.png");
        progressOverlay.setShowLoad(false);
        runButton.setText(I18nUtils.getMessage("simulationForm.sim.start"));
        runButton.setIcon(start);
    }

    public WebProgressBar getProgressBar() {
        return progressBar;
    }

    public WebButton getShowButton() {
        return showButton;
    }

    public WebButton getExportButton() {
        return exportButton;
    }

    private void subscribeOnExportButton() {
        exportButton.addActionListener(e -> {

            SimulationTask task = controller.getCurrentTask();
            if (task != null && task.getResult() != null) {
                try {
                    task.export();

                    String msg = String.format(I18nUtils.getMessage("simulationForm.export.success"), ExportService.EXPORT_FILEPATH);
                    String title = I18nUtils.getMessage("simulationForm.export");
                    JOptionPane.showMessageDialog(controller.getSimWin(), msg, title, JOptionPane.INFORMATION_MESSAGE);
                } catch (RuntimeException re) {
                    String msg = I18nUtils.getMessage("simulationForm.export.failed");
                    String title = I18nUtils.getMessage("simulationForm.export");
                    JOptionPane.showMessageDialog(controller.getSimWin(), msg, title, JOptionPane.INFORMATION_MESSAGE);

                    re.printStackTrace();
                }
            }
        });
    }
}
