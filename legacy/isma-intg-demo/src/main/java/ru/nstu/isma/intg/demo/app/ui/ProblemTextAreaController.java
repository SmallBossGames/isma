package ru.nstu.isma.intg.demo.app.ui;

import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;
import ru.nstu.isma.intg.demo.app.models.ProblemModel;
import ru.nstu.isma.intg.demo.app.models.ProblemType;
import ru.nstu.isma.intg.demo.app.services.CauchyProblemService;
import ru.nstu.isma.intg.demo.app.ui.utils.I18nUtils;

import javax.swing.*;
import java.util.Arrays;
import java.util.Locale;

/**
 * @author Mariya Nasyrova
 * @since 16.10.2014
 */
public class ProblemTextAreaController extends DemoFormControllerAdapter {

    private DemoFormController demoFormController;
    private JTextArea problemTextArea;
    private ProblemModel problemModel;
    private CauchyProblem cauchyProblem;

    public ProblemTextAreaController(DemoFormController demoFormController, JTextArea problemTextArea) {
        this.demoFormController = demoFormController;
        this.problemTextArea = problemTextArea;
    }

    @Override
    public void problemSaved(ProblemModel problem) {
        this.problemModel = problem;
        this.cauchyProblem = CauchyProblemService.getCauchyProblem(problem);

        problemTextArea.setText(getTextDescriptions(problemModel, cauchyProblem));
    }

    @Override
    public void i18nChanged(Locale newLocale) {
        super.i18nChanged(newLocale);
        if (problemModel != null && cauchyProblem != null) {
            problemTextArea.setText(getTextDescriptions(problemModel, cauchyProblem));
        }
    }

    private String getTextDescriptions(ProblemModel model, CauchyProblem cauchyProblem) {
        CauchyInitials initials = cauchyProblem.getCauchyInitials();

        StringBuilder builder = new StringBuilder();

        builder.append(String.format(I18nUtils.getMessage("demo.problem.nameTemplate"), I18nUtils.getProblemTypeName(model.getType())));
        builder.append("\n\n");

        String template = I18nUtils.getMessage("demo.problem.initialsTemplate");
        builder.append(String.format(template, initials.getStart(), initials.getEnd(), initials.getStepSize()));
        if (ProblemType.REACTION_DIFFUSION.equals(model.getType())) {
            builder.append("\n");
            builder.append("J: " + model.getReactionDiffusionParamJ());
            builder.append("\n");
            builder.append("K: " + model.getReactionDiffusionParamK());
        }
        builder.append("\n");
        builder.append("y0 = " + Arrays.toString(initials.getY0()));

        builder.append("\n\n\n");
        builder.append(I18nUtils.getMessage("demo.problem.odeHeader"));
        builder.append("\n");
        for (DifferentialEquation de : cauchyProblem.getDaeSystem().getDifferentialEquations()) {
            builder.append(de.getDescription());
            builder.append("\n");
        }

        return builder.toString();
    }

}
