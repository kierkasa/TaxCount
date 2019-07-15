package com.kierkasa.taxcount;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.kierkasa.taxcount.MyFuntion.douToString;
import static com.kierkasa.taxcount.MyFuntion.radio_changed;
import static com.kierkasa.taxcount.MyFuntion.radio_original;
import static com.kierkasa.taxcount.MyFuntion.strToDouble;

public class QuickFragment extends Fragment implements RadioGroup.OnCheckedChangeListener,CompoundButton.OnCheckedChangeListener,TaxCountCallBack{
    private View view;

    private CheckBox children_edu_checkBox, continuing_edu_checkBox, home_loans_checkBox, housing_rents_checkBox, support_old_checkBox;
    private RadioGroup children_edu_group, continuing_edu_group, home_loans_group, housing_rents_group, support_old_group;
    private RadioButton children_edu_500, children_edu_1000, children_edu_2000, continuing_edu_400, continuing_edu_3600, home_loans_500,
            home_loans_1000, housing_rents_1500, support_old_500, support_old_1000, support_old_2000;
    private EditText pretax_income_input, endowment_insurance_input, medical_insurance_input, unemployment_insurance_input, housing_fund_input;
    private TextView income_tax_value, after_tax_value;

    private double pretax_income_sum, children_edu_sum, continuing_edu_sum, home_loans_sum, housing_rents_sum, support_old_sum, endowment_insurance_sum,
            medical_insurance_sum, unemployment_insurance_sum, housing_fund_sum, income_tax_sum, after_tax_sum;

    private InputMethodManager imm;
    //private MyHandler myHandler;
    private TaxCountPresenter taxCountPresenter;
    
