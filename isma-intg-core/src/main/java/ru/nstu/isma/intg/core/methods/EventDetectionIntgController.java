package ru.nstu.isma.intg.core.methods;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.EventFunction;
import ru.nstu.isma.intg.api.calcmodel.EventFunctionGroup;
import ru.nstu.isma.intg.api.methods.IntgController;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.core.methods.utils.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Maria
 * @since 18.05.2016
 */
public class EventDetectionIntgController extends IntgController {

    // Значение, которое используется при вычислении частных производных.
    private static final double INCREMENT = 0.00001;

    private final double gamma;

    public EventDetectionIntgController(double gamma) {
        if (gamma < 0.0 || gamma > 1.0) {
            throw new IllegalArgumentException("Gamma should be >= 0 and <= 1.");
        }

        this.gamma = gamma;
    }

    public EventDetectionIntgController(double gamma, boolean isEnabled) {
        if (gamma < 0.0 || gamma > 1.0) {
            throw new IllegalArgumentException("Gamma should be >= 0 and <= 1.");
        }

        this.gamma = gamma;
        this.setEnabled(isEnabled);
    }

    public double getGamma() {
        return gamma;
    }

    public double predictNextStep(IntgPoint intgPoint, List<EventFunctionGroup> eventFunctionGroups) {

        double[] steps = new double[eventFunctionGroups.size()];
        int i = 0;

        for (EventFunctionGroup eventFunctionGroup : eventFunctionGroups) {
            steps[i] = predictNextStep(intgPoint, eventFunctionGroup);
            i++;
        }

        return MathUtils.min(steps);
    }

    public double predictNextStep(IntgPoint intgPoint, EventFunctionGroup eventFunctionGroup) {
        ArrayList<EventFunction> eventFunctions = eventFunctionGroup.getEventFunctions();
        int eventFunctionCount = eventFunctions.size();

        double[] steps = new double[eventFunctionCount];

        for (int i = 0; i < eventFunctionCount; i++) {
            EventFunction eventFunction = eventFunctions.get(i);

            // Вычисляем значение событийной функции для y.
            double g = eventFunction.apply(intgPoint.getY(), intgPoint.getRhs());

            if (g > 0) {
                steps[i] = Double.MAX_VALUE;
            } else {
                double DgDyAndRhsProduct = calculateDgDyAndRhsProduct(intgPoint, eventFunction, g);
                double DgDt = calculateDgDt(intgPoint, eventFunction, g);

                double step = (gamma - 1.0) * g / (DgDyAndRhsProduct + DgDt);
                steps[i] = step > 0 ? step : Double.MAX_VALUE;
            }
        }

        double predictedStep;
        switch (eventFunctionGroup.getStepChoiceRule()) {
            case MAX:
                predictedStep = MathUtils.max(steps);
                break;
            case MIN:
                predictedStep = MathUtils.min(steps);
                break;
            default:
                predictedStep = steps[0]; // TODO: для одного значения переделать
                break;
        }

        return predictedStep > 0 ? predictedStep : intgPoint.getNextStep(); // TODO: отрефакторить, более не актуально
    }

    private double calculateDgDyAndRhsProduct(IntgPoint intgPoint, EventFunction eventFunction, double g) {
        double[] y = intgPoint.getY(); // Массив y на текущем шаге интегрирования.
        int length = y.length;

        double[] yInc = new double[length]; // Массив y с приращенными значениями.
        for (int i = 0; i < length; i++) {
            yInc[i] = y[i] + INCREMENT;
        }

        double[] yToInc = Arrays.copyOf(y, length); // Массив y для приращения.

        double[] DgDy = new double[length]; // Частная производная от событийной функции по переменной y.
        double DgDyAndRhsProduct = 0.0; // Произведение DgDy на правую часть.

        // В цикле делаем рассчет, выполняя приращение значений в y по одному.
        for (int i = 0; i < length; i++) {
            yToInc[i] = yInc[i]; // Заменяем одно значение на приращенное.

            double gInc = eventFunction.apply(yToInc, intgPoint.getRhs());
            DgDy[i] = (gInc - g) / INCREMENT;

            DgDyAndRhsProduct += DgDy[i] * intgPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX][i];

            yToInc[i] = y[i]; // Возвращаем значение назад.
        }

        return DgDyAndRhsProduct;
    }

    private double calculateDgDt(IntgPoint intgPoint, EventFunction eventFunction, double g) {
        double gInc = eventFunction.apply(intgPoint.getY(), intgPoint.getRhs());
        double DgDt = (gInc - g) / INCREMENT;
        return DgDt;
    }

}
