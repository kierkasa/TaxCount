package com.kierkasa.taxcount;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import static com.kierkasa.taxcount.MyFuntion.dpToPx;
import static com.kierkasa.taxcount.MyFuntion.getScreenSize;

public class HintFragment extends Fragment {
    private View view;
    private TextView instruction, tips, reference, instruction_content1, instruction_content2, tips_content1, tips_content2,
            tips_content3, tips_content4, tips_content5, reference_linking_content1, reference_linking_content2, reference_linking_content3, reference_linking_content4, help;

    public static HintFragment newInstance() {
        Log.d("Tax","hintfragment start");
        HintFragment fragment = new HintFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_hint, container, false);

        initWidget();
        initListener();

        Log.d("Tax","hintfragment view will return");
        return view;
    }
    
    private void initWidget() {
        Log.d("Tax","hintfragment initwidget start");
        instruction_content1 = view.findViewById(R.id.instruction_content1);
        instruction_content2 = view.findViewById(R.id.instruction_content2);
        tips_content1 = view.findViewById(R.id.tips_content1);
        tips_content2 = view.findViewById(R.id.tips_content2);
        tips_content3 = view.findViewById(R.id.tips_content3);
        tips_content4 = view.findViewById(R.id.tips_content4);
        tips_content5 = view.findViewById(R.id.tips_content5);
        reference_linking_content1 = view.findViewById(R.id.reference_linking_content1);
        reference_linking_content2 = view.findViewById(R.id.reference_linking_content2);
        reference_linking_content3 = view.findViewById(R.id.reference_linking_content3);
        reference_linking_content4 = view.findViewById(R.id.reference_linking_content4);
        help = view.findViewById(R.id.help);

        instruction = view.findViewById(R.id.instruction);
        tips = view.findViewById(R.id.tips);
        reference = view.findViewById(R.id.reference_linking);

        ScrollView scrollView = view.findViewById(R.id.hint_scroll);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) scrollView.getLayoutParams();
        layoutParams.height = getScreenSize(getActivity(), "height") - dpToPx(getActivity(), 150) - help.getBottom();
        scrollView.setLayoutParams(layoutParams);
        Log.d("Tax","hintfragment initwidget stop");
    }

    private void initListener() {
        Log.d("Tax","hintfragment initListener start");
        instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instruction_content1.getVisibility() == View.GONE) {
                    instruction_content1.setVisibility(View.VISIBLE);
                    instruction_content2.setVisibility(View.VISIBLE);
                } else {
                    instruction_content1.setVisibility(View.GONE);
                    instruction_content2.setVisibility(View.GONE);
                }
            }
        });
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tips_content1.getVisibility() == View.GONE) {
                    tips_content1.setVisibility(View.VISIBLE);
                    tips_content2.setVisibility(View.VISIBLE);
                    tips_content3.setVisibility(View.VISIBLE);
                    tips_content4.setVisibility(View.VISIBLE);
                    tips_content5.setVisibility(View.VISIBLE);
                } else {
                    tips_content1.setVisibility(View.GONE);
                    tips_content2.setVisibility(View.GONE);
                    tips_content3.setVisibility(View.GONE);
                    tips_content4.setVisibility(View.GONE);
                    tips_content5.setVisibility(View.GONE);
                }
            }
        });
        reference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reference_linking_content1.getVisibility() == View.GONE) {
                    reference_linking_content1.setVisibility(View.VISIBLE);
                    reference_linking_content2.setVisibility(View.VISIBLE);
                    reference_linking_content3.setVisibility(View.VISIBLE);
                    reference_linking_content4.setVisibility(View.VISIBLE);
                } else {
                    reference_linking_content1.setVisibility(View.GONE);
                    reference_linking_content2.setVisibility(View.GONE);
                    reference_linking_content3.setVisibility(View.GONE);
                    reference_linking_content4.setVisibility(View.GONE);
                }
            }
        });
        Log.d("Tax","hintfragment initListenr stop");
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
}
