package com.sachkomaxim.pzvpks.lab2.threads;

import com.sachkomaxim.pzvpks.lab2.data.Data;

import java.util.concurrent.BrokenBarrierException;

public class T1 extends Thread {
    private final Data data;

    public T1(Data data) {
        this.data = data;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T1
        System.out.println("Task T1 is started");

        try {
            // Локальні змінні
            int a1;
            int d1;

            // Введення R, Z
            data.R = data.putValuesIntoVector();
            data.Z = data.putValuesIntoVector();

            // Сигнал і очікування введення даних у потоках T2, T3, T4
            data.B1.await();

            // Обчислення1 a1 = Bн * Zн
            a1 = data.partialScalarProductOfTwoVectors(data.B, data.Z, 0);

            // Обчислення2 а = а + а1
            // КД1, захищений запис нового значення спільного ресурсу a із використанням атомарної змінної
            data.a.addAndGet(a1);                                  //КД1

            // Обчислення3 Sн = R * MCн
            data.S = data.partialMultiplyVectorAndMatrix(data.R, data.MC, data.S, 0);

            // Сигнал за допомогою семафору про завершення обчислення а1 і Sн потокам T2, T3, T4
            data.S2.release();
            data.S3.release();
            data.S4.release();

            // Очікування на завершення обчислень а та Sн у потоках T2, Т3, T4
            data.S1.acquire(3);

            // Копіювання СP a1 = a
            // Вхід до КД2 або очікування на вихід T2, T3, T4 з КД2
            data.S6.acquire();
            a1 = data.a.get();                                     //КД2
            // Сигнал про вихід з КД2
            data.S6.release();

            // Використання механізму критичних секцій під час копіювання d1 = d
            // КД3, synchronized-блок із синхронізацією на об'єкті CS1
            synchronized (data.CS1) {
                d1 = data.d;                                       //КД3
            }

            // Обчислення4 Xн = S * MDн + a1 * Eн * d1
            data.X = data.calculateXn(data.S, data.MD, a1, data.E, d1, data.X, 0);

            // Сигнал за допомогою семафору потоку T3 про завершення обчислень Xн
            data.S5.release();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        // Повідомлення про завершення виконання потоку T1
        System.out.println("Task T1 is finished");
    }
}
