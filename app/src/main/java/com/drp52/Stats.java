package com.drp52;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.drp52.data.database.Player;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.function.Function;

public class Stats extends AppCompatActivity {
    DatabaseAdapter db = new FirebaseAdaptor();
    MaterialButton BackButton;
    TextView NameText, TeamText, PositionsText, GoalsText;
    String uid, firstname, surname, teamname;
    @SuppressLint({"WrongViewCast", "MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Bundle extras = getIntent().getExtras();
        uid = (String) extras.get("uid");
        firstname = (String) extras.get("firstname");
        surname = (String) extras.get("surname");
        teamname = (String) extras.get("teamname");

        BackButton = findViewById(R.id.statsBackButton);
        BackButton.setOnClickListener(v -> finish());

        NameText = findViewById(R.id.textView32);
        NameText.setText(firstname + " " + surname);

        TeamText = findViewById(R.id.textView34);
        TeamText.setText(teamname);

        PositionsText = findViewById(R.id.textView37);
        Function<Object, Void> funcPos = (Object o) -> {
            List<String> positions = (List<String>) o;
            if (positions == null || positions.size() == 0){
                PositionsText.setText("no selected positions");
            }
            else if (positions.size() == 1){
                PositionsText.setText(positions.get(0));
            }
            else{
                StringBuilder sb = new StringBuilder();
                sb.append(positions.get(0));
                positions.remove(0);
                for (String s:positions) {
                    sb.append(", ");
                    sb.append(s);
                }
                PositionsText.setText(sb.toString());
            }
            return null;
        };
        db.chainDocVal("players", uid, "positions", funcPos);

        GoalsText = findViewById(R.id.textView38);
        Function<Object, Void> getGoals = (Object o) -> {
            Long goals = (o == null) ? 0 : (Long) o;
            GoalsText.setText(goals.toString());
            return null;
        };
        db.chainDocVal("players", uid, "goals", getGoals);
    }
}