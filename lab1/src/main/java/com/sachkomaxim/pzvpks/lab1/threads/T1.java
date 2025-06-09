package com.sachkomaxim.pzvpks.lab1.threads;

import com.sachkomaxim.pzvpks.lab1.data.Data;

public class T1 extends Thread {
    private final int n;
    private final int inputOption;

    private int e;
    private int[] A, B, C, D;
    private int[][] MA, ME;

    public T1(int n, int inputOption) {
        super();
        this.n = n;
        this.inputOption = inputOption;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T1
        System.out.println("Task T1 is started");

        // Отримання вхідних даних (A, B, C, D, MA, ME)
        A = Data.getVector("A", n, inputOption, 1);
        B = Data.getVector("B", n, inputOption, 1);
        C = Data.getVector("C", n, inputOption, 1);
        D = Data.getVector("D", n, inputOption, 1);

        MA = Data.getMatrix("MA", n, inputOption, 1);
        ME = Data.getMatrix("ME", n, inputOption, 1);

        // Обчислення функції F1
        e = Data.F1(A, B, C, D, MA, ME);

        // Виведення результату
        if (n < 1000) {
            Data.printNumber(e, "e");
        } else {
            Data.writeNumberToFile(e, "e");
        }

        // Повідомлення про завершення виконання потоку T1
        System.out.println("Task T1 is finished");
    }
}
