package ru.nstu.isma.intg.api;

import java.util.List;
import java.util.function.Consumer;

public interface IntgResultPointProvider {

    void read(Consumer<List<IntgResultPoint>> handler);

}
