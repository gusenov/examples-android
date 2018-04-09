package com.example.implicit_intent_with_action;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Intent intent = getIntent();
        String messageText = intent.getStringExtra(Intent.EXTRA_TEXT);
        TextView messageView = (TextView)findViewById(R.id.message);
        messageView.setText(messageText);
    }

}
