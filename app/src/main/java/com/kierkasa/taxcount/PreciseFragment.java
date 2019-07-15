package com.kierkasa.taxcount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.kierkasa.taxcount.MyFuntion.dpToPx;
import static com.kierkasa.taxcount.MyFuntion.getScreenSize;
import static com.kierkasa.taxcount.MyFuntion.strToInt;
import static com.kierkasa.taxcount.PreciseValueAdapter.anyChanged;
import static com.kierkasa.taxcount.PreciseValueAdapter.douArray;
import static com.kierkasa.taxcount.PreciseValueAdapter.isInput;

public class PreciseFragment extends Fragment implements TaxCountCallBack, View.OnClickListener{

    private View view;
    
    private List<PreciseValue> preciseValueList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PreciseValueAdapter adapter;

    private TaxCountPresenter taxCountPresenter;

    private Button button_month_1, button_month_2, button_month_3, button_month_4, button_month_5, button_month_6, button_month_7, button_month_8,
            button_month_9, button_month_10, button_month_11, button_month_12, button_month_total, button_precise_calculate, button_precise_clean, icon_count, icon_accurate, icon_hint;
    private TextView stay_img, preciseCalculate;

    private int button_month_1_x, button_month_1_y, button_month_6_x, button_month_6_y, button_month_7_x, button_month_7_y, screen_width,
            move_x_int, mTop, mLeft, mTop2, mLeft2, mTop3, mLeft3, accumulation_move_x, precise_calculation_height, offset_bt6, offset_bt7, offset_x, offset_y, offset, i;

    private InputMethodManager imm;
    private MyHandler handler;
    //返回fragment实例
    public static PreciseFragment newInstance() {
        Log.d("Tax","precisefragment start");
        PreciseFragment fragment = new PreciseFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.precise_calculate, container, false);

        initWidget();
        initRecycler();
        initListener();

