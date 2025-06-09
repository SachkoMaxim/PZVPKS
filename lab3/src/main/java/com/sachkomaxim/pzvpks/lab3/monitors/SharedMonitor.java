package com.sachkomaxim.pzvpks.lab3.monitors;

public class SharedMonitor {
    private int a = Integer.MAX_VALUE;
    private int d;

    public synchronized int copya() {
        return a;
    }

    public synchronized int copyd() {
        return d;
    }

    public synchronized void writed(int value) {
        d = value;
    }

    public synchronized void findMina(int ai) {
        a = Math.min(a, ai);
    }
}
