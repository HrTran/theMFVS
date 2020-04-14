package com.tranhuy.runner;

import com.tranhuy.model.Graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class StaticTest {
    public static void main(String[] args) {
        int N = 400;
        int threshold = 1;
        createFile(N, threshold);

    }

    public static void createFile(int N, int threshold) {
        double p = 0D;

        try {
            FileWriter file = new FileWriter(String.format("graphs/p=0.0%d/graphs_%d.txt", threshold, N));
            PrintWriter printWriter = new PrintWriter(file);

            System.out.println("Start writing to file...");
            printWriter.println(N);

            for(int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    p = Math.random() * ( 100 - 0 );
                    if(p < threshold) {
                        try {
                            System.out.println("Prob: " + p);
                            printWriter.println(String.format("%d %d", i, j));
                        } catch (Exception e) {
                            System.out.println(e.getMessage() + " causes " + e.getCause());
                            e.printStackTrace();
                        }
                    }
                }
            }

            System.out.println("Done!");
            printWriter.close();
            file.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
