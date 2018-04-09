package com.example.layout_inflater_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LayoutInflater ltInflater;
    private LinearLayout linLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linLayout = (LinearLayout)findViewById(R.id.linLayout);

        ltInflater = getLayoutInflater();
        View view = ltInflater.inflate(R.layout.text, null, false);
        linLayout.addView(view);

        ltInflater.inflate(R.layout.btn, linLayout, true);

        addRow("Марья", "Бухгалтер", 10000, 0xFF2F4F4F);
        addRow("Петр", "Программер", 13000, 0xFF800080);
        addRow("Антон", "Программер", 13000, 0xFF2F4F4F);
        addRow("Даша", "Бухгалтер", 10000, 0xFF800080);
    }

    private void addRow(String firstName, String occupation, Integer salary, Integer bgcolor) {
        View row = ltInflater.inflate(R.layout.group, null, false);

        row.setBackgroundColor(bgcolor);
        ((TextView)row.findViewById(R.id.firstName)).setText(firstName);
        ((TextView)row.findViewById(R.id.occupation)).setText(occupation);
        ((TextView)row.findViewById(R.id.salary)).setText(String.format(Locale.getDefault(), "%d", salary));

        linLayout.addView(row);
    }
}
