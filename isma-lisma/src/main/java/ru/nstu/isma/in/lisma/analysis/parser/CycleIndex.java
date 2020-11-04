package ru.nstu.isma.in.lisma.analysis.parser;

import java.util.*;

/**
 * Created by Alex on 08.12.2015.
 */

public class CycleIndex implements Iterable<Integer> {

    private String name;

    private Set<Integer> indexes = new HashSet<>();

    public CycleIndex(String name) {
        this.name = name;
    }

    public void addInterval(Integer from, Integer to) {
        for (int i = Math.min(from, to); i <= Math.max(from, to); i++) {
            indexes.add(i);
        }
    }

    public void addInterval(Integer point) {
        indexes.add(point);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new CycleIndexIterator(this);
    }

    private ArrayList<Integer> asList() {
        ArrayList<Integer> list = new ArrayList<>();
        indexes.forEach(list::add);
        return list;
    }

    private class CycleIndexIterator implements Iterator<Integer> {

        private CycleIndex idx;

        private List<Integer> indexes;

        private int current = -1;

        public CycleIndexIterator(CycleIndex idx) {
            this.idx = idx;
            indexes = idx.asList();

        }

        @Override
        public boolean hasNext() {
            return current < indexes.size() - 1;
        }

        @Override
        public Integer next() {
            return indexes.get(++current);
        }
    }
}
