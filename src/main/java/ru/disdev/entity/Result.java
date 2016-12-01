package ru.disdev.entity;

import javafx.beans.property.*;
import ru.disdev.entity.input.enums.*;

public class Result {
    @Column(name = "Материал", type = Type.OBJECT, description = "Материал шестерни и колеса")
    private ObjectProperty<Material> material = new SimpleObjectProperty<>();
    @Column(name = "Вид колеса", type = Type.OBJECT, description = "Вид колеса")
    private ObjectProperty<CircleType> circleType = new SimpleObjectProperty<>();
    @Column(name = "Ширина венца", type = Type.NUMBER, description = "Ширина венца")
    private DoubleProperty bw = new SimpleDoubleProperty();
    @Column(name = "Межосевое расстояние", type = Type.NUMBER, description = "Межосевое расстояние")
    private DoubleProperty aw = new SimpleDoubleProperty();
    @Column(name = "Прочность зубьев", type = Type.OBJECT, description = "Прочность поверхности зубьев")
    private ObjectProperty<StrengthTeeth> strengthTeeth = new SimpleObjectProperty<>();
    @Column(name = "Расположение вала", type = Type.OBJECT, description = "Расположение вала")
    private ObjectProperty<ShaftPosition> shaftPosition = new SimpleObjectProperty<>();
    @Column(name = "Расстояние межуду опорами вала", type = Type.NUMBER, description = "Расстояние межуду опорами вала")
    private DoubleProperty L = new SimpleDoubleProperty();
    @Column(name = "Диаметр вала под опорами", type = Type.NUMBER, description = "Диаметр вала под опорами")
    private DoubleProperty dop = new SimpleDoubleProperty();
    @Column(name = "Крутящиц момент", type = Type.NUMBER, description = "Крутящиц момент")
    private DoubleProperty t1 = new SimpleDoubleProperty();
    @Column(name = "Теормообработка", type = Type.OBJECT, description = "Теормообработка")
    private ObjectProperty<Curing> curing = new SimpleObjectProperty<>();
    @Column(name = "Толщина упроченного слоя", type = Type.OBJECT, description = "Толщина упроченного слоя")
    private ObjectProperty<SymplisticLayer> symplisticLayer = new SimpleObjectProperty<>();

    @Column(name = "Ступенчатая нагрузка", type = Type.BOOLEAN, description = "Ступенчатая нагрузка")
    private BooleanProperty stepLoad = new SimpleBooleanProperty();
    @Column(name = "Частота вращения", type = Type.NUMBER, description = "Частота вращения")
    private DoubleProperty _n = new SimpleDoubleProperty();
    @Column(name = "Tч", type = Type.NUMBER, description = "Полноче число циклов работы за расчетный срок служы")
    private DoubleProperty t = new SimpleDoubleProperty();
    @Column(name = "Ti1", type = Type.NUMBER, description = "Крутящий момент соотвествующий i-ой циклограмме нагрузки")
    private DoubleProperty Ti1 = new SimpleDoubleProperty();
    @Column(name = "N", type = Type.NUMBER, description = "Сумарное число циклов перемены нагружения")
    private DoubleProperty N = new SimpleDoubleProperty();
    @Column(name = "nцi", type = Type.NUMBER, description = "Число циклов перемены напряжения за вермя действия момениа")
    private DoubleProperty ni = new SimpleDoubleProperty();
    @Column(name = "Коэффициент смещения", type = Type.NUMBER, description = "Коэффициент смещения")
    private DoubleProperty x = new SimpleDoubleProperty();
    @Column(name = "Эквивалетное число зубьев", type = Type.NUMBER, description = "Эквивалетное число зубьев")
    private DoubleProperty z = new SimpleDoubleProperty();
    @Column(name = "Тип нагрузки", type = Type.OBJECT, description = "Тип нагрузки")
    private ObjectProperty<LoadType> loadTypeForSigm = new SimpleObjectProperty<>();
    @Column(name = "Коэффициент mf", type = Type.NUMBER, description = "Коэффициент mf")
    private DoubleProperty mF = new SimpleDoubleProperty();
    @Column(name = "Передаточное чилсло", description = "Передаточное чилсло", type = Type.NUMBER)
    private DoubleProperty u = new SimpleDoubleProperty();
    @Column(name = "PhiBa", type = Type.NUMBER, description = "OТношение ширины венца к межосевому расстоянию")
    private DoubleProperty phiBa = new SimpleDoubleProperty();
    @Column(name = "Зацепление", description = "Тип зацепеления", type = Type.OBJECT)
    private ObjectProperty<GearingType> gearingType = new SimpleObjectProperty<>();


