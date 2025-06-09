package com.sachkomaxim.pzvpks.lab2.threads;

import com.sachkomaxim.pzvpks.lab2.data.Data;

import java.util.concurrent.BrokenBarrierException;

public class T2 extends Thread {
    private final Data data;

    public T2(Data data) {
        this.data = data;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T2
        System.out.println("Task T2 is started");

        try {
            // Локальні змінні
            int a2;
            int d2;

            // Введення MD
            data.MD = data.putValuesIntoMatrix();

            // Сигнал і очікування введення даних у потоках T1, T3, T4
            data.B1.await();

            // Обчислення1 a2 = Bн * Zн
            a2 = data.partialScalarProductOfTwoVectors(data.B, data.Z, 1);

            // Обчислення2 а = а + а2
            // КД1, захищений запис нового значення спільного ресурсу a із використанням атомарної змінної
            data.a.addAndGet(a2);                                  //КД1

            // Обчислення3 Sн = R * MCн
            data.S = data.partialMultiplyVectorAndMatrix(data.R, data.MC, data.S, 1);

            // Сигнал за допомогою семафору про завершення обчислення а2 і Sн потокам T1, T3, T4
            data.S1.release();
            data.S3.release();
            data.S4.release();

            // Очікування на завершення обчислень а та Sн у потоках T1, Т3, T4
            data.S2.acquire(3);

            // Копіювання СP a2 = a
            // Вхід до КД2 або очікування на вихід T1, T3, T4 з КД2
            data.S6.acquire();
            a2 = data.a.get();                                     //КД2
            // Сигнал про вихід з КД2
            data.S6.release();

            // Використання механізму критичних секцій під час копіювання d2 = d
            // КД3, synchronized-блок із синхронізацією на об'єкті CS1
            synchronized (data.CS1) {
                d2 = data.d;                                       //КД3
            }

            // Обчислення4 Xн = S * MDн + a2 * Eн * d2
            data.X = data.calculateXn(data.S, data.MD, a2, data.E, d2, data.X, 1);

            // Сигнал за допомогою семафору потоку T3 про завершення обчислень Xн
            data.S5.release();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        // Повідомлення про завершення виконання потоку T2
        System.out.println("Task T2 is finished");
    }
}
