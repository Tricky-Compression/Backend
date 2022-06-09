package ru.tricky_compression.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

public class DataFrame {
    private String content;

    public DataFrame(String dataFrame) {
        this.content = dataFrame;
    }

    public DataFrame() {}

    public String getContent() {
        return content;
    }
}
