package ru.disdev.entity.input;

import javafx.beans.property.*;
import ru.disdev.entity.Type;

public class InputData {
    @TextField(name = "a", description = "gg")
    private IntegerProperty a = new SimpleIntegerProperty();
    @CheckBox(name = "val", description = "%45")
    private BooleanProperty b = new SimpleBooleanProperty();
    @ComboBox(name = "11", description = "g45", enumClass = Type.class)
    private ObjectProperty<Type> c = new SimpleObjectProperty<>();

    public Number getA() {
        return a.get();
    }

    public IntegerProperty aProperty() {
        return a;
    }
}
