package ru.nstu.isma.intg.api.calcmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
public class DaeSystemChangeSet implements Serializable {

    private Map<Integer, Change<DifferentialEquation>> differentialEquations = new HashMap<>();
    private Map<Integer, Change<AlgebraicEquation>> algebraicEquations = new HashMap<>();

    public Map<Integer, Change<DifferentialEquation>> getDifferentialEquations() {
        return differentialEquations;
    }

    public Map<Integer, Change<AlgebraicEquation>> getAlgebraicEquations() {
        return algebraicEquations;
    }

    public void add(DifferentialEquation deToChange, String author) {
        differentialEquations.put(deToChange.getIndex(), new Change<>(deToChange, author));
    }

    public void add(AlgebraicEquation aeToChange, String author) {
        algebraicEquations.put(aeToChange.getIndex(), new Change<>(aeToChange, author));
    }

    public boolean isEmpty() {
        return differentialEquations.isEmpty() && algebraicEquations.isEmpty();
    }

    public DaeSystem apply(DaeSystem daeSystem) {
        DaeSystem patchedDaeSystem = daeSystem.copy();

        DifferentialEquation[] diffEqs = patchedDaeSystem.getDifferentialEquations();
        differentialEquations.entrySet().stream()
                .forEach(deEntry -> {
                    int arrayIndex = patchedDaeSystem.mapDeIndexToArrayIndex(deEntry.getKey());
                    if (arrayIndex >= 0) {
                        diffEqs[arrayIndex] = deEntry.getValue().getValue();
                    }
                });

        AlgebraicEquation[] algEqs = patchedDaeSystem.getAlgebraicEquations();
        algebraicEquations.entrySet().stream()
                .filter(aeEntry -> daeSystem.containsAlgebraicEquation(aeEntry.getKey()))
                .forEach(aeEntry -> algEqs[aeEntry.getKey()] = aeEntry.getValue().getValue());

        return patchedDaeSystem;
    }

    public static class Change<T extends Serializable> implements Serializable {

        private final T value;
        private final String author;

        public Change(T value, String author) {
            this.value = value;
            this.author = author;
        }

        public T getValue() {
            return value;
        }

        public String getAuthor() {
            return author;
        }

    }

}
