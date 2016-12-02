package ru.disdev.calculator;

import javafx.beans.property.Property;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.disdev.entity.Column;
import ru.disdev.entity.Result;
import ru.disdev.entity.input.InputData;
import ru.disdev.entity.input.enums.GearingType;
import ru.disdev.entity.table.FifthTableData;
import ru.disdev.holder.*;

import java.util.HashMap;
import java.util.Map;

public class Calculator {

    private static final double KMA = 1400;

    private final InputData inputData;
    private final Result result = new Result();

    private Calculator(InputData inputData) {
        this.inputData = inputData;
    }

    private Result getResult() throws Exception {
        mergeInputData();
        calcA();
        calcM();
        return result;
    }

    private void calcM() {
        double Yf1 = FourthTableHolder.getInstance().get(result.getZ(), result.getX());
        result.setYf1(Yf1);
        double a = result.getA();
        double bw = result.getBw();
        FifthTableData fifthTableData = FifthTableHolder.getInstance()
                .get(result.getCuring(), result.getLoadTypeForSigm());
        double sigm_FP = fifthTableData.getSigmFP();
        result.setSigm_FP(sigm_FP);
        double Nf0 = fifthTableData.getNfo();
        result.setNf0(Nf0);
        double NFe = 60 * result.getT() * result.get_n();
        result.setNFe(NFe);
        double Kfl = Math.pow(Nf0 / NFe, result.getmF());
        result.setKf1(Kfl);
        double sigmFp = sigm_FP * Kfl;
        result.setSigmFP(sigmFp);
        double arg = result.getGearingType() == GearingType._2 ? -1 : 1;
        double m = KMA * result.getT1() * (result.getU() + arg) * Yf1 /
                (a * bw * sigmFp);
        result.setM(m);
        result.setMm(SecondTableHolder.getInstance().find(m));
    }

    private void calcA() {
        double Ka = ThirdTableHolder.getInstance()
                .find(result.getMaterial(), result.getCircleType())
                .getKa();
        result.setKa(Ka);
        double u = result.getU();
        double dw1 = 2 * result.getAw() / (u + 1);
        result.setDw1(dw1);
        double b = result.getBw() / dw1;
        result.setPhiBd(b);
        double Khb = FirstTableHolder.getInstance()
                .find(b, result.getShaftPosition(), result.getStrengthTeeth())
                .getKhb();
        result.setKhb(Khb);
        double phiBa = result.getPhiBa();
        FifthTableData fifthTableData = FifthTableHolder.getInstance()
                .get(result.getCuring(), result.getLoadTypeForSigm());
        double sigm_HP = fifthTableData.getSimgHP();
        result.setSigm_HP(sigm_HP);
        double Nh0 = fifthTableData.getNHo();
        result.setNHo(Nh0);
        double Nhe = 60 * result.getT() * result.get_n();
        result.setNHe(Nhe);
        double Khl = Math.pow(Nh0 / Nhe, 1 / 6);
        result.setKhl(Khl);
        double sigmHP = sigm_HP * Khl;
        result.setSigmHP(sigmHP);
        double arg = result.getGearingType() == GearingType._2 ? -1 : 1;
        double sqrt = result.getT1() * Khb / (u * phiBa * Math.pow(sigmHP, 2));
        double a = Ka * (u + arg) * Math.pow(sqrt, 1 / 3);
        result.setA(a);
    }

    @SuppressWarnings("unchecked")
    private void mergeInputData() throws Exception {
        Map<String, Property<Object>> resultMap = new HashMap<>();
        FieldUtils.getFieldsListWithAnnotation(Result.class, Column.class).forEach(field -> {
            String name = field.getName();
            Property<Object> objectProperty = null;
            try {
                objectProperty = (Property<Object>) FieldUtils.readField(field, result, true);
            } catch (IllegalAccessException ignored) {
            }
            resultMap.put(name, objectProperty);
        });
        FieldUtils.getAllFieldsList(InputData.class).forEach(field -> {
            Property<Object> property = resultMap.get(field.getName());
            if (property != null) {
                try {
                    Property<Object> o = (Property<Object>) FieldUtils.readField(field, inputData, true);
                    property.setValue(o.getValue());
                } catch (IllegalAccessException ignored) {
                }
            }
        });

    }

    public static Result calc(InputData inputData) throws Exception {
        Calculator calculator = new Calculator(inputData);
        return calculator.getResult();
    }
}
