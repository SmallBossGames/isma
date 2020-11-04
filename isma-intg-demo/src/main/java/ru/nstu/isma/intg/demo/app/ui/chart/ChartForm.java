package ru.nstu.isma.intg.demo.app.ui.chart;

import com.goebl.simplify.PointExtractor;
import com.goebl.simplify.Simplify;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import ru.nstu.isma.intg.api.IntgResultPoint;
import ru.nstu.isma.intg.api.IntgResultPointProvider;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.demo.app.ui.Form;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mariya Nasyrova
 * @since 17.10.2014
 */
public class ChartForm {
    private JPanel mainPanel;
    private JPanel chartPanel;

    public ChartForm(Form parent) {
        setupUI();
    }

    public void show(final IntgResultPointProvider resultPointProvider) {
        XYDataset dataSet = createChartDataSet(resultPointProvider, true);
        //XYDataset dataSet = createChartDataSet(resultPointProvider, false);

        SwingUtilities.invokeLater(() -> {
            JFrame jFrame = new JFrame("Isma 2015");
            jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    "", "X", "Y", dataSet, PlotOrientation.VERTICAL, true, true, false);
            XYPlot plot = (XYPlot) chart.getPlot();
            plot.setBackgroundPaint(Color.white);
            plot.setBackgroundAlpha(0.5f);
            chart.removeLegend();

            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();


/*                renderer.setBaseShapesVisible(true);
            renderer.setDrawOutlines(true);
            //        renderer.setUseFillPaint(true);
            //        renderer.setBaseFillPaint(Color.white);
            renderer.setSeriesStroke(0, new BasicStroke(3.0f));
            renderer.setSeriesOutlineStroke(0, new BasicStroke(2.0f));
            renderer.setSeriesShape(0, new Ellipse2D.Double(0,0,0,0));
            //renderer.setPaint(Color.RED);*/
            //plot.setDomainGridlinePaint(Color.gray);
            plot.setRangeGridlinePaint(Color.gray);

            ValueAxis rangeAxis = plot.getRangeAxis();
            Font bold = rangeAxis.getTickLabelFont().deriveFont(Font.BOLD);
            rangeAxis.setTickLabelFont(bold);

            ValueAxis domainAxis = plot.getDomainAxis();
            Font domainAxisFont = domainAxis.getTickLabelFont().deriveFont(Font.BOLD);
            domainAxis.setTickLabelFont(domainAxisFont);


            ChartPanel cp = new ChartPanel(chart);
            chartPanel.add(cp);

            jFrame.add(mainPanel);
            jFrame.pack();
            Dimension screeSize = Toolkit.getDefaultToolkit().getScreenSize();
            jFrame.setLocation(screeSize.width / 2 - jFrame.getSize().width / 2, screeSize.height / 2 - jFrame.getSize().height / 2);
            jFrame.setVisible(true);
        });
    }

    private XYDataset createChartDataSet(IntgResultPointProvider resultPointProvider, boolean simplify) {
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
                IntgResultPoint[] simplifiedResultPoints = simplify(resultPoints);
                processedResultPoints = Arrays.asList(simplifiedResultPoints); // TODO: optimize
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
                        ySeries.add(new LinkedList<>());
                        yLabels.add("y" + String.valueOf(i));
                    }
                    for (int i = 0; i < aeCount; i++) {
                        ySeries.add(new LinkedList<>());
                        yLabels.add("y" + String.valueOf(i));
                    }
                }

                int seriesIndex = 0;
                xValues.add(point.getX());
                for (int i = 0; i < deCount; i++) {
                    ySeries.get(seriesIndex).add(point.getYForDe()[i]);
                    seriesIndex++;
                }

                for (int i = 0; i < aeCount; i++) {
                    ySeries.get(seriesIndex).add(point.getRhs()[DaeSystem.RHS_AE_PART_IDX][i]);
                    seriesIndex++;
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

    private IntgResultPoint[] simplify(IntgResultPoint[] intgResultPoints) {
        IntgResultPointExtractor extractor = new IntgResultPointExtractor();
        Simplify<IntgResultPoint> simplify = new Simplify<>(new IntgResultPoint[0], extractor);
        IntgResultPoint[] simplified = simplify.simplify(intgResultPoints, 20f, false);
        return simplified;
    }

    private void setupUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        chartPanel = new JPanel();
        chartPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(chartPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }


    private static class IntgResultPointExtractor implements PointExtractor<IntgResultPoint> {
        private static final int FACTOR = 1000000;

        private int yForDeIndex = 0;
        private int rhsForDeIndex = 0;
        private int rhsForAeIndex = 0;

        @Override
        public double getX(IntgResultPoint intgResultPoint) {
            return intgResultPoint.getX() * FACTOR;
        }

        @Override
        public double getY(IntgResultPoint intgResultPoint) {
            Double yForDe = tryGetYForDe(intgResultPoint);
            if (yForDe != null) return yForDe;

            Double rhsForDe = tryGetRhsForDe(intgResultPoint);
            if (rhsForDe != null) return rhsForDe;

            Double rhsForAe = tryGetRhsForAe(intgResultPoint);
            if (rhsForAe != null) return rhsForAe;

            yForDeIndex = 0;
            rhsForDeIndex = 0;
            rhsForAeIndex = 0;
            return getY(intgResultPoint);
        }

        private Double tryGetRhsForAe(IntgResultPoint intgResultPoint) {
            double[] rhsForAe = intgResultPoint.getRhs()[DaeSystem.RHS_AE_PART_IDX];
            if (rhsForAeIndex < rhsForAe.length) {
                double y = rhsForAe[rhsForAeIndex];
                rhsForAeIndex++;
                return y;
            }
            return null;
        }

        private Double tryGetRhsForDe(IntgResultPoint intgResultPoint) {
            double[] rhsForDe = intgResultPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX];
            if (rhsForDeIndex < rhsForDe.length) {
                double y = rhsForDe[rhsForDeIndex];
                rhsForDeIndex++;
                return y;
            }
            return null;
        }

        private Double tryGetYForDe(IntgResultPoint intgResultPoint) {
            if (yForDeIndex < intgResultPoint.getYForDe().length) {
                double y = intgResultPoint.getYForDe()[yForDeIndex];
                yForDeIndex++;
                return y;
            }
            return null;
        }
    }
}
