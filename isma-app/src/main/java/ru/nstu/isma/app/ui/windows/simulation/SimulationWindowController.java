package ru.nstu.isma.app.ui.windows.simulation;

import com.alee.laf.rootpane.WebDialog;
import ru.nstu.isma.app.env.project.RunConfiguration;
import ru.nstu.isma.app.util.Consts;
import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.core.sim.controller.Controller;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials;
import ru.nstu.isma.intg.api.methods.AccuracyIntgController;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.lib.IntgMethodLibrary;

import javax.swing.*;
import java.util.LinkedList;

/**
 * Created by Bessonov Alex on 22.07.2016.
 */

public class SimulationWindowController {

    private SimulationWindow sim;

    private SimulationTask currentTask;


    public SimulationWindowController(SimulationWindow sim) {
        this.sim = sim;
    }

    public boolean validateFields() {
        return sim.confParamPanel().validateFields();
    }

    public RunConfiguration getRunConfiguration() {
        return sim.confParamPanel().populateRunConfig();
    }

    public void runSim(HSM model) {
        sim.confParamPanel().validateFields();

        JProgressBar progressBar = sim.getToolbar().getProgressBar();

        RunConfiguration conf = getRunConfiguration();
        try {
            progressBar.setMinimum(0);
            progressBar.setMaximum(100);
            progressBar.setValue(0);

            CauchyInitials cauchyInitials = new CauchyInitials();
            cauchyInitials.setStart(conf.getStartTime());
            cauchyInitials.setEnd(conf.getEndTime());
            cauchyInitials.setStepSize(conf.getStep());

            IntgMethod intgMethod = IntgMethodLibrary.getIntgMethod(conf.getMethod());

            AccuracyIntgController accuracyController = intgMethod.getAccuracyController();
            if (accuracyController != null) {
                accuracyController.setEnabled(conf.getAccurate());

                if (conf.getAccurate())
                    accuracyController.setAccuracy(conf.getAccuracy());

            }

            if (intgMethod.getStabilityController() != null)
                intgMethod.getStabilityController().setEnabled(conf.getStable());


            model.initTimeEquation(conf.getStartTime());

            String resultFileName = null;
            if (conf.getResultStorage() == RunConfiguration.ResultStorageType.FILE) {
                resultFileName = Consts.RESULT_FILE_NAME;
            }

            boolean eventDetectionEnabled = conf.getEventDetectionCheck();
            double eventDetectionGamma = eventDetectionEnabled ? conf.getEventDetectionGammaField() : 0.0;
            double eventDetectionStepBoundLow = Double.MIN_VALUE;

            if (conf.getEventDetectionStepBoundCheck() && conf.getEventDetectionStepBoundCheck()) {
                eventDetectionStepBoundLow = conf.getEventDetectionStepBoundLowField();
            }

            Controller isma = new Controller(model, cauchyInitials, intgMethod,
                    conf.getParallel(), conf.getIntgServer(), conf.getIntgPort(),
                    resultFileName,
                    eventDetectionEnabled,
                    eventDetectionGamma,
                    eventDetectionStepBoundLow);

            double interval = cauchyInitials.getEnd() - cauchyInitials.getStart();

            currentTask = new SimulationTask(progressBar, isma, null, new LinkedList<>(), interval, conf.getStartTime(), this);
            currentTask.execute();

            currentTask.addDoneListener(() -> {
                sim.getToolbar().getShowButton().setEnabled(true);
                sim.getToolbar().getExportButton().setEnabled(true);
                return null;
            });


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopSim() {
        if (currentTask != null)
            currentTask.cancel(true);

    }


    public void showSim(boolean simplify, boolean highQuality, double tolerance) {
        if (currentTask != null && currentTask.getResult() != null) {
            ResultViewWindow viewWindow = new ResultViewWindow(getModel(), currentTask, simplify, highQuality, tolerance);
            viewWindow.setVisible(true);

        }
    }

    public SimulationTask getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(SimulationTask currentTask) {
        this.currentTask = currentTask;
    }

    public SimulationWindow getSimWin() {
        return sim;
    }

    public HSM getModel() {
        return getSimWin().getModel();
    }
}
