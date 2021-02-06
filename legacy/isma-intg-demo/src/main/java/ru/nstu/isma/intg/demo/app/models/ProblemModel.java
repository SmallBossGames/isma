package ru.nstu.isma.intg.demo.app.models;

/**
 * @author Mariya Nasyrova
 * @since 13.10.14
 */
public class ProblemModel {

    private ProblemType type;
    private double intervalStart;
    private double intervalEnd;
    private double step;
    private int reactionDiffusionParamJ;
    private int reactionDiffusionParamK;

    public ProblemModel() {
    }

    public ProblemModel(ProblemType type, double intervalStart, double intervalEnd, double step, double accuracy) {
        this.type = type;
        this.intervalStart = intervalStart;
        this.intervalEnd = intervalEnd;
        this.step = step;
    }

    public ProblemModel(ProblemModel source) {
        this.type = source.type;
        this.intervalStart = source.intervalStart;
        this.intervalEnd = source.intervalEnd;
        this.step = source.step;
        this.reactionDiffusionParamJ = source.reactionDiffusionParamJ;
        this.reactionDiffusionParamK = source.reactionDiffusionParamK;
    }

    public ProblemType getType() {
        return type;
    }

    public void setType(ProblemType type) {
        this.type = type;
    }

    public double getIntervalStart() {
        return intervalStart;
    }

    public void setIntervalStart(double intervalStart) {
        this.intervalStart = intervalStart;
    }

    public double getIntervalEnd() {
        return intervalEnd;
    }

    public void setIntervalEnd(double intervalEnd) {
        this.intervalEnd = intervalEnd;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public int getReactionDiffusionParamJ() {
        return reactionDiffusionParamJ;
    }

    public void setReactionDiffusionParamJ(int reactionDiffusionParamJ) {
        this.reactionDiffusionParamJ = reactionDiffusionParamJ;
    }

    public int getReactionDiffusionParamK() {
        return reactionDiffusionParamK;
    }

    public void setReactionDiffusionParamK(int reactionDiffusionParamK) {
        this.reactionDiffusionParamK = reactionDiffusionParamK;
    }
}
