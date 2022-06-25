package ru.tricky_compression.entity;

@SuppressWarnings("unused")
public class Timestamps {
    private long clientStart;
    private long clientEnd;
    private long serverStart;
    private long serverEnd;

    public long getClientStart() {
        return clientStart;
    }

    public long getClientEnd() {
        return clientEnd;
    }

    public long getServerStart() {
        return serverStart;
    }

    public long getServerEnd() {
        return serverEnd;
    }

    public void setClientStart(long clientStart) {
        this.clientStart = clientStart;
    }

    public void setClientEnd(long clientEnd) {
        this.clientEnd = clientEnd;
    }

    public void setServerStart() {
        serverStart = System.nanoTime();
    }

    public void setServerEnd() {
        serverEnd = System.nanoTime();
    }
}