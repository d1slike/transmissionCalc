package ru.disdev.holder;

import ru.disdev.utils.ArrayUtils;

import java.util.HashSet;
import java.util.Set;

public class SecondTableHolder {
    private static SecondTableHolder ourInstance = new SecondTableHolder();

    public static SecondTableHolder getInstance() {
        return ourInstance;
    }

    private Set<Double> values = new HashSet<>();

    public double find(double value) {
        return ArrayUtils.findNearest(value, values);
    }

    public void initValues(Set<Double> set) {
        values.addAll(set);
    }

    private SecondTableHolder() {
    }
}
