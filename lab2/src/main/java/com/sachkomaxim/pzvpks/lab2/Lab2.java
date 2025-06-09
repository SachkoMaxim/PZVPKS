package com.sachkomaxim.pzvpks.lab2;

import com.sachkomaxim.pzvpks.lab2.data.Data;
import com.sachkomaxim.pzvpks.lab2.threads.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Lab2 {
    public static void main(String[] args) {
        // Очікування налаштування користувачем кількості виділених ядер для програми
        // Не впливає на вимірюваний час виконання програми
        System.out.print("Press Enter after setting the number of cores allocated to the program");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Lab2 is started");

        int N = 10000; // Розмір векторів і квадратних матриць
        //int N = 4; // Розмір векторів і квадратних матриць
        int P = 4; // Кількість процесорів

        Data data = new Data(N, P);

        // Створення потоків
        T1 T1 = new T1(data);
        T2 T2 = new T2(data);
        T3 T3 = new T3(data);
        T4 T4 = new T4(data);

        // Час початку виконання потоків
        double startTime = (double) System.nanoTime() / 1000000000F;

        T1.start();
        T2.start();
        T3.start();
        T4.start();

        try {
            T1.join();
            T2.join();
            T3.join();
            T4.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        double endTime = (double) System.nanoTime() / 1000000000F;
        Object time = (Object) (endTime - startTime);
        System.out.println("Lab2: the program execution time was " + String.format("%.2f с", time));

        System.out.println("Lab2 is finished");
    }
}