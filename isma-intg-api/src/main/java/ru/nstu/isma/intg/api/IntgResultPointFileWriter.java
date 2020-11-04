package ru.nstu.isma.intg.api;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Maria
 * @since 14.03.2016
 */
public class IntgResultPointFileWriter implements Consumer<IntgResultPoint>, AutoCloseable {

    private static final String COMMA = ",";

    private volatile boolean opened = false;
    private volatile boolean closed = false;

    private final Writer writer;
    private final BlockingQueue<IntgResultPoint> queue;

    private Thread workerThread;

    private boolean firstTime = true;

    public IntgResultPointFileWriter(String pathname) throws IOException {
        File file = new File(pathname);
        FileWriter fileWriter = new FileWriter(file);
        this.writer = new BufferedWriter(fileWriter);
        this.queue = new LinkedBlockingQueue<>();
    }

    private void open() {
        if (opened) {
            throw new IllegalStateException("open() has already called");
        }

        opened = true;
        workerThread = new Thread(new Worker(), "file-writer");
        workerThread.start();
    }

    @Override
    public void close() {
        closed = true;
    }

    @Override
    public void accept(IntgResultPoint intgResultPoint) {
        /*if (!opened) {
            throw new IllegalStateException("open() call expected before accept()");
        }*/

        if (firstTime) {
            appendHeader(intgResultPoint);
            firstTime = false;
            open();
        }

        try {
            queue.put(intgResultPoint);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void await() {
        try {
            workerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private void appendHeader(IntgResultPoint intgResultPoint) {
        try {
            String header = buildCsvHeader(intgResultPoint);
            writer.append(header);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildCsvHeader(IntgResultPoint firstResultPoint) {
        StringBuilder builder = new StringBuilder();
        builder.append(1); // x count
        builder.append(COMMA).append(firstResultPoint.getYForDe().length); // y count;
        builder.append(COMMA).append(firstResultPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX].length); // rhsForDeCount;
        builder.append(COMMA).append(firstResultPoint.getRhs()[DaeSystem.RHS_AE_PART_IDX].length); // rhsForAeCount;
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    private String buildCsvString(IntgResultPoint resultPoint) {
        StringBuilder builder = new StringBuilder();

        builder.append(resultPoint.getX());

        for (double y : resultPoint.getYForDe()) {
            builder.append(COMMA).append(y);
        }

        double[] yForAeArray = resultPoint.getRhs()[DaeSystem.RHS_AE_PART_IDX];
        for (double yForAe : yForAeArray) {
            builder.append(COMMA).append(yForAe);
        }

        double[] fArray = resultPoint.getRhs()[DaeSystem.RHS_DE_PART_IDX];
        for (double f : fArray) {
            builder.append(COMMA).append(f);
        }

        builder.append(System.lineSeparator());
        return builder.toString();
    }


    private class Worker implements Runnable {
        @Override
        public void run() {
            // TODO: исправить
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (opened && !closed && !queue.isEmpty()) {
                try {
                    IntgResultPoint intgResultPoint = queue.poll(100, TimeUnit.MICROSECONDS);
                    if (intgResultPoint != null) {
                        try {
                            writer.append(buildCsvString(intgResultPoint));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
