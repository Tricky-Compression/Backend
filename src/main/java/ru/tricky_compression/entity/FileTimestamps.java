package ru.tricky_compression.entity;

@SuppressWarnings("unused")
public class FileTimestamps {
    private final Timestamps timestamps = new Timestamps();
    private String filename;

    public FileTimestamps() {
    }

    public FileTimestamps(String filename) {
        this.filename = filename;
    }

    public final Timestamps getTimestamps() {
        return timestamps;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
