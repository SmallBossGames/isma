package ru.nstu.isma.core.sim.controller;

import ru.nstu.isma.core.sim.controller.gen.EquationIndexProvider;
import ru.nstu.isma.intg.api.IntgMetricData;
import ru.nstu.isma.intg.api.IntgResultPointProvider;

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
public class HybridSystemIntgResult {

    private EquationIndexProvider equationIndexProvider;
    private IntgMetricData metricData;
    private IntgResultPointProvider resultPointProvider;

    public EquationIndexProvider getEquationIndexProvider() {
        return equationIndexProvider;
    }

    public void setEquationIndexProvider(EquationIndexProvider equationIndexProvider) {
        this.equationIndexProvider = equationIndexProvider;
    }

    public IntgMetricData getMetricData() {
        return metricData;
    }

    public void setMetricData(IntgMetricData metricData) {
        this.metricData = metricData;
    }

    public IntgResultPointProvider getResultPointProvider() {
        return resultPointProvider;
    }

    public void setResultPointProvider(IntgResultPointProvider resultPointProvider) {
        this.resultPointProvider = resultPointProvider;
    }
}
