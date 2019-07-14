package com.kierkasa.taxcount;

import android.util.Log;

public class TaxCountPresenter {

    private TaxCountCallBack taxCountCallBack;
    private QuickCountModel quickCountModel;
    private PreciseCountModel preciseCountModel;

    public TaxCountPresenter(TaxCountCallBack taxCountCallBack) {
        this.taxCountCallBack = taxCountCallBack;
        quickCountModel = new QuickCountModel();
        preciseCountModel = new PreciseCountModel();
    }

    public void quickCount(double pretax_income_sum, double children_edu_sum, double continuing_edu_sum, double home_loans_sum, double housing_rents_sum, double support_old_sum,
                           double endowment_insurance_sum, double medical_insurance_sum, double unemployment_insurance_sum, double housing_fund_sum) {

        double dou[] = quickCountModel.quickCalculate(pretax_income_sum, children_edu_sum, continuing_edu_sum, home_loans_sum, housing_rents_sum, support_old_sum, endowment_insurance_sum,
                medical_insurance_sum, unemployment_insurance_sum, housing_fund_sum);

        taxCountCallBack.quickCountSetValue(dou);

    }

    public void quickFinalCount(double pretax_income_sum, double children_edu_sum, double continuing_edu_sum, double home_loans_sum, double housing_rents_sum, double support_old_sum,
                                double endowment_insurance_sum, double medical_insurance_sum, double unemployment_insurance_sum, double housing_fund_sum) {
        double dou[] = quickCountModel.quickFinalCalculate(pretax_income_sum, children_edu_sum, continuing_edu_sum, home_loans_sum, housing_rents_sum, support_old_sum, endowment_insurance_sum,
                medical_insurance_sum, unemployment_insurance_sum, housing_fund_sum);
        taxCountCallBack.quickCountSetValue(dou);
    }

    public void preciseCount(double[][] dou) {
        Log.d("Tax", "presenter start calculate");
        double[][] doubles = preciseCountModel.preciseCalculate(dou);
        Log.d("Tax", "presenter stop, start use callback.setValue");
        taxCountCallBack.preciseCountSetValue(doubles);
    }
}
