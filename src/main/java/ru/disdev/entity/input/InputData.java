package ru.disdev.entity.input;

import javafx.beans.property.*;
import ru.disdev.entity.Type;
import ru.disdev.entity.input.conditional.Condition;
import ru.disdev.entity.input.conditional.DependOn;

public class InputData {
    @TextField(name = "a", description = "gg")
    @DependOn(id = 1, showOn = CheckBoxState.UNCHEKED)
    private IntegerProperty a = new SimpleIntegerProperty();
    @CheckBox(name = "val", description = "%45")
    @Condition(1)
    private BooleanProperty b = new SimpleBooleanProperty();
    @ComboBox(name = "11", description = "g45", enumClass = Type.class)
    @DependOn(id = 1, showOn = CheckBoxState.CHECKED)
    private ObjectProperty<Type> c = new SimpleObjectProperty<>();

    public Number getA() {
        return a.get();
    }

    public IntegerProperty aProperty() {
        return a;
    }
}
