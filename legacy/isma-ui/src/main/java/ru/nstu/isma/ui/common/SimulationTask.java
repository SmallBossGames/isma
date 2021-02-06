package ru.nstu.isma.ui.common;

import ru.nstu.isma.core.sim.controller.SimulationCoreController;
import ru.nstu.isma.core.sim.controller.HybridSystemIntgResult;
import ru.nstu.isma.intg.api.IntgResultMemoryStore;
import ru.nstu.isma.intg.api.IntgResultPointFileReader;
import ru.nstu.isma.intg.api.IntgResultPointProvider;
import ru.nstu.isma.ui.common.out.IsmaChart;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * Created by Bessonov Alex
 * on 19.01.2015.
 */
public class SimulationTask<W extends OutputAware> extends SwingWorker<Void, Void> implements PropertyChangeListener {
    private SimulationCoreController simulationCoreController;

    private Double len;

    private Double start;

    private Integer progress = 0;

    private HybridSystemIntgResult result;

    private W mw;

    private LinkedList<String> toShow;

    JProgressBar progressBar;

    public SimulationTask(JProgressBar progressBar, SimulationCoreController simulationCoreController, W mw, LinkedList<String> toShow, double interval, double start) {
        this.progressBar = progressBar;
        this.simulationCoreController = simulationCoreController;
        this.mw = mw;
        this.len = interval;
        this.toShow = toShow;
        this.start = start;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            addPropertyChangeListener(this);
            simulationCoreController.addStepChangeListener(d -> {
                progress = getProgress(d);
                if (progress < 0) progress = 0;
                if (progress > 100) progress = 100;
                setProgress(progress);
            });
            setProgress(progress);
            result = simulationCoreController.simulate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    private int getProgress(Double cur) {
        return (int) Math.round(100.0 * (cur - start) / len);
    }

    public HybridSystemIntgResult getResult() {
        return result;
    }

    @Override
    public void done() {
        //show();
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
                inputStream = new FileInputStream(SimulationForm.RESULT_FILE_NAME);
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

    public void export() {
        ExportService.toFile(result);
    }
}
