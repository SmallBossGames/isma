package ru.nstu.isma.intg.api.calcmodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс, содержащий изменения для последующей модификации текущего состояния ГС.
 *
 * @author Maria Nasyrova
 * @since 05.10.2015
 */
public class HybridSystemChangeSet extends DaeSystemChangeSet {

    private Map<Integer, Change<DifferentialEquation>> initialValueSetters = new HashMap<>();

    public Map<Integer, Change<DifferentialEquation>> getInitialValueSetters() {
        return initialValueSetters;
    }

    public void add(int yIndex, DifferentialEquation setter, String author) {
        initialValueSetters.put(yIndex, new Change<>(setter, author));
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && initialValueSetters.isEmpty();
    }

}
