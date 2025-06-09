package com.sachkomaxim.pzvpks.lab1;

import com.sachkomaxim.pzvpks.lab1.data.Data;
import com.sachkomaxim.pzvpks.lab1.threads.*;

public class Lab1 {
    public static void main(String[] args) {
        // Виведення повідомлення про початок виконання головного потоку
        System.out.println("Main thread (Lab1) is started");

        // Отримання значення n
        int n = Data.getN();

        int inputOption = 0;

        // Якщо ж n >= 1000, то відбувається отримання варіанту надавання вхідних даних
        if (n >= 1000) {
            inputOption = Data.getInputOptionFromConsole();
        }

        // Час початку виконання потоків
        double startTime = (double) System.nanoTime() / 1000000000F;

        // Створення потоків із заданням отриманих налаштувань у конструктор
        T1 t1 = new T1(n, inputOption);
        T2 t2 = new T2(n, inputOption);
        T3 t3 = new T3(n, inputOption);

        // Встановлення пріоритетів для потоків
        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);
        t3.setPriority(Thread.NORM_PRIORITY);

        // Запуск потоків
        t1.start();
        t2.start();
        t3.start();

        // Очікування завершення виконання потоків
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Якщо ж n >= 1000, то вивести час виконання програми
        if (n >= 1000) {
            double endTime = (double) System.nanoTime() / 1000000000F;
            Object time = (Object) (endTime - startTime);
            System.out.println("Main thread (Lab1): the program execution time was " + String.format("%.2f с", time));
        }

        // Виведення повідомлення про завершення виконання головного потоку
        System.out.println("Main thread (Lab1) is finished");
    }
}
