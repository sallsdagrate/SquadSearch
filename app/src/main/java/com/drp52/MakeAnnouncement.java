package com.drp52;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.google.android.material.button.MaterialButton;

public class MakeAnnouncement extends AppCompatActivity {

    EditText text;
    MaterialButton backBtn, saveBtn, automatedAnnouncementBtn;
    DatabaseAdapter db = new FirebaseAdaptor();
    String tid;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_announcement);

        backBtn = findViewById(R.id.makeAnnBackBtn);
        backBtn.setOnClickListener(v -> finish());

        String possibleText;
        text = findViewById(R.id.makeAnnText);
        if (getIntent().hasExtra("message")) {
            text.setText(getIntent().getStringExtra("message"));
        }
        saveBtn = findViewById(R.id.makeAnnSaveBtn);
        saveBtn.setOnClickListener(v -> {
            if (getIntent().hasExtra("type")){
                tid = getIntent().getStringExtra("tid");
                String ann = text.getText().toString();
                db.createTeamAnnouncement(tid, ann);
                Toast.makeText(MakeAnnouncement.this, "Announced to your team", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(MakeAnnouncement.this, "Error. Reload app", Toast.LENGTH_LONG).show();
                finish();
            }

        });

        automatedAnnouncementBtn = findViewById(R.id.announceTeamSelectionBtn);
        automatedAnnouncementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FixturesWithTeamSelected.class);
                intent.putExtra("tid", getIntent().getStringExtra("tid"));
                startActivity(intent);
//                finish();
            }
        });

    }
}