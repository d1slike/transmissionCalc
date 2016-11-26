package ru.disdev.entity.input.enums;

public enum ShaftPosition {
    SYM("Симметричное"), NOT_SYM("Несимметричное"), CONSOLE("Консольное");
    private final String description;

    ShaftPosition(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
