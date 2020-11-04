package ru.nstu.isma.ui.common.out;

import com.goebl.simplify.Simplify;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.core.common.SimulationResult;
import ru.nstu.isma.core.sim.controller.HybridSystemIntgResult;
import ru.nstu.isma.core.sim.controller.gen.EquationIndexProvider;
import ru.nstu.isma.intg.api.IntgResultPoint;
import ru.nstu.isma.intg.api.IntgResultPointProvider;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.ui.common.OutputAware;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bessonov Alex
 * on 03.10.14.
 */
public class IsmaChart<W extends OutputAware> extends JFrame {

    private final static Logger logger = LoggerFactory.getLogger(IsmaChart.class);

    XYSeries series1;
    XYSeriesCollection dataset;
    JFreeChart chart;
    String output;
    W mw;

    public IsmaChart(W mw) {
        this.mw = mw;
        series1 = new XYSeries("result");
        dataset = new XYSeriesCollection();
        setSize(830, 600);
    }

    public void draw(SimulationResult pointLists) {
        output = "";
        pointLists.equations().stream().filter(e -> !e.equals("time")).
                forEach(e -> {
                    output = output.concat("/*=======================*/").concat("\n");
                    output = output.concat("/*========= " + e + " =========*/").concat("\n");
                    series1 = new XYSeries(e);
                    pointLists.getPoints(e).stream().forEach(p -> {
                        series1.add(p.getX(), p.getY());
                        output = output.concat("[" + e + "] ").concat("x = ").concat(String.valueOf(p.getX())).concat("    ");
                        output = output.concat("y = ").concat(String.valueOf(p.getY())).concat("\n");
                    });
                    output = output.concat("/*=======================*/").concat("\n");
                    dataset.addSeries(series1);
                });

        chart = ChartFactory.createXYLineChart(
                "Simulation results", "X", "Y",
                dataset,
                PlotOrientation.VERTICAL, true, false, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel);
        setVisible(true);
        if (mw.getOutputArea() != null)
            mw.getOutputArea().setText(output);
        centerWindowLocation();
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

    public void show(final HybridSystemIntgResult result, List<String> visibleSeriesCodes,
                     boolean simplify, boolean highQuality, double tolerance) {
        XYDataset dataSet = createChartDataSet(
                result.getResultPointProvider(), result.getEquationIndexProvider(), visibleSeriesCodes,
                simplify, highQuality, tolerance);

        SwingUtilities.invokeLater(() -> {
            JFrame jFrame = new JFrame("Isma 2015");
            jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

            JFreeChart chart1 = ChartFactory.createXYLineChart("", "X", "Y", dataSet, PlotOrientation.VERTICAL, true, true, false);
            XYPlot plot = (XYPlot) chart1.getPlot();
            plot.setBackgroundPaint(new Color(231, 232, 219));
            plot.setBackgroundAlpha(0.5f);

            plot.setRangeGridlinePaint(Color.gray);

            // TODO: раскомментировать, если нужны точки на графике.
            // plot.setRenderer(new XYLineAndShapeRenderer());

            ValueAxis rangeAxis = plot.getRangeAxis();
            Font bold = rangeAxis.getTickLabelFont().deriveFont(Font.BOLD);
            rangeAxis.setTickLabelFont(bold);

            ValueAxis domainAxis = plot.getDomainAxis();
            Font domainAxisFont = domainAxis.getTickLabelFont().deriveFont(Font.BOLD);
            domainAxis.setTickLabelFont(domainAxisFont);

            ChartPanel cp = new ChartPanel(chart1);

            add(cp);
            pack();
            setVisible(true);
            if (mw.getOutputArea() != null) {
                mw.getOutputArea().setText(output);
            }
            centerWindowLocation();
        });
    }

    private XYDataset createChartDataSet(IntgResultPointProvider resultPointProvider,
                                         EquationIndexProvider equationIndexProvider, List<String> visibleSeriesCodes,
                                         boolean simplify, boolean highQuality, double tolerance) {
        LinkedList<Double> xValues = new LinkedList<>();
        ArrayList<LinkedList<Double>> ySeries = new ArrayList<>();
        ArrayList<String> yLabels = new ArrayList<>();

        resultPointProvider.read(intgResultPoints -> {
            boolean isCountInitialized = false;
            int deCount = 0;
            int aeCount = 0;

            List<IntgResultPoint> processedResultPoints = null;
            if (!simplify) {
                processedResultPoints = intgResultPoints;
            } else {
                IntgResultPoint[] resultPoints = new IntgResultPoint[intgResultPoints.size()];
                resultPoints = intgResultPoints.toArray(resultPoints);

                long start = System.currentTimeMillis();
                IntgResultPoint[] simplifiedResultPoints = simplify(resultPoints, highQuality, tolerance);
                long end = System.currentTimeMillis();

                processedResultPoints = Arrays.asList(simplifiedResultPoints); // TODO: optimize

                int beforePointCount = resultPoints.length;
                int afterPointCount = simplifiedResultPoints.length;
                long time = end - start;
                String algorithm = highQuality ? "Douglas-Peucker" : "Radial-Distance";
                logger.info("Simplification: before={} points; after={} points; time={} ms; algorithm={}; tolerance={}.",
                        beforePointCount, afterPointCount, time, algorithm, tolerance);
            }

            for (IntgResultPoint point : processedResultPoints) {
                if (!isCountInitialized) {
                    deCount = point.getYForDe().length;
                    aeCount = point.getRhs()[DaeSystem.RHS_AE_PART_IDX].length;
                    isCountInitialized = true;
                }

                if (ySeries.isEmpty()) {
                    // Инициализируем структуры для серий данных на первом шаге.
                    for (int i = 0; i < deCount; i++) {
                        String code = equationIndexProvider.getDifferentialEquationCode(i);
                        if (visibleSeriesCodes.contains(code)) {
                            ySeries.add(new LinkedList<>());
                            yLabels.add(code);
                        }
                    }
                    for (int i = 0; i < aeCount; i++) {
                        String code = equationIndexProvider.getAlgebraicEquationCode(i);
                        if (visibleSeriesCodes.contains(code)) {
                            ySeries.add(new LinkedList<>());
                            yLabels.add(code);
                        }
                    }
                }

                int seriesIndex = 0;
                xValues.add(point.getX());
                for (int i = 0; i < deCount; i++) {
                    String code = equationIndexProvider.getDifferentialEquationCode(i);
                    if (visibleSeriesCodes.contains(code)) {
                        ySeries.get(seriesIndex).add(point.getYForDe()[i]);
                        seriesIndex++;
                    }
                }

                for (int i = 0; i < aeCount; i++) {
                    String code = equationIndexProvider.getAlgebraicEquationCode(i);
                    if (visibleSeriesCodes.contains(code)) {
                        ySeries.get(seriesIndex).add(point.getRhs()[DaeSystem.RHS_AE_PART_IDX][i]);
                        seriesIndex++;
                    }
                }
            }
        });

        double[] x = xValues.stream().mapToDouble(Double::doubleValue).toArray();
        DefaultXYDataset dataSet = new DefaultXYDataset();

        for (int i = 0; i < ySeries.size(); i++) {
            double[][] series = new double[2][];
            series[0] = x;
            series[1] = ySeries.get(i).stream().mapToDouble(Double::doubleValue).toArray();
            String code = yLabels.get(i);
            dataSet.addSeries(code, series);
        }

        return dataSet;
    }

    private IntgResultPoint[] simplify(IntgResultPoint[] intgResultPoints, boolean highQuality, double tolerance) {
        IntgResultPointExtractor extractor = new IntgResultPointExtractor();
        Simplify<IntgResultPoint> simplify = new Simplify<>(new IntgResultPoint[0], extractor);
        IntgResultPoint[] simplified = simplify.simplify(intgResultPoints, tolerance, highQuality);
        return simplified;
    }

}
