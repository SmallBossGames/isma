package ru.nstu.isma.intg.core.solvers.utils;

import java.util.LinkedList;
import java.util.List;

public class DataBalanceUtils {

    public static List<Range> getRankDataPortionRanges(int dataPortionCount, int rankCount) {
        List<Range> ranges = new LinkedList<>();

        int[] portionSizes = getRankPortionSizes(dataPortionCount, rankCount);
        for (int i = 0; i < portionSizes.length; i++) {
            int start = 0;
            int end = 0;
            if (portionSizes[i] != 0) {
                if (!ranges.isEmpty()) {
                    Range prevRange = ranges.get(i - 1);
                    start = prevRange.getEnd();
                }
                end = start + portionSizes[i];
            }
            Range range = new Range(start, end);
            ranges.add(range);
        }

        return ranges;
    }

    private static int[] getRankPortionSizes(int dataPortionCount, int rankCount) {
        int[] portionSizes = new int[rankCount];

        if (dataPortionCount == 0 && rankCount == 0) {return portionSizes;}

        if (dataPortionCount >= rankCount) {
            int portionSize = dataPortionCount / rankCount;
            for (int i = 0; i < rankCount; i++) {
                portionSizes[i] = portionSize;
            }
        }

        int remainPortions = dataPortionCount % rankCount;
        if (remainPortions > 0) {
            int[] remainPortionSizes = balanceByRoundRobin(remainPortions, rankCount);
            for (int i = 0; i < remainPortionSizes.length; i++) {
                portionSizes[i] += remainPortionSizes[i];
            }
        }

        return portionSizes;
    }

    private static int[] balanceByRoundRobin(int dataPortionCount, int rankCount) {
        int[] portionSizes = new int[rankCount];
        int rank = 0;
        for (int i = 0; i < dataPortionCount; i++) {
            if (rank >= rankCount - 1) {
                rank = 0;
            }
            portionSizes[rank]++;
            rank++;
        }
        return portionSizes;
    }

    public static class Range {
        private final int start;
        private final int end;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public int size() {
            return Math.abs(end - start);
        }
    }
}
