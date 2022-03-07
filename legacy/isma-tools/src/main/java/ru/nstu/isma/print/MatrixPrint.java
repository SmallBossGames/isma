package ru.nstu.isma.print;

/**
 * Created by Bessonov Alex
 * on 14.03.2015.
 */
public class MatrixPrint {
    public static void print(String label, double[][] matrix, double[] b) {
        System.out.println(" =====  Matrix: " + label + " ========");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + "   \t");
            }
            System.out.print("\t   =   \t");
            System.out.println(b[i]);
        }
    }

    public static void print(String label, double[][] matrix) {
        System.out.println(" =====  Matrix: " + label + " ========");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + "   \t");
            }
            System.out.print("\n");
        }
    }
}
