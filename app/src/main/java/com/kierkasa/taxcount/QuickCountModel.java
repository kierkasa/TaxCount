package com.kierkasa.taxcount;

import android.util.Log;

import java.math.BigDecimal;

import static com.kierkasa.taxcount.MyFuntion.getMax;
import static com.kierkasa.taxcount.MyFuntion.toFixed2;

public class QuickCountModel {

    public double[] quickCalculate(double pretax_income_sum, double children_edu_sum, double continuing_edu_sum, double home_loans_sum, double housing_rents_sum, double support_old_sum,
                                   double endowment_insurance_sum, double medical_insurance_sum, double unemployment_insurance_sum, double housing_fund_sum) {

        Log.d("Tax", "model.1 start");
        if (pretax_income_sum == 0) {
            endowment_insurance_sum = 0;       //endowment_insurance_sum值
            medical_insurance_sum = 0;       //medical_insurance_sum
            unemployment_insurance_sum = 0;       //unemployment_insurance_sum
            housing_fund_sum = 0;       //housing_fund_sum
        } else if (pretax_income_sum >= 25401) {
            endowment_insurance_sum = 2032.08;
            medical_insurance_sum = 511.02;
            unemployment_insurance_sum = 50.8;
            housing_fund_sum = 3048.12;
        } else {
            endowment_insurance_sum = toFixed2(pretax_income_sum*0.08);
            medical_insurance_sum = toFixed2(pretax_income_sum*0.02+3);
            unemployment_insurance_sum = toFixed2(pretax_income_sum*0.002);
            housing_fund_sum = toFixed2(pretax_income_sum*0.12);
        }

        double[] value = quickFinalCalculate(pretax_income_sum, children_edu_sum, continuing_edu_sum, home_loans_sum, housing_rents_sum, support_old_sum, endowment_insurance_sum,
                medical_insurance_sum, unemployment_insurance_sum, housing_fund_sum);

        return value;
    }
    
    public double[] quickFinalCalculate(double pretax_income_sum, double children_edu_sum, double continuing_edu_sum, double home_loans_sum, double housing_rents_sum, double support_old_sum,
                                        double endowment_insurance_sum, double medical_insurance_sum, double unemployment_insurance_sum, double housing_fund_sum) {
        Log.d("Tax", "model.2 start");
        double[] value = new double[6];
        double taxable_income;      //应纳税所得额
        double tax;     //税率
        double month_tax_acount;        //当月个人所得税
        double after_tax_acount;        //税后收入
        double spe_expend;      //专项支出
        double other_tax_acount;        //其他税收支出，医疗保险、养老保险、失业保险、住房公积金等
        double exemption;       //免税额，包括基础5000工资、专项支出、其他税收支出

        spe_expend = children_edu_sum + continuing_edu_sum + home_loans_sum + housing_rents_sum + support_old_sum;
        other_tax_acount = endowment_insurance_sum + medical_insurance_sum + unemployment_insurance_sum + housing_fund_sum;
        exemption = 5000 + spe_expend + other_tax_acount;

        //计算应纳税所得额
        if ((pretax_income_sum - exemption) > 0) {
            taxable_income = pretax_income_sum - exemption;
        } else {
            taxable_income = 0;
        }

        if (taxable_income <= 0) {
            tax = 0;
        } else if (taxable_income <= 3000) {
            tax = 0.03;
        } else if (taxable_income <= 12000) {
            tax = 0.1;
        } else if (taxable_income <= 25000) {
            tax = 0.2;
        } else if (taxable_income <= 35000) {
            tax = 0.25;
        } else if (taxable_income <= 55000) {
            tax = 0.3;
        } else if (taxable_income <= 80000) {
            tax = 0.35;
        } else {
            tax = 0.45;
        }

        month_tax_acount = getMax(0, taxable_income*0.03, taxable_income*0.1-210, taxable_income*0.2-1410,
                taxable_income*0.25-2660, taxable_income*0.3-4410, taxable_income*0.35-7160, taxable_income*0.45-15160);
        after_tax_acount = pretax_income_sum - other_tax_acount - month_tax_acount;

        value[0] = endowment_insurance_sum;
        value[1] = medical_insurance_sum;
        value[2] = unemployment_insurance_sum;
        value[3] = housing_fund_sum;
        value[4] = toFixed2(month_tax_acount);
        value[5] = toFixed2(after_tax_acount);

        return value;
    }

}
