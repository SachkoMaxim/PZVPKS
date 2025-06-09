package com.sachkomaxim.pzvpks.lab3.threads;

import com.sachkomaxim.pzvpks.lab3.data.Data;
import com.sachkomaxim.pzvpks.lab3.monitors.SharedMonitor;
import com.sachkomaxim.pzvpks.lab3.monitors.SynchronizedMonitor;

public class T4 extends Thread {
    private final Data data;
    private final SynchronizedMonitor SynchronizedMon;
    private final SharedMonitor SharedMon;

    private final int from;
    private final int to;

    public T4(Data data, SynchronizedMonitor SyncMon, SharedMonitor ShrMon) {
        this.data = data;
        this.SynchronizedMon = SyncMon;
        this.SharedMon = ShrMon;
        from = 3 * data.H;
        to = 4 * data.H - 1;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T4
        System.out.println("Task T4 is started");

        // Локальні змінні
        int a4;
        int d4;

        // Введення MM, Z, d
        data.MM = data.putValuesIntoMatrix();
        data.Z = data.putValuesIntoVector();
        this.SharedMon.writed(1);

        // Сигнал задачі T1, T2, T3 про введення MM, Z, d
        this.SynchronizedMon.signalInput();

        // Очікування введення даних у задачі T2
        this.SynchronizedMon.waitInput();

        // Обчислення1 a4 = min(Bн)
        a4 = data.findMinValInVector(data.B, 3);

        // Обчислення2 a = min(а, а4)
        // КД1
        this.SharedMon.findMina(a4);                           //КД1

        // Сигнал T1, T2, T3 про завершення обчислень a
        this.SynchronizedMon.signalMina();

        // Очікування на завершення обчислень a у задачах T1, T2, T3
        this.SynchronizedMon.waitMina();

        // Копіювання СP d4 = d
        // КД2
        d4 = this.SharedMon.copyd();                           //КД2

        // Обчислення3 Aн = sort(d4 * Bн + Z * (MM * MXн))
        data.calculateUnsortedAh(d4, data.B, data.Z, data.MM, data.MX, data.A, from, 3);
        Data.mergeSort(data.A, from, to);

        // Сигнал задачі Т3 про завершення Обчислення3 Ан
        this.SynchronizedMon.signalCalcAh4to3();

        // Очікування на завершення Обчислення6 A в задачі Т1
        this.SynchronizedMon.waitCalcA();

        // Копіювання СP a4 = a
        // КД3
        a4 = this.SharedMon.copya();                           //КД3

        // Обчислення6 Xн = a4 * Aн
        data.calculateXn(a4, data.A, from, 3);

        // Очікування на завершення обчислень Xн у потоках T1, Т2, T3
        this.SynchronizedMon.waitCalcX();

        //Виведення результату X
        Data.printVectorX(data.X, "X");

        // Повідомлення про завершення виконання потоку T4
        System.out.println("Task T4 is finished");
    }
}
