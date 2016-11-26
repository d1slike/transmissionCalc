package ru.disdev.entity.input.enums;

public enum SymplisticLayer {
    _1("1...3mm"),
    _2("(0,2...0,3)m"),
    _3("(0,2...0,25)m"),
    _4("(0,15...0,2)m"),
    _6("(0,1...0,13)m"),
    _7("---");
    private final String description;

    SymplisticLayer(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
