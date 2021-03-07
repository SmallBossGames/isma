package ru.nstu.isma.hsm.linear;

import java.io.Serializable;

/**
 * Created by Bessonov Alex
 * on 14.03.2015.
 */
public abstract class LinearSystemMatrix implements Serializable {
    private int size;
    private HMLinearAlg[][] a;
    private HMLinearAlg[] b;

    protected LinearSystemMatrix() {
        initMatrix();
    }

    public LinearSystemMatrix(int size) {
        setSize(size);
        initMatrix();
    }

    public void setSize(int size) {
        this.size = size;
        a = new HMLinearAlg[size][size];
        b = new HMLinearAlg[size];
    }

    protected abstract void initMatrix();

    double[][] getA(double[] y) {
        double[][] dA = new double[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                dA[i][j] = (a[i][j] != null) ? a[i][j].calculateRightMember(y) : 0;
        return dA;
    }

    double[] getB(double[] y) {
        double[] db = new double[size];
        for (int i = 0; i < size; i++)
            db[i] = (b[i] != null) ? b[i].calculateRightMember(y) : 0;
        return db;
    }

    public void addAElem(int row, int col, HMLinearAlg a) {
        this.a[row][col] = a;
    }

    public void addBElem(int row, HMLinearAlg a) {
        this.b[row] = a;
    }
}
