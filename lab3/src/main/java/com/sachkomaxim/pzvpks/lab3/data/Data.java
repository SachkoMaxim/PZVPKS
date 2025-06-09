package com.sachkomaxim.pzvpks.lab3.data;

import java.util.Arrays;

public class Data {
    public final int N;    // Розмір векторів і квадратних матриць
    public final int P;    // Кількість процесорів
    public final int H;    // Розмір підвектора і кількість стовпців підматриці

    public Data(int n, int p) {
        N = n;
        P = p;
        H = N / P;
        X = new int[N];
        A = new int[N];
    }

    // Глобальні змінні
    public int[] B;
    public int[] Z;
    public int[] A;
    public int[][] MM;
    public int[][] MX;
    public int[] X;

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

    public int[] putValuesIntoB() {
        // Встановлення для деяких елементів B значень, відмінних від 4,
        // для перевірки правильності сортування та знаходження мінімального значення
        int[] vector = new int[N];
        for (int i = 0; i < N; i++) {
            vector[i] = 4;
        }
        vector[0] = 2;
        vector[2 * H] = 1;
        vector[3 * H] = 3;
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

    public int[] addOfTwoVectors(int[] vector1, int[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }

        int[] result = new int[vector1.length];

        for (int i = 0; i < vector1.length; i++) {
            result[i] = vector1[i] + vector2[i];
        }
        return result;
    }

    private int[] multiplyVectorAndScalar(int[] vector, int scalar) {
        int[] result = new int[vector.length];

        for (int i = 0; i < vector.length; i++) {
            result[i] = vector[i] * scalar;
        }
        return result;
    }

    public int findMinValInVector (int[] vector, int n) {
        int min = Integer.MAX_VALUE;
        int start = n * H;

        for (int i = start; i < start + H; i++) {
            if (vector[i] < min) {
                min = vector[i];
            }
        }
        return min;
    }

    public int[] multiplyVectorAndMatrix(int[] vector, int[][] matrix) {
        if (vector.length != matrix.length) {
            throw new IllegalArgumentException("Matrix must have the same number of rows as length of Vector");
        }

        int[] result = new int[matrix[0].length];

        for (int i = 0; i < matrix[0].length; i++) {
            result[i] = 0; // ОБОВ'ЯЗКОВО обнуляємо
            for (int j = 0; j < vector.length; j++) {
                result[i] += vector[j] * matrix[j][i];
            }
        }
        return result;
    }

    private static int[][] multiplyTowMatrices(int[][] matrix1, int[][] matrix2) {
        if (matrix1[0].length != matrix2.length) {
            throw new IllegalArgumentException("Matrix2 must have the same number of rows as number of columns in Matrix1");
        }

        int[][] result = new int[matrix1.length][matrix2[0].length];

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                result[i][j] = 0; // ОБОВ'ЯЗКОВО обнуляємо
                for (int k = 0; k < matrix1[0].length; k++) { // Спільна розмірність
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    public static void mergeSort(int[] array, int from, int to) {
        if (from < to) {
            int mid = (from + to) / 2;
            mergeSort(array, from, mid);
            mergeSort(array, mid + 1, to);
            merge(array, from, mid, to);
        }
    }

    public static void merge(int[] array, int from, int center, int to) {
        int n1 = center - from + 1;
        int n2 = to - center;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(array, from, leftArray, 0, n1);
        for (int j = 0; j < n2; ++j) {
            rightArray[j] = array[center + 1 + j];
        }


        int i = 0, j = 0;
        int k = from;
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }

    public int[] getPartOfVector(int[] vector, int n) {
        int[] result = new int[H];
        int start = n * H;

        for (int i = 0; i < result.length; i++) {
            result[i] = vector[i + start];
        }
        return result;
    }

    public static void insertVectorPartIntoVector(int[] partOfVector, int[] vector, int from) {
        for (int i = 0; i < partOfVector.length; i++) {
            vector[i + from] = partOfVector[i];
        }
    }

    public int[][] getPartOfMatrixColumns(int[][] matrix, int n) {
        int[][] result = new int[matrix.length][H];
        int start = n * H;

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = matrix[i][j + start];
            }
        }
        return result;
    }

    public void calculateUnsortedAh(int di, int[] B, int[] Z, int[][] MM, int[][] MX, int[] A, int from, int n) {
        // Обчислення3 Aн = sort(di * Bн + Z * (MM * MXн))
        int[] Ah_uns = addOfTwoVectors(
                multiplyVectorAndScalar(
                        getPartOfVector(B, n), // Bн
                        di // di
                ), // di * Bн
                multiplyVectorAndMatrix(
                        Z, // Z
                        multiplyTowMatrices(
                                MM, // MM
                                getPartOfMatrixColumns(MX, n) // MXн
                        ) // (MM * MXн)
                ) // Z * (MM * MXн)
        ); // di * Bн + Z * (MM * MXн)

        insertVectorPartIntoVector(Ah_uns, A, from);
    }

    public void calculateXn(int a, int[] A, int from, int n) {
        // Обчислення6 Xн = a * Aн
        int[] Xh = multiplyVectorAndScalar(getPartOfVector(A, n), a); // a * Aн
        insertVectorPartIntoVector(Xh, X, from);
    }

    public static void printVector(int[] vector) {
        System.out.println(Arrays.toString(vector));
    }

    public static void printVectorX(int[] vector, String vectorName) {
        System.out.println("Calculation result: " + vectorName + " = " + Arrays.toString(vector));
    }
}
