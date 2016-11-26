package ru.disdev.entity.input.enums;

public enum CircleType {
    P("Прямозубые"), K_AND_SH("Косозубые и шевронные");
    private final String description;

    CircleType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
