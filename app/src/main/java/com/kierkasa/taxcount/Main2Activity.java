package com.kierkasa.taxcount;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import static com.kierkasa.taxcount.PreciseValueAdapter.isInput;

public class Main2Activity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private MyViewPager viewPager;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                item = menuItem;
                switch (item.getItemId()) {
                    case R.id.count:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.accurate:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.hint:
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });
        Log.d("Tax", "start viewpager");

        viewPager = new MyViewPager(this);
        viewPager.setScroll(false);
        viewPager = findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (item != null) {
                    item.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                item = bottomNavigationView.getMenu().getItem(i);
                item.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        List<Fragment> list = new ArrayList<>();
        list.add(QuickFragment.newInstance());
        list.add(PreciseFragment.newInstance());
        list.add(HintFragment.newInstance());
        viewPagerAdapter.setList(list);
    }

    //点击空白处关闭软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*if (this.getCurrentFocus() != null) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);*/
        InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (Main2Activity.this.getCurrentFocus() != null) {
                if (Main2Activity.this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(Main2Activity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
