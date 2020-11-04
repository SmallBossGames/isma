package ru.nstu.isma.intg.demo.app.services;

import ru.nstu.isma.intg.api.methods.AccuracyIntgController;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.demo.app.models.MethodModel;
import ru.nstu.isma.intg.lib.IntgMethodLibrary;

/**
 * @author Mariya Nasyrova
 * @since 17.10.2014
 */
public class IntgMethodService {

    public static IntgMethod getIntgMethod(MethodModel method) {
        IntgMethod intgMethod = IntgMethodLibrary.createMethod(method.getType());

        AccuracyIntgController accuracyController = intgMethod.getAccuracyController();
        if (accuracyController != null) {
            accuracyController.setEnabled(method.isAccurate());
            if (method.isAccurate()) {
                accuracyController.setAccuracy(method.getAccuracy());
            }
        }

        if (intgMethod.getStabilityController() != null) {
            intgMethod.getStabilityController().setEnabled(method.isStable());
        }

        return intgMethod;
    }

}
