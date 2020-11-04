package ru.nstu.isma.intg.tests;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.api.*;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.core.solvers.DefaultCauchyProblemSolver;
import ru.nstu.isma.intg.core.solvers.DefaultDaeSystemStepSolver;
import ru.nstu.isma.intg.demo.problems.ReactionDiffusionCauchyProblem;
import ru.nstu.isma.intg.lib.rungeKutta.rk22.Rk2IntgMethod;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileResultStoreTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private CauchyProblem cauchyProblem;
    private IntgMethod intgMethod;

    @Before
    public void beforeTest() throws Exception {
        intgMethod = new Rk2IntgMethod();
        cauchyProblem = new ReactionDiffusionCauchyProblem(2, 2);
        cauchyProblem.getCauchyInitials().setStart(0.0);
        cauchyProblem.getCauchyInitials().setEnd(1.0);
        cauchyProblem.getCauchyInitials().setStepSize(0.1);
    }

    @Test
    public void test() throws Exception {
        DefaultCauchyProblemSolver cauchyProblemSolver = new DefaultCauchyProblemSolver();
        DefaultDaeSystemStepSolver stepSolver = new DefaultDaeSystemStepSolver(intgMethod, cauchyProblem.getDaeSystem());

        IntgResultMemoryStore memoryDataStorage = new IntgResultMemoryStore();
        IntgMetricData metricData = cauchyProblemSolver.solve(cauchyProblem, stepSolver, memoryDataStorage);
        logger.info("Finished simulation using an in-memory result store. Total time: {} ms.",
                metricData.getSimulationTime());

        String filename = "test-result.csv";
        try (IntgResultPointFileWriter fileWriter = new IntgResultPointFileWriter(filename)){
            //fileWriter.open();
            metricData = cauchyProblemSolver.solve(cauchyProblem, stepSolver, fileWriter);
            fileWriter.await();
        }
        logger.info("Finished simulation using a file-based result store. Total time: {} ms.",
                metricData.getSimulationTime());

        List<IntgResultPoint> memoryResultPoints = getResultPoints(memoryDataStorage);

        List<IntgResultPoint> fileResultPoints;
        try(InputStream inputStream = new FileInputStream(filename)) {
            IntgResultPointFileReader fileReader = new IntgResultPointFileReader();
            fileReader.init(inputStream);
            fileResultPoints = getResultPoints(fileReader);
        }

        assertEquals(memoryResultPoints.size(), fileResultPoints.size());

        boolean equals = false;
        for (IntgResultPoint memoryResultPoint : memoryResultPoints) {
            equals = false;

            for (IntgResultPoint fileResultPoint : fileResultPoints) {
                if (memoryResultPoint.equals(fileResultPoint)) {
                    equals = true;
                    break;
                }
            }

            if (!equals) {
                break;
            }
        }
        assertTrue(equals);
    }

    private List<IntgResultPoint> getResultPoints(IntgResultPointProvider resultPointProvider) {
        List<IntgResultPoint> results = new ArrayList<>();
        resultPointProvider.read(results::addAll);
        return results;
    }

}
