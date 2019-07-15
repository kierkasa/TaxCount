package com.kierkasa.taxcount;

import android.util.Log;

import static com.kierkasa.taxcount.MyFuntion.douToString;
import static com.kierkasa.taxcount.MyFuntion.getMax;
import static com.kierkasa.taxcount.MyFuntion.toFixed;
import static com.kierkasa.taxcount.MyFuntion.toFixed2;

public class PreciseCountModel {

    //需要参数： dou[0]税前收入  dou[4]子女教育  dou[7]继续教育  dou[10]住房利息贷款  dou[13]住房租金  dou[16]赡养老人  dou[17]养老保险  dou[18]医疗保险  dou[29]失业保险  dou[20]住房公积金
    //生成参数： dou[21]应纳税所得额  dou[22]税率  dou[23]累计应缴个税  dou[24]当月应缴个税  dou[25]当月到手收入
    //额外参数： dou[31]前月应纳税额度  dou[32]前月个税(保险、公积金)  dou[33]last_zero_count
    public double[][] preciseCalculate(double[][] dou) {
        Log.d("Tax", "precise model start first calculate");
        dou[0][31] = 0;
        dou[0][33] = 0;
        dou[0] = preciseMonthCalculate(dou[0]);
        dou[0][24] = dou[0][23];        //1月当月应缴个税
        double accumulate_month_income_tax = dou[0][24];        //当月应缴个税累计值
        dou[0][25] = toFixed2(dou[0][25] - dou[0][24]);     //实际到手收入

        dou[1][31] = dou[0][31];        //传入前月应纳税额度
        dou[1][33] = dou[0][33];        //传入直到前月为止零收入月份计数
        dou[1] = preciseMonthCalculate(dou[1]);
        if (dou[1][23] < accumulate_month_income_tax) {
            dou[1][24] = 0;
        } else {
            dou[1][24] = toFixed2(dou[1][23] - accumulate_month_income_tax);
        }
        accumulate_month_income_tax += dou[1][24];
        dou[1][25] = toFixed2(dou[1][25] - dou[1][24]);

        dou[2][31] = dou[1][31];
        dou[2][33] = dou[1][33];
        dou[2] = preciseMonthCalculate(dou[2]);
        if (dou[2][23] < accumulate_month_income_tax) {
            dou[2][24] = 0;
        } else {
            dou[2][24] = toFixed2(dou[2][23] - accumulate_month_income_tax);
        }
        accumulate_month_income_tax += dou[2][24];
        dou[2][25] = toFixed2(dou[2][25] - dou[2][24]);

        dou[3][31] = dou[2][31];
        dou[3][33] = dou[2][33];
        dou[3] = preciseMonthCalculate(dou[3]);
        if (dou[3][23] < accumulate_month_income_tax) {
            dou[3][24] = 0;
        } else {
            dou[3][24] = toFixed2(dou[3][23] - accumulate_month_income_tax);
        }
        accumulate_month_income_tax += dou[3][24];
        dou[3][25] = toFixed2(dou[3][25] - dou[3][24]);
        
        dou[4][31] = dou[3][31];
        dou[4][33] = dou[3][33];
        dou[4] = preciseMonthCalculate(dou[4]);
        if (dou[4][23] < accumulate_month_income_tax) {
            dou[4][24] = 0;
        } else {
            dou[4][24] = toFixed2(dou[4][23] - accumulate_month_income_tax);
        }
        accumulate_month_income_tax += dou[4][24];
        dou[4][25] = toFixed2(dou[4][25] - dou[4][24]);
        
        dou[5][31] = dou[4][31];
        dou[5][33] = dou[4][33];
        dou[5] = preciseMonthCalculate(dou[5]);
        if (dou[5][23] < accumulate_month_income_tax) {
            dou[5][24] = 0;
        } else {
            dou[5][24] = toFixed2(dou[5][23] - accumulate_month_income_tax);
        }
        accumulate_month_income_tax += dou[5][24];
        dou[5][25] = toFixed2(dou[5][25] - dou[5][24]);
        
        dou[6][31] = dou[5][31];
        dou[6][33] = dou[5][33];
        dou[6] = preciseMonthCalculate(dou[6]);
        if (dou[6][23] < accumulate_month_income_tax) {
            dou[6][24] = 0;
        } else {
            dou[6][24] = toFixed2(dou[6][23] - accumulate_month_income_tax);
        }
        accumulate_month_income_tax += dou[6][24];
        dou[6][25] = toFixed2(dou[6][25] - dou[6][24]);
        
        dou[7][31] = dou[6][31];
        dou[7][33] = dou[6][33];
        dou[7] = preciseMonthCalculate(dou[7]);
        if (dou[7][23] < accumulate_month_income_tax) {
            dou[7][24] = 0;
        } else {
            dou[7][24] = toFixed2(dou[7][23] - accumulate_month_income_tax);
        }
        accumulate_month_income_tax += dou[7][24];
        dou[7][25] = toFixed2(dou[7][25] - dou[7][24]);

        dou[8][31] = dou[7][31];
        dou[8][33] = dou[7][33];
        dou[8] = preciseMonthCalculate(dou[8]);
        if (dou[8][23] < accumulate_month_income_tax) {
            dou[8][24] = 0;
        } else {
            dou[8][24] = toFixed2(dou[8][23] - accumulate_month_income_tax);
        }
        accumulate_month_income_tax += dou[8][24];
        dou[8][25] = toFixed2(dou[8][25] - dou[8][24]);
        
        dou[9][31] = dou[8][31];
        dou[9][33] = dou[8][33];
        dou[9] = preciseMonthCalculate(dou[9]);
        if (dou[9][23] < accumulate_month_income_tax) {
            dou[9][24] = 0;
        } else {
            dou[9][24] = toFixed2(dou[9][23] - accumulate_month_income_tax);
        }
        accumulate_month_income_tax += dou[9][24];
        dou[9][25] = toFixed2(dou[9][25] - dou[9][24]);
        
        dou[10][31] = dou[9][31];
        dou[10][33] = dou[9][33];
        dou[10] = preciseMonthCalculate(dou[10]);
        if (dou[10][23] < accumulate_month_income_tax) {
            dou[10][24] = 0;
        } else {
            dou[10][24] = toFixed2(dou[10][23] - accumulate_month_income_tax);
        }
        accumulate_month_income_tax += dou[10][24];
        dou[10][25] = toFixed2(dou[10][25] - dou[10][24]);

        dou[11][31] = dou[10][31];
        dou[11][33] = dou[10][33];
        dou[11] = preciseMonthCalculate(dou[11]);
        if (dou[11][23] < accumulate_month_income_tax) {
            dou[11][24] = 0;
        } else {
            dou[11][24] = toFixed2(dou[11][23] - accumulate_month_income_tax);
        }
        accumulate_month_income_tax += dou[11][24];
        dou[11][25] = toFixed2(dou[11][25] - dou[11][24]);

        dou[12] = preciseFinalCalculate(dou[12]);
        dou[12][24] = toFixed2(accumulate_month_income_tax);
        dou[12][25] = toFixed2(dou[0][25]+dou[1][25]+dou[2][25]+dou[3][25]+dou[4][25]+dou[5][25]+dou[6][25]+dou[7][25]+dou[8][25]+dou[9][25]+dou[10][25]+dou[11][25]);

        return dou;
    }

