package ru.nstu.isma.intg.demo.problems.lotkaVolterra;


import ru.nstu.isma.intg.demo.problems.lotkaVolterra.utils.Pair;

public class LVHabitatArea {

    private Pair<Double, Double> onX;
    private Pair<Double, Double> onZ;

    public LVHabitatArea() {
    }

    public LVHabitatArea(Pair<Double, Double> onX, Pair<Double, Double> onZ) {
        this.onX = onX;
        this.onZ = onZ;
    }

    public Pair<Double, Double> getOnX() {
        return onX;
    }

    public void setOnX(Pair<Double, Double> onX) {
        this.onX = onX;
    }

    public Pair<Double, Double> getOnZ() {
        return onZ;
    }

    public void setOnZ(Pair<Double, Double> onZ) {
        this.onZ = onZ;
    }
}
