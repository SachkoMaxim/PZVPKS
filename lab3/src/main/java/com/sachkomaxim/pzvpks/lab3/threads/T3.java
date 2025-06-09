package com.sachkomaxim.pzvpks.lab3.threads;

import com.sachkomaxim.pzvpks.lab3.data.Data;
import com.sachkomaxim.pzvpks.lab3.monitors.SharedMonitor;
import com.sachkomaxim.pzvpks.lab3.monitors.SynchronizedMonitor;

public class T3 extends Thread {
    private final Data data;
    private final SynchronizedMonitor SynchronizedMon;
    private final SharedMonitor SharedMon;

    private final int from;
    private final int to;
    private final int toT4;

    public T3(Data data, SynchronizedMonitor SyncMon, SharedMonitor ShrMon) {
        this.data = data;
        this.SynchronizedMon = SyncMon;
        this.SharedMon = ShrMon;
        from = 2 * data.H;
        to = 3 * data.H - 1;
        toT4 = 4 * data.H - 1;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T3
        System.out.println("Task T3 is started");

        // Локальні змінні
        int a3;
        int d3;

        // Очікування введення даних у задачах T2, T4
        this.SynchronizedMon.waitInput();

        // Обчислення1 a3 = min(Bн)
        a3 = data.findMinValInVector(data.B, 2);

        // Обчислення2 a = min(а, а1)
        // КД1
        this.SharedMon.findMina(a3);                           //КД1

        // Сигнал T1, T2, T4 про завершення обчислень a
        this.SynchronizedMon.signalMina();

        // Очікування на завершення обчислень a у задачах T1, T2, T4
        this.SynchronizedMon.waitMina();

        // Копіювання СP d3 = d
        // КД2
        d3 = this.SharedMon.copyd();                           //КД2

        // Обчислення3 Aн = sort(d3 * Bн + Z * (MM * MXн))
        data.calculateUnsortedAh(d3, data.B, data.Z, data.MM, data.MX, data.A, from, 2);
        Data.mergeSort(data.A, from, to);

        // Очікування на завершення Обчислення3 Aн в задачі Т4
        this.SynchronizedMon.waitCalcAh4to3();

        // Обчислення4 A2н = sort*(Aн, Aн)
        Data.mergeSort(data.A, from, toT4);

        // Сигнал задачі Т1 про завершення обчислення А2н
        this.SynchronizedMon.signalCalcA2h3to1();

        // Очікування на завершення Обчислення6 A в задачі Т1
        this.SynchronizedMon.waitCalcA();

        // Копіювання СP a3 = a
        // КД3
        a3 = this.SharedMon.copya();                           //КД3

        // Обчислення6 Xн = a1 * Aн
        data.calculateXn(a3, data.A, from, 2);

        // Сигнал T4 про завершення обчислень Xн
        this.SynchronizedMon.signalCalcX();

        // Повідомлення про завершення виконання потоку T3
        System.out.println("Task T3 is finished");
    }
}
