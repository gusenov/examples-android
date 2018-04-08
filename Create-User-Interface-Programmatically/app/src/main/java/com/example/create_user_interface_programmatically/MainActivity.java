package com.example.create_user_interface_programmatically;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linLayout = new LinearLayout(this);
        linLayout.setOrientation(LinearLayout.VERTICAL);

        LayoutParams linLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        setContentView(linLayout, linLayoutParams);




        TextView tv = new TextView(this);
        tv.setText("TextView");
        linLayout.addView(tv);

        Button btn = new Button(this);
        btn.setText("Button");
        linLayout.addView(btn);

        LayoutParams lpView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lpView);
        btn.setLayoutParams(lpView);




        LinearLayout linLayoutForBtn = new LinearLayout(this);
        linLayoutForBtn.setOrientation(LinearLayout.HORIZONTAL);
        lpView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpView.leftMargin = 100;
        linLayoutForBtn.setLayoutParams(lpView);
        linLayout.addView(linLayoutForBtn);


        lpView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpView.leftMargin = 50;

        btn = new Button(this);
        btn.setLayoutParams(lpView);
        btn.setText("Button1");
        linLayoutForBtn.addView(btn);

        btn = new Button(this);
        btn.setLayoutParams(lpView);
        btn.setText("Button1");
        linLayoutForBtn.addView(btn);

        btn = new Button(this);
        btn.setLayoutParams(lpView);
        btn.setText("Button1");
        linLayoutForBtn.addView(btn);




        tv = new TextView(this);
        lpView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpView.gravity = Gravity.CENTER_HORIZONTAL;
        tv.setLayoutParams(lpView);
        tv.setText("TextView");
        linLayout.addView(tv);




        tv = new TextView(this);
        lpView = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpView.gravity = Gravity.RIGHT;
        lpView.rightMargin = 50;
        tv.setLayoutParams(lpView);
        tv.setText("TextView");
        linLayout.addView(tv);
    }
}