        //点击空白处关闭键盘，调用activity中重写的方法
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                getActivity().onTouchEvent(motionEvent);
                return false;
            }
        });

        startThread();

        Log.d("Tax","precisefragment view will return");
        return view;
    }

    //初始化用于recyclerView的adapter中的list
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

    //初始化RecyclerView
    private void initRecycler() {
        recyclerView = view.findViewById(R.id.precise_recycler);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(14);
        initPreciseValue(douArray);
        adapter = new PreciseValueAdapter(preciseValueList);
        recyclerView.setAdapter(adapter);

        accumulation_move_x = 0;
        i = 0;
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (getActivity().getCurrentFocus() != null) {
                        if (getActivity().getCurrentFocus().getWindowToken() != null) {
                            imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                }
                return getActivity().onTouchEvent(event);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                accumulation_move_x += dx;
                move_x_int = (accumulation_move_x * 114)/screen_width ;
                setViewMargin(move_x_int, dy);

                if (anyChanged) {
                    //有数据改变时才刷新
                    adapter.notifyDataSetChanged();
                    anyChanged = false;
                }

                //解决缓存原因造成的页面混乱问题
                View view = recyclerView.getChildAt(0);
                if (layoutManager.findFirstVisibleItemPosition() < 12) {
                    if (view.findViewById(R.id.children_edu_checkBox_p).getVisibility() == View.INVISIBLE) {
                        adapter.notifyItemChanged(layoutManager.findFirstVisibleItemPosition());
                    }
                }

                /*TranslateAnimation animation = new TranslateAnimation(0, move_x,0,0);
                animation.setFillAfter(true);
                animation.setDuration(10);
                back_img.startAnimation(animation);*/
                //ObjectAnimator.ofFloat(back_img, "translationX", 0, move_x).setDuration(100).start();

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        Log.d("Tax", "precise initrecycler complete");
    }

    //初始化控件
    private void initWidget() {

        taxCountPresenter = new TaxCountPresenter(this);
        screen_width = getScreenSize(getActivity(), "width");
        mTop = dpToPx(getActivity(), 7);
        mLeft = dpToPx(getActivity(), 63);
        mTop2 = dpToPx(getActivity(), 7);
        mLeft2 = dpToPx(getActivity(), 253);
        mTop3 = dpToPx(getActivity(), 42);
        mLeft3 = dpToPx(getActivity(), 16);
        offset_bt6 = dpToPx(getActivity(), 190);
        offset_bt7 = dpToPx(getActivity(), 228);
        offset_x = dpToPx(getActivity(), 237);
        offset_y = dpToPx(getActivity(), 35);
        offset = dpToPx(getActivity(), 38);
        

        button_month_1 = view.findViewById(R.id.month_1);
        button_month_2 = view.findViewById(R.id.month_2);
        button_month_3 = view.findViewById(R.id.month_3);
        button_month_4 = view.findViewById(R.id.month_4);
        button_month_5 = view.findViewById(R.id.month_5);
        button_month_6 = view.findViewById(R.id.month_6);
        button_month_7 = view.findViewById(R.id.month_7);
        button_month_8 = view.findViewById(R.id.month_8);
        button_month_9 = view.findViewById(R.id.month_9);
        button_month_10 = view.findViewById(R.id.month_10);
        button_month_11 = view.findViewById(R.id.month_11);
        button_month_12 = view.findViewById(R.id.month_12);
        button_month_total = view.findViewById(R.id.month_total);
        button_precise_calculate = view.findViewById(R.id.precise_calculate);
        button_precise_clean = view.findViewById(R.id.precise_clean);

        stay_img = view.findViewById(R.id.stay_img);
        preciseCalculate = view.findViewById(R.id.preciseCalculation);

        imm= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        handler = new MyHandler(getActivity());

        Log.d("Tax", "precise initwidget complete");
    }

    private void initListener() {
        Log.d("Tax", "precise initListener start");
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

        Log.d("Tax", "precise initListener complete");
    }

    private void startThread() {
        new Thread() {
            @Override
            public void run() {
                Log.d("Tax", "precise thread start");
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
                Log.d("Tax", "precise thread complete");
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.precise_calculate:
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                taxCountPresenter.preciseCount(douArray);
                adapter.notifyDataSetChanged();
                break;
            case R.id.precise_clean:
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                cleanPreciseCount();
                break;
            default:
                Button m = view.findViewById(v.getId());
                int flag;
                if (m.getText().toString().equals("总")) {
                    flag = 13;
                } else {
                    flag = strToInt(m.getText().toString());
                }
                final TopSmoothScroller topSmoothScroller = new TopSmoothScroller(getActivity());
                topSmoothScroller.setTargetPosition(flag-1);
                layoutManager.startSmoothScroll(topSmoothScroller);

                break;
        }
    }

    @Override
    public void preciseCountSetValue(double[][] dou) {
        Log.d("Tax", "start set value");
        douArray = dou;
        adapter.notifyDataSetChanged();
        if (douArray[12][23] < douArray[12][24]) {
            Toast.makeText(getActivity(), "您有应退个税，目前政策为年度内暂不退税，如截至12月仍有应退个税，可在个人所得税汇算清缴时办理退税事宜，详情可拨打12366咨询税务人员", Toast.LENGTH_SHORT).show();
        } else if (douArray[12][23] > douArray[12][24]) {
            Toast.makeText(getActivity(), "您有应补个税，可在个人所得税汇算清缴时办理补缴事宜，详情可拨打12366咨询税务人员，或添加微信号***进行咨询", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void quickCountSetValue(double[] doubles) {
    }

    //清零
    private void cleanPreciseCount() {
        for (int page=0; page<13; page++) {
            Arrays.fill(douArray[page], 0);
        }
        adapter.notifyDataSetChanged();
    }

    //stay_img控件移动
    private void setViewMargin(int dx, int dy) {
        int top, left;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) stay_img.getLayoutParams();

        if (dx <= offset_bt6){
            top = mTop;
            left = mLeft + dx;
        } else if (dx<=offset_bt7&&dx>offset_bt6) {
            top = mTop2 + (dx-offset_bt6)*offset_y/offset;
            left = mLeft2 + (dx-offset_bt6)*(-offset_x)/offset;
        } else if (dx>offset_bt7) {
            top = mTop3;
            left = mLeft3 + (dx - offset_bt7);
        } else {
            top = mTop;
            left = mLeft;
        }


        layoutParams.topMargin = top;
        layoutParams.leftMargin = left;
        stay_img.setLayoutParams(layoutParams);
    }

    private class MyHandler extends Handler {
        private WeakReference<FragmentActivity> weakReference;

        private MyHandler(FragmentActivity fragmentActivity) {
            weakReference = new WeakReference<>(fragmentActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FragmentActivity activity = weakReference.get();
            switch(msg.what) {
                case 0:
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                default:
                    break;
            }
        }
    }
}
