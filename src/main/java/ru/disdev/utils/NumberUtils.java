package ru.disdev.utils;

public class NumberUtils {
    public static Double parseDoubleORNull(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception ignored) {

        }
        return null;
    }
}
