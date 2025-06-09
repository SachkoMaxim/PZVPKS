package com.sachkomaxim.pzvpks.lab2.threads;

import com.sachkomaxim.pzvpks.lab2.data.Data;

import java.util.concurrent.BrokenBarrierException;

public class T4 extends Thread {
    private final Data data;

    public T4(Data data) {
        this.data = data;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T4
        System.out.println("Task T4 is started");

        try {
            // Локальні змінні
            int a4;
            int d4;

            // Введення MC, E
            data.MC = data.putValuesIntoMatrix();
            data.E = data.putValuesIntoVector();

            // Сигнал і очікування введення даних у потоках T1, T2, T3
            data.B1.await();

            // Обчислення1 a4 = Bн * Zн
            a4 = data.partialScalarProductOfTwoVectors(data.B, data.Z, 3);

            // Обчислення2 а = а + а4
            // КД1, захищений запис нового значення спільного ресурсу a із використанням атомарної змінної
            data.a.addAndGet(a4);                                  //КД1

            // Обчислення3 Sн = R * MCн
            data.S = data.partialMultiplyVectorAndMatrix(data.R, data.MC, data.S, 3);

            // Сигнал за допомогою семафору про завершення обчислення а4 і Sн потокам T1, T2, T3
            data.S1.release();
            data.S2.release();
            data.S3.release();

            // Очікування на завершення обчислень а та Sн у потоках T1, Т2, T3
            data.S4.acquire(3);

            // Копіювання СP a4 = a
            // Вхід до КД2 або очікування на вихід T1, T2, T3 з КД2
            data.S6.acquire();
            a4 = data.a.get();                                     //КД2
            // Сигнал про вихід з КД2
            data.S6.release();

            // Використання механізму критичних секцій під час копіювання d4 = d
            // КД3, synchronized-блок із синхронізацією на об'єкті CS1
            synchronized (data.CS1) {
                d4 = data.d;                                       //КД3
            }

            // Обчислення4 Xн = S * MDн + a4 * Eн * d4
            data.X = data.calculateXn(data.S, data.MD, a4, data.E, d4, data.X, 3);

            // Сигнал за допомогою семафору потоку T3 про завершення обчислень Xн
            data.S5.release();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        // Повідомлення про завершення виконання потоку T4
        System.out.println("Task T4 is finished");
    }
}
