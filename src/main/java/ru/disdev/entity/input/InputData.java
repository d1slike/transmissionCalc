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
    @TextField(name = "Ширина венца", description = "Ширина венца")
    @Valid(min = 0.01, max = 300)
    private DoubleProperty bw = new SimpleDoubleProperty();
    @TextField(name = "Межосевое расстояние(мм)", description = "Межосевое расстояние")
    @Valid(min = 0.01, max = 200)
    private DoubleProperty aw = new SimpleDoubleProperty();
    @TextField(name = "Передаточное чилсло", description = "Передаточное чилсло")
    @Valid(min = 0.01, max = 100)
    private DoubleProperty u = new SimpleDoubleProperty();
    @TextField(name = "PhiBa", description = "OТношение ширины венца к межосевому расстоянию")
    private DoubleProperty phiBa = new SimpleDoubleProperty();
    @ComboBox(name = "Прочность зубьев", description = "Прочность поверхности зубьев", enumClass = StrengthTeeth.class)
    private ObjectProperty<StrengthTeeth> strengthTeeth = new SimpleObjectProperty<>();
    @ComboBox(name = "Расположение вала", description = "Расположение вала", enumClass = ShaftPosition.class)
    private ObjectProperty<ShaftPosition> shaftPosition = new SimpleObjectProperty<>();
    @TextField(name = "Расстояние межуду опорами вала", description = "Расстояние межуду опорами вала")
    private DoubleProperty L = new SimpleDoubleProperty();
    @TextField(name = "Диаметр вала под опорами", description = "Диаметр вала под опорами")
    private DoubleProperty dop = new SimpleDoubleProperty();
    @TextField(name = "Крутящиц момент", description = "Крутящиц момент")
    private DoubleProperty t1 = new SimpleDoubleProperty();
    @ComboBox(name = "Теормообработка", description = "Теормообработка", enumClass = Curing.class)
    private ObjectProperty<Curing> curing = new SimpleObjectProperty<>();
    @ComboBox(name = "Толщина упроченного слоя", description = "Толщина упроченного слоя", enumClass = SymplisticLayer.class)
    private ObjectProperty<SymplisticLayer> symplisticLayer = new SimpleObjectProperty<>();

    @CheckBox(name = "Ступенчатая нагрузка", description = "Ступенчатая нагрузка")
    @Condition(1)
    private BooleanProperty stepLoad = new SimpleBooleanProperty();
    @TextField(name = "Частота вращения", description = "Частота вращения")
    @DependOn(id = 1, showOn = CheckBoxState.UNCHEKED)
    private DoubleProperty n = new SimpleDoubleProperty();
    @TextField(name = "Tч", description = "Полноче число циклов работы за расчетный срок служы")
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

    @TextField(name = "Коэффициент смещения", description = "Коэффициент смещения")
    private DoubleProperty x = new SimpleDoubleProperty();
    @TextField(name = "Эквивалетное число зубьев", description = "Эквивалетное число зубьев")
    private DoubleProperty z = new SimpleDoubleProperty();
    @ComboBox(name = "Тип нагрузки", description = "Тип нагрузки", enumClass = LoadType.class)
    private ObjectProperty<LoadType> loadTypeForSigm = new SimpleObjectProperty<>();
    @TextField(name = "Коэффициент mf", description = "Коэффициент mf")
    private DoubleProperty mF = new SimpleDoubleProperty();

    @ComboBox(name = "Зацепление", description = "Тип зацепеления", enumClass = GearingType.class)
    private ObjectProperty<GearingType> gearingType = new SimpleObjectProperty<>();

}