    public static QuickFragment newInstance() {
        Log.d("Tax","quickfragment start");
        QuickFragment fragment = new QuickFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
        view = inflater.inflate(R.layout.activity_main, container, false);

        initWidget();
        initListener();

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                getActivity().onTouchEvent(motionEvent);
                return false;
            }
        });

        Log.d("Tax","quickfragment view will return");
        return view;
    }

    //初始化控件
    private void initWidget() {
        children_edu_checkBox = view.findViewById(R.id.children_edu_checkBox);
        continuing_edu_checkBox = view.findViewById(R.id.continuing_edu_checkBox);
        home_loans_checkBox = view.findViewById(R.id.home_loans_checkBox);
        housing_rents_checkBox = view.findViewById(R.id.housing_rents_checkBox);
        support_old_checkBox = view.findViewById(R.id.support_old_checkBox);

        children_edu_group = view.findViewById(R.id.children_edu_group);
        continuing_edu_group = view.findViewById(R.id.continuing_edu_group);
        home_loans_group = view.findViewById(R.id.home_loans_group);
        housing_rents_group = view.findViewById(R.id.housing_rents_group);
        support_old_group = view.findViewById(R.id.support_old_group);

        children_edu_500 = view.findViewById(R.id.children_edu_500);
        children_edu_1000 = view.findViewById(R.id.children_edu_1000);
        children_edu_2000 = view.findViewById(R.id.children_edu_2000);
        continuing_edu_400 = view.findViewById(R.id.continuing_edu_400);
        continuing_edu_3600 = view.findViewById(R.id.continuing_edu_3600);
        home_loans_500 = view.findViewById(R.id.home_loans_500);
        home_loans_1000 = view.findViewById(R.id.home_loans_1000);
        housing_rents_1500 = view.findViewById(R.id.housing_rents_1500);
        support_old_500 = view.findViewById(R.id.support_old_500);
        support_old_1000 = view.findViewById(R.id.support_old_1000);
        support_old_2000 = view.findViewById(R.id.support_old_2000);

        pretax_income_input = view.findViewById(R.id.pretax_income_input);
        endowment_insurance_input = view.findViewById(R.id.endowment_insurance_input);
        medical_insurance_input = view.findViewById(R.id.medical_insurance_input);
        unemployment_insurance_input = view.findViewById(R.id.unemployment_insurance_input);
        housing_fund_input = view.findViewById(R.id.housing_fund_input);
        income_tax_value = view.findViewById(R.id.income_tax_value);
        after_tax_value = view.findViewById(R.id.after_tax_value);

        taxCountPresenter = new TaxCountPresenter(this);        //创建Presenter实例，传入MainActivity作为callback
        imm= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        initSum();      //初始化各元素sum值

        Button quickCalculate = view.findViewById(R.id.quick_calculate);
        quickCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                startCalculate();
            }
        });
        Button quickClean = view.findViewById(R.id.quick_clean);
        quickClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                cleanQuickPage();
            }
        });
        
    }

    //初始化listener
    private void initListener() {
        children_edu_checkBox.setOnCheckedChangeListener(this);
        continuing_edu_checkBox.setOnCheckedChangeListener(this);
        home_loans_checkBox.setOnCheckedChangeListener(this);
        housing_rents_checkBox.setOnCheckedChangeListener(this);
        support_old_checkBox.setOnCheckedChangeListener(this);

        children_edu_group.setOnCheckedChangeListener(this);
        continuing_edu_group.setOnCheckedChangeListener(this);
        home_loans_group.setOnCheckedChangeListener(this);
        housing_rents_group.setOnCheckedChangeListener(this);
        support_old_group.setOnCheckedChangeListener(this);

        pretax_income_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //控制两位小数“num”即为要控制的位数, 这里直接改为2
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (2 + 1));
                        pretax_income_input.setText(s);
                        pretax_income_input.setSelection(s.length());
                    }
                }

                //限制只能输入一次小数点
                if (pretax_income_input.getText().toString().indexOf(".") >= 0) {
                    if (pretax_income_input.getText().toString().indexOf(".", pretax_income_input.getText().toString().indexOf(".") + 1) > 0) {
                        pretax_income_input.setText(pretax_income_input.getText().toString().substring(0, pretax_income_input.getText().toString().length() - 1));
                        pretax_income_input.setSelection(pretax_income_input.getText().toString().length());
                    }
                }
                //第一次输入为点的时候
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    pretax_income_input.setText(s);
                    pretax_income_input.setSelection(2);
                }
                //个位数为0的时候
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        pretax_income_input.setText(s.subSequence(0, 1));
                        pretax_income_input.setSelection(1);
                    }
                }

                if (s.length() > 0) {
                    pretax_income_sum = strToDouble(pretax_income_input.getText().toString());
                } else {
                    pretax_income_sum = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //startCalculate();
            }
        });
        endowment_insurance_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //控制两位小数“num”即为要控制的位数, 这里直接改为2
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (2 + 1));
                        endowment_insurance_input.setText(s);
                        endowment_insurance_input.setSelection(s.length());
                    }
                }

                //限制只能输入一次小数点
                if (endowment_insurance_input.getText().toString().indexOf(".") >= 0) {
                    if (endowment_insurance_input.getText().toString().indexOf(".", endowment_insurance_input.getText().toString().indexOf(".") + 1) > 0) {
                        endowment_insurance_input.setText(endowment_insurance_input.getText().toString().substring(0, endowment_insurance_input.getText().toString().length() - 1));
                        endowment_insurance_input.setSelection(endowment_insurance_input.getText().toString().length());
                    }
                }
                //第一次输入为点的时候
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    endowment_insurance_input.setText(s);
                    endowment_insurance_input.setSelection(2);
                }
                //个位数为0的时候
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        endowment_insurance_input.setText(s.subSequence(0, 1));
                        endowment_insurance_input.setSelection(1);
                    }
                }

                if (s.length() > 0) {
                    endowment_insurance_sum = strToDouble(s.toString());
                } else {
                    endowment_insurance_sum = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                startQuickFinalCalculate();
            }
        });
        medical_insurance_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //控制两位小数“num”即为要控制的位数, 这里直接改为2
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (2 + 1));
                        medical_insurance_input.setText(s);
                        medical_insurance_input.setSelection(s.length());
                    }
                }

                //限制只能输入一次小数点
                if (medical_insurance_input.getText().toString().indexOf(".") >= 0) {
                    if (medical_insurance_input.getText().toString().indexOf(".", medical_insurance_input.getText().toString().indexOf(".") + 1) > 0) {
                        medical_insurance_input.setText(medical_insurance_input.getText().toString().substring(0, medical_insurance_input.getText().toString().length() - 1));
                        medical_insurance_input.setSelection(medical_insurance_input.getText().toString().length());
                    }
                }
                //第一次输入为点的时候
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    medical_insurance_input.setText(s);
                    medical_insurance_input.setSelection(2);
                }
                //个位数为0的时候
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        medical_insurance_input.setText(s.subSequence(0, 1));
                        medical_insurance_input.setSelection(1);
                    }
                }

                if (s.length() > 0) {
                    medical_insurance_sum = strToDouble(s.toString());
                } else {
                    medical_insurance_sum = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                startQuickFinalCalculate();
            }
        });
        unemployment_insurance_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //控制两位小数“num”即为要控制的位数, 这里直接改为2
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (2 + 1));
                        unemployment_insurance_input.setText(s);
                        unemployment_insurance_input.setSelection(s.length());
                    }
                }

                //限制只能输入一次小数点
                if (unemployment_insurance_input.getText().toString().indexOf(".") >= 0) {
                    if (unemployment_insurance_input.getText().toString().indexOf(".", unemployment_insurance_input.getText().toString().indexOf(".") + 1) > 0) {
                        unemployment_insurance_input.setText(unemployment_insurance_input.getText().toString().substring(0, unemployment_insurance_input.getText().toString().length() - 1));
                        unemployment_insurance_input.setSelection(unemployment_insurance_input.getText().toString().length());
                    }
                }
                //第一次输入为点的时候
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    unemployment_insurance_input.setText(s);
                    unemployment_insurance_input.setSelection(2);
                }
                //个位数为0的时候
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        unemployment_insurance_input.setText(s.subSequence(0, 1));
                        unemployment_insurance_input.setSelection(1);
                    }
                }

                if (s.length() > 0) {
                    unemployment_insurance_sum = strToDouble(s.toString());
                } else {
                    unemployment_insurance_sum = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                startQuickFinalCalculate();
            }
        });
        housing_fund_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //控制两位小数“num”即为要控制的位数, 这里直接改为2
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (2 + 1));
                        housing_fund_input.setText(s);
                        housing_fund_input.setSelection(s.length());
                    }
                }

                //限制只能输入一次小数点
                if (housing_fund_input.getText().toString().indexOf(".") >= 0) {
                    if (housing_fund_input.getText().toString().indexOf(".", housing_fund_input.getText().toString().indexOf(".") + 1) > 0) {
                        housing_fund_input.setText(housing_fund_input.getText().toString().substring(0, housing_fund_input.getText().toString().length() - 1));
                        housing_fund_input.setSelection(housing_fund_input.getText().toString().length());
                    }
                }
                //第一次输入为点的时候
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    housing_fund_input.setText(s);
                    housing_fund_input.setSelection(2);
                }
                //个位数为0的时候
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        housing_fund_input.setText(s.subSequence(0, 1));
                        housing_fund_input.setSelection(1);
                    }
                }

                if (s.length() > 0) {
                    housing_fund_sum = strToDouble(s.toString());
                } else {
                    housing_fund_sum = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                startQuickFinalCalculate();
            }
        });
    }

    private void startThread() {
        new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        initListener();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    //单选框点击事件监听接口方法
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {     //单选框点击事件，根据选择的内容为对应项赋值

        switch (group.getId()) {        //获取RadioGroup Id
            case R.id.children_edu_group:
                switch (checkedId) {        //获取选中的单选框
                    case R.id.children_edu_500:
                        children_edu_sum = 500;
                        break;
                    case R.id.children_edu_1000:
                        children_edu_sum = 1000;
                        break;
                    case R.id.children_edu_2000:
                        children_edu_sum = 2000;
                        break;
                    default:
                        break;
                }
                break;
            case R.id.continuing_edu_group:
                switch (checkedId) {
                    case R.id.continuing_edu_400:
                        continuing_edu_sum = 400;
                        break;
                    case R.id.continuing_edu_3600:
                        continuing_edu_sum = 3600;
                        break;
                    default:
                        break;
                }
                break;
            case R.id.home_loans_group:
                switch (checkedId) {
                    case R.id.home_loans_500:
                        home_loans_sum = 500;
                        break;
                    case R.id.home_loans_1000:
                        home_loans_sum = 1000;
                        break;
                    default:
                        break;
                }
                break;
            case R.id.housing_rents_group:
                switch (checkedId) {
                    case R.id.housing_rents_1500:
                        housing_rents_sum = 1500;
                        break;
                    default:
                        break;
                }
                break;
            case R.id.support_old_group:
                switch (checkedId) {
                    case R.id.support_old_500:
                        support_old_sum = 500;
                        break;
                    case R.id.support_old_1000:
                        support_old_sum = 1000;
                        break;
                    case R.id.support_old_2000:
                        support_old_sum = 2000;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

    }

    //CheckBox点击事件监听接口方法
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isCheacked) {

        switch (buttonView.getId()) {
            case R.id.children_edu_checkBox:
                if (isCheacked) {
                    children_edu_group.setClickable(true);      //RadioGroup组设置为可选状态
                    children_edu_500.setButtonTintList(radio_changed);      //更改单选框颜色模式
                    children_edu_1000.setButtonTintList(radio_changed);
                    children_edu_2000.setButtonTintList(radio_changed);
                    children_edu_500.setClickable(true);
                    children_edu_1000.setClickable(true);
                    children_edu_2000.setClickable(true);

                } else {
                    cleanChildren_edu();
                }
                break;
            case R.id.continuing_edu_checkBox:
                if (isCheacked) {
                    continuing_edu_group.setClickable(true);
                    continuing_edu_400.setButtonTintList(radio_changed);
                    continuing_edu_3600.setButtonTintList(radio_changed);
                    continuing_edu_400.setClickable(true);
                    continuing_edu_3600.setClickable(true);
                } else {
                    cleanContinuing_edu();
                }
                break;
            case R.id.home_loans_checkBox:
                if (isCheacked) {
                    home_loans_group.setClickable(true);
                    housing_rents_checkBox.setChecked(false);
                    housing_rents_group.setClickable(false);
                    housing_rents_1500.setButtonTintList(radio_original);
                    housing_rents_sum = 0;
                    home_loans_500.setButtonTintList(radio_changed);
                    home_loans_1000.setButtonTintList(radio_changed);
                    home_loans_500.setClickable(true);
                    home_loans_1000.setClickable(true);
                } else {
                    cleanHome_loans();
                }
                break;
            case R.id.housing_rents_checkBox:
                if (isCheacked) {
                    housing_rents_group.setClickable(true);
                    home_loans_checkBox.setChecked(false);
                    home_loans_group.setClickable(false);
                    home_loans_500.setButtonTintList(radio_original);
                    home_loans_1000.setButtonTintList(radio_original);
                    home_loans_sum = 0;
                    housing_rents_1500.setButtonTintList(radio_changed);
                    housing_rents_1500.setClickable(true);
                } else {
                    cleanHousing_rents();
                }
                break;
            case R.id.support_old_checkBox:
                if (isCheacked) {
                    support_old_group.setClickable(true);
                    support_old_500.setButtonTintList(radio_changed);
                    support_old_1000.setButtonTintList(radio_changed);
                    support_old_2000.setButtonTintList(radio_changed);
                    support_old_500.setClickable(true);
                    support_old_1000.setClickable(true);
                    support_old_2000.setClickable(true);
                } else {
                    cleanSupport_old();
                }
                break;
            default:
                break;
        }
    }

    //CheckBox取消点击时执行的方法
    private void cleanChildren_edu() {
        children_edu_group.clearCheck();
        children_edu_group.setClickable(false);
        children_edu_500.setClickable(false);
        children_edu_1000.setClickable(false);
        children_edu_2000.setClickable(false);
        children_edu_500.setButtonTintList(radio_original);
        children_edu_1000.setButtonTintList(radio_original);
        children_edu_2000.setButtonTintList(radio_original);
        children_edu_sum = 0;
    }
    private void cleanContinuing_edu() {
        continuing_edu_group.clearCheck();
        continuing_edu_group.setClickable(false);
        continuing_edu_400.setButtonTintList(radio_original);
        continuing_edu_3600.setButtonTintList(radio_original);
        continuing_edu_400.setClickable(false);
        continuing_edu_3600.setClickable(false);
        continuing_edu_sum = 0;
    }
    private void cleanHome_loans() {
        home_loans_group.clearCheck();
        home_loans_group.setClickable(false);
        home_loans_500.setButtonTintList(radio_original);
        home_loans_1000.setButtonTintList(radio_original);
        home_loans_500.setClickable(false);
        home_loans_1000.setClickable(false);
        home_loans_sum = 0;
    }
    private void cleanHousing_rents() {
        housing_rents_group.clearCheck();
        housing_rents_group.setClickable(false);
        housing_rents_1500.setButtonTintList(radio_original);
        housing_rents_1500.setClickable(false);
        housing_rents_sum = 0;
    }
    private void cleanSupport_old() {
        support_old_group.clearCheck();
        support_old_group.setClickable(false);
        support_old_500.setButtonTintList(radio_original);
        support_old_1000.setButtonTintList(radio_original);
        support_old_2000.setButtonTintList(radio_original);
        support_old_500.setClickable(false);
        support_old_1000.setClickable(false);
        support_old_2000.setClickable(false);
        support_old_sum = 0;
    }

    //快速计算的结果更新到页面
    @Override
    public void quickCountSetValue(double[] dou) {
        //Message message = myHandler.obtainMessage(0, "");
        //myHandler.sendMessage(message);

        //加入EditText输入判断，如果值未变不进行重写，避免再次触发EditText的输入监听listener
        if (!douToString(dou[0]).equals(endowment_insurance_input.getText().toString())) {
            endowment_insurance_input.setText(douToString(dou[0]));
        }
        if (!douToString(dou[1]).equals(medical_insurance_input.getText().toString())) {
            medical_insurance_input.setText(douToString(dou[1]));
        }
        if (!douToString(dou[2]).equals(unemployment_insurance_input.getText().toString())) {
            unemployment_insurance_input.setText(douToString(dou[2]));
        }
        if (!douToString(dou[3]).equals(housing_fund_input.getText().toString())) {
            housing_fund_input.setText(douToString(dou[3]));
        }
        income_tax_value.setText(douToString(dou[4]));
        after_tax_value.setText(douToString(dou[5]));
    }

    @Override
    public void preciseCountSetValue(double[][] dou) {
    }

    /*private static class MyHandler extends Handler {
        private WeakReference<MainActivity> weakReference;

        private MyHandler(MainActivity mainActivity) {
            weakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity = weakReference.get();
            switch(msg.what) {
                case 0:
                    activity.endowment_insurance_input.setText(msg.);
                    break;
                default:
                    break;
            }
        }
    }*/

    //初始化元素sum值
    public void initSum() {
        pretax_income_sum = 0;
        children_edu_sum = 0;
        continuing_edu_sum = 0;
        home_loans_sum = 0;
        housing_rents_sum = 0;
        support_old_sum = 0;
        endowment_insurance_sum = 0;
        medical_insurance_sum = 0;
        unemployment_insurance_sum = 0;
        housing_fund_sum = 0;
        income_tax_sum = 0;
        after_tax_sum = 0;
    }

    public void startCalculate() {
        Log.d("Tax", "startCalculate");
        taxCountPresenter.quickCount(pretax_income_sum, children_edu_sum, continuing_edu_sum, home_loans_sum, housing_rents_sum,
                support_old_sum, endowment_insurance_sum, medical_insurance_sum, unemployment_insurance_sum, housing_fund_sum);
    }

    public void startQuickFinalCalculate() {
        taxCountPresenter.quickFinalCount(pretax_income_sum, children_edu_sum, continuing_edu_sum, home_loans_sum, housing_rents_sum,
                support_old_sum, endowment_insurance_sum, medical_insurance_sum, unemployment_insurance_sum, housing_fund_sum);
    }

    public void cleanQuickPage() {
        pretax_income_input.setText("");
        endowment_insurance_input.setText("");
        medical_insurance_input.setText("");
        unemployment_insurance_input.setText("");
        housing_fund_input.setText("");
        income_tax_value.setText("");
        after_tax_value.setText("");

        initSum();

        children_edu_checkBox.setChecked(false);
        cleanChildren_edu();
        continuing_edu_checkBox.setChecked(false);
        cleanContinuing_edu();
        home_loans_checkBox.setChecked(false);
        cleanHome_loans();
        housing_rents_checkBox.setChecked(false);
        cleanHousing_rents();
        support_old_checkBox.setChecked(false);
        cleanSupport_old();
    }


}
