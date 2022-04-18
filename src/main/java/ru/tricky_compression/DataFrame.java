package ru.tricky_compression;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

public class DataFrame {
    private String dataFrame;

    public DataFrame(String dataFrame) {
        this.dataFrame = dataFrame;
    }

    public DataFrame() {}

    public String getDataFrame() {
        return dataFrame;
    }
}
