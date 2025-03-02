package ru.nstu.isma.intg.api.calcmodel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Расчетная модель ГС.
 */
public class HybridSystem implements Serializable {

    // TODO: возможно вынести в HSM.
    public static final String INIT_PSEUDO_STATE = "initPseudo";

    /** Основная система уравнений */
    private final DaeSystem daeSystem;

    /** Состояния */
    private final Map<String, State> states;

    /** Псевдосостояния (например, предикаты условных блоков) */
    private final Map<String, State> pseudoStates;

    /** Текущее состояние */
    // private String currentState = HSM.INIT_STATE; // TODO
    private String currentState = "init";

    public HybridSystem(DaeSystem daeSystem, Map<String, State> states, Map<String, State> pseudoStates) {
        this.daeSystem = daeSystem;
        this.states = states;

        validatePseudoStates(pseudoStates);
        this.pseudoStates = pseudoStates;
    }

    public HybridSystem(HybridSystem hsToCopy) {
        this(hsToCopy.getDaeSystem(), hsToCopy.getStates(), hsToCopy.getPseudoStates());
    }

    public DaeSystem getDaeSystem() {
        return daeSystem;
    }

    public Map<String, State> getStates() {
        return states;
    }

    public Map<String, State> getPseudoStates() {
        return pseudoStates;
    }

    public HybridSystemChangeSet checkTransitions(double[] yForDe, double[][] rhs) {
        HybridSystemChangeSet changeSet = new HybridSystemChangeSet();

        State initPseudoState = pseudoStates.get(INIT_PSEUDO_STATE);
        if (initPseudoState != null) {
            initPseudoState.getGuards()
                    .stream()
                    .filter(g -> g.apply(yForDe, rhs))
                    .forEach(g -> addChanges(pseudoStates.get(g.getToStateName()), yForDe, changeSet));
        }

        Guard matchedGuard = states.get(currentState).getGuards().stream()
                .filter(guard -> guard.apply(yForDe, rhs))
                .findFirst().orElse(null);
        if (matchedGuard != null) {
            addChanges(states.get(matchedGuard.getToStateName()), yForDe, changeSet);
            currentState = matchedGuard.getToStateName();
        }

        return changeSet;
    }

    // TODO: private
    public HybridSystemChangeSet addChanges(State toState, double[] yForDe, HybridSystemChangeSet changeSet) {
        toState.getDifferentialEquations().stream().forEach(de -> changeSet.add(de, toState.getName()));
        toState.getAlgebraicEquations().stream().forEach(ae -> changeSet.add(ae, toState.getName()));
        toState.getSetters().stream().forEach(s -> changeSet.add(s.getIndex(), s, toState.getName()));
        return changeSet;
    }

    public State getCurrentState() {
        return states.get(currentState);
    }

    private void validatePseudoStates(Map<String, State> pseudoStates) {
        if (pseudoStates.isEmpty()) {
            return;
        }

        if (!pseudoStates.containsKey(INIT_PSEUDO_STATE)) {
            throw new IllegalArgumentException(
                    "Pseudo states must have special init state \"" + INIT_PSEUDO_STATE + "\"");
        }
    }

    public static class State {

        private final String name;
        private final List<DifferentialEquation> differentialEquations;
        private final List<AlgebraicEquation> algebraicEquations;
        private final List<Guard> guards;
        private final List<DifferentialEquation> setters;

        public State(String name, List<DifferentialEquation> differentialEquations,
                     List<AlgebraicEquation> algebraicEquations, List<Guard> guards) {
            this(name, differentialEquations, algebraicEquations, guards, null);
        }

        public State(String name, List<DifferentialEquation> differentialEquations,
                     List<AlgebraicEquation> algebraicEquations, List<Guard> guards, List<DifferentialEquation> setters) {
            this.name = name;
            this.differentialEquations = differentialEquations;
            this.algebraicEquations = algebraicEquations;
            this.guards = guards;
            this.setters = setters;
        }

        public String getName() {
            return name;
        }

        public List<DifferentialEquation> getDifferentialEquations() {
            return differentialEquations;
        }

        public List<AlgebraicEquation> getAlgebraicEquations() {
            return algebraicEquations;
        }

        public List<DifferentialEquation> getSetters() {
            return setters;
        }

        public List<Guard> getGuards() {
            return guards;
        }
    }

}
