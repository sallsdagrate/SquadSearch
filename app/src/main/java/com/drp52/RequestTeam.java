package com.drp52;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class RequestTeam extends AppCompatActivity {
    DatabaseAdapter db = new FirebaseAdaptor();
    MaterialButton BackButton, ApplyButton;
    Spinner TeamMenu;
    Map<Integer, String> posToId;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_team);

        TeamMenu = findViewById(R.id.teamMenu2);
        BackButton = findViewById(R.id.backButton);
        ApplyButton = findViewById(R.id.applyButton);

        posToId = new HashMap<>();

        Function<List<Object>, Void> teamFunc = (List<Object> objects) -> {
            List<String> teams = new ArrayList<>();
            teams.add("No Team Filter");
            for (Object o: objects) {
                DocumentSnapshot doc = (DocumentSnapshot) o;
                posToId.put(teams.size(), doc.getId());
                teams.add((String) doc.get("name"));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                    teams);
            TeamMenu.setAdapter(adapter);
            TeamMenu.setSelection(0);
            return null;
        };
        db.chainColVal("teams", teamFunc);

        ApplyButton.setOnClickListener(v -> {
            int selected = TeamMenu.getSelectedItemPosition();
            if (selected != 0) {
                String teamId = posToId.get(selected);
                String user = FirebaseAuth.getInstance().getUid();
                db.updateList("teams", teamId, "playerRequests", user, true);
//                db.updateField("players", user, "teamUid", teamId);
                Toast.makeText(getApplicationContext(),"Request sent", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(),"No team selected", Toast.LENGTH_SHORT).show();
            }
        });

        BackButton.setOnClickListener(v -> {
            finish();
        });
    }
}