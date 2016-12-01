package ru.disdev.entity.input.enums;

public enum GearingType {
    _1("Внешнее"), _2("Внутреннее");

    private final String description;

    GearingType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
