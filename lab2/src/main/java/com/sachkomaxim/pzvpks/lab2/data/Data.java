package com.sachkomaxim.pzvpks.lab2.data;

import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Data {
    public final int N;    // Розмір векторів і квадратних матриць
    public final int P;    // Кількість процесорів
    public final int H;    // Розмір підвектора і кількість стовпців підматриці

    public Data(int n, int p) {
        N = n;
        P = p;
        H = N / P;
        S = new int[N];
        X = new int[N];
    }

    // Глобальні змінні
    public int d;
    public int[] R;
    public int[] B;
    public int[] Z;
    public int[] E;
    public int[][] MC;
    public int[][] MD;
    public int[] X;
    public int[] S;

    // Атомарна змінна
    public AtomicInteger a = new AtomicInteger(0);

    // Бар'єри
    public CyclicBarrier B1 = new CyclicBarrier(4);

    // Семафори
    public Semaphore S1 = new Semaphore(0);
    public Semaphore S2 = new Semaphore(0);
    public Semaphore S3 = new Semaphore(0);
    public Semaphore S4 = new Semaphore(0);
    public Semaphore S5 = new Semaphore(0);
    public Semaphore S6 = new Semaphore(1);

    // Об'єкт CS1 для синхронізації (для критичної секції)
    public final Object CS1 = new Object();

    public int putValueIntoScalar() {
        return 1;
    }

    public int[] putValuesIntoVector() {
        int[] vector = new int[N];
        for (int i = 0; i < N; i++) {
            vector[i] = putValueIntoScalar();
        }
        return vector;
    }

    public int[][] putValuesIntoMatrix() {
        int[][] matrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = putValueIntoScalar();
            }
        }
        return matrix;
    }

    public int[] partialAddOfTwoVectors(int[] vector1, int[] vector2, int[] result, int n) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }

        if (vector1.length != result.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }

        int start = n * H;

        for (int i = start; i < start + H; i++) {
            result[i] = vector1[i] + vector2[i];
        }
        return result;
    }

    public int partialScalarProductOfTwoVectors(int[] vector1, int[] vector2, int n) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }

        int start = n * H;

        int scalarProduct = 0;

        for (int i = start; i < start + H; i++) {
            scalarProduct += vector1[i] * vector2[i];
        }
        return scalarProduct;
    }

    private int[] partialMultiplyVectorAndScalar(int[] vector, int scalar, int[] result, int n) {
        if (vector.length != result.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }

        int start = n * H;

        for (int i = start; i < start + H; i++) {
            result[i] = vector[i] * scalar;
        }
        return result;
    }

    public int[] partialMultiplyVectorAndMatrix(int[] vector, int[][] matrix, int[] result, int n) {
        if (vector.length != matrix.length) {
            throw new IllegalArgumentException("Matrix must have the same number of rows as length of Vector");
        }

        if (vector.length != result.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }

        int start = n * H;

        for (int i = start; i < start + H; i++) {
            result[i] = 0; // ОБОВ'ЯЗКОВО обнуляємо
            for (int j = 0; j < vector.length; j++) {
                result[i] += vector[j] * matrix[j][i];
            }
        }
        return result;
    }

    public int[] calculateXn(int[] S, int[][] MD, int a, int[] E, int d, int[] X, int n) {
        //Обчислення4 Xн = S * MDн + a * Eн * d
        int[] Y1 = new int[N];
        int[] Y2 = new int[N];
        Y1 = partialMultiplyVectorAndMatrix(S, MD, Y1, n); // S * MDн
        Y2 = partialMultiplyVectorAndScalar(E, a * d, Y2, n); // a * Eн * d

        X = partialAddOfTwoVectors(Y1, Y2, X, n); // S * MDн + a * Eн * d

        return X;
    }

    public static void printVector(int[] vector, String vectorName) {
        System.out.println("Calculation result: " + vectorName + " = " + Arrays.toString(vector));
    }
}
