package ru.nstu.isma.intg.demo.problems.lotkaVolterra;

public class LVBoundaryCondition {

    private int bottomBoundary;
    private int bottomBounder;
    private int topBoundary;
    private int topBounder;

    public LVBoundaryCondition(int bottomBoundary, int bottomBounder, int topBoundary, int topBounder) {
        this.bottomBoundary = bottomBoundary;
        this.bottomBounder = bottomBounder;
        this.topBoundary = topBoundary;
        this.topBounder = topBounder;
    }

    public int getBoundedValue(int value) {
        if (value == bottomBoundary) { return bottomBounder; }
        if (value == topBoundary) { return topBounder; }
        return value;
    }
}
