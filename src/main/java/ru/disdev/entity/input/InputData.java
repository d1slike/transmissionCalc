package ru.disdev.entity.input;

import javafx.beans.property.*;
import ru.disdev.entity.input.conditional.Condition;
import ru.disdev.entity.input.conditional.DependOn;
import ru.disdev.entity.input.enums.*;

public class InputData {
    @ComboBox(name = "Материал", description = "Материал шестерни и колеса", enumClass = Material.class)
    private ObjectProperty<Material> material = new SimpleObjectProperty<>();
    @ComboBox(name = "Вид колеса", description = "Вид колеса", enumClass = CircleType.class)
    private ObjectProperty<CircleType> circleType = new SimpleObjectProperty<>();
    @TextField(name = "bw", description = "Ширина венца")
    private DoubleProperty bw = new SimpleDoubleProperty();
    @TextField(name = "aw", description = "Межосевое расстояние")
    private DoubleProperty aw = new SimpleDoubleProperty();
    @ComboBox(name = "Прочность зубьев", description = "Прочность поверхности зубьев", enumClass = StrengthTeeth.class)
    private ObjectProperty<StrengthTeeth> strengthTeeth = new SimpleObjectProperty<>();
    @ComboBox(name = "Расположение вала", description = "Расположение вала", enumClass = ShaftPosition.class)
    private ObjectProperty<ShaftPosition> shaftPosition = new SimpleObjectProperty<>();
    @TextField(name = "L", description = "Расстояние межуду опорами вала")
    private DoubleProperty L = new SimpleDoubleProperty();
    @TextField(name = "dоп", description = "Диаметр вала под опорами")
    private DoubleProperty dop = new SimpleDoubleProperty();
    @TextField(name = "T1", description = "Крутящиц момент")
    private DoubleProperty t1 = new SimpleDoubleProperty();
    @ComboBox(name = "Теормообработка", description = "Теормообработка", enumClass = Curing.class)
    private ObjectProperty<Curing> curing = new SimpleObjectProperty<>();
    @ComboBox(name = "dm", description = "Толщина упроченного слоя", enumClass = SymplisticLayer.class)
    private ObjectProperty<SymplisticLayer> symplisticLayer = new SimpleObjectProperty<>();
    @CheckBox(name = "Ступенчатая нагрузка", description = "Ступенчатая нагрузка")
    @Condition(1)
    private BooleanProperty stepLoad = new SimpleBooleanProperty();
    @TextField(name = "n", description = "Частота вращения")
    @DependOn(id = 1, showOn = CheckBoxState.UNCHEKED)
    private DoubleProperty n = new SimpleDoubleProperty();
    @TextField(name = "tч", description = "Полноче число циклов работы за расчетный срок служы")
    @DependOn(id = 1, showOn = CheckBoxState.UNCHEKED)
    private DoubleProperty t = new SimpleDoubleProperty();
    @TextField(name = "Ti1", description = "Крутящий момент соотвествующий i-ой циклограмме нагрузки")
    @DependOn(id = 1, showOn = CheckBoxState.CHECKED)
    private DoubleProperty Ti1 = new SimpleDoubleProperty();
    @TextField(name = "N", description = "Сумарное число циклов перемены нагружения")
    @DependOn(id = 1, showOn = CheckBoxState.CHECKED)
    private DoubleProperty N = new SimpleDoubleProperty();
    @TextField(name = "nцi", description = "Число циклов перемены напряжения за вермя действия момениа")
    @DependOn(id = 1, showOn = CheckBoxState.CHECKED)
    private DoubleProperty ni = new SimpleDoubleProperty();

}
