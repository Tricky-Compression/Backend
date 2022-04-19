package ru.tricky_compression;

import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private final List<Element> dataBase = new ArrayList<>();

    public void add(Element element) {
        dataBase.add(element);
        System.out.println("size = " + dataBase.size());
    }

    public Element get(int id) {
        if (id < dataBase.size()) {
            return dataBase.get(id);
        } else {
            return null;
        }
    }
}
