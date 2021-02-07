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
public abstract class DemoFormControllerAdapter implements DemoFormControllerListener {

    @Override
    public void formOpened(ProblemType[] problemTypes, IntgMethodType[] methodTypes) {

    }

    @Override
    public void problemSelected(ProblemType problemType) {

    }

    @Override
    public void problemSaved(ProblemModel problem) {

    }

    @Override
    public void methodSelected(IntgMethodType methodType) {

    }

    @Override
    public void methodSaved(MethodModel method) {

    }

    @Override
    public void simulationStarted() {

    }

    @Override
    public void simulationFinished(IntgMetricData metricData, IntgResultPointProvider resultPointProvider) {

    }

    @Override
    public void i18nChanged(Locale newLocale) {

    }
}
