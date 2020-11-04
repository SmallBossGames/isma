package ru.nstu.isma.ui.minor.beditor;

import ru.nstu.isma.core.sim.controller.HybridSystemIntgResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Created by Bessonov Alex
 * on 10.03.2015.
 */
public class PrintResultService {

    public static String EQUATIONS = "EQUATIONS:";
    public static String POINTS = "POINTS:";
    public static String TIME = "TIME";
    public static String LABEL = "LABEL:";
    public static String SEPARATOR = "//===========================================";

    public static void print(String fileName, HybridSystemIntgResult result) throws FileNotFoundException {
        File file = new File(fileName);
        file.getParentFile().mkdirs();


        PrintWriter out = new PrintWriter(file);

// TODO: implement output to a file
//        int size = result.getTransposedY().length;
//        out.println(EQUATIONS + size);
//        if (size == 0)
//            return;
//
//        out.println(POINTS + result.getX().length);
//
//        out.println(SEPARATOR);
//        out.println(TIME);
//        for (int i = 0; i < result.getX().length; i++) {
//            out.printf(Locale.US, "%.6f", result.getX()[i]);
//            out.println();
//        }
//        for (int i = 0; i < size; i++) {
//            printEq(out, result.getMapper().code(i), result.getX(), result.getTransposedY()[i]);
//        }
        out.close();
    }

    private static void printEq(PrintWriter out, String label, double[] x, double[] y) {
        if (x.length != y.length)
            throw new RuntimeException("Incorrect size: x = " + x.length + ", y = " + y.length);

        out.println(SEPARATOR);
        out.println(LABEL + label);
        for (int i = 0; i < x.length; i++) {
            out.printf(Locale.US, "%.6f", y[i]);
            out.println();
        }
    }


}
