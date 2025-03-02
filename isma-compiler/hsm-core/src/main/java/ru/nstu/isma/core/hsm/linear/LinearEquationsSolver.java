package ru.nstu.isma.core.hsm.linear;


import ru.nstu.isma.print.MatrixPrint;

/**
 * Created by Дмитрий Достовалов
 * on 13.03.2015.
 */

public class LinearEquationsSolver {
    double[][] LU;  // LU-разложение матрицы A для задачи Ax=b.
    int[] kd;       // При LU-разложении выполняется выбор ведущего элемента,
    // т.е. перестановки строк. Здесь запоминаются перестановки.
    int size;

    public LinearEquationsSolver(double[][] A) {
        MatrixPrint.print("input", A);
        size = A.length;
        LU = new double[size][size];
        for (int i = 0; i < size; i++) {
            if (A[i].length != size) throw new RuntimeException("Different matrix A size!");
            for (int j = 0; j < size; j++) {
                LU[i][j] = A[i][j];
            }
        }
        kd = new int[size];
        Dec();      // Выполняется LU-разложение
    }

    private void Dec() {
        int m;
        double t;

        kd[size - 1] = 1;
        if (size > 1) {
            for (int j = 0; j < size - 1; j++) {
                m = j;
                for (int i = j + 1; i < size; i++)
                    if (Math.abs(LU[i][j]) > Math.abs(LU[m][j])) m = i;
                kd[j] = m;
                t = LU[m][j];
                if (m != j) {
                    kd[size - 1] = -kd[size - 1];
                    LU[m][j] = LU[j][j];
                    LU[j][j] = t;
                }
                if (Math.abs(t) < 1.0e-20) {
                    kd[size - 1] = 0;
                    throw new RuntimeException("Matrix A is singular!");
                }
                t = 1.0 / t;
                for (int i = j + 1; i < size; i++)
                    LU[i][j] = -LU[i][j] * t;
                for (int i = j + 1; i < size; i++) {
                    t = LU[m][i];
                    LU[m][i] = LU[j][i];
                    LU[j][i] = t;
                    if (Math.abs(t) > 1.0e-20)
                        for (int k = j + 1; k < size; k++)
                            LU[k][i] = LU[k][i] + LU[k][j] * t;
                }
            }
        }
        if (Math.abs(LU[size - 1][size - 1]) < 1.0e-20) {
            kd[size - 1] = 0;
            throw new RuntimeException("Matrix A is singular!");
        }
    }

    public double[] solve(double[] b) {
        MatrixPrint.print("LU", LU, b);
        if (b.length != size) throw new RuntimeException("Different size!");

        double[] x = new double[size];
        for (int i = 0; i < size; i++)
            x[i] = b[i];

        double t;
        int m;

        if (size > 1) {
            for (int j = 0; j < size - 1; j++) {
                m = kd[j];
                t = x[m];
                x[m] = x[j];
                x[j] = t;
                for (int i = j + 1; i < size; i++)
                    x[i] = x[i] + LU[i][j] * t;
            }
            for (int j = size - 1; j > 0; j--) {
                x[j] = x[j] / LU[j][j];
                t = -x[j];
                for (int i = 0; i < j; i++)
                    x[i] = x[i] + LU[i][j] * t;
            }
        }
        x[0] = x[0] / LU[0][0];

        return x;
    }
}
