package ru.disdev.holder;

import ru.disdev.entity.TableData;

public class TableHolder {

    private static TableHolder ourInstance = new TableHolder();

    public static TableHolder getInstance() {
        return ourInstance;
    }

    private TableData tableData;

    private TableHolder() {

    }
}
