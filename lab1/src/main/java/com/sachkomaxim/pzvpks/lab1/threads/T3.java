package com.sachkomaxim.pzvpks.lab1.threads;

import com.sachkomaxim.pzvpks.lab1.data.Data;

public class T3 extends Thread {
    private final int n;
    private final int inputOption;

    private int[][] MP, MR, MS, MO;

    public T3(int n, int inputOption) {
        super();
        this.n = n;
        this.inputOption = inputOption;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T3
        System.out.println("Task T3 is started");

        // Отримання вхідних даних
        MP = Data.getMatrix("MP", n, inputOption, 3);
        MR = Data.getMatrix("MR", n, inputOption, 3);
        MS = Data.getMatrix("MS", n, inputOption, 3);

        // Обчислення функції F3
        MO = Data.F3(MP, MR, MS);

        // Виведення результату
        if (n < 1000) {
            Data.printMatrix(MO, "MO");
        } else {
            Data.writeMatrixToFile(MO, "MO");
        }

        // Повідомлення про завершення виконання потоку T3
        System.out.println("Task T3 is finished");
    }
}
