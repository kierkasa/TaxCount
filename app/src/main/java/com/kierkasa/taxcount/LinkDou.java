package com.kierkasa.taxcount;

public class LinkDou {
    public double[][] dou = {{100,1000},{200,2000}};

    public LinkDou() {}

    public void setDou(int i, int j, double d) {
        if (i<2&&j<2) {
            dou[i][j] = d;
        }
    }

    public double getNum(int i, int j) {
        if (i<2&&j<2) {
            return dou[i][j];
        }
        return 0;
    }
}
