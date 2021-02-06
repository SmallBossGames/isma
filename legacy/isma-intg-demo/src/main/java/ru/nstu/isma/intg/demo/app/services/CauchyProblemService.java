package ru.nstu.isma.intg.demo.app.services;

import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;
import ru.nstu.isma.intg.demo.app.models.ProblemModel;
import ru.nstu.isma.intg.demo.problems.*;

/**
 * @author Mariya Nasyrova
 * @since 16.10.2014
 */
public class CauchyProblemService {

    public static CauchyProblem getCauchyProblem(ProblemModel problem) {
        CauchyProblem cauchyProblem = createCauchyProblem(problem);
        cauchyProblem.getCauchyInitials().setStart(problem.getIntervalStart());
        cauchyProblem.getCauchyInitials().setEnd(problem.getIntervalEnd());
        cauchyProblem.getCauchyInitials().setStepSize(problem.getStep());
        return cauchyProblem;
    }

    // TODO: add creation of REACTION_DIFFUSION problem.
    private static CauchyProblem createCauchyProblem(ProblemModel problem) {
        switch (problem.getType()) {
            case TWO_DIMENSIONAL:
                return new TwoDimensionalCauchyProblem();
            case FOUR_DIMENSIONAL:
                return new FourDimensionalCauchyProblem();
            case REACTION_DIFFUSION:
                return new ReactionDiffusionCauchyProblem(problem.getReactionDiffusionParamJ(), problem.getReactionDiffusionParamK());
            case LORENZ_SYSTEM:
                return new LorenzSystem();
            case VAN_DER_POL:
                return new VanDerPolOscillator();
            default:
                throw new IllegalStateException("Unknown type of cauchy problem: " + problem.getType());
        }
    }
}
