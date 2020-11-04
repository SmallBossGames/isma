package ru.nstu.isma.ui.common;

import ru.nstu.isma.core.sim.controller.HybridSystemIntgResult;
import ru.nstu.isma.core.sim.controller.gen.EquationIndexProvider;
import ru.nstu.isma.intg.api.IntgResultPoint;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;

import java.io.*;

public class ExportService {

    public static final String EXPORT_FILEPATH = ".\\export.csv";
    private static final String COMMA_AND_SPACE = ", ";

    public static void toFile(HybridSystemIntgResult result) {
        File file = new File(EXPORT_FILEPATH);

        try (Writer writer = new FileWriter(file)) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
                String header = buildHeader(result);
                bufferedWriter.write(header);

                String points = buildPoints(result);
                bufferedWriter.write(points);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String buildHeader(HybridSystemIntgResult result) {
        StringBuilder header = new StringBuilder();

        // x
        header.append("x")
                .append(COMMA_AND_SPACE);

        EquationIndexProvider eqIdxProvider = result.getEquationIndexProvider();

        // Дифференциальные переменные
        int deCount = eqIdxProvider.getDifferentialEquationCount();
        for (int i = 0; i < deCount; i++) {
            header.append(eqIdxProvider.getDifferentialEquationCode(i))
                    .append(COMMA_AND_SPACE);
        }

        // Алгебраические переменные
        int aeCount = eqIdxProvider.getAlgebraicEquationCount();
        for (int i = 0; i < aeCount; i++) {
            header.append(eqIdxProvider.getAlgebraicEquationCode(i))
                    .append(COMMA_AND_SPACE);
        }

        // Правая часть
        for (int i = 0; i < deCount; i++) {
            header.append("f")
                    .append(String.valueOf(i))
                    .append(COMMA_AND_SPACE);
        }

        // Удаляем последний пробел и запятую и заменяем на перенос строки
        header.delete(header.length() - 2, header.length())
                .append("\n");

        return header.toString();
    }

    private static String buildPoints(HybridSystemIntgResult result) {
        StringBuilder points = new StringBuilder();

        if (result.getResultPointProvider() != null) {
            result.getResultPointProvider().read(intgResultPoints -> {
                for (IntgResultPoint p : intgResultPoints) {
                    // x
                    points.append(p.getX())
                            .append(COMMA_AND_SPACE);

                    // Дифференциальные переменные
                    for (double yForDe : p.getYForDe()) {
                        points.append(yForDe)
                                .append(COMMA_AND_SPACE);
                    }

                    // Алгебраические переменные
                    for (double yForAe : p.getRhs()[DaeSystem.RHS_AE_PART_IDX]) {
                        points.append(yForAe)
                                .append(COMMA_AND_SPACE);
                    }

                    // Правая часть
                    for (double f : p.getRhs()[DaeSystem.RHS_DE_PART_IDX]) {
                        points.append(f)
                                .append(COMMA_AND_SPACE);
                    }

                    // Удаляем последний пробел и запятую и заменяем на перенос строки
                    points.delete(points.length() - 2, points.length())
                            .append("\n");
                }
            });
        }

        return points.toString();
    }

}
