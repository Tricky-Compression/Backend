package ru.tricky_compression.entity;

public class FileData {

    Timestamps timestamps = new Timestamps();

    private String filename;

    private byte[] data;

    public FileData() {
    }

    public FileData(String filename) {
        this.filename = filename;
    }

    public final Timestamps getTimestamps() {
        return timestamps;
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
