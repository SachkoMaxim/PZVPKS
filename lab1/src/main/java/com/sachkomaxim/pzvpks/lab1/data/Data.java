package com.sachkomaxim.pzvpks.lab1.data;

import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Data {
    // scanner використовується тільки у головному потоці для отримання n та варіанту надання вхідних даних.
    // Потоками T1, T2, T3 не використовується.
    private static final Scanner scanner = new Scanner(System.in);

    public static int getN() {
        while (true) {
            System.out.print("Enter n: ");
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            }
            System.out.println("n must be an integer");
        }
    }

    public static int getInputOptionFromConsole() {
        while (true) {
            System.out.println("Enter the data entry option." +
                    "\n1 for reading from a file, " +
                    "\n2 to set all elements to the given value, " +
                    "\n3 to use the random value generator ");
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            }
        }
    }

    public static int F1(int[] A, int[] B, int[] C, int[] D, int[][] MA, int[][] ME) {
        // e = ((A + B) * (C + D * (MA * ME)))
        int[] resA_B = addTwoVectors(A, B); // (A + B)

        int[][] resMA_ME = multiplyTowMatrices(MA, ME); // (MA * ME)

        int[] resD_MA_ME = multiplyVectorAndMatrix(D, resMA_ME); // D * (MA * ME))

        int[] resC_D_MA_ME = addTwoVectors(C, resD_MA_ME); // (C + D * (MA * ME))

        int e = scalarProductOfTwoVectors(resA_B, resC_D_MA_ME); // ((A + B) * (C + D * (MA * ME)))

        return e;
    }

    public static int[][] F2(int[][] MG, int[][] MH, int[][] MK, int[][] ML) {
        // MF = (MG + MH) * (MK * ML) * (MG + ML)
        int[][] resMG_MH = addTwoMatrices(MG, MH); // (MG + MH)

        int[][] resMK_ML = multiplyTowMatrices(MK, ML); // (MK * ML)

        int[][] resMG_ML = addTwoMatrices(MG, ML); // (MG + ML)

        int[][] resMG_MH_MK_ML = multiplyTowMatrices(resMG_MH, resMK_ML); // (MG + MH) * (MK * ML)

        int[][] MF = multiplyTowMatrices(resMG_MH_MK_ML, resMG_ML); // (MG + MH) * (MK * ML) * (MG + ML)

        return MF;
    }

    public static int[][] F3(int[][] MP, int[][] MR, int[][] MS) {
        // MO = MP * MR + MS
        int[][] resMP_MR = multiplyTowMatrices(MP, MR); // MP * MR

        int[][] MO = addTwoMatrices(resMP_MR, MS); // MP * MR + MS

        return MO;
    }

    private static int[] addTwoVectors(int[] vector1, int[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }

        int[] summary = new int[vector1.length];

        for (int i = 0; i < vector1.length; i++) {
            summary[i] = vector1[i] + vector2[i];
        }
        return summary;
    };

    private static int scalarProductOfTwoVectors(int[] vector1, int[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }

        int scalarProduct = 0;

        for (int i = 0; i < vector1.length; i++) {
            scalarProduct += vector1[i] * vector2[i];
        }
        return scalarProduct;
    }

    private static int[][] addTwoMatrices(int[][] matrix1, int[][] matrix2) {
        if (matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length) {
            throw new IllegalArgumentException("Matrices must have the same size");
        }

        int[][] summary = new int[matrix1.length][matrix1[0].length];

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                summary[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return summary;
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

    private static int[] multiplyVectorAndMatrix(int[] vector, int[][] matrix) {
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

    public static int[] getVector(String vectorName, int n, int inputOption, int defaultValue) {
        if (n < 1000) { // Для n < 1000 отримати дані з консолі
            return getVectorFromConsole(vectorName, n);
        }
        if (inputOption == 1) { // Зчитати вектор з файла
            return getVectorFromFile(vectorName, n);
        }
        if (inputOption == 2) { // Заповнити вектор одним значенням (1, 2 або 3)
            return getVectorFilledWithDefaultValue(n, defaultValue);
        }
        // Згенерувати вектор з випадковими значеннями
        return getVectorWithRandomValues(n);
    }

    public static int[][] getMatrix(String matrixName, int n, int inputOption, int defaultValue) {
        if (n < 1000) { // Для n < 1000 отримати дані з консолі
            return getMatrixFromConsole(matrixName, n);
        }
        if (inputOption == 1) { // Зчитати матрицю з файла
            return getMatrixFromFile(matrixName, n);
        }
        if (inputOption == 2) { // Заповнити матрицю одним значенням (1, 2 або 3)
            return getMatrixFilledWithDefaultValue(n, defaultValue);
        }
        // Згенерувати матрицю з випадковими значеннями
        return getMatrixWithRandomValues(n);
    }

    private static int[] getVectorFromConsole(String vectorName, int n) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int[] result = new int[n];
            for (int i = 0; i < n; i++) {
                System.out.println("Enter " + vectorName + "[" + i + "] element:");
                result[i] = Integer.parseInt(reader.readLine());
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int[][] getMatrixFromConsole(String matrixName, int n) {
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            result[i] = getVectorFromConsole(matrixName + "[" + i + "]", n);
        }
        return result;
    }

    public static int[] getVectorFromFile(String vectorName, int n) {
        try (BufferedReader reader = new BufferedReader(new FileReader(vectorName + ".txt"))) {
            int[] result = new int[n];
            String[] vectorLine = reader.readLine().split(" ");
            for (int i = 0; i < n; i++) {
                result[i] = Integer.parseInt(vectorLine[i]);
            }
            return result;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[][] getMatrixFromFile(String matrixName, int n) {
        try (BufferedReader reader = new BufferedReader(new FileReader(matrixName + ".txt"))) {
            int[][] result = new int[n][n];
            for (int i = 0; i < n; i++) {
                String[] matrixLine = reader.readLine().split(" ");
                for (int j = 0; j < n; j++) {
                    result[i][j] = Integer.parseInt(matrixLine[j]);
                }
            }
            return result;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int[] getVectorFilledWithDefaultValue(int n, int defaultValue) {
        int[] result = new int[n];
        Arrays.fill(result, defaultValue);
        return result;
    }

    private static int[][] getMatrixFilledWithDefaultValue(int n, int defaultValue) {
        int[][] result = new int[n][n];
        for (int[] row: result) {
            Arrays.fill(row, defaultValue);
        }
        return result;
    }

    private static int[] getVectorWithRandomValues(int n) {
        int[] result = new int[n];
        Random random = new Random();
        for (int i = 0; i < result.length; i++) {
            result[i] = random.nextInt(100); // Заповнити значеннями віл 0 до 99 включно
        }
        return result;
    }

    private static int[][] getMatrixWithRandomValues(int n) {
        int[][] result = new int[n][n];
        Random random = new Random();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = random.nextInt(100); // Заповнити значеннями віл 0 до 99 включно
            }
        }
        return result;
    }

    public static void printNumber(int number, String numberName) {
        System.out.println(numberName + ": " + number);
    }

    public static void printVector(int[] vector, String vectorName) {
        System.out.println(vectorName + ": " + Arrays.toString(vector));
    }

    public static void printMatrix(int[][] matrix, String matrixName) {
        System.out.println(matrixName + ": ");
        for (int i = 0; i < matrix.length; i++) {
            printVector(matrix[i], matrixName + "[" + i + "]");
        }
    }

    public static void writeNumberToFile(int number, String numberName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(numberName + ".txt"))) {
            writer.write(String.valueOf(number));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeMatrixToFile(int[][] matrix, String matrixName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(matrixName + ".txt"))) {
            for (int[] row: matrix) {
                writer.write(Arrays.toString(row).replaceAll("[,\\[\\]]", "") + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
