package com.tranhuy.utils;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * Create by huytt99 on 09/04/2020
 */
public class MatrixUtils {
    public double[][] multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix) {
        double[][] result = new double[firstMatrix.length][secondMatrix[0].length];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = multiplyMatricesCell(firstMatrix, secondMatrix, row, col);
            }
        }

        return result;
    }

    public double multiplyMatricesCell(double[][] firstMatrix, double[][] secondMatrix, int row, int col) {
        double cell = 0;
        for (int i = 0; i < secondMatrix.length; i++) {
            cell += firstMatrix[row][i] * secondMatrix[i][col];
        }
        return cell;
    }

    /**
     * This method solves linear system of equations
     * to find Stationary distribution vector
     * @param transition the transition matrix
     * @param N the size of matrix
     * @return the stationary distribution vector in shape(N, 1)
     */
    public Matrix computeByLinearMethod(double[][] transition, int N) {
        Matrix A = new Matrix(transition);
        A = A.transpose();
        Matrix x = new Matrix(N, 1, 1.0 / N); // initial guess for eigenvector

        Matrix B = A.minus(Matrix.identity(N, N));
        for (int j = 0; j < N; j++)
            B.set(0, j, 1.0);
        Matrix b = new Matrix(N, 1);
        b.set(0, 0, 1.0);
        x = B.solve(b);

        return x;
    }

}
