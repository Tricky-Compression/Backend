package ru.tricky_compression;

public class File {
    private String filename;

    private byte[] data;

    File() {}

    File(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }
}
