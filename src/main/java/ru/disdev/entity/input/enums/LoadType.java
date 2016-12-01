package ru.disdev.entity.input.enums;

public enum LoadType {
    REV("Реверсивная"), NOT_REV("Нереверсивная");

    private final String description;

    LoadType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
