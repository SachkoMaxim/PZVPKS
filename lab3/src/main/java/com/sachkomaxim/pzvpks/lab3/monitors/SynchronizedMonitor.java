package com.sachkomaxim.pzvpks.lab3.monitors;

public class SynchronizedMonitor {
    private int F1 = 0;
    private int F2 = 0;
    private int F3 = 0;
    private int F4 = 0;
    private int F5 = 0;
    private int F6 = 0;
    private int F7 = 0;

    public synchronized void signalInput() {
        F1++;
        if (F1 == 2) {
            notifyAll();
        }
    }

    public synchronized void waitInput() {
        try {
            if (F1 != 2) {
                wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void signalMina() {
        F2++;
        if (F2 == 4) {
            notifyAll();
        }
    }

    public synchronized void waitMina() {
        try {
            if (F2 != 4) {
                wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void signalCalcAh2to1() {
        F3++;
        if (F3 == 1) {
            notifyAll();
        }
    }

    public synchronized void waitCalcAh2to1() {
        try {
            if (F3 != 1) {
                wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void signalCalcAh4to3() {
        F4++;
        if (F4 == 1) {
            notifyAll();
        }
    }

    public synchronized void waitCalcAh4to3() {
        try {
            if (F4 != 1) {
                wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void signalCalcA2h3to1() {
        F5++;
        if (F5 == 1) {
            notifyAll();
        }
    }

    public synchronized void waitCalcA2h3to1() {
        try {
            if (F5 != 1) {
                wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void signalCalcA() {
        F6++;
        if (F6 == 1) {
            notifyAll();
        }
    }

    public synchronized void waitCalcA() {
        try {
            if (F6 != 1) {
                wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void signalCalcX() {
        F7++;
        if (F7 == 3) {
            notifyAll();
        }
    }

    public synchronized void waitCalcX() {
        try {
            if (F7 != 3) {
                wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
