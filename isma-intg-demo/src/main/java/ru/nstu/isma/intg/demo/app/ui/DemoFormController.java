package ru.nstu.isma.intg.demo.app.ui;

import ru.nstu.isma.intg.api.IntgMetricData;
import ru.nstu.isma.intg.api.IntgResultMemoryStore;
import ru.nstu.isma.intg.api.IntgResultPointProvider;
import ru.nstu.isma.intg.demo.app.models.*;
import ru.nstu.isma.intg.demo.app.services.SimulationService;
import ru.nstu.isma.intg.demo.app.ui.chart.ChartForm;
import ru.nstu.isma.intg.demo.app.ui.method.MethodForm;
import ru.nstu.isma.intg.demo.app.ui.method.MethodFormControllerAdapter;
import ru.nstu.isma.intg.demo.app.ui.problem.ProblemForm;
import ru.nstu.isma.intg.demo.app.ui.problem.ProblemFormControllerAdapter;
import ru.nstu.isma.intg.demo.app.ui.utils.I18nUtils;
import ru.nstu.isma.intg.lib.IntgMethodType;

import java.util.Locale;

/**
 * @author Mariya Nasyrova
 * @since 13.10.14
 */
public class DemoFormController extends FormController<DemoFormControllerListener> {

    private DemoForm demoForm;
    private DemoModel demoModel;
    private long simulationStartTime;

    public DemoFormController(DemoForm demoForm) {
        this.demoForm = demoForm;
        this.demoModel = new DemoModel();
    }

    public long getSimulationStartTime() {
        return simulationStartTime;
    }

    public void formOpened(ProblemType[] availableProblemTypes, IntgMethodType[] methods) {
        demoModel.clear();
        demoModel.setAvailableProblemTypes(availableProblemTypes);
        demoModel.setMethods(methods);
        for (DemoFormControllerListener listener : getListeners()) {
            listener.formOpened(availableProblemTypes, methods);
        }
    }

    public void problemSelected(ProblemType problemType) {
        demoModel.setSelectedProblem(problemType);

        for (DemoFormControllerListener listener : getListeners()) {
            listener.problemSelected(problemType);
        }

        ProblemForm problemForm = new ProblemForm(demoForm);
        problemForm.getFormController().addListener(new ProblemFormControllerAdapter() {
            @Override
            public void problemSaved(ProblemModel problem) {
                DemoFormController.this.problemSaved(problem);
            }
        });

        ProblemModel problemModel = ProblemModelFactory.createProblemWithDefaultValues(problemType);
        problemForm.show(problemModel);
    }

    public void problemSaved(ProblemModel problem) {
        demoModel.setSavedProblem(problem);
        for (DemoFormControllerListener listener : getListeners()) {
            listener.problemSaved(problem);
        }
    }

    public void methodSelected(IntgMethodType methodType) {
        demoModel.setSelectedMethod(methodType);

        for (DemoFormControllerListener listener : getListeners()) {
            listener.methodSelected(methodType);
        }

        MethodModel methodModel = MethodModelFactory.createMethodModel(methodType);

        MethodForm methodForm = new MethodForm(demoForm);
        methodForm.getFormController().addListener(new MethodFormControllerAdapter() {
            @Override
            public void methodSaved(MethodModel method) {
                DemoFormController.this.methodSaved(method);
            }
        });
        methodForm.show(methodModel);
    }

    public void methodSaved(MethodModel method) {
        demoModel.setSavedMethod(method);
        for (DemoFormControllerListener listener : getListeners()) {
            listener.methodSaved(method);
        }
    }

    public void simulationStarted() {
        simulationStartTime = System.currentTimeMillis();
        for (DemoFormControllerListener listener : getListeners()) {
            listener.simulationStarted();
        }

        IntgResultMemoryStore memoryStore = new IntgResultMemoryStore();
        IntgMetricData metricData = SimulationService.solve(
                demoModel.getSavedProblem(), demoModel.getSavedMethod(), memoryStore);
        simulationFinished(metricData, memoryStore);
    }

    public void simulationFinished(IntgMetricData metricData, IntgResultPointProvider resultPointProvider) {
        for (DemoFormControllerListener listener : getListeners()) {
            listener.simulationFinished(metricData, resultPointProvider);
        }

        ChartForm chartForm = new ChartForm(demoForm);
        chartForm.show(resultPointProvider);
    }

    public void i18nChanged(Locale newLocale) {
        demoModel.setLocale(newLocale);

        for (DemoFormControllerListener listener : getListeners()) {
            listener.i18nChanged(newLocale);
        }

        demoForm.getProblemLabel().setText(I18nUtils.getMessage("demo.problem.label"));
        demoForm.getResultLabel().setText(I18nUtils.getMessage("demo.result.label"));
    }

}
