package com.sachkomaxim.pzvpks.lab3.threads;

import com.sachkomaxim.pzvpks.lab3.data.Data;
import com.sachkomaxim.pzvpks.lab3.monitors.SharedMonitor;
import com.sachkomaxim.pzvpks.lab3.monitors.SynchronizedMonitor;

public class T1 extends Thread {
    private final Data data;
    private final SynchronizedMonitor SynchronizedMon;
    private final SharedMonitor SharedMon;

    private final int from;
    private final int to;
    private final int toT2;
    private final int toT4;

    public T1(Data data, SynchronizedMonitor SyncMon, SharedMonitor ShrMon) {
        this.data = data;
        this.SynchronizedMon = SyncMon;
        this.SharedMon = ShrMon;
        from = 0;
        to = data.H - 1;
        toT2 = 2 * data.H - 1;
        toT4 = 4 * data.H - 1;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T1
        System.out.println("Task T1 is started");

        // Локальні змінні
        int a1;
        int d1;

        // Очікування введення даних у задачах T2, T4
        this.SynchronizedMon.waitInput();

        // Обчислення1 a1 = min(Bн)
        a1 = data.findMinValInVector(data.B, 0);

        // Обчислення2 a = min(а, а1)
        // КД1
        this.SharedMon.findMina(a1);                           //КД1

        // Сигнал T2, T3, T4 про завершення обчислень a
        this.SynchronizedMon.signalMina();

        // Очікування на завершення обчислень a у задачах T2, T3, T4
        this.SynchronizedMon.waitMina();

        // Копіювання СP d1 = d
        // КД2
        d1 = this.SharedMon.copyd();                           //КД2

        // Обчислення3 Aн = sort(d1 * Bн + Z * (MM * MXн))
        data.calculateUnsortedAh(d1, data.B, data.Z, data.MM, data.MX, data.A, from, 0);
        Data.mergeSort(data.A, from, to);

        // Очікування на завершення Обчислення3 Aн в задачі Т2
        this.SynchronizedMon.waitCalcAh2to1();

        // Обчислення4 A2н = sort*(Aн, Aн)
        Data.mergeSort(data.A, from, toT2);

        // Очікування на завершення Обчислення4 A2н в задачі Т3
        this.SynchronizedMon.waitCalcA2h3to1();

        // Обчислення5 A = sort*(A2н, A2н)
        Data.mergeSort(data.A, from, toT4);

        // Сигнал Т2, Т3, Т4 про завершення обчислення A
        this.SynchronizedMon.signalCalcA();

        // Копіювання СP a1 = a
        // КД3
        a1 = this.SharedMon.copya();                           //КД3

        // Обчислення6 Xн = a1 * Aн
        data.calculateXn(a1, data.A, from, 0);

        // Сигнал T4 про завершення обчислень Xн
        this.SynchronizedMon.signalCalcX();

        // Повідомлення про завершення виконання потоку T1
        System.out.println("Task T1 is finished");
    }
}
