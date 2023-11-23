package com.drp52;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.function.Function;

public class ManageTeam extends AppCompatActivity {

    String currentTid = "";

    MaterialButton backBtn, createFixture, makeAnnouncement, requests;
    TextView teamName;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_team);

        backBtn = findViewById(R.id.manageTeamBackBtn);
        backBtn.setOnClickListener(v -> finish());

        teamName = findViewById(R.id.manageTeamTeamNameText);
        DatabaseAdapter db = new FirebaseAdaptor();
        Function<Object, Void> func = (Object o) -> {
            if (o == null) {
                Toast.makeText(ManageTeam.this, "Error: Team was not registered properly", Toast.LENGTH_LONG).show();
                finish();
            } else {
                String tid = (String) o;
                currentTid = tid;
                Function<Object, Void> queryName = (Object nameObj) -> {
                    String name = (String) nameObj;
                    teamName.setText((CharSequence) name);
                    return null;
                };
                db.chainDocVal("teams", tid, "name", queryName);
            }
            return null;
        };

        db.chainDocVal("players",
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                "teamUid",
                func);

        createFixture = findViewById(R.id.manageTeamMakeFixtureBtn);
        createFixture.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), CreateFixture.class)
                    .putExtra("currentTid", currentTid));
        });

        makeAnnouncement = findViewById(R.id.manageTeamMakeTeamAnnouncementBtn);
        makeAnnouncement.setOnClickListener(v -> {
//                TODO make announcements to team
                Intent intent = new Intent(getApplicationContext(), MakeAnnouncement.class);
                intent.putExtra("type", "team");
                intent.putExtra("tid", currentTid);
                startActivity(intent);
            });

        requests = findViewById(R.id.manageTeamRequests);
        requests.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PlayerRequests.class);
            intent.putExtra("tid", currentTid);
            startActivity(intent);
        });
    }
}