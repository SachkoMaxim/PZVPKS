package com.sachkomaxim.pzvpks.lab3.threads;

import com.sachkomaxim.pzvpks.lab3.data.Data;
import com.sachkomaxim.pzvpks.lab3.monitors.SharedMonitor;
import com.sachkomaxim.pzvpks.lab3.monitors.SynchronizedMonitor;

public class T2 extends Thread {
    private final Data data;
    private final SynchronizedMonitor SynchronizedMon;
    private final SharedMonitor SharedMon;

    private final int from;
    private final int to;

    public T2(Data data, SynchronizedMonitor SyncMon, SharedMonitor ShrMon) {
        this.data = data;
        this.SynchronizedMon = SyncMon;
        this.SharedMon = ShrMon;
        from = data.H;
        to = 2 * data.H - 1;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T2
        System.out.println("Task T2 is started");

        // Локальні змінні
        int a2;
        int d2;

        // Введення B, MX
        // Встановлення для деяких елементів B значень, відмінних від 4,
        // для перевірки правильності сортування та знаходження мінімального значення
        data.B = data.putValuesIntoB();
        data.MX = data.putValuesIntoMatrix();

        // Сигнал задачі T1, T3, T4 про введення B, MX
        this.SynchronizedMon.signalInput();

        // Очікування введення даних у задачі T4
        this.SynchronizedMon.waitInput();

        // Обчислення1 a2 = min(Bн)
        a2 = data.findMinValInVector(data.B, 1);

        // Обчислення2 a = min(а, а2)
        // КД1
        this.SharedMon.findMina(a2);                           //КД1

        // Сигнал T1, T3, T4 про завершення обчислень a
        this.SynchronizedMon.signalMina();

        // Очікування на завершення обчислень a у задачах T1, T3, T4
        this.SynchronizedMon.waitMina();

        // Копіювання СP d2 = d
        // КД2
        d2 = this.SharedMon.copyd();                           //КД2

        // Обчислення3 Aн = sort(d2 * Bн + Z * (MM * MXн))
        data.calculateUnsortedAh(d2, data.B, data.Z, data.MM, data.MX, data.A, from, 1);
        Data.mergeSort(data.A, from, to);

        // Сигнал задачі Т1 про завершення Обчислення3 Ан
        this.SynchronizedMon.signalCalcAh2to1();

        // Очікування на завершення Обчислення6 A в задачі Т1
        this.SynchronizedMon.waitCalcA();

        // Копіювання СP a1 = a
        // КД3
        a2 = this.SharedMon.copya();                           //КД3

        // Обчислення6 Xн = a2 * Aн
        data.calculateXn(a2, data.A, from, 1);

        // Сигнал T4 про завершення обчислень Xн
        this.SynchronizedMon.signalCalcX();

        // Повідомлення про завершення виконання потоку T2
        System.out.println("Task T2 is finished");
    }
}
