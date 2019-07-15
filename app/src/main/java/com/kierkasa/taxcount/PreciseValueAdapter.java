package com.kierkasa.taxcount;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import static com.kierkasa.taxcount.MyFuntion.douToString;
import static com.kierkasa.taxcount.MyFuntion.intToString;
import static com.kierkasa.taxcount.MyFuntion.radio_changed;
import static com.kierkasa.taxcount.MyFuntion.radio_original;
import static com.kierkasa.taxcount.MyFuntion.strToDouble;
import static com.kierkasa.taxcount.MyFuntion.strToInt;
import static com.kierkasa.taxcount.MyFuntion.toFixed2;

public class PreciseValueAdapter extends RecyclerView.Adapter<PreciseValueAdapter.ViewHolder> {
    private List<PreciseValue> preciseValueList;
    public static int page;
    public static double[][] douArray = new double[13][44];
    public static boolean anyChanged = false;
    public static boolean isInput = false;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox children_edu_checkBox, continuing_edu_checkBox, home_loans_checkBox, housing_rents_checkBox, support_old_checkBox, continue_write_checkBox;
        RadioGroup children_edu_group, continuing_edu_group, home_loans_group, housing_rents_group, support_old_group;
        RadioButton children_edu_500, children_edu_1000, children_edu_2000, continuing_edu_400, continuing_edu_3600, home_loans_500,
                home_loans_1000, housing_rents_1500, support_old_500, support_old_1000, support_old_2000;
        TextView children_edu_total, continuing_edu_total, home_loans_total, housing_rents_total, support_old_total, hide_text, taxable_income, tax_rate, total_income_tax, month_income_tax, month_final_income;
        EditText pretax_income_input, endowment_insurance_input, medical_insurance_input, unemployment_insurance_input, housing_fund_input;

