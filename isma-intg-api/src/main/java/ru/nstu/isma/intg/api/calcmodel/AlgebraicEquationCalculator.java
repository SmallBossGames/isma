package ru.nstu.isma.intg.api.calcmodel;

/**
 * @author Maria Nasyrova
 * @since 04.10.2015
 */
public class AlgebraicEquationCalculator implements AlgebraicEquationResultProvider {

    private final double[] y;
    private final Double[] calcResults;
    private final AlgebraicEquation[] algebraicEquations;

    public AlgebraicEquationCalculator(double[] y, AlgebraicEquation[] algebraicEquations) {
        this.y = y;
        this.calcResults = new Double[algebraicEquations.length];
        this.algebraicEquations = algebraicEquations;
    }

    @Override
    public double getValue(int index) {
        if (calcResults[index] == null) {
            calcResults[index] = algebraicEquations[index].apply(y, this);
        }
        return calcResults[index];
    }

    public double[] getValues() {
        double[] values = new double[calcResults.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = getValue(i);
        }
        return values;
    }
}
