package ru.nstu.isma.ui.minor.beditor;

import ru.nstu.isma.intg.api.IntgResultPointProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static ru.nstu.isma.ui.minor.beditor.PrintResultService.*;

/**
 * Created by Bessonov Alex
 * on 10.03.2015.
 */
public class SimpleIntgRes {
    public double[] x;
    public Map<String, double[]> y = new HashMap<>();

    public SimpleIntgRes(String fileName) {
        try {
            read(fileName);
        } catch (IOException e) {
            new RuntimeException(e);
        }
    }

    private IntgResultPointProvider read(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        Integer cnt = readIntParam(br, EQUATIONS, "equation count expected!");
        Integer points = readIntParam(br, POINTS, "point count expected!");
        if (cnt == 0)
            return null;

        String line;

        while ((line = br.readLine()) != null) {
            if (line.contains("//"))
                continue;
            if (line.contains(TIME)) {
                x = readPointList(br, points);
            }

            if (line.contains(LABEL)) {
                String code = line.replace(LABEL, "").trim();
                double[] tmpY = readPointList(br, points);
                y.put(code, tmpY);
            }
        }

        // TODO
        return null;
    }

    private Integer readIntParam(BufferedReader br, String param, String errMsg) throws IOException {
        String line = br.readLine();
        if (line.contains(param)) {
            line = line.replace(param, "").trim();
            return Integer.valueOf(line);
        } else throw new RuntimeException("Incorrect output file: " + errMsg);
    }

    private double[] readPointList(BufferedReader br, int size) throws IOException {
        String line;
        double[] res = new double[size];
        for (int i = 0; (line = br.readLine()) != null && i < size; i++) {
            res[i] = Double.valueOf(line);
        }
        return res;
    }
}
