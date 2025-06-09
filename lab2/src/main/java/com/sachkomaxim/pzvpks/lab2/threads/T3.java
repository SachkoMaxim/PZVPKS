package com.sachkomaxim.pzvpks.lab2.threads;

import com.sachkomaxim.pzvpks.lab2.data.Data;

import java.util.concurrent.BrokenBarrierException;

public class T3 extends Thread {
    private final Data data;

    public T3(Data data) {
        this.data = data;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T3
        System.out.println("Task T3 is started");

        try {
            // Локальні змінні
            int a3;
            int d3;

            // Введення B, d
            data.B = data.putValuesIntoVector();
            // Синхронізована ініціалізація змінної d (для використання synchronized під час копіювання d)
            synchronized (data.CS1) {
                data.d = data.putValueIntoScalar();
            }

            // Сигнал і очікування введення даних у потоках T1, T2, T4
            data.B1.await();

            // Обчислення1 a3 = Bн * Zн
            a3 = data.partialScalarProductOfTwoVectors(data.B, data.Z, 2);

            // Обчислення2 а = а + а3
            // КД1, захищений запис нового значення спільного ресурсу a із використанням атомарної змінної
            data.a.addAndGet(a3);                                  //КД1

            // Обчислення3 Sн = R * MCн
            data.S = data.partialMultiplyVectorAndMatrix(data.R, data.MC, data.S, 2);

            // Сигнал за допомогою семафору про завершення обчислення а3 і Sн потокам T1, T2, T4
            data.S1.release();
            data.S2.release();
            data.S4.release();

            // Очікування на завершення обчислень а та Sн у потоках T1, Т2, T4
            data.S3.acquire(3);

            // Копіювання СP a3 = a
            // Вхід до КД2 або очікування на вихід T1, T2, T4 з КД2
            data.S6.acquire();
            a3 = data.a.get();                                     //КД2
            // Сигнал про вихід з КД2
            data.S6.release();

            // Використання механізму критичних секцій під час копіювання d3 = d
            // КД3, synchronized-блок із синхронізацією на об'єкті CS1
            synchronized (data.CS1) {
                d3 = data.d;                                       //КД3
            }

            // Обчислення4 Xн = S * MDн + a3 * Eн * d3
            data.X = data.calculateXn(data.S, data.MD, a3, data.E, d3, data.X, 2);

            // Очікування за допомогою семафору на завершення обчислень Xн у потоках T1, Т2, T4
            data.S5.acquire(3);

            //Виведення результату X
            Data.printVector(data.X, "X");
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        // Повідомлення про завершення виконання потоку T3
        System.out.println("Task T3 is finished");
    }
}
