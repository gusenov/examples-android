package com.example.layout_inflater_chat_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.content.res.ResourcesCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private final SimpleDateFormat df = new SimpleDateFormat("HH:mm");
    private Integer msgCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout msgLinLayout = (LinearLayout)findViewById(R.id.msgLinLayout);
        final LayoutInflater ltInflater = getLayoutInflater();

        findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = ltInflater.inflate(R.layout.msg, null, false);

                msgCnt++;
                ((TextView)view.findViewById(R.id.msgText)).setText(msgCnt.toString() + ". " +
                        ((EditText)findViewById(R.id.msgEditText)).getText().toString()
                );

                if (msgCnt % 2 != 0) {
                    view.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.rounded_border_odd, null));
                } else {
                    view.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.rounded_border_even, null));
                }

                Calendar c = Calendar.getInstance();
                String formattedDate = df.format(c.getTime());
                ((TextView)view.findViewById(R.id.msgTime)).setText(formattedDate);

                msgLinLayout.addView(view);

                ((EditText)findViewById(R.id.msgEditText)).setText("");
            }
        });

        findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText)findViewById(R.id.msgEditText)).setText("");
            }
        });
    }

}
