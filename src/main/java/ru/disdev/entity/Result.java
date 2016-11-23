package ru.disdev.entity;

import javafx.beans.property.*;

public class Result {

    @Column(name = "A", description = "test A", type = Type.STRING)
    private final StringProperty a = new SimpleStringProperty();
    @Column(name = "B", description = "test B", type = Type.NUMBER)
    private final IntegerProperty b = new SimpleIntegerProperty();
    @Column(name = "C", description = "test 3333", type = Type.NUMBER)
    private final DoubleProperty c = new SimpleDoubleProperty();

    public String getA() {
        return a.get();
    }

    public StringProperty aProperty() {
        return a;
    }

    public int getB() {
        return b.get();
    }

    public ReadOnlyIntegerProperty bProperty() {
        return b;
    }

    public double getC() {
        return c.get();
    }

    public DoubleProperty cProperty() {
        return c;
    }
}
