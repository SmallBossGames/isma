package ru.nstu.isma.core.common;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Bessonov Alex
 * on 12.10.2014.
 */
public class SimulationResult {
    protected ArrayList<Double> x = new ArrayList<>();
    protected Map<String, ArrayList<Double>> y = new HashMap<>();

    public void addEquation(String name) {
        if (y.containsKey(name))
            return;
        y.put(name, new ArrayList<>());
    }

    public void addX(Double x) {
        this.x.add(x);
    }

    public void addY(String name, Double y) {
        assertEquationContains(name);
        this.y.get(name).add(y);
    }

    public List<Point> getPoints(String name) {
        assertEquationContains(name);
        assertResultCorrect(name);
        List<Point> res = new LinkedList<>();
        for (int i = 0; i < x.size(); i++)
            res.add(new Point(x.get(i), y.get(name).get(i)));
        return res;
    }

    public List<String> equations() {
        return y.keySet().stream().collect(Collectors.toList());
    }

    private void assertEquationContains(String name) {
        if (!this.y.containsKey(name))
            throw new RuntimeException("SimulationResult don't contain equation " + name);
    }

    private void assertResultCorrect(String name) {
        if (y.get(name).size() != x.size())
            throw new RuntimeException("SimulationResult have different size for equation " + name);
    }
}
