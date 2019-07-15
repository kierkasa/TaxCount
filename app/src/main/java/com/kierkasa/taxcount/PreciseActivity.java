package com.kierkasa.taxcount;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.kierkasa.taxcount.MyFuntion.dpToPx;
import static com.kierkasa.taxcount.MyFuntion.getScreenSize;
import static com.kierkasa.taxcount.MyFuntion.strToInt;
import static com.kierkasa.taxcount.MyFuntion.toFixed;
import static com.kierkasa.taxcount.MyFuntion.toFixed2;
import static com.kierkasa.taxcount.PreciseValueAdapter.anyChanged;
import static com.kierkasa.taxcount.PreciseValueAdapter.douArray;

//取消使用
public class PreciseActivity extends AppCompatActivity implements View.OnClickListener,TaxCountCallBack {

    private List<PreciseValue> preciseValueList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PreciseValueAdapter adapter;

    private TaxCountPresenter taxCountPresenter;

    private Button button_month_1, button_month_2, button_month_3, button_month_4, button_month_5, button_month_6, button_month_7, button_month_8,
            button_month_9, button_month_10, button_month_11, button_month_12, button_month_total, button_precise_calculate, button_precise_clean, icon_count, icon_accurate, icon_hint;

    private TextView stay_img, preciseCalculate;

    private int button_month_1_x, button_month_1_y, button_month_6_x, button_month_6_y, button_month_7_x, button_month_7_y, screen_width,
            move_x_int, mTop, mLeft, mTop2, mLeft2, mTop3, mLeft3, accumulation_move_x, precise_calculation_height;

    //private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.precise_calculate);

        icon_accurate = findViewById(R.id.accurate);
        icon_accurate.setBackgroundResource(R.drawable.set_calculate_stay);

        taxCountPresenter = new TaxCountPresenter(this);
        //screen_density = this.getResources().getDisplayMetrics().density;
        screen_width = getScreenSize(this, "width");
        mTop = dpToPx(this, 7);
        mLeft = dpToPx(this, 63);
        mTop2 = dpToPx(this, 7);
        mLeft2 = dpToPx(this, 253);
        mTop3 = dpToPx(this, 42);
        mLeft3 = dpToPx(this, 16);

        recyclerView = findViewById(R.id.precise_recycler);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(14);

        initPreciseValue(douArray);
        adapter = new PreciseValueAdapter(preciseValueList);
        recyclerView.setAdapter(adapter);

        button_month_1 = findViewById(R.id.month_1);
        button_month_2 = findViewById(R.id.month_2);
        button_month_3 = findViewById(R.id.month_3);
        button_month_4 = findViewById(R.id.month_4);
        button_month_5 = findViewById(R.id.month_5);
        button_month_6 = findViewById(R.id.month_6);
        button_month_7 = findViewById(R.id.month_7);
        button_month_8 = findViewById(R.id.month_8);
        button_month_9 = findViewById(R.id.month_9);
        button_month_10 = findViewById(R.id.month_10);
        button_month_11 = findViewById(R.id.month_11);
        button_month_12 = findViewById(R.id.month_12);
        button_month_total = findViewById(R.id.month_total);
        button_precise_calculate = findViewById(R.id.precise_calculate);
        button_precise_clean = findViewById(R.id.precise_clean);
        icon_count = findViewById(R.id.count);
        icon_hint = findViewById(R.id.hint);
        
        button_month_1.setOnClickListener(this);
        button_month_2.setOnClickListener(this);
        button_month_3.setOnClickListener(this);
        button_month_4.setOnClickListener(this);
        button_month_5.setOnClickListener(this);
        button_month_6.setOnClickListener(this);
        button_month_7.setOnClickListener(this);
        button_month_8.setOnClickListener(this);
        button_month_9.setOnClickListener(this);
        button_month_10.setOnClickListener(this);
        button_month_11.setOnClickListener(this);
        button_month_12.setOnClickListener(this);
        button_month_total.setOnClickListener(this);
        button_precise_calculate.setOnClickListener(this);
        button_precise_clean.setOnClickListener(this);
        icon_count.setOnClickListener(this);
        icon_hint.setOnClickListener(this);

        stay_img = findViewById(R.id.stay_img);
        preciseCalculate = findViewById(R.id.preciseCalculation);

        //myHandler = new MyHandler(this);

        new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                button_month_1_x = button_month_1.getLeft();
                button_month_1_y = button_month_1.getTop();
                button_month_6_x = button_month_6.getLeft();
                button_month_6_y = button_month_6.getTop();
                button_month_7_x = button_month_7.getLeft();
                button_month_7_y = button_month_7.getTop();
                precise_calculation_height = preciseCalculate.getBottom();

                mTop = button_month_1_y - precise_calculation_height;
                mLeft = button_month_1_x;
                mTop2 = button_month_6_y - precise_calculation_height;
                mLeft2 = button_month_6_x;
                mTop3 = button_month_7_y - precise_calculation_height;
                mLeft3 = button_month_7_x;

