package ru.disdev.holder;


public class FirstTableHolder {

    private static FirstTableHolder ourInstance = new FirstTableHolder();

    public static FirstTableHolder getInstance() {
        return ourInstance;
    }

    private FirstTableHolder() {

    }
}