    public double[] preciseMonthCalculate(double[] monthDou) {
        Log.d("Tax","start Month calculate");
        if (monthDou[0] <= 0) {
            monthDou[21] = 0;
            monthDou[22] = 0;
            monthDou[23] = 0;
            monthDou[25] = 0;
            monthDou[33] = toFixed(monthDou[33] + 1);
            return monthDou;
        } else {
            double taxable_income = 0;
            double tax = 0;
            double total_income_tax = 0;        //累计纳税值
            double after_tax_income = 0;        //税后收入
            double special_dedution = toFixed2(monthDou[4] + monthDou[7] + monthDou[10] + monthDou[13] + monthDou[16]);     //专项扣除
            double individual_tax = toFixed2(monthDou[17] + monthDou[18] + monthDou[19] + monthDou[20]);        //个税
            double pre_taxable_income_dedution = toFixed2(5000 + special_dedution + individual_tax);       //前一个月缴税扣除额度
            double month_taxable_income = toFixed2(monthDou[0] + monthDou[31] - pre_taxable_income_dedution);       //用在后一个月作为前月的应纳税额度

            if (month_taxable_income > 0) {
                taxable_income = month_taxable_income;
            } else {
                taxable_income = 0;
            }

            if (month_taxable_income <= 0) {
                tax = 0;
            } else if (month_taxable_income <= 36000) {
                tax = 0.03;
            } else if (month_taxable_income <= 144000) {
                tax = 0.1;
            } else if (month_taxable_income <= 300000) {
                tax = 0.2;
            } else if (month_taxable_income <= 420000) {
                tax = 0.25;
            } else if (month_taxable_income <= 660000) {
                tax = 0.3;
            } else if (month_taxable_income <= 960000) {
                tax = 0.35;
            } else {
                tax = 0.45;
            }

            total_income_tax = toFixed2(getMax(0, taxable_income*0.03, taxable_income*0.1-2520, taxable_income*0.2-16920,
                    taxable_income*0.25-31920, taxable_income*0.3-52920, taxable_income*0.35-85920, taxable_income*0.45-181920));

            after_tax_income = toFixed2(monthDou[0] - individual_tax);

            monthDou[21] = taxable_income;
            monthDou[22] = tax;
            monthDou[23] = total_income_tax;
            monthDou[25] = after_tax_income;
            monthDou[31] = month_taxable_income;
            return monthDou;

        }
    }

