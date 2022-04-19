package ru.tricky_compression;

public class File {
    private String filename;

    private char[] data;

    File() {}

    File(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public char[] getData() {
        return data;
    }
}
