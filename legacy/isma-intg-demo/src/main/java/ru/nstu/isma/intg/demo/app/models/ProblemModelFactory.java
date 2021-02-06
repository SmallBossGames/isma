package ru.nstu.isma.intg.demo.app.models;

public final class ProblemModelFactory {

    private ProblemModelFactory() {
    }

    public static ProblemModel createProblemWithDefaultValues(ProblemType type) {
        ProblemModel model = new ProblemModel();
        model.setType(type);

        switch (type) {
            case TWO_DIMENSIONAL:
                model.setIntervalStart(0.0);
                model.setIntervalEnd(50.0);
                model.setStep(0.5);
                break;
            case FOUR_DIMENSIONAL:
                model.setIntervalStart(0.0);
                model.setIntervalEnd(4.5);
                model.setStep(0.3);
                break;
            case REACTION_DIFFUSION:
                model.setIntervalStart(0.0);
                model.setIntervalEnd(1.0);
                model.setStep(0.001);
                model.setReactionDiffusionParamJ(2);
                model.setReactionDiffusionParamK(2);
                break;
            case LORENZ_SYSTEM:
                model.setIntervalStart(0.0);
                model.setIntervalEnd(10.0);
                model.setStep(0.01);
                break;
            case VAN_DER_POL:
                model.setIntervalStart(0.0);
                model.setIntervalEnd(10.0);
                model.setStep(0.01);
                break;
        }

        return model;
    }

}