        public ViewHolder(View view) {
            super(view);

            children_edu_checkBox = view.findViewById(R.id.children_edu_checkBox_p);
            continuing_edu_checkBox = view.findViewById(R.id.continuing_edu_checkBox_p);
            home_loans_checkBox = view.findViewById(R.id.home_loans_checkBox_p);
            housing_rents_checkBox = view.findViewById(R.id.housing_rents_checkBox_p);
            support_old_checkBox = view.findViewById(R.id.support_old_checkBox_p);
            continue_write_checkBox = view.findViewById(R.id.continue_write);

            children_edu_total = view.findViewById(R.id.children_edu_total_p);
            continuing_edu_total = view.findViewById(R.id.continuing_edu_total_p);
            home_loans_total = view.findViewById(R.id.home_loans_total_p);
            housing_rents_total = view.findViewById(R.id.housing_rents_total_p);
            support_old_total = view.findViewById(R.id.support_old_total_p);

            children_edu_group = view.findViewById(R.id.children_edu_group_p);
            continuing_edu_group = view.findViewById(R.id.continuing_edu_group_p);
            home_loans_group = view.findViewById(R.id.home_loans_group_p);
            housing_rents_group = view.findViewById(R.id.housing_rents_group_p);
            support_old_group = view.findViewById(R.id.support_old_group_p);

            children_edu_500 = view.findViewById(R.id.children_edu_500_p);
            children_edu_1000 = view.findViewById(R.id.children_edu_1000_p);
            children_edu_2000 = view.findViewById(R.id.children_edu_2000_p);
            continuing_edu_400 = view.findViewById(R.id.continuing_edu_400_p);
            continuing_edu_3600 = view.findViewById(R.id.continuing_edu_3600_p);
            home_loans_500 = view.findViewById(R.id.home_loans_500_p);
            home_loans_1000 = view.findViewById(R.id.home_loans_1000_p);
            housing_rents_1500 = view.findViewById(R.id.housing_rents_1500_p);
            support_old_500 = view.findViewById(R.id.support_old_500_p);
            support_old_1000 = view.findViewById(R.id.support_old_1000_p);
            support_old_2000 = view.findViewById(R.id.support_old_2000_p);
            
            pretax_income_input = view.findViewById(R.id.pretax_income_input_p);
            endowment_insurance_input = view.findViewById(R.id.endowment_insurance_input_p);
            medical_insurance_input = view.findViewById(R.id.medical_insurance_input_p);
            unemployment_insurance_input = view.findViewById(R.id.unemployment_insurance_input_p);
            housing_fund_input = view.findViewById(R.id.housing_fund_input_p);


            hide_text = view.findViewById(R.id.hide_text);
            taxable_income = view.findViewById(R.id.taxable_income_value_p);
            tax_rate = view.findViewById(R.id.tax_rate_value_p);
            total_income_tax = view.findViewById(R.id.total_income_tax_value_p);
            month_income_tax = view.findViewById(R.id.month_income_tax_value_p);
            month_final_income = view.findViewById(R.id.month_final_income_value_p);
        }
    }

    public PreciseValueAdapter(List<PreciseValue> list) {
        preciseValueList = list;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewype) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.precise_month_part, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        
        //税前收入编辑时触发的功能，将内容延续到后方item中
        holder.pretax_income_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //控制两位小数“num”即为要控制的位数, 这里直接改为2
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (2 + 1));
                        holder.pretax_income_input.setText(s);
                        holder.pretax_income_input.setSelection(s.length());
                    }
                }

                //限制只能输入一次小数点
                if (holder.pretax_income_input.getText().toString().indexOf(".") >= 0) {
                    if (holder.pretax_income_input.getText().toString().indexOf(".", holder.pretax_income_input.getText().toString().indexOf(".") + 1) > 0) {
                        holder.pretax_income_input.setText(holder.pretax_income_input.getText().toString().substring(0, holder.pretax_income_input.getText().toString().length() - 1));
                        holder.pretax_income_input.setSelection(holder.pretax_income_input.getText().toString().length());
                    }
                }
                //第一次输入为点的时候
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    holder.pretax_income_input.setText(s);
                    holder.pretax_income_input.setSelection(2);
                }
                //个位数为0的时候
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        holder.pretax_income_input.setText(s.subSequence(0, 1));
                        holder.pretax_income_input.setSelection(1);
                    }
                }

                if (s.length() > 0) {
                    page = holder.getAdapterPosition();
                    douArray[page][0] = strToDouble(s.toString());
                } else {
                    douArray[page][0] = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int original_page = page;
                if (douArray[page][0] != douArray[page][26]) {      //变更后的值与原值比较，只有改变后才出发延续填写；RecyclerView全局更新时，首个item处的EditText会触发此listener，因此加入数值变化判定
                    if (douArray[page][1] == 0) {
                        Log.d("Tax", "page-" + page + " pretax_income for start");
                        for (; page < 11; page++) {
                            if (douArray[page + 1][1] == 0) {
                                douArray[page + 1][0] = douArray[original_page][0];
                            } else {
                                continue;
                            }
                        }
                    }
                    anyChanged = true;
                    douArray[12][0] = toFixed2(douArray[0][0]+douArray[1][0]+douArray[2][0]+douArray[3][0]+douArray[4][0]+douArray[5][0]+douArray[6][0]+
                            douArray[7][0]+douArray[8][0]+douArray[9][0]+douArray[10][0]+douArray[11][0]);
                }
            }
        });
        //养老保险编辑时触发
        holder.endowment_insurance_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //控制两位小数“num”即为要控制的位数, 这里直接改为2
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (2 + 1));
                        holder.endowment_insurance_input.setText(s);
                        holder.endowment_insurance_input.setSelection(s.length());
                    }
                }

                //限制只能输入一次小数点
                if (holder.endowment_insurance_input.getText().toString().indexOf(".") >= 0) {
                    if (holder.endowment_insurance_input.getText().toString().indexOf(".", holder.endowment_insurance_input.getText().toString().indexOf(".") + 1) > 0) {
                        holder.endowment_insurance_input.setText(holder.endowment_insurance_input.getText().toString().substring(0, holder.endowment_insurance_input.getText().toString().length() - 1));
                        holder.endowment_insurance_input.setSelection(holder.endowment_insurance_input.getText().toString().length());
                    }
                }
                //第一次输入为点的时候
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    holder.endowment_insurance_input.setText(s);
                    holder.endowment_insurance_input.setSelection(2);
                }
                //个位数为0的时候
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        holder.endowment_insurance_input.setText(s.subSequence(0, 1));
                        holder.endowment_insurance_input.setSelection(1);
                    }
                }

                if (s.length() > 0) {
                    page = holder.getAdapterPosition();
                    douArray[page][17] = strToDouble(s.toString());
                } else {
                    douArray[page][17] = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (douArray[page][17] != douArray[page][27]) {      //变更后的值与原值比较，只有改变后才出发延续填写；RecyclerView全局更新时，首个item处的EditText会触发此listener，因此加入数值变化判定
                    int original_page = page;
                    if (douArray[page][1] == 0) {
                        Log.d("Tax", "page-" + page + " endowment_insurance for start");
                        for (; page < 11; page++) {
                            if (douArray[page + 1][1] == 0) {
                                douArray[page + 1][17] = douArray[original_page][17];
                            } else {
                                continue;
                            }
                        }
                    }
                    anyChanged = true;
                    douArray[12][17] = toFixed2(douArray[0][17]+douArray[1][17]+douArray[2][17]+douArray[3][17]+douArray[4][17]+douArray[5][17]+douArray[6][17]+
                            douArray[7][17]+douArray[8][17]+douArray[9][17]+douArray[10][17]+douArray[11][17]);
                }
            }
        });
        //医疗保险编辑时触发
        holder.medical_insurance_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //控制两位小数“num”即为要控制的位数, 这里直接改为2
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (2 + 1));
                        holder.medical_insurance_input.setText(s);
                        holder.medical_insurance_input.setSelection(s.length());
                    }
                }

                //限制只能输入一次小数点
                if (holder.medical_insurance_input.getText().toString().indexOf(".") >= 0) {
                    if (holder.medical_insurance_input.getText().toString().indexOf(".", holder.medical_insurance_input.getText().toString().indexOf(".") + 1) > 0) {
                        holder.medical_insurance_input.setText(holder.medical_insurance_input.getText().toString().substring(0, holder.medical_insurance_input.getText().toString().length() - 1));
                        holder.medical_insurance_input.setSelection(holder.medical_insurance_input.getText().toString().length());
                    }
                }
                //第一次输入为点的时候
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    holder.medical_insurance_input.setText(s);
                    holder.medical_insurance_input.setSelection(2);
                }
                //个位数为0的时候
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        holder.medical_insurance_input.setText(s.subSequence(0, 1));
                        holder.medical_insurance_input.setSelection(1);
                    }
                }

                if (s.length() > 0) {
                    page = holder.getAdapterPosition();
                    douArray[page][18] = strToDouble(s.toString());
                } else {
                    douArray[page][18] = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (douArray[page][18] != douArray[page][28]) {      //变更后的值与原值比较，只有改变后才出发延续填写；RecyclerView全局更新时，首个item处的EditText会触发此listener，因此加入数值变化判定
                    int original_page = page;
                    if (douArray[page][1] == 0) {
                        Log.d("Tax", "page-" + page + " endowment_insurance for start");
                        for (; page < 11; page++) {
                            if (douArray[page + 1][1] == 0) {
                                douArray[page + 1][18] = douArray[original_page][18];
                            } else {
                                continue;
                            }
                        }
                    }
                    anyChanged = true;
                    douArray[12][18] = toFixed2(douArray[0][18]+douArray[1][18]+douArray[2][18]+douArray[3][18]+douArray[4][18]+douArray[5][18]+douArray[6][18]+
                            douArray[7][18]+douArray[8][18]+douArray[9][18]+douArray[10][18]+douArray[11][18]);
                }
            }
        });
        //失业保险编辑时触发
        holder.unemployment_insurance_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //控制两位小数“num”即为要控制的位数, 这里直接改为2
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (2 + 1));
                        holder.unemployment_insurance_input.setText(s);
                        holder.unemployment_insurance_input.setSelection(s.length());
                    }
                }

                //限制只能输入一次小数点
                if (holder.unemployment_insurance_input.getText().toString().indexOf(".") >= 0) {
                    if (holder.unemployment_insurance_input.getText().toString().indexOf(".", holder.unemployment_insurance_input.getText().toString().indexOf(".") + 1) > 0) {
                        holder.unemployment_insurance_input.setText(holder.unemployment_insurance_input.getText().toString().substring(0, holder.unemployment_insurance_input.getText().toString().length() - 1));
                        holder.unemployment_insurance_input.setSelection(holder.unemployment_insurance_input.getText().toString().length());
                    }
                }
                //第一次输入为点的时候
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    holder.unemployment_insurance_input.setText(s);
                    holder.unemployment_insurance_input.setSelection(2);
                }
                //个位数为0的时候
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        holder.unemployment_insurance_input.setText(s.subSequence(0, 1));
                        holder.unemployment_insurance_input.setSelection(1);
                    }
                }

                if (s.length() > 0) {
                    page = holder.getAdapterPosition();
                    douArray[page][19] = strToDouble(s.toString());
                } else {
                    douArray[page][19] = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (douArray[page][19] != douArray[page][29]) {      //变更后的值与原值比较，只有改变后才出发延续填写；RecyclerView全局更新时，首个item处的EditText会触发此listener，因此加入数值变化判定
                    int original_page = page;
                    if (douArray[page][1] == 0) {
                        Log.d("Tax", "page-" + page + " unemployment_insurance for start");
                        for (; page < 11; page++) {
                            if (douArray[page + 1][1] == 0) {
                                douArray[page + 1][19] = douArray[original_page][19];
                            } else {
                                continue;
                            }
                        }
                    }
                    anyChanged = true;
                    douArray[12][19] = toFixed2(douArray[0][19]+douArray[1][19]+douArray[2][19]+douArray[3][19]+douArray[4][19]+douArray[5][19]+douArray[6][19]+
                            douArray[7][19]+douArray[8][19]+douArray[9][19]+douArray[10][19]+douArray[11][19]);
                }
            }
        });
        //住房公积金编辑时触发
        holder.housing_fund_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //控制两位小数“num”即为要控制的位数, 这里直接改为2
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + (2 + 1));
                        holder.housing_fund_input.setText(s);
                        holder.housing_fund_input.setSelection(s.length());
                    }
                }

                //限制只能输入一次小数点
                if (holder.housing_fund_input.getText().toString().indexOf(".") >= 0) {
                    if (holder.housing_fund_input.getText().toString().indexOf(".", holder.housing_fund_input.getText().toString().indexOf(".") + 1) > 0) {
                        holder.housing_fund_input.setText(holder.housing_fund_input.getText().toString().substring(0, holder.housing_fund_input.getText().toString().length() - 1));
                        holder.housing_fund_input.setSelection(holder.housing_fund_input.getText().toString().length());
                    }
                }
                //第一次输入为点的时候
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    holder.housing_fund_input.setText(s);
                    holder.housing_fund_input.setSelection(2);
                }
                //个位数为0的时候
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        holder.housing_fund_input.setText(s.subSequence(0, 1));
                        holder.housing_fund_input.setSelection(1);
                    }
                }

                if (s.length() > 0) {
                    page = holder.getAdapterPosition();
                    douArray[page][20] = strToDouble(s.toString());
                } else {
                    douArray[page][20] = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (douArray[page][20] != douArray[page][30]) {      //变更后的值与原值比较，只有改变后才出发延续填写；RecyclerView全局更新时，首个item处的EditText会触发此listener，因此加入数值变化判定
                    int original_page = page;
                    if (douArray[page][1] == 0) {
                        Log.d("Tax", "page-" + page + " housing_fund for start");
                        for (; page < 11; page++) {
                            if (douArray[page + 1][1] == 0) {
                                douArray[page + 1][20] = douArray[original_page][20];
                            } else {
                                continue;
                            }
                        }
                    }
                    anyChanged = true;
                    douArray[12][20] = toFixed2(douArray[0][20]+douArray[1][20]+douArray[2][20]+douArray[3][20]+douArray[4][20]+douArray[5][20]+douArray[6][20]+
                            douArray[7][20]+douArray[8][20]+douArray[9][20]+douArray[10][20]+douArray[11][20]);
                }
            }
        });

        //延续填写监听，针对单选框，如果延续填写由未选改为勾选，此时改变单选框选择内容，应将其延续，但如果此item后单选框对应项checkBox未开启，由于checkBox对应值未延续，而不能将单选框内容延续；所以针对此情况调整
        holder.continue_write_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                page = holder.getAdapterPosition();
                if (isChecked) {
                    douArray[page][1] = 0;
                    /*int original_page = page;
                    for (; page < 11; page++) {
                        if (douArray[page+1][1] == 0) {
                            douArray[page+1][2] = douArray[original_page][2];
                            douArray[page+1][5] = douArray[original_page][5];
                            douArray[page+1][8] = douArray[original_page][8];
                            douArray[page+1][11] = douArray[original_page][11];
                            douArray[page+1][14] = douArray[original_page][14];
                        } else {
                            continue;
                        }
                    }*/
                } else {
                    douArray[page][1] = 1;
                }
            }
        });

        //子女教育CheckBox变化触发，延续填写是否勾选
        holder.children_edu_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                page = holder.getAdapterPosition();
                if (isChecked) {
                    holder.children_edu_group.setClickable(true);
                    holder.children_edu_500.setClickable(true);
                    holder.children_edu_1000.setClickable(true);
                    holder.children_edu_2000.setClickable(true);
                    holder.children_edu_500.setButtonTintList(radio_changed);
                    holder.children_edu_1000.setButtonTintList(radio_changed);
                    holder.children_edu_2000.setButtonTintList(radio_changed);

                    douArray[page][2] = 1;
                } else {
                    holder.children_edu_group.clearCheck();
                    holder.children_edu_group.setClickable(false);
                    holder.children_edu_500.setClickable(false);
                    holder.children_edu_1000.setClickable(false);
                    holder.children_edu_2000.setClickable(false);
                    holder.children_edu_500.setButtonTintList(radio_original);
                    holder.children_edu_1000.setButtonTintList(radio_original);
                    holder.children_edu_2000.setButtonTintList(radio_original);

                    douArray[page][2] = 0;
                    douArray[page][3] = 0;      //children_edu_group设为未被选中任何项
                    douArray[page][4] = 0;      //children_edu的值设为0
                }

                if (douArray[page][34] != douArray[page][2]) {
                    anyChanged = true;
                    int original_page = page;
                    if (douArray[page][1] == 0) {
                        Log.d("Tax", "page-" + page + "children_edu_Checkbox for start");
                        for (; page < 11; page++) {
                            if (douArray[page + 1][1] == 0) {
                                douArray[page + 1][2] = douArray[original_page][2];
                                douArray[page + 1][3] = douArray[original_page][3];
                                douArray[page + 1][4] = douArray[original_page][4];
                            } else {
                                continue;
                            }
                        }
                    }
                    if (douArray[original_page][2] == 0) {
                        douArray[12][4] = toFixed2(douArray[0][4] + douArray[1][4] + douArray[2][4] + douArray[3][4] + douArray[4][4] + douArray[5][4] + douArray[6][4] +
                                douArray[7][4] + douArray[8][4] + douArray[9][4] + douArray[10][4] + douArray[11][4]);
                    }
                }
            }
        });
        //继续教育CheckBox变化触发，延续填写是否勾选
        holder.continuing_edu_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                page = holder.getAdapterPosition();
                if (isChecked) {
                    holder.continuing_edu_group.setClickable(true);
                    holder.continuing_edu_400.setClickable(true);
                    holder.continuing_edu_3600.setClickable(true);
                    holder.continuing_edu_400.setButtonTintList(radio_changed);
                    holder.continuing_edu_3600.setButtonTintList(radio_changed);

                    douArray[page][5] = 1;
                } else {
                    holder.continuing_edu_group.clearCheck();
                    holder.continuing_edu_group.setClickable(false);
                    holder.continuing_edu_400.setClickable(false);
                    holder.continuing_edu_3600.setClickable(false);
                    holder.continuing_edu_400.setButtonTintList(radio_original);
                    holder.continuing_edu_3600.setButtonTintList(radio_original);

                    douArray[page][5] = 0;
                    douArray[page][6] = 0;
                    douArray[page][7] = 0;
                }

                if (douArray[page][36] != douArray[page][5]) {
                    anyChanged = true;
                    int original_page = page;
                    if (douArray[page][1] == 0) {
                        Log.d("Tax", "page-" + page + "continuing_edu_Checkbox for start");
                        for (; page < 11; page++) {
                            if (douArray[page + 1][1] == 0) {
                                douArray[page + 1][5] = douArray[original_page][5];
                                douArray[page + 1][6] = douArray[original_page][6];
                                douArray[page + 1][7] = douArray[original_page][7];
                            } else {
                                continue;
                            }
                        }
                    }
                    if (douArray[original_page][5] == 0) {
                        douArray[12][7] = toFixed2(douArray[0][7] + douArray[1][7] + douArray[2][7] + douArray[3][7] + douArray[4][7] + douArray[5][7] + douArray[6][7] +
                                douArray[7][7] + douArray[8][7] + douArray[9][7] + douArray[10][7] + douArray[11][7]);
                    }
                }
            }
        });
        //住房利息贷款CheckBox变化触发，延续填写是否勾选
        holder.home_loans_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                page = holder.getAdapterPosition();
                if (isChecked) {
                    holder.home_loans_group.setClickable(true);
                    holder.home_loans_500.setClickable(true);
                    holder.home_loans_1000.setClickable(true);
                    holder.home_loans_500.setButtonTintList(radio_changed);
                    holder.home_loans_1000.setButtonTintList(radio_changed);
                    douArray[page][8] = 1;

                    holder.housing_rents_checkBox.setChecked(false);
                    holder.housing_rents_group.clearCheck();
                    holder.housing_rents_group.setClickable(false);
                    holder.housing_rents_1500.setClickable(false);
                    douArray[page][11] = 0;
                    douArray[page][12] = 0;
                    douArray[page][13] = 0;
                } else {
                    holder.home_loans_group.clearCheck();
                    holder.home_loans_group.setClickable(false);
                    holder.home_loans_500.setClickable(false);
                    holder.home_loans_1000.setClickable(false);
                    holder.home_loans_500.setButtonTintList(radio_original);
                    holder.home_loans_1000.setButtonTintList(radio_original);

                    douArray[page][8] = 0;
                    douArray[page][9] = 0;
                    douArray[page][10] = 0;
                }

                if (douArray[page][38] != douArray[page][8]) {
                    anyChanged = true;
                    int original_page = page;
                    if (douArray[page][1] == 0) {
                        Log.d("Tax", "page-" + page + "home_loans_Checkbox for start");
                        for (; page < 11; page++) {
                            if (douArray[page + 1][1] == 0) {
                                douArray[page + 1][8] = douArray[original_page][8];
                                douArray[page + 1][9] = douArray[original_page][9];
                                douArray[page + 1][10] = douArray[original_page][10];

                                douArray[page + 1][11] = douArray[original_page][11];
                                douArray[page + 1][12] = douArray[original_page][12];
                                douArray[page + 1][13] = douArray[original_page][13];
                            } else {
                                continue;
                            }
                        }
                    }
                    if (douArray[original_page][8] == 0) {
                        douArray[12][10] = toFixed2(douArray[0][10] + douArray[1][10] + douArray[2][10] + douArray[3][10] + douArray[4][10] + douArray[5][10] + douArray[6][10] +
                                douArray[7][10] + douArray[8][10] + douArray[9][10] + douArray[10][10] + douArray[11][10]);
                    }
                    //notifyDataSetChanged();
                }
            }
        });
        //住房租金CheckBox变化触发，延续填写是否勾选
        holder.housing_rents_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                page = holder.getAdapterPosition();
                if (isChecked) {
                    holder.housing_rents_group.setClickable(true);
                    holder.housing_rents_1500.setClickable(true);
                    holder.housing_rents_1500.setButtonTintList(radio_changed);
                    douArray[page][11] = 1;

                    holder.home_loans_checkBox.setChecked(false);
                    holder.home_loans_group.clearCheck();
                    holder.home_loans_group.setClickable(false);
                    holder.home_loans_500.setClickable(false);
                    holder.home_loans_1000.setClickable(false);
                    douArray[page][8] = 0;
                    douArray[page][9] = 0;
                    douArray[page][10] = 0;
                } else {
                    holder.housing_rents_group.clearCheck();
                    holder.housing_rents_group.setClickable(false);
                    holder.housing_rents_1500.setClickable(false);
                    holder.housing_rents_1500.setButtonTintList(radio_original);

                    douArray[page][11] = 0;
                    douArray[page][12] = 0;
                    douArray[page][13] = 0;
                }

                if (douArray[page][40] != douArray[page][11]) {
                    anyChanged = true;
                    int original_page = page;
                    if (douArray[page][1] == 0) {
                        Log.d("Tax", "page-" + page + "housing_rents_Checkbox for start");
                        for (; page < 11; page++) {
                            if (douArray[page + 1][1] == 0) {
                                douArray[page + 1][11] = douArray[original_page][11];
                                douArray[page + 1][12] = douArray[original_page][12];
                                douArray[page + 1][13] = douArray[original_page][13];

                                douArray[page + 1][8] = douArray[original_page][8];
                                douArray[page + 1][9] = douArray[original_page][9];
                                douArray[page + 1][10] = douArray[original_page][10];
                            } else {
                                continue;
                            }
                        }
                    }
                    if (douArray[original_page][11] == 0) {
                        douArray[12][13] = toFixed2(douArray[0][13] + douArray[1][13] + douArray[2][13] + douArray[3][13] + douArray[4][13] + douArray[5][13] + douArray[6][13] +
                                douArray[7][13] + douArray[8][13] + douArray[9][13] + douArray[10][13] + douArray[11][13]);
                    }
                }
            }
        });
        //赡养老人CheckBox变化触发，延续填写是否勾选
        holder.support_old_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                page = holder.getAdapterPosition();
                if (isChecked) {
                    holder.support_old_group.setClickable(true);
                    holder.support_old_500.setClickable(true);
                    holder.support_old_1000.setClickable(true);
                    holder.support_old_2000.setClickable(true);
                    holder.support_old_500.setButtonTintList(radio_changed);
                    holder.support_old_1000.setButtonTintList(radio_changed);
                    holder.support_old_2000.setButtonTintList(radio_changed);

                    douArray[page][14] = 1;
                } else {
                    holder.support_old_group.clearCheck();
                    holder.support_old_group.setClickable(false);
                    holder.support_old_500.setClickable(false);
                    holder.support_old_1000.setClickable(false);
                    holder.support_old_2000.setClickable(false);
                    holder.support_old_500.setButtonTintList(radio_original);
                    holder.support_old_1000.setButtonTintList(radio_original);
                    holder.support_old_2000.setButtonTintList(radio_original);

                    douArray[page][14] = 0;
                    douArray[page][15] = 0;
                    douArray[page][16] = 0;
                }

                if (douArray[page][42] != douArray[page][14]) {
                    anyChanged = true;
                    int original_page = page;
                    if (douArray[page][1] == 0) {
                        Log.d("Tax", "page-" + page + "support_old_Checkbox for start");
                        for (; page < 11; page++) {
                            if (douArray[page + 1][1] == 0) {
                                douArray[page + 1][14] = douArray[original_page][14];
                                douArray[page + 1][15] = douArray[original_page][15];
                                douArray[page + 1][16] = douArray[original_page][16];
                            } else {
                                continue;
                            }
                        }
                    }
                    if (douArray[original_page][14] == 0) {
                        douArray[12][16] = toFixed2(douArray[0][16] + douArray[1][16] + douArray[2][16] + douArray[3][16] + douArray[4][16] + douArray[5][16] + douArray[6][16] +
                                douArray[7][16] + douArray[8][16] + douArray[9][16] + douArray[10][16] + douArray[11][16]);
                    }
                }
            }
        });

        //子女教育单选框触发监听，延续所选值
        holder.children_edu_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (holder.children_edu_checkBox.isChecked()) {
                    page = holder.getAdapterPosition();
                    switch (checkedId) {
                        case R.id.children_edu_500_p:
                            douArray[page][3] = 1;
                            douArray[page][4] = 500;
                            break;
                        case R.id.children_edu_1000_p:
                            douArray[page][3] = 2;
                            douArray[page][4] = 1000;
                            break;
                        case R.id.children_edu_2000_p:
                            douArray[page][3] = 3;
                            douArray[page][4] = 2000;
                            break;
                        default:
                            douArray[page][3] = 0;
                            douArray[page][4] = 0;
                            break;
                    }
                    if (douArray[page][35] != douArray[page][3]) {
                        anyChanged = true;
                        int original_page = page;
                        if (douArray[page][1] == 0) {
                            Log.d("Tax", "page-" + page + "children_group_Checkbox for start");
                            for (; page < 11; page++) {
                                if (douArray[page + 1][1] == 0) {
                                    douArray[page + 1][2] = douArray[original_page][2];
                                    douArray[page + 1][3] = douArray[original_page][3];
                                    douArray[page + 1][4] = douArray[original_page][4];
                                } else {
                                    continue;
                                }
                            }
                        }
                        douArray[12][4] = toFixed2(douArray[0][4] + douArray[1][4] + douArray[2][4] + douArray[3][4] + douArray[4][4] + douArray[5][4] + douArray[6][4] +
                                douArray[7][4] + douArray[8][4] + douArray[9][4] + douArray[10][4] + douArray[11][4]);
                    }
                }

            }
        });
        //继续教育单选框触发监听，延续所选值
        holder.continuing_edu_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (holder.continue_write_checkBox.isChecked()) {
                    page = holder.getAdapterPosition();
                    switch (checkedId) {
                        case R.id.continuing_edu_400_p:
                            douArray[page][6] = 1;
                            douArray[page][7] = 400;
                            break;
                        case R.id.continuing_edu_3600_p:
                            douArray[page][6] = 2;
                            douArray[page][7] = 3600;
                            break;
                        default:
                            douArray[page][6] = 0;
                            douArray[page][7] = 0;
                            break;
                    }
                    if (douArray[page][37] != douArray[page][6]) {
                        anyChanged = true;
                        int original_page = page;
                        if (douArray[page][1] == 0) {
                            Log.d("Tax", "page-" + page + "children_group_Checkbox for start");
                            for (; page < 11; page++) {
                                if (douArray[page + 1][1] == 0) {
                                    douArray[page + 1][5] = douArray[original_page][5];
                                    douArray[page + 1][6] = douArray[original_page][6];
                                    douArray[page + 1][7] = douArray[original_page][7];
                                } else {
                                    continue;
                                }
                            }
                        }
                        douArray[12][7] = toFixed2(douArray[0][7] + douArray[1][7] + douArray[2][7] + douArray[3][7] + douArray[4][7] + douArray[5][7] + douArray[6][7] +
                                douArray[7][7] + douArray[8][7] + douArray[9][7] + douArray[10][7] + douArray[11][7]);
                    }
                }

            }
        });
        //住房利息贷款单选框触发监听，延续所选值
        holder.home_loans_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (holder.home_loans_checkBox.isChecked()) {
                    page = holder.getAdapterPosition();
                    switch (checkedId) {
                        case R.id.home_loans_500_p:
                            douArray[page][9] = 1;
                            douArray[page][10] = 500;
                            break;
                        case R.id.home_loans_1000_p:
                            douArray[page][9] = 2;
                            douArray[page][10] = 1000;
                            break;
                        default:
                            douArray[page][9] = 0;
                            douArray[page][10] = 0;
                            break;
                    }
                    if (douArray[page][39] != douArray[page][9]) {
                        anyChanged = true;
                        int original_page = page;
                        if (douArray[page][1] == 0) {
                            Log.d("Tax", "page-" + page + "children_group_Checkbox for start");
                            for (; page < 11; page++) {
                                if (douArray[page + 1][1] == 0) {
                                    douArray[page + 1][8] = douArray[original_page][8];
                                    douArray[page + 1][9] = douArray[original_page][9];
                                    douArray[page + 1][10] = douArray[original_page][10];

                                    douArray[page + 1][11] = douArray[original_page][11];
                                    douArray[page + 1][12] = douArray[original_page][12];
                                    douArray[page + 1][13] = douArray[original_page][13];
                                } else {
                                    continue;
                                }
                            }
                        }
                        douArray[12][10] = toFixed2(douArray[0][10] + douArray[1][10] + douArray[2][10] + douArray[3][10] + douArray[4][10] + douArray[5][10] + douArray[6][10] +
                                douArray[7][10] + douArray[8][10] + douArray[9][10] + douArray[10][10] + douArray[11][10]);
                    }
                }

            }
        });
        //住房租金单选框触发监听，延续所选值
        holder.housing_rents_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (holder.housing_rents_checkBox.isChecked()) {
                    page = holder.getAdapterPosition();
                    switch (checkedId) {
                        case R.id.housing_rents_1500_p:
                            douArray[page][12] = 1;
                            douArray[page][13] = 1500;
                            break;
                        default:
                            douArray[page][12] = 0;
                            douArray[page][13] = 0;
                            break;
                    }
                    if (douArray[page][41] != douArray[page][12]) {
                        anyChanged = true;
                        int original_page = page;
                        if (douArray[page][1] == 0) {
                            Log.d("Tax", "page-" + page + "children_group_Checkbox for start");
                            for (; page < 11; page++) {
                                if (douArray[page + 1][1] == 0) {
                                    douArray[page + 1][11] = douArray[original_page][11];
                                    douArray[page + 1][12] = douArray[original_page][12];
                                    douArray[page + 1][13] = douArray[original_page][13];

                                    douArray[page + 1][8] = douArray[original_page][8];
                                    douArray[page + 1][9] = douArray[original_page][9];
                                    douArray[page + 1][10] = douArray[original_page][10];
                                } else {
                                    continue;
                                }
                            }
                        }
                        douArray[12][13] = toFixed2(douArray[0][13] + douArray[1][13] + douArray[2][13] + douArray[3][13] + douArray[4][13] + douArray[5][13] + douArray[6][13] +
                                douArray[7][13] + douArray[8][13] + douArray[9][13] + douArray[10][13] + douArray[11][13]);
                    }
                }

            }
        });
        //赡养老人单选框触发监听，延续所选值
        holder.support_old_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (holder.support_old_checkBox.isChecked()) {
                    page = holder.getAdapterPosition();
                    switch (checkedId) {
                        case R.id.support_old_500_p:
                            douArray[page][15] = 1;
                            douArray[page][16] = 500;
                            break;
                        case R.id.support_old_1000_p:
                            douArray[page][15] = 2;
                            douArray[page][16] = 1000;
                            break;
                        case R.id.support_old_2000_p:
                            douArray[page][15] = 3;
                            douArray[page][16] = 2000;
                            break;
                        default:
                            douArray[page][15] = 0;
                            douArray[page][16] = 0;
                            break;
                    }
                    if (douArray[page][43] != douArray[page][15]) {
                        anyChanged = true;
                        int original_page = page;
                        if (douArray[page][1] == 0) {
                            Log.d("Tax", "page-" + page + "children_group_Checkbox for start");
                            for (; page < 11; page++) {
                                if (douArray[page + 1][1] == 0) {
                                    douArray[page + 1][14] = douArray[original_page][14];
                                    douArray[page + 1][15] = douArray[original_page][15];
                                    douArray[page + 1][16] = douArray[original_page][16];
                                } else {
                                    continue;
                                }
                            }
                        }
                        douArray[12][16] = toFixed2(douArray[0][16] + douArray[1][16] + douArray[2][16] + douArray[3][16] + douArray[4][16] + douArray[5][16] + douArray[6][16] +
                                douArray[7][16] + douArray[8][16] + douArray[9][16] + douArray[10][16] + douArray[11][16]);
                    }
                }

            }
        });
        
        return holder;
    }

    //dou[position][0]:税前收入-pretax_income_input ; dou{position][1]:延续填写-continue_write_checkBox
    //dou[position][2]:子女教育CheckBox-children_edu_checkBox ; dou[position][3]:子女教育单选框-children_edu_group(0,1,2,3) ; dou[position][4]:子女教育值-children_edu_sum
    //dou[position][5]:继续教育CheckBox-continuing_edu_checkBox ; dou[position][6]:继续教育单选框-continuing_edu_group(0,1,2) ; dou[position][7]:继续教育值-continuing_edu_sum
    //dou[position][8]:住房利息贷款CheckBox-home_loans_checkBox ; dou[position][9]:住房利息贷款单选框-home_loans_group(0,1,2) ; dou[position][10]:住房利息贷款值-home_loans_sum
    //dou[position][11]:住房租金CheckBox-housing_rents_checkBox ; dou[position][12]:住房租金单选框-housing_rents_group(0,1) ; dou[position][13]:住房租金值-housing_rents_sum
    //dou[position][14]:赡养老人CheckBox-support_old_checkBox ; dou[position][15]:赡养老人单选框-support_old_group(0,1,2,3) ; dou[position][16]:赡养老人值-support_old_sum
    //dou[position][17]:养老保险-endowment_insurance_input ; dou[position][18]:医疗保险-medical_insurance_input ; dou[position][19]:失业保险-unemployment_insurance_input
    //dou[position][20]:住房公积金-housing_fund_input
    //dou[position][21]:应纳税所得额-taxable_income ; dou[position][22]:税率/预扣率-tax_rate ; dou[position][23]:累计应缴个税-total_income_tax
    //dou[position][24]:当月应缴个税-month_income_tax ; dou[position][25]:当月到手收入-month_final_income
    //dou[position][26]:税前收入原值-pretax_income_input_original ; dou[position][27]:养老保险原值 ; dou[position][28]:医疗保险原值
    //dou[position][29]:失业保险原值 ; dou[position][30]:住房公积金原值
    //dou[position][34]:子女教育checkbox原值 ; dou[[position][35]:子女教育单选框原值 ; dou[position][36]:继续教育checkbox原值 ; dou[position][37]:继续教育单选框原值
    //dou[position][38]:住房利息贷款checkbox原值 ; dou[position][39]:住房利息贷款单选框原值 ; dou[position][40]:住房租金checkbox原值 ; dou[position][41]:住房租金单选框原值
    //dou[position][42]:赡养老人checkbox原值 ; dou[position][43]:赡养老人单选框原值
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PreciseValue preciseValue = preciseValueList.get(position);
        double[][] dou = preciseValue.getDou();

        //holder.itemView.setTag(position);
        holder.hide_text.setText(preciseValue.getFlag());
        douArray[position][26] = dou[position][0];      //变更税前收入原值为延续后的值
        douArray[position][27] = dou[position][17];
        douArray[position][28] = dou[position][18];
        douArray[position][29] = dou[position][19];
        douArray[position][30] = dou[position][20];

        douArray[position][34] = dou[position][2];
        douArray[position][36] = dou[position][5];
        douArray[position][38] = dou[position][8];
        douArray[position][40] = dou[position][11];
        douArray[position][42] = dou[position][14];
        douArray[position][35] = dou[position][3];
        douArray[position][37] = dou[position][6];
        douArray[position][39] = dou[position][9];
        douArray[position][41] = dou[position][12];
        douArray[position][43] = dou[position][15];

        holder.pretax_income_input.setText(douToString(dou[position][0]));
        holder.endowment_insurance_input.setText(douToString(dou[position][17]));
        holder.medical_insurance_input.setText(douToString(dou[position][18]));
        holder.unemployment_insurance_input.setText(douToString(dou[position][19]));
        holder.housing_fund_input.setText(douToString(dou[position][20]));

        holder.taxable_income.setText(douToString(dou[position][21]));
        holder.tax_rate.setText(douToString(dou[position][22]));
        holder.total_income_tax.setText(douToString(dou[position][23]));
        holder.month_income_tax.setText(douToString(dou[position][24]));
        holder.month_final_income.setText(douToString(dou[position][25]));

        if (position==12) {
            holder.pretax_income_input.setFocusableInTouchMode(false);
            holder.endowment_insurance_input.setFocusableInTouchMode(false);
            holder.medical_insurance_input.setFocusableInTouchMode(false);
            holder.unemployment_insurance_input.setFocusableInTouchMode(false);
            holder.housing_fund_input.setFocusableInTouchMode(false);
            holder.continue_write_checkBox.setVisibility(View.GONE);

            holder.children_edu_checkBox.setVisibility(View.INVISIBLE);
            holder.children_edu_group.setVisibility(View.GONE);
            holder.children_edu_total.setVisibility(View.VISIBLE);
            holder.children_edu_total.setText(douToString(dou[position][4]));
            
            holder.continuing_edu_checkBox.setVisibility(View.INVISIBLE);
            holder.continuing_edu_group.setVisibility(View.GONE);
            holder.continuing_edu_total.setVisibility(View.VISIBLE);
            holder.continuing_edu_total.setText(douToString(dou[position][7]));

            holder.home_loans_checkBox.setVisibility(View.INVISIBLE);
            holder.home_loans_group.setVisibility(View.GONE);
            holder.home_loans_total.setVisibility(View.VISIBLE);
            holder.home_loans_total.setText(douToString(dou[position][10]));

            holder.housing_rents_checkBox.setVisibility(View.INVISIBLE);
            holder.housing_rents_group.setVisibility(View.GONE);
            holder.housing_rents_total.setVisibility(View.VISIBLE);
            holder.housing_rents_total.setText(douToString(dou[position][13]));

            holder.support_old_checkBox.setVisibility(View.INVISIBLE);
            holder.support_old_group.setVisibility(View.GONE);
            holder.support_old_total.setVisibility(View.VISIBLE);
            holder.support_old_total.setText(douToString(dou[position][16]));
        } else {
            //对子女教育项的相关元素赋值
            if (dou[position][2] == 1) {
                holder.children_edu_checkBox.setChecked(true);
                holder.children_edu_group.setClickable(true);
                holder.children_edu_500.setClickable(true);
                holder.children_edu_1000.setClickable(true);
                holder.children_edu_2000.setClickable(true);
                holder.children_edu_500.setButtonTintList(radio_changed);
                holder.children_edu_1000.setButtonTintList(radio_changed);
                holder.children_edu_2000.setButtonTintList(radio_changed);
                switch (douToString(dou[position][3])) {
                    case "1":
                        holder.children_edu_500.setChecked(true);
                        break;
                    case "2":
                        holder.children_edu_1000.setChecked(true);
                        break;
                    case "3":
                        holder.children_edu_2000.setChecked(true);
                        break;
                    default:
                        holder.children_edu_500.setChecked(false);
                        holder.children_edu_1000.setChecked(false);
                        holder.children_edu_2000.setChecked(false);
                        break;
                }
            } else {
                holder.children_edu_checkBox.setChecked(false);
                holder.children_edu_group.setClickable(false);
                holder.children_edu_500.setChecked(false);
                holder.children_edu_1000.setChecked(false);
                holder.children_edu_2000.setChecked(false);
                holder.children_edu_500.setClickable(false);
                holder.children_edu_1000.setClickable(false);
                holder.children_edu_2000.setClickable(false);
                holder.children_edu_500.setButtonTintList(radio_original);
                holder.children_edu_1000.setButtonTintList(radio_original);
                holder.children_edu_2000.setButtonTintList(radio_original);
            }
            
            //对继续教育相关元素赋值
            if (dou[position][5] == 1) {
                holder.continuing_edu_checkBox.setChecked(true);
                holder.continuing_edu_group.setClickable(true);
                holder.continuing_edu_400.setClickable(true);
                holder.continuing_edu_3600.setClickable(true);
                holder.continuing_edu_400.setButtonTintList(radio_changed);
                holder.continuing_edu_3600.setButtonTintList(radio_changed);
                switch (douToString(dou[position][6])) {
                    case "1":
                        holder.continuing_edu_400.setChecked(true);
                        break;
                    case "2":
                        holder.continuing_edu_3600.setChecked(true);
                        break;
                    default:
                        holder.continuing_edu_400.setChecked(false);
                        holder.continuing_edu_3600.setChecked(false);
                        break;
                }
            } else {
                holder.continuing_edu_checkBox.setChecked(false);
                holder.continuing_edu_group.setClickable(false);
                holder.continuing_edu_400.setChecked(false);
                holder.continuing_edu_3600.setChecked(false);
                holder.continuing_edu_400.setClickable(false);
                holder.continuing_edu_3600.setClickable(false);
                holder.continuing_edu_400.setButtonTintList(radio_original);
                holder.continuing_edu_3600.setButtonTintList(radio_original);
            }

            //对住房利息贷款项相关元素赋值
            if (dou[position][8] == 1) {
                holder.home_loans_checkBox.setChecked(true);
                holder.home_loans_group.setClickable(true);
                holder.home_loans_500.setClickable(true);
                holder.home_loans_1000.setClickable(true);
                holder.home_loans_500.setButtonTintList(radio_changed);
                holder.home_loans_1000.setButtonTintList(radio_changed);
                switch (douToString(dou[position][9])) {
                    case "1":
                        holder.home_loans_500.setChecked(true);
                        break;
                    case "2":
                        holder.home_loans_1000.setChecked(true);
                        break;
                    default:
                        holder.home_loans_500.setChecked(false);
                        holder.home_loans_1000.setChecked(false);
                        break;
                }
            } else {
                holder.home_loans_checkBox.setChecked(false);
                holder.home_loans_group.setClickable(false);
                holder.home_loans_500.setChecked(false);
                holder.home_loans_1000.setChecked(false);
                holder.home_loans_500.setClickable(false);
                holder.home_loans_1000.setClickable(false);
                holder.home_loans_500.setButtonTintList(radio_original);
                holder.home_loans_1000.setButtonTintList(radio_original);
            }

            //住房租金项相关元素赋值
            if (dou[position][11] == 1) {
                holder.housing_rents_checkBox.setChecked(true);
                holder.housing_rents_group.setClickable(true);
                holder.housing_rents_1500.setClickable(true);
                holder.housing_rents_1500.setButtonTintList(radio_changed);
                switch (douToString(dou[position][12])) {
                    case "1":
                        holder.housing_rents_1500.setChecked(true);
                        break;
                    default:
                        holder.housing_rents_1500.setChecked(false);
                        break;
                }
            } else {
                holder.housing_rents_checkBox.setChecked(false);
                holder.housing_rents_group.setClickable(false);
                holder.housing_rents_1500.setChecked(false);
                holder.housing_rents_1500.setClickable(false);
                holder.housing_rents_1500.setButtonTintList(radio_original);
            }

            //赡养老人相关元素赋值
            if (dou[position][14] == 1) {
                holder.support_old_checkBox.setChecked(true);
                holder.support_old_group.setClickable(true);
                holder.support_old_500.setClickable(true);
                holder.support_old_1000.setClickable(true);
                holder.support_old_2000.setClickable(true);
                holder.support_old_500.setButtonTintList(radio_changed);
                holder.support_old_1000.setButtonTintList(radio_changed);
                holder.support_old_2000.setButtonTintList(radio_changed);
                switch (douToString(dou[position][15])) {
                    case "1":
                        holder.support_old_500.setChecked(true);
                        break;
                    case "2":
                        holder.support_old_1000.setChecked(true);
                        break;
                    case "3":
                        holder.support_old_2000.setChecked(true);
                        break;
                    default:
                        holder.support_old_500.setChecked(false);
                        holder.support_old_1000.setChecked(false);
                        holder.support_old_2000.setChecked(false);
                        break;
                }
            } else {
                holder.support_old_checkBox.setChecked(false);
                holder.support_old_group.setClickable(false);
                holder.support_old_500.setChecked(false);
                holder.support_old_1000.setChecked(false);
                holder.support_old_2000.setChecked(false);
                holder.support_old_500.setClickable(false);
                holder.support_old_1000.setClickable(false);
                holder.support_old_2000.setClickable(false);
                holder.support_old_500.setButtonTintList(radio_original);
                holder.support_old_1000.setButtonTintList(radio_original);
                holder.support_old_2000.setButtonTintList(radio_original);
            }

            //延续填写框赋值
            if (dou[position][1] == 1) {
                holder.continue_write_checkBox.setChecked(false);
            } else {
                holder.continue_write_checkBox.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return preciseValueList.size();
    }
}
