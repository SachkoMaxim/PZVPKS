package com.sachkomaxim.pzvpks.lab1.threads;

import com.sachkomaxim.pzvpks.lab1.data.Data;

public class T2 extends Thread {
    private final int n;
    private final int inputOption;

    private int[][] MG, MH, MK, ML, MF;

    public T2(int n, int inputOption) {
        super();
        this.n = n;
        this.inputOption = inputOption;
    }
    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T2
        System.out.println("Task T2 is started");

        // Отримання вхідних даних (MG, MH, MK, ML)
        MG = Data.getMatrix("MG", n, inputOption, 2);
        MH = Data.getMatrix("MH", n, inputOption, 2);
        MK = Data.getMatrix("MK", n, inputOption, 2);
        ML = Data.getMatrix("ML", n, inputOption, 2);

        // Обчислення функції F2
        MF = Data.F2(MG, MH, MK, ML);

        // Виведення результату
        if (n < 1000) {
            Data.printMatrix(MF, "MF");
        } else {
            Data.writeMatrixToFile(MF, "MF");
        }

        // Повідомлення про завершення виконання потоку T2
        System.out.println("Task T2 is finished");
    }
}
