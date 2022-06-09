package ru.tricky_compression.database;

import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private final List<Element> dataBase = new ArrayList<>();

    public void add(Element element) {
        dataBase.add(element);
    }

    public Element get(int id) {
        return dataBase.get(id);
    }

    public int size() {
        return dataBase.size();
    }
}