    public double[] preciseFinalCalculate(double[] monthDou) {
        Log.d("Tax","start Final calculate");
        if (monthDou[0] <= 0) {
            monthDou[21] = 0;
            monthDou[22] = 0;
            monthDou[23] = 0;
            monthDou[25] = 0;
            return monthDou;
        } else {
            double taxable_income = 0;
            double tax = 0;
            double total_income_tax = 0;        //累计纳税值
            double after_tax_income = 0;        //税后收入
            double special_dedution = toFixed2(monthDou[4] + monthDou[7] + monthDou[10] + monthDou[13] + monthDou[16]);     //专项扣除
            double individual_tax = toFixed2(monthDou[17] + monthDou[18] + monthDou[19] + monthDou[20]);        //个税
            double pre_taxable_income_dedution = toFixed2(5000*(12-monthDou[33]) + special_dedution + individual_tax);       //合计缴税扣除额度，减去收入为0的月份基础免税额
            double month_taxable_income = toFixed2(monthDou[0] - pre_taxable_income_dedution);       //用在后一个月作为前月的应纳税额度
            Log.d("Tax", "专项扣除：" + special_dedution + " ,个税：" + individual_tax + " ,合计扣税额度：" + pre_taxable_income_dedution + " ,应纳税所得额：" + month_taxable_income);

            if (month_taxable_income > 0) {
                taxable_income = month_taxable_income;
            } else {
                taxable_income = 0;
            }

            if (month_taxable_income <= 0) {
                tax = 0;
            } else if (month_taxable_income <= 36000) {
                tax = 0.03;
            } else if (month_taxable_income <= 144000) {
                tax = 0.1;
            } else if (month_taxable_income <= 300000) {
                tax = 0.2;
            } else if (month_taxable_income <= 420000) {
                tax = 0.25;
            } else if (month_taxable_income <= 660000) {
                tax = 0.3;
            } else if (month_taxable_income <= 960000) {
                tax = 0.35;
            } else {
                tax = 0.45;
            }

            total_income_tax = toFixed2(getMax(0, taxable_income*0.03, taxable_income*0.1-2520, taxable_income*0.2-16920,
                    taxable_income*0.25-31920, taxable_income*0.3-52920, taxable_income*0.35-85920, taxable_income*0.45-181920));

            after_tax_income = toFixed2(monthDou[0] - individual_tax);

            monthDou[21] = taxable_income;
            monthDou[22] = tax;
            monthDou[23] = total_income_tax;
            monthDou[25] = after_tax_income;
            return monthDou;

        }
    }
}
