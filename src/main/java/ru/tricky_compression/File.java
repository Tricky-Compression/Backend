package ru.tricky_compression;

public class File {
    long clientStart;
    long clientEnd;
    long serverStart;
    long serverEnd;

    private String filename;

    private byte[] data;

    File() {
    }

    File(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

    public void setServerStart() {
        serverStart = System.nanoTime();
    }

    public void setServerEnd() {
        serverEnd = System.nanoTime();
    }

}
