package com.drp52;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class AnnouncementFullView extends AppCompatActivity {
    EditText text;
    MaterialButton backBtn;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_full_view);

        backBtn = findViewById(R.id.fullAnnBackBtn);
        backBtn.setOnClickListener(v -> finish());

        text = findViewById(R.id.fullAnnText);
//        text.setEnabled(false);
        text.setKeyListener(null);
        if(getIntent().hasExtra("ann")){
            text.setText(getIntent().getStringExtra("ann"));
        }

    }
}