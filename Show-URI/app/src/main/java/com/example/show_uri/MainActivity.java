package com.example.show_uri;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private Uri data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonCall).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextPhone = (EditText)findViewById(R.id.editTextPhone);
                data = Uri.parse(String.format("tel:%s", editTextPhone.getText().toString()));
                intent = new Intent(Intent.ACTION_VIEW, data);
                startActivity(intent);
            }
        });

        findViewById(R.id.buttonMap).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextLatitude = (EditText)findViewById(R.id.editTextLatitude);
                EditText editTextLoingitude = (EditText)findViewById(R.id.editTextLoingitude);
                String uri = String.format("geo:%s,%s",
                        editTextLatitude.getText().toString(),
                        editTextLoingitude.getText().toString());
                Log.d("ME", uri);
                data = Uri.parse(uri);
                intent = new Intent(Intent.ACTION_VIEW, data);
                startActivity(intent);
            }
        });

        findViewById(R.id.buttonWeb).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextURL = (EditText)findViewById(R.id.editTextURL);
                data = Uri.parse(String.format("http://%s", editTextURL.getText().toString()));
                intent = new Intent(Intent.ACTION_VIEW, data);
                startActivity(intent);
            }
        });

    }
}
