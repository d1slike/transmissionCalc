package ru.disdev.entity.table;

public class FirstTableData {
    private final double Khb;
    private final double Kfb;

    public FirstTableData(double khb, double kfb) {
        Khb = khb;
        Kfb = kfb;
    }

    public double getKhb() {
        return Khb;
    }

    public double getKfb() {
        return Kfb;
    }
}
