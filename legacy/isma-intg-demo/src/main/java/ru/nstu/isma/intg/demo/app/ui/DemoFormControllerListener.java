package ru.nstu.isma.intg.demo.app.ui;

import ru.nstu.isma.intg.api.IntgMetricData;
import ru.nstu.isma.intg.api.IntgResultPointProvider;
import ru.nstu.isma.intg.demo.app.models.MethodModel;
import ru.nstu.isma.intg.demo.app.models.ProblemModel;
import ru.nstu.isma.intg.demo.app.models.ProblemType;
import ru.nstu.isma.intg.lib.IntgMethodType;

import java.util.Locale;

/**
 * @author Mariya Nasyrova
 * @since 13.10.14
 */
public interface DemoFormControllerListener {

    void formOpened(ProblemType[] problemType, IntgMethodType[] methodTypes);

    void problemSelected(ProblemType problemType);

    void problemSaved(ProblemModel problem);

    void methodSelected(IntgMethodType methodType);

    void methodSaved(MethodModel method);

    void simulationStarted();

    void simulationFinished(IntgMetricData metricData, IntgResultPointProvider resultPointProvider);

    void i18nChanged(Locale newLocale);

}
