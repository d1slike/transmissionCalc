package ru.disdev.holder;

import ru.disdev.utils.ArrayUtils;
import ru.disdev.utils.MD5Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FourthTableHolder {
    private static FourthTableHolder ourInstance = new FourthTableHolder();

    public static FourthTableHolder getInstance() {
        return ourInstance;
    }

    private Map<String, Double> dataMap = new HashMap<>();
    private Set<Double> xVariants = new HashSet<>();
    private Set<Double> zVariants = new HashSet<>();


    private FourthTableHolder() {
    }

    public void put(String key, Double value) {
        dataMap.put(key, value);
    }

    public void init(Set<Double> zVariants, Set<Double> xVariants) {
        this.xVariants.addAll(xVariants);
        this.zVariants.addAll(zVariants);
    }

    public double get(double z, double x) {
        if (z > 150) {
            z = 151;
        }
        z = ArrayUtils.findNearest(z, zVariants);
        x = ArrayUtils.findNearest(x, xVariants);
        return dataMap.get(MD5Utils.get(z, x));
    }
}
