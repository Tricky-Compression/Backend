package ru.tricky_compression;

import java.util.stream.IntStream;

public class DataFrame {
    private final IntStream dataFrame;

    public DataFrame(IntStream dataFrame) {
        this.dataFrame = dataFrame;
    }

    public IntStream getDataFrame() {
        return dataFrame;
    }
}
