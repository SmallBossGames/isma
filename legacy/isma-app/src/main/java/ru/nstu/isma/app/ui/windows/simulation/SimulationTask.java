package ru.nstu.isma.app.ui.windows.simulation;

import ru.nstu.isma.app.ui.common.ExportService;
import ru.nstu.isma.app.ui.common.OutputAware;
import ru.nstu.isma.app.ui.common.out.IsmaChart;
import ru.nstu.isma.app.util.Consts;
import ru.nstu.isma.core.sim.controller.SimulationCoreController;
import ru.nstu.isma.core.sim.controller.HybridSystemIntgResult;
import ru.nstu.isma.intg.api.IntgResultMemoryStore;
import ru.nstu.isma.intg.api.IntgResultPointFileReader;
import ru.nstu.isma.intg.api.IntgResultPointProvider;
//import ru.nstu.isma.ui.common.ExportService;
//import ru.nstu.isma.ui.common.OutputAware;
//import ru.nstu.isma.ui.common.out.IsmaChart;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by Bessonov Alex
 * on 19.01.2015.
 */
public class SimulationTask<W extends OutputAware> extends SwingWorker<Void, Integer> implements PropertyChangeListener {
    private SimulationCoreController simulationCoreController;

    private Double len;

    private Double start;

    private Integer progress = 0;

    private HybridSystemIntgResult result;

    private W mw;

    private LinkedList<String> toShow;

    JProgressBar progressBar;

    private SimulationWindowController simulationWindowController;

    private List<Supplier> doneListeners = new LinkedList<>();


    public SimulationTask(JProgressBar progressBar, SimulationCoreController simulationCoreController, W mw, LinkedList<String> toShow, double interval, double start, SimulationWindowController simulationWindowController) {
        this.progressBar = progressBar;
        this.simulationCoreController = simulationCoreController;
        this.mw = mw;
        this.len = interval;
        this.toShow = toShow;
        this.start = start;
        this.simulationWindowController = simulationWindowController;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            addPropertyChangeListener(this);

            simulationCoreController.addStepChangeListener(d -> {
                progress = getProgress(d);
                if (progress < 0) progress = 0;
                if (progress > 100) progress = 100;

                publish(progress);
            });

            // init 0
            setProgress(progress);

            // start
            result = simulationCoreController.simulate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected void process(List<Integer> chunk) {
        setProgress(chunk.get(chunk.size() - 1));
    }

    private int getProgress(Double cur) {

        return (int) Math.round(100.0 * (cur - start) / len);
    }

    public HybridSystemIntgResult getResult() {
        return result;
    }

    @Override
    public void done() {
        simulationWindowController.getSimWin().getToolbar().setRunButtonView();
        simulationWindowController.getSimWin().getToolbar().getProgressBar().setValue(0);
        doneListeners.forEach(s ->s.get());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        progressBar.setValue(progress);
    }

    public void show(boolean simplify, boolean highQuality, double tolerance) {
        IntgResultPointProvider resultPointProvider = result.getResultPointProvider();

        // TODO: отрефакторить
        if (resultPointProvider instanceof IntgResultMemoryStore) {
            IsmaChart ismaChart = new IsmaChart(mw);
            ismaChart.show(result, toShow, simplify, highQuality, tolerance);
        } else {
            // TODO: сделать, чтобы из файла считывались только видимые линии.
            InputStream inputStream;
            try {
                inputStream = new FileInputStream(Consts.RESULT_FILE_NAME);
                IntgResultPointFileReader reader = (IntgResultPointFileReader) result.getResultPointProvider();
                reader.init(inputStream);
            } catch (IOException e) {
                throw new RuntimeException("Failed to open file result.csv");
            }

            IsmaChart ismaChart = new IsmaChart(mw);
            ismaChart.show(result, toShow, simplify, highQuality, tolerance);

            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Failed to close file result.csv");
            }
        }
    }

    public void setToShow(LinkedList<String> toShow) {
        this.toShow = toShow;
    }

    public void addDoneListener(Supplier supplier) {
        doneListeners.add(supplier);
    }

    public void export() {
        ExportService.toFile(result);
    }
}
