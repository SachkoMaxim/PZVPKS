#include <iostream>
#include <omp.h>
#include <algorithm>
#include <sstream>
#include <chrono>

using namespace std;

// Оголошення функцій
void initVariables();
void putValuesIntoVector(int64_t *);
void putValuesIntoB(int64_t *);
void putValuesIntoMatrix(int64_t **);
int findMinValInVector(const int64_t *, int);
void calculateAh();
void calculateE(int64_t, int64_t, int);
void printVector(int64_t *, const std::string&);

// Константи
const int N = 2000;
const int P = 4;
const int H = N / P;

// Скаляри
int64_t a;
int64_t d;

// Вектори
int64_t *A;
int64_t *B;
int64_t *Q;
int64_t *E;
int64_t *A_MM_MO;

// Матриці
int64_t **MR;
int64_t **MM;
int64_t **MO;
int64_t **MM_MO;

int main() {
    // Очікування, поки користувач натисне на Enter
    std::cin.get();

    std::cout << "Lab4 is started\n";

    // Початок вимірювання часу виконання
    auto startTime = std::chrono::high_resolution_clock::now();

    // Створення масивів
    initVariables();

    int T_id;
    int64_t a_i, d_i;

    // Створення потоків та їхній запуск
#pragma omp parallel num_threads(P) private(T_id, a_i, d_i) shared(a, d)
    {
        // Отримання номеру потоку
        T_id = omp_get_thread_num();

        // Виведення інформації про початок роботи потоку
#pragma omp critical
        {
            cout << "Task T" << T_id + 1 << " is started\n";
        }

        // Введення даних
        switch (T_id)
        {
        case 0: // T1: Введення MR, Q
            putValuesIntoMatrix(MR);
            putValuesIntoVector(Q);
            break;
        case 1: // T2: Введення B, MM
            // Встановлення для деяких елементів B значень, відмінних від 4,
            // для перевірки правильності знаходження мінімального значення
            putValuesIntoB(B);
            putValuesIntoMatrix(MM);
            break;
        case 2: // T3: Введення MO, d
            putValuesIntoMatrix(MO);
            d = 1;
            break;
        }

        // Бар'єр B1 для синхронізації по введенню даних
#pragma omp barrier

        // Обчислення1 ai = min(Bн)
        a_i = findMinValInVector(B, T_id);

        // Обчислення2 a = min(а, аi)
        // Захищений запис a = min(a, ai) в спільний ресурс a за допомогою CS1
        // КД1
#pragma omp critical(CS1)
        {
            a = min(a, a_i);
        }

        // Обчислення3 Aн = (B * MRн)
        calculateAh();

        // Бар'єр B2 для синхронізації по обчисленню a та A
#pragma omp barrier

        // Копія ai = a
        // КД2
        // Захищене копіювання спільного ресурсу а за допомогою CS2
#pragma omp critical(CS2)
        {
            a_i = a;
        }

        // Копія di = d
        // КД3
        // Захищене копіювання спільного ресурсу d за допомогою CS3
#pragma omp critical(CS3)
        {
            d_i = d;
        }

        // Обчислення4 Eн = A * (MM * MOн) + ai * Qн * di
        calculateE(a_i, d_i, T_id);

        // Бар'єр B3 для синхронізації по обчисленню E
#pragma omp barrier

        // Виведення інформації про завершення роботи потоку
#pragma omp critical
        {
            // Виведення результату обчислень
            if (T_id == 0) { // T1
                printVector(E, "Calculation result: E");
            }
            cout << "Task T" << T_id + 1 << " is finished\n";
        }
    }

    // Виведення часу виконання
    auto endTime = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "Lab4: the program execution time was " << (double)duration.count() / 1000.0F << " seconds" << std::endl;

    return 0;
}

void initVariables() {
    a = INT64_MAX;

    A = new int64_t[N];
    B = new int64_t[N];
    Q = new int64_t[N];
    E = new int64_t[N];
    A_MM_MO = new int64_t[N];

    MR = new int64_t *[N];
    MM = new int64_t *[N];
    MO = new int64_t *[N];
    MM_MO = new int64_t *[N];

    for (int i = 0; i < N; ++i) {
        MR[i] = new int64_t[N];
        MM[i] = new int64_t[N];
        MO[i] = new int64_t[N];
        MM_MO[i] = new int64_t[N];

        A[i] = 0;
        A_MM_MO[i] = 0;
        E[i] = 0;
    }
}

void putValuesIntoVector(int64_t *vector) {
    for (int i = 0; i < N; i++) {
        vector[i] = 1;
    }
}

void putValuesIntoB(int64_t *vector) {
    // Встановлення для деяких елементів B значень, відмінних від 4,
    // для перевірки правильності знаходження мінімального значення
    for (int i = 0; i < N; i++) {
        vector[i] = 4;
    }
    vector[0] = 2;
    vector[2 * H] = 1;
    vector[3 * H] = 3;
}

void putValuesIntoMatrix(int64_t **matrix) {
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            matrix[i][j] = 1;
        }
    }
}

int findMinValInVector(const int64_t *vector, int T_id) {
    int64_t a_i = INT64_MAX;

    int start = T_id * H;
    int end = (T_id + 1) * H;

    for (int i = start; i < end; i++) {
        if (vector[i] < a_i) {
            a_i = vector[i];
        }
    }

    return a_i;
}

void calculateAh() {
    // Обчислення3 Aн = (B * MRн)
    int T_id = omp_get_thread_num();
    int start = T_id * H;
    int end = (T_id + 1) * H;

    for (int i = start; i < end; i++) {
        A[i] = 0;
        for (int j = 0; j < N; j++) {
            A[i] += B[j] * MR[j][i];
        }
    }
}

void calculateE(int64_t a_i, int64_t d_i, int T_id) {
    // Обчислення4 Eн = A * (MM * MOн) + ai * Qн * di
    int start = T_id * H;
    int end = (T_id + 1) * H;

    // (MM * MOн)
    for (int j = start; j < end; j++) {
        for (int i = 0; i < N; i++) {
            MM_MO[i][j] = 0;
            for (int k = 0; k < N; k++) {
                MM_MO[i][j] += MM[i][k] * MO[k][j];
            }
        }
    }

    // A * (MM * MOн)
    for (int i = start; i < end; i++) {
        A_MM_MO[i] = 0;
        for (int j = 0; j < N; j++) {
            A_MM_MO[i] += A[j] * MM_MO[j][i];
        }
    }

    // A * (MM * MOн) + ai * Qн * di
    for (int i = start; i < end; i++) {
        E[i] = A_MM_MO[i] + (a_i * Q[i] * d_i);
    }
}

void printVector(int64_t *vec, const std::string& resultText) {
    std::stringstream ss;
    ss << resultText << " = { ";
    for (int i = 0; i < N; ++i) {
        ss << vec[i] << "; ";
    }
    ss << "}\n";
    std::cout << ss.str();
}
