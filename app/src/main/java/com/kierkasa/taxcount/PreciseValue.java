package com.kierkasa.taxcount;

public class PreciseValue {

    private String flag;
    private double[][] dou;

    public PreciseValue(String flag, double[][] dou) {
        this.flag = flag;
        this.dou = dou;
    }

    public String getFlag() {
        return flag;
    }

    public double[][] getDou() {
        return dou;
    }
}
