package ru.nstu.isma.intg.core.methods.utils;

public class MathUtils {
    public static double max(double[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }

        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            double item = array[i];
            if (item > max) {
                max = item;
            }
        }
        return max;
    }

    public static double min(double[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }

        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            double item = array[i];
            if (item < min) {
                min = item;
            }
        }
        return min;
    }
}
