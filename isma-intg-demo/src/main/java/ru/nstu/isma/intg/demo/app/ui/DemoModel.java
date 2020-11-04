package ru.nstu.isma.intg.demo.app.ui;

import ru.nstu.isma.intg.demo.app.models.MethodModel;
import ru.nstu.isma.intg.demo.app.models.ProblemModel;
import ru.nstu.isma.intg.demo.app.models.ProblemType;
import ru.nstu.isma.intg.lib.IntgMethodType;

import java.util.Locale;

/**
 * @author Mariya Nasyrova
 * @since 13.10.14
 */
public class DemoModel {

    private ProblemType[] availableProblemTypes = new ProblemType[0];
    private ProblemType selectedProblem;
    private ProblemModel savedProblem;

    private IntgMethodType[] methods = new IntgMethodType[0];
    private IntgMethodType selectedMethod;
    private MethodModel savedMethod;

    private String problemDescription;
    private String results;

    private Locale locale;

    public DemoModel() {
    }

    public ProblemType[] getAvailableProblemTypes() {
        return availableProblemTypes;
    }

    public void setAvailableProblemTypes(ProblemType[] problemTypes) {
        this.availableProblemTypes = problemTypes;
    }

    public ProblemType getSelectedProblem() {
        return selectedProblem;
    }

    public void setSelectedProblem(ProblemType selectedProblem) {
        this.selectedProblem = selectedProblem;
    }

    public ProblemModel getSavedProblem() {
        return savedProblem;
    }

    public void setSavedProblem(ProblemModel savedProblem) {
        this.savedProblem = savedProblem;
    }

    public IntgMethodType[] getMethods() {
        return methods;
    }

    public void setMethods(IntgMethodType[] methods) {
        this.methods = methods;
    }

    public IntgMethodType getSelectedMethod() {
        return selectedMethod;
    }

    public void setSelectedMethod(IntgMethodType selectedMethodType) {
        this.selectedMethod = selectedMethodType;
    }

    public MethodModel getSavedMethod() {
        return savedMethod;
    }

    public void setSavedMethod(MethodModel savedMethod) {
        this.savedMethod = savedMethod;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void clear() {
        availableProblemTypes = new ProblemType[0];
        selectedProblem = null;
        savedProblem = null;

        methods = new IntgMethodType[0];
        selectedMethod = null;
        savedMethod = null;

        problemDescription = null;
        results = null;

        locale = new Locale("ru");
    }
}
