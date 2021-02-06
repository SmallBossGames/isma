package ru.nstu.isma.core.sim.fdm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * by
 * Bessonov Alex.
 * Date: 13.12.13 Time: 1:43
 * Итератор перебора всех вариантов индекса
 */

public class FDMIndexIterator {
    private ArrayList<FDMIndexedApxVar> indexes = new ArrayList<FDMIndexedApxVar>();

    private Map<String, FDMIndexedApxVar> nameMapping = new HashMap<String, FDMIndexedApxVar>();

    boolean readyToFight = false;

    public void addIndex(FDMIndexedApxVar v) {
        indexes.add(v);
        nameMapping.put(v.getCode(), v);
        readyToFight = false;
    }

    public List<FDMIndexedApxVar> start() {
        for (FDMIndexedApxVar v : indexes) {
            v.setIndex(1);
        }
        readyToFight = true;
        return indexes;
    }

    public List<FDMIndexedApxVar> next() {
        if (!readyToFight) {
            throw new RuntimeException("Iterator not ready! Call 'start()' method before!");
        }
        // предотвращаем выход за границу
        if (atEnd()) {
            throw new RuntimeException("Iterator at the end!");
        }
        int curIdx = 0;
        boolean success = false;
        FDMIndexedApxVar cur;
        while(!success) {
            cur = indexes.get(curIdx);
            cur.inc();
            if (!cur.isValid()) {
                cur.setIndex(1);
                curIdx++;
            } else {
                success = true;
            }
        }
        return indexes;
    }

    public ArrayList<FDMIndexedApxVar> get() {
        return indexes;
    }

    public FDMIndexedApxVar getIndex(String code) {
        return nameMapping.get(code);
    }

    public boolean atEnd() {
        for (FDMIndexedApxVar v : indexes) {
            if (!v.isMax()) {
                return false;
            }
        }
        return true;
    }

}
