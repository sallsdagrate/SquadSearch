package com.drp52;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Filter extends AppCompatActivity {
    DatabaseAdapter db = new FirebaseAdaptor();
    MaterialButton ApplyButton;
    Spinner TeamMenu, OpponentMenu, PitchMenu, FormationMenu;
    Map<Integer, String> posToId;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        posToId = new HashMap<>();

        TeamMenu = findViewById(R.id.teamMenu);
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

        OpponentMenu = findViewById(R.id.opponentMenu);
        Function<List<Object>, Void> opponentFunc = (List<Object> objects) -> {
            Set<String> opponents = new HashSet<>();
            for (Object o: objects) {
                DocumentSnapshot doc = (DocumentSnapshot) o;
                opponents.add((String) doc.get("opponent"));
            }
            List<String> opponentList = new ArrayList<>(opponents);
            opponentList.add(0, "No opponent selected");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                    opponentList);
            OpponentMenu.setAdapter(adapter);
            OpponentMenu.setSelection(0);
            return null;
        };
        db.chainColVal("fixtures", opponentFunc);

        PitchMenu = findViewById(R.id.pitchMenu);
        Function<List<Object>, Void> pitchFunc = (List<Object> objects) -> {
            Set<String> pitches = new HashSet<>();
            for (Object o: objects) {
                DocumentSnapshot doc = (DocumentSnapshot) o;
                pitches.add((String) doc.get("pitch"));
            }
            List<String> pitchList = new ArrayList<>(pitches);
            pitchList.add(0, "No pitch selected");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                    pitchList);
            PitchMenu.setAdapter(adapter);
            PitchMenu.setSelection(0);
            return null;
        };
        db.chainColVal("fixtures", pitchFunc);

        FormationMenu = findViewById(R.id.formationMenu);
        Function<List<Object>, Void> formationFunc = (List<Object> objects) -> {
            Set<String> formations = new HashSet<>();
            for (Object o: objects) {
                DocumentSnapshot doc = (DocumentSnapshot) o;
                formations.add((String) doc.get("formation"));
            }
            List<String> formationList = new ArrayList<>(formations);
            formationList.add(0, "No formation selected");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                    formationList);
            FormationMenu.setAdapter(adapter);
            FormationMenu.setSelection(0);
            return null;
        };
        db.chainColVal("fixtures", formationFunc);

        ApplyButton = findViewById(R.id.applyFilterButton);
        ApplyButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Fixtures.class);
            Map<String, String> queries = new HashMap<>();

            int selected = TeamMenu.getSelectedItemPosition();
            if (selected != 0) {
                String teamId = posToId.get(selected);
                queries.put("team", teamId);
            }

            Map<String, Spinner> filters = Map.of(
                    "opponent", OpponentMenu,
                    "pitch", PitchMenu,
                    "formation", FormationMenu);

            for (Map.Entry<String, Spinner> entry : filters.entrySet()) {
                Spinner menu = entry.getValue();
                selected = menu.getSelectedItemPosition();
                if (selected != 0) {
                    String item = (String) menu.getSelectedItem();
                    queries.put(entry.getKey(), item);
                }
            }

            ParcelableQuery parcelableQuery = new ParcelableQuery(queries);
            intent.putExtra("parcelable_q", parcelableQuery);
            v.getContext().startActivity(intent);
        });
    }
}