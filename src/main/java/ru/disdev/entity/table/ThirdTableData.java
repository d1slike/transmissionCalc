package ru.disdev.entity.table;

public class ThirdTableData {
    private final double Ka;
    private final double Kd;
    private final double Zm;

    public ThirdTableData(double ka, double kd, double zm) {
        Ka = ka;
        Kd = kd;
        Zm = zm;
    }

    public double getZm() {
        return Zm;
    }

    public double getKd() {
        return Kd;
    }

    public double getKa() {
        return Ka;
    }
}
