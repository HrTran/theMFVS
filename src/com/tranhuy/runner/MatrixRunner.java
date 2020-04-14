package com.tranhuy.runner;

import com.tranhuy.model.Matries;
import com.tranhuy.utils.MatrixUtils;
import Jama.Matrix;
import Jama.EigenvalueDecomposition;

/**
 * Create by huytt99 on 09/04/2020
 */
public class MatrixRunner {
    // Driver program
    public static void main(String[] args)
    {
        int N = 3;
        Matries mat = new Matries(N);
        MatrixUtils matUtils = new MatrixUtils();

        // example 1
//        double transition[][] = { {-0.3, 0.4, 0, 0},
//                {0.2, -0.4, 1, 0},
//                {0.1, 0, 1, 0},
//                {1, 1, 1, 0}
//        };
//        double result[][] = { {0},
//                {0},
//                {0},
//                {1}
//        };

        // example 2
        double transition[][] = {
                {0.6, 0.1, 0.3},
                {0.1, 0.7, 0.2},
                {0.2, 0.2, 0.6}
        };

        // Example 3
//        double A[][] = { {-4, -2},
//                {4, -8}
//        };
//        double B[][] = { {-8, 4},
//                {8, -4}
//        };

        // Example 4
//        double[][] transition = { { 0.386, 0.147, 0.202, 0.062, 0.140, 0.047, 0.016},
//                { 0.107, 0.267, 0.227, 0.120, 0.207, 0.052, 0.020},
//                { 0.035, 0.101, 0.188, 0.191, 0.357, 0.067, 0.061},
//                { 0.021, 0.039, 0.112, 0.212, 0.431, 0.124, 0.061},
//                { 0.009, 0.024, 0.075, 0.123, 0.473, 0.171, 0.125},
//                { 0.000, 0.103, 0.041, 0.088, 0.301, 0.312, 0.155},
//                { 0.000, 0.008, 0.036, 0.083, 0.364, 0.235, 0.274}
//        };


        /*
        double [][]adj = new double[N][N]; // To store adjoint of A[][]
        double [][]inv = new double[N][N]; // To store inverse of A[][]

        System.out.print("Input matrix is :\n");
        mat.display(transition);

        System.out.print("\nThe Adjoint is :\n");
        mat.adjoint(transition, adj);
        mat.display(adj);

        System.out.print("\nThe Inverse is :\n");
        if (mat.inverse(transition, inv))
            mat.display(inv);

        System.out.print("\nThe Product is :\n");
        mat.display(matUtils.multiplyMatrices(inv, result));
        */

        // https://introcs.cs.princeton.edu/java/95linear/MarkovChain.java.html
        //
        // If ergordic, stationary distribution = unique solution to Ax = x
        // up to scaling factor.
        // We solve (A - I) x = 0, but replace row 0 with constraint that
        // says the sum of x coordinates equals one
        Matrix x = matUtils.computeByLinearMethod(transition, N);
        x.print(6, 4);
        System.out.println("Col: " + x.getColumnDimension());
        System.out.println("Row: " + x.getRowDimension());


//        double max = 0.0D;
//        int a = 0, b = 0;
//        for(int i = 0 ; i < x.getRowDimension(); i++) {
//            for(int j = 0 ; j < x.getColumnDimension(); j++) {
//                if( x.get(i, j) >= max ) {
//                    max = x.get(i, j);
//                    a = i; b = j;
//                }
//            }
//        }
//        System.out.println("Max: " + max + " - i = " + a + " - j = " + b);

    }
}
