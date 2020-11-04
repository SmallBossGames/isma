package ru.nstu.isma.intg.demo.app.ui;

import ru.nstu.isma.intg.api.IntgMetricData;
import ru.nstu.isma.intg.api.IntgResultPoint;
import ru.nstu.isma.intg.api.IntgResultPointProvider;
import ru.nstu.isma.intg.demo.app.ui.utils.I18nUtils;

import javax.swing.*;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * @author Mariya Nasyrova
 * @since 17.10.2014
 */
public class ResultTextAreaController extends DemoFormControllerAdapter {

    private DemoFormController demoFormController;
    private JTextArea resultTextArea;
    private IntgMetricData metricData;
    private IntgResultPointProvider resultPointProvider;

    public ResultTextAreaController(DemoFormController demoFormController, JTextArea resultTextArea) {
        this.demoFormController = demoFormController;
        this.resultTextArea = resultTextArea;
    }

    @Override
    public void simulationFinished(IntgMetricData metricData, IntgResultPointProvider resultPointProvider) {
        this.metricData = metricData;
        this.resultPointProvider = resultPointProvider;
        resultTextArea.setText(getTextDescriptions());
    }

    @Override
    public void i18nChanged(Locale newLocale) {
        super.i18nChanged(newLocale);
        resultTextArea.setText(getTextDescriptions());
    }

    private String getTextDescriptions() {
        if (metricData == null || resultPointProvider == null) { return ""; }

        StringBuilder builder = new StringBuilder();
        builder.append(I18nUtils.getMessage("demo.result.simulationFinished"));
        builder.append("\n");

        if (metricData != null) {
            builder.append(String.format(I18nUtils.getMessage("demo.result.timeTemplate"), metricData.getSimulationTime()));
            builder.append("\n");
        }

        if (resultPointProvider != null) {
            builder.append(I18nUtils.getMessage("demo.result.resultBuildingInProgress"));
            builder.append("\n");

            long start = System.currentTimeMillis();
            String resultText = formatResultAsString(resultPointProvider);
            long end = System.currentTimeMillis();
            builder.append(String.format(I18nUtils.getMessage("demo.result.resultBuildingTimeTemplate"), end - start));
            builder.append("\n");
            builder.append(I18nUtils.getMessage("demo.result.simulationResults"));
            builder.append("\n");
            builder.append(resultText);
            builder.append("\n");
        }

        long endOfAll = System.currentTimeMillis();
        builder.append(String.format(I18nUtils.getMessage("demo.result.totalTimeTemplate"), endOfAll - demoFormController.getSimulationStartTime()));
        return builder.toString();
    }

    private String formatResultAsString(IntgResultPointProvider resultPointProvider) {
        StringBuilder sb = new StringBuilder();

        resultPointProvider.read(new Consumer<List<IntgResultPoint>>() {
            @Override
            public void accept(List<IntgResultPoint> intgResultPoints) {
                intgResultPoints.stream().forEach(p -> {
                    sb.append("\n x: ").append(p.getX());
                    sb.append("\t y: ");
                    for (int i = 0; i < p.getYForDe().length; i++) {
                        sb.append(i + 1).append("=").append(p.getYForDe()[i]).append(";\t ");
                    }
                });
            }
        });

        return sb.toString();
    }
}