                //Message message = myHandler.obtainMessage(0, origin);
                //myHandler.sendMessage(message);

            }
        }.start();


        accumulation_move_x = 0;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                accumulation_move_x += dx;
                move_x_int = (accumulation_move_x * 114)/screen_width ;
                setViewMargin(move_x_int, dy);

                if (anyChanged) {
                    adapter.notifyDataSetChanged();
                    anyChanged = false;
                }

                /*TranslateAnimation animation = new TranslateAnimation(0, move_x,0,0);
                animation.setFillAfter(true);
                animation.setDuration(10);
                back_img.startAnimation(animation);*/
                //ObjectAnimator.ofFloat(back_img, "translationX", 0, move_x).setDuration(100).start();

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    private void initPreciseValue(double[][] dou) {
        Log.d("Tax","init start");
        PreciseValue page1 = new PreciseValue("1", dou);
        PreciseValue page2 = new PreciseValue("2", dou);
        PreciseValue page3 = new PreciseValue("3", dou);
        PreciseValue page4 = new PreciseValue("4", dou);
        PreciseValue page5 = new PreciseValue("5", dou);
        PreciseValue page6 = new PreciseValue("6", dou);
        PreciseValue page7 = new PreciseValue("7", dou);
        PreciseValue page8 = new PreciseValue("8", dou);
        PreciseValue page9 = new PreciseValue("9", dou);
        PreciseValue page10 = new PreciseValue("10", dou);
        PreciseValue page11 = new PreciseValue("11", dou);
        PreciseValue page12 = new PreciseValue("12", dou);
        PreciseValue pageTotal = new PreciseValue("13", dou);
        preciseValueList.add(page1);
        preciseValueList.add(page2);
        preciseValueList.add(page3);
        preciseValueList.add(page4);
        preciseValueList.add(page5);
        preciseValueList.add(page6);
        preciseValueList.add(page7);
        preciseValueList.add(page8);
        preciseValueList.add(page9);
        preciseValueList.add(page10);
        preciseValueList.add(page11);
        preciseValueList.add(page12);
        preciseValueList.add(pageTotal);
        Log.d("Tax","init stop");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.precise_calculate:
                taxCountPresenter.preciseCount(douArray);
                adapter.notifyDataSetChanged();
                break;
            case R.id.precise_clean:
                //adapter.notifyDataSetChanged();
                cleanPreciseCount();
                break;
            case R.id.count:
                Intent intent = new Intent(PreciseActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.hint:
                Intent intent1 = new Intent(PreciseActivity.this, HintActivity.class);
                startActivity(intent1);
                break;
            default:
                Button m = findViewById(v.getId());
                int flag;
                if (m.getText().toString().equals("总")) {
                    flag = 13;
                } else {
                    flag = strToInt(m.getText().toString());
                }
                final TopSmoothScroller topSmoothScroller = new TopSmoothScroller(this);
                topSmoothScroller.setTargetPosition(flag-1);
                layoutManager.startSmoothScroll(topSmoothScroller);

                /*if (n != null) {
                    n.setBackgroundResource(R.drawable.set_month_button);
                }
                m.setBackgroundResource(R.drawable.set_month_button_stay);
                n = m;*/
                break;
        }
    }

    @Override
    public void preciseCountSetValue(double[][] dou) {
        Log.d("Tax", "start set value");
        douArray = dou;
        adapter.notifyDataSetChanged();
        if (douArray[12][23] < douArray[12][24]) {
            Toast.makeText(this, "您有应退个税，目前政策为年度内暂不退税，如截至12月仍有应退个税，可在个人所得税汇算清缴时办理退税事宜，详情可拨打12366咨询税务人员", Toast.LENGTH_SHORT).show();
        } else if (douArray[12][23] > douArray[12][24]) {
            Toast.makeText(this, "您有应补个税，可在个人所得税汇算清缴时办理补缴事宜，详情可拨打12366咨询税务人员，或添加微信号***进行咨询", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void quickCountSetValue(double[] doubles) {
    }

    private void cleanPreciseCount() {
        for (int page=0; page<13; page++) {
            Arrays.fill(douArray[page], 0);
        }
        adapter.notifyDataSetChanged();
    }

    /*private class MyHandler extends Handler {
        private WeakReference<PreciseActivity> weakReference;

        private MyHandler(PreciseActivity preciseActivity) {
            weakReference = new WeakReference<>(preciseActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PreciseActivity activity = weakReference.get();
            switch(msg.what) {
                case 0:
                    back_img.smoothScrollTo(strToInt(msg.obj.toString()),0,10);
                    inner_back.setBackgroundResource(R.drawable.set_month_button_stay);
                    break;
                default:
                    break;
            }
        }
    }*/

    //stay_img控件移动
    private void setViewMargin(int dx, int dy) {
        int top, left;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) stay_img.getLayoutParams();

        if (dx <=570){
            top = mTop;
            left = mLeft + dx;
        } else if (dx<=684&&dx>570) {
            top = mTop2 + (dx-570)*105/144;
            left = mLeft2 + (dx-570)*(-711)/144;
        } else if (dx>684) {
            top = mTop3;
            left = mLeft3 + (dx - 684);
        } else {
            top = mTop;
            left = mLeft;
        }


        layoutParams.topMargin = top;
        layoutParams.leftMargin = left;
        stay_img.setLayoutParams(layoutParams);
    }

    //点击空白处关闭软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.getCurrentFocus() != null) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}
