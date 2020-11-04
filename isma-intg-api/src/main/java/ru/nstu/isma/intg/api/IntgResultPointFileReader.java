package ru.nstu.isma.intg.api;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class IntgResultPointFileReader implements IntgResultPointProvider {

    private static final String COMMA = ",";

    private InputStream inputStream;
    private Integer xCount;
    private Integer yForDeCount;
    private Integer rhsForDeCount;
    private Integer rhsForAeCount;

    public void init(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream can't be null");
        }
        this.inputStream = inputStream;
    }

    @Override
    public void read(Consumer<List<IntgResultPoint>> handler) {
        if (inputStream == null) {
            throw new IllegalStateException("init(...) call expected before read(...)");
        }

        try(InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            // Считываем информацию о данных из первой строчки в файле
            String header = bufferedReader.readLine();
            initFromHeader(header);

            // Считываем результаты
            List<IntgResultPoint> resultPoints = new LinkedList<>();
            String pointString;
            while((pointString = bufferedReader.readLine()) != null){
                IntgResultPoint resultPoint = buildIntgResultPoint(pointString);
                resultPoints.add(resultPoint);
            }
            handler.accept(resultPoints);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void initFromHeader(String header) {
        String[] values = header.split(COMMA);

        if (values.length != 4) {
            throw new RuntimeException("Header expected to be size of 4: x count, yForDe count, rhsForDe count, rhsForAe count");
        }

        xCount = Integer.valueOf(values[0]);
        yForDeCount = Integer.valueOf(values[1]);
        rhsForDeCount = Integer.valueOf(values[2]);
        rhsForAeCount = Integer.valueOf(values[3]);
    }

    private IntgResultPoint buildIntgResultPoint(String pointString) {
        String[] values = pointString.split(COMMA);

        int expectedValueCount = xCount + yForDeCount + rhsForDeCount + rhsForAeCount;
        int actualValueCount = values.length;
        if (actualValueCount != expectedValueCount) {
            throw new RuntimeException("Expected value count per string is " + expectedValueCount + " but was " + actualValueCount);
        }

        double x = Double.valueOf(values[0]);
        double[] yForDe = new double[yForDeCount];
        double[] rhsForDe = new double[rhsForDeCount];
        double[] rhsForAe = new double[rhsForAeCount];

        int valueIndex = 1;

        for (int i = 0; i < yForDeCount; i++) {
            yForDe[i] = Double.valueOf(values[valueIndex]);
            valueIndex++;
        }

        for (int i = 0; i < rhsForDeCount; i++) {
            rhsForDe[i] = Double.valueOf(values[valueIndex]);
            valueIndex++;
        }

        for (int i = 0; i < rhsForAeCount; i++) {
            rhsForAe[i] = Double.valueOf(values[valueIndex]);
            valueIndex++;
        }

        double[][] rhs = new double[DaeSystem.RHS_PART_COUNT][];
        rhs[DaeSystem.RHS_DE_PART_IDX] = rhsForDe;
        rhs[DaeSystem.RHS_AE_PART_IDX] = rhsForAe;

        return new IntgResultPoint(x, yForDe, rhs);
    }

}
