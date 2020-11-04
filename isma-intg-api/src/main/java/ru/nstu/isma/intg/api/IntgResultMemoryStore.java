package ru.nstu.isma.intg.api;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class IntgResultMemoryStore implements Consumer<IntgResultPoint>, IntgResultPointProvider {

    private final List<IntgResultPoint> results = new LinkedList<>();

    @Override
    public void accept(IntgResultPoint intgResultPoint) {
        results.add(intgResultPoint);
    }

    @Override
    public void read(Consumer<List<IntgResultPoint>> handler) {
        handler.accept(results);
    }

}
