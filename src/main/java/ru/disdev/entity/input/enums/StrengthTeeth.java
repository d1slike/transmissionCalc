package ru.disdev.entity.input.enums;

public enum StrengthTeeth {
    GTE_350(">= 350"), LT_350("< 350");
    private final String description;

    StrengthTeeth(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
