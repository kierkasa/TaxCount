package com.kierkasa.taxcount;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import static com.kierkasa.taxcount.MyFuntion.dpToPx;
import static com.kierkasa.taxcount.MyFuntion.getScreenSize;

public class HintActivity extends AppCompatActivity {

    private Button icon_count, icon_accurate, icon_hint;
    private TextView instruction, tips, reference, instruction_content1, instruction_content2, tips_content1, tips_content2, 
            tips_content3, tips_content4, tips_content5, reference_linking_content1, reference_linking_content2, reference_linking_content3, reference_linking_content4, help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);

        icon_hint = findViewById(R.id.hint);
        icon_hint.setBackgroundResource(R.drawable.set_hint_stay);
        
        instruction_content1 = findViewById(R.id.instruction_content1);
        instruction_content2 = findViewById(R.id.instruction_content2);
        tips_content1 = findViewById(R.id.tips_content1);
        tips_content2 = findViewById(R.id.tips_content2);
        tips_content3 = findViewById(R.id.tips_content3);
        tips_content4 = findViewById(R.id.tips_content4);
        tips_content5 = findViewById(R.id.tips_content5);
        reference_linking_content1 = findViewById(R.id.reference_linking_content1);
        reference_linking_content2 = findViewById(R.id.reference_linking_content2);
        reference_linking_content3 = findViewById(R.id.reference_linking_content3);
        reference_linking_content4 = findViewById(R.id.reference_linking_content4);
        help = findViewById(R.id.help);

        icon_count = findViewById(R.id.count);
        icon_accurate = findViewById(R.id.accurate);

        
        instruction = findViewById(R.id.instruction);
        tips = findViewById(R.id.tips);
        reference = findViewById(R.id.reference_linking);
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

        new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        icon_count.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HintActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                        icon_accurate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent1 = new Intent(HintActivity.this, PreciseActivity.class);
                                startActivity(intent1);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        ScrollView scrollView = findViewById(R.id.hint_scroll);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) scrollView.getLayoutParams();
        layoutParams.height = getScreenSize(this, "height") - dpToPx(this, 150) - help.getBottom();
        scrollView.setLayoutParams(layoutParams);
    }
}
