package ru.nstu.isma.intg.core.solvers.parallel.utils;

import mpi.MPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class MPIUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MPIUtils.class);
    private static final Marker MPI_DATA_MARKER = MarkerFactory.getMarker("MPI_DATA");

    public static int getRank() {
        return MPI.COMM_WORLD.Rank();
    }

    public static int getSize() {
        return MPI.COMM_WORLD.Size();
    }

    public static void barrier() {
        MPI.COMM_WORLD.Barrier();
    }

    public static double[] allGather(double[] sendBuff) {
        //  У всех узлов размер отправляемых данных одинаков.
        int sendingSize = sendBuff.length;

        int rankCount = MPI.COMM_WORLD.Size();

        // Размер массива получаемых данных равен произведенияю количества узлов на размер отправляемых данных.
        double[] recvBuff = new double[rankCount * sendingSize];

        if (LOGGER.isDebugEnabled(MPI_DATA_MARKER)) {
            LOGGER.debug(MPI_DATA_MARKER, "#{} rank: sending [#{}]", getRank(), buildLogMsg(sendBuff));
        }

        // Отправляем данные текущего узла всем остальным узлам сети.
        MPI.COMM_WORLD.Allgather(sendBuff, 0, sendingSize, MPI.DOUBLE, recvBuff, 0, sendingSize, MPI.DOUBLE);

        if (LOGGER.isDebugEnabled(MPI_DATA_MARKER)) {
            LOGGER.debug(MPI_DATA_MARKER, "#{} rank: received [#{}]", getRank(), buildLogMsg(recvBuff));
        }

        // Поскольку в нашем массиве отправляемое вычисленное значение занимает два элемента массива:
        // индекс уравнения и само значение, то размер итогового массива в два раза меньше отправляемого.
        double[] gatheredArray = new double[sendingSize / 2];

        for (int k = 0; k < rankCount; k++) { // Для каждого узла.
            int offset = k * sendingSize;

            for (int m = 0; m < sendingSize; m += 2) {
                int idx = (int) recvBuff[offset + m]; // Получаем индекс уравнения.
                if (idx < -0.5) {
                    break;
                }
                // Если он больше -1, значит в полученном массиве для уравнения по этому индексу есть вычисленное значение.

                // Запоминаем значение по этому индексу.
                gatheredArray[idx] = recvBuff[offset + m + 1];
            }
        }

        return gatheredArray;
    }

    private static String buildLogMsg(double[] buff) {
        StringBuilder msg = new StringBuilder();

        msg.append("[\n");

        for (int i = 0; i < buff.length; i++) {
            msg.append("\t");
            msg.append("idx=").append(i).append(", ");
            msg.append("val=").append(buff[i]).append("\n");

        }
        msg.append("]");

        return msg.toString();
    }

}