    @Column(name = "Межосевое расстояние", description = "Межосевое расстояние", type = Type.NUMBER)
    private DoubleProperty a = new SimpleDoubleProperty();
    @Column(name = "m", description = "Модуль выносливаости по изгибу", type = Type.NUMBER)
    private DoubleProperty m = new SimpleDoubleProperty();
    @Column(name = "m'", description = "Модуль выносливаости по изгибу(округленный)", type = Type.NUMBER)
    private DoubleProperty mm = new SimpleDoubleProperty();


    public Material getMaterial() {
        return material.get();
    }

    public ObjectProperty<Material> materialProperty() {
        return material;
    }

    public CircleType getCircleType() {
        return circleType.get();
    }

    public ObjectProperty<CircleType> circleTypeProperty() {
        return circleType;
    }

    public double getBw() {
        return bw.get();
    }

    public DoubleProperty bwProperty() {
        return bw;
    }

    public double getAw() {
        return aw.get();
    }

    public DoubleProperty awProperty() {
        return aw;
    }

    public StrengthTeeth getStrengthTeeth() {
        return strengthTeeth.get();
    }

    public ObjectProperty<StrengthTeeth> strengthTeethProperty() {
        return strengthTeeth;
    }

    public ShaftPosition getShaftPosition() {
        return shaftPosition.get();
    }

    public ObjectProperty<ShaftPosition> shaftPositionProperty() {
        return shaftPosition;
    }

    public double getL() {
        return L.get();
    }

    public DoubleProperty lProperty() {
        return L;
    }

    public double getDop() {
        return dop.get();
    }

    public DoubleProperty dopProperty() {
        return dop;
    }

    public double getT1() {
        return t1.get();
    }

    public DoubleProperty t1Property() {
        return t1;
    }

    public Curing getCuring() {
        return curing.get();
    }

    public ObjectProperty<Curing> curingProperty() {
        return curing;
    }

    public SymplisticLayer getSymplisticLayer() {
        return symplisticLayer.get();
    }

    public ObjectProperty<SymplisticLayer> symplisticLayerProperty() {
        return symplisticLayer;
    }

    public boolean isStepLoad() {
        return stepLoad.get();
    }

    public BooleanProperty stepLoadProperty() {
        return stepLoad;
    }

    public double get_n() {
        return _n.get();
    }

    public DoubleProperty _nProperty() {
        return _n;
    }

    public double getT() {
        return t.get();
    }

    public DoubleProperty tProperty() {
        return t;
    }

    public double getTi1() {
        return Ti1.get();
    }

    public DoubleProperty ti1Property() {
        return Ti1;
    }

    public double getNi() {
        return ni.get();
    }

    public DoubleProperty niProperty() {
        return ni;
    }

    public double getN() {
        return N.get();
    }

    public DoubleProperty nProperty() {
        return N;
    }

    public double getX() {
        return x.get();
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public double getZ() {
        return z.get();
    }

    public DoubleProperty zProperty() {
        return z;
    }

    public LoadType getLoadTypeForSigm() {
        return loadTypeForSigm.get();
    }

    public ObjectProperty<LoadType> loadTypeForSigmProperty() {
        return loadTypeForSigm;
    }

    public double getmF() {
        return mF.get();
    }

    public DoubleProperty mFProperty() {
        return mF;
    }

    public double getU() {
        return u.get();
    }

    public DoubleProperty uProperty() {
        return u;
    }

    public double getPhiBa() {
        return phiBa.get();
    }

    public DoubleProperty phiBaProperty() {
        return phiBa;
    }

    public GearingType getGearingType() {
        return gearingType.get();
    }

    public ObjectProperty<GearingType> gearingTypeProperty() {
        return gearingType;
    }

    public double getA() {
        return a.get();
    }

    public DoubleProperty aProperty() {
        return a;
    }

    public void setA(double a) {
        this.a.set(a);
    }

    public void setM(double m) {
        this.m.set(m);
    }

    public void setMm(double mm) {
        this.mm.set(mm);
    }
}
