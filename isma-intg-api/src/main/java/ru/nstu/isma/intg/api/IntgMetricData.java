package ru.nstu.isma.intg.api;

/**
 * @author Maria
 * @since 03.04.2016
 */
public class IntgMetricData {

    private long startTime;
    private long endTime;

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getSimulationTime() {
        return endTime - startTime;
    }
}
