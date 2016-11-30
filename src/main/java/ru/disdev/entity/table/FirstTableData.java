package ru.disdev.entity.table;

public class FirstTableData {
    private final double Khl;
    private final double Kfl;

    public FirstTableData(double khl, double kfl) {
        Khl = khl;
        Kfl = kfl;
    }

    public double getKhl() {
        return Khl;
    }

    public double getKfl() {
        return Kfl;
    }
}
