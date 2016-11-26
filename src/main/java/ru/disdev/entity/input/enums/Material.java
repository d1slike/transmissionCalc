package ru.disdev.entity.input.enums;

public enum Material {
    S_S("Сталь-сталь"),
    S_CH("Сталь-чугун"),
    S_B("Сталь-бронза"),
    CH_CH("Чугун-чугун"),
    T_S("Текстолит-сталь"),
    D_S("ДСП-сталь"),
    P_S("Полиамид-сталь");
    private final String translation;

    Material(String translation) {
        this.translation = translation;
    }

    public String toString() {
        return translation;
    }
}
