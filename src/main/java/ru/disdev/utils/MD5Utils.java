package ru.disdev.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.stream.Stream;

public class MD5Utils {
    public static String get(Object... objects) {
        StringBuilder builder = new StringBuilder();
        Stream.of(objects).forEach(o -> builder.append(o.toString()));
        return DigestUtils.md5Hex(builder.toString());
    }
}
