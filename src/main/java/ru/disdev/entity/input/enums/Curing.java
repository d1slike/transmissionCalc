package ru.disdev.entity.input.enums;

public enum Curing {
    _1("Улучшение"),
    _2("Закалка"),
    _3("ТВЧ скваозная"),
    _4("ТВЧ поверхностная"),
    _5("Нормализация"),
    _6("Закалка объемная"),
    _7("Цементация"),
    _8("Азотрироване"),
    _9("С высоким отпуском"),
    _10("Нитроцементация"),
    _11("---");
    private String description;

    Curing(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        values();
        return description;
    }
}
