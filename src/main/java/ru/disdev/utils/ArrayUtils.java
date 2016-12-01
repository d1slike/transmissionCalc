package ru.disdev.utils;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Collection;

public class ArrayUtils {
    public static double findNearest(double value, Collection<Double> collection) {
        return collection.stream().map(v -> new ImmutablePair<>(value - v, v))
                .sorted((a, b) -> (int) Math.round(a.getLeft() - b.getLeft()))
                .findFirst()
                .get()
                .getRight();
    }
}
