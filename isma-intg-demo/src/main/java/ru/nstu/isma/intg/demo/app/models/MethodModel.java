package ru.nstu.isma.intg.demo.app.models;

import ru.nstu.isma.intg.lib.IntgMethodType;

/**
 * @author Mariya Nasyrova
 * @since 13.10.14
 */
public class MethodModel {

    public static final String INTG_SERVER_HOST = "localhost";
    public static final int INTG_SERVER_PORT = 7890;

    private IntgMethodType type;
    private boolean isAccurate;
    private boolean isStable;
    private boolean isParallel;
    private double accuracy;
    private String intgServerHost = INTG_SERVER_HOST;
    private int intgServerPort = INTG_SERVER_PORT;


    public MethodModel() {
    }

    public MethodModel(MethodModel source) {
        this.type = source.type;
        this.isAccurate = source.isAccurate;
        this.isStable = source.isStable;
        this.isParallel = source.isParallel;
        this.intgServerHost = source.intgServerHost;
        this.intgServerPort = source.intgServerPort;
    }

    public IntgMethodType getType() {
        return type;
    }

    public void setType(IntgMethodType type) {
        this.type = type;
    }

    public boolean isAccurate() {
        return isAccurate;
    }

    public void setAccurate(boolean isAccurate) {
        this.isAccurate = isAccurate;
    }

    public boolean isStable() {
        return isStable;
    }

    public void setStable(boolean isStable) {
        this.isStable = isStable;
    }

    public boolean isParallel() {
        return isParallel;
    }

    public void setParallel(boolean isParallel) {
        this.isParallel = isParallel;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getIntgServerHost() {
        return intgServerHost;
    }

    public void setIntgServerHost(String intgServerHost) {
        this.intgServerHost = intgServerHost;
    }

    public int getIntgServerPort() {
        return intgServerPort;
    }

    public void setIntgServerPort(int intgServerPort) {
        this.intgServerPort = intgServerPort;
    }
}
