package ru.disdev.entity.table;

public class FifthTableData {
    private final double sigmFP;
    private final double Nfo;
    private final double simgHP;
    private final double NHo;

    public FifthTableData(double sigmFm, double nfo, double simgHP, double nHo) {
        this.sigmFP = sigmFm;
        Nfo = nfo;
        this.simgHP = simgHP;
        NHo = nHo;
    }

    public double getSigmFP() {
        return sigmFP;
    }

    public double getNfo() {
        return Nfo;
    }

    public double getSimgHP() {
        return simgHP;
    }

    public double getNHo() {
        return NHo;
    }
}
