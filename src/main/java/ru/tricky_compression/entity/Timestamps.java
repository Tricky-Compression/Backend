package ru.tricky_compression.entity;

@SuppressWarnings("unused")
public class Timestamps {
    long clientStart;
    long clientEnd;
    long serverStart;
    long serverEnd;


    public final long getServerStart() {
        return serverStart;
    }

    public final long getServerEnd() {
        return serverEnd;
    }

    public final void setServerStart() {
        serverStart = System.nanoTime();
    }

    public final void setServerEnd() {
        serverEnd = System.nanoTime();
    }
}
