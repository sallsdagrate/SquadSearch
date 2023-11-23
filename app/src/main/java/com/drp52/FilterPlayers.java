package com.drp52;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.drp52.data.database.Player;
import com.drp52.data.database.Position;
import com.drp52.data.database.Team;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FilterPlayers extends AppCompatActivity {

    List<Integer> ids = new ArrayList<>(List.of(
            R.id.switchGK,
            R.id.switchLB,
            R.id.switchCB,
            R.id.switchRB,
            R.id.switchLM,
            R.id.switchCM,
            R.id.switchRM,
            R.id.switchLW,
            R.id.switchST,
            R.id.switchRW));
    DatabaseAdapter db = new FirebaseAdaptor();
    MaterialButton ApplyButton;
    Set<Position> playerPositions;

    Spinner TeamMenu;
    Map<Integer, String> posToId;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_players);
        posToId = new HashMap<>();
        playerPositions = new HashSet<>();

        int idInd = 0;
        for (Position p : Position.values()) {
            Integer id = ids.get(idInd); //get id of ui switch element
            @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s = findViewById(id); //get appropriate switch
            s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //add listener to switch
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        playerPositions.add(p);
                    }
                    else {
                        playerPositions.remove(p);
                    }
                }
            });
            idInd ++;

        TeamMenu = findViewById(R.id.filterPlayersTeamMenu);
        Function<List<Object>, Void> teamFunc = (List<Object> objects) -> {
            List<String> teams = new ArrayList<>();
            teams.add("No Team Filter");
            for (Object o: objects) {
                DocumentSnapshot doc = (DocumentSnapshot) o;
                posToId.put(teams.size(), doc.getId());
                teams.add((String) doc.get("name"));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                    teams);
            TeamMenu.setAdapter(adapter);
            TeamMenu.setSelection(0);
            return null;
        };
        db.chainColVal("teams", teamFunc);

        ApplyButton = findViewById(R.id.applyFilterPlayersBtn);
        ApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseTeam.class);
                Map<String, String> queries = new HashMap<>();
                int selected = TeamMenu.getSelectedItemPosition();
                if (selected != 0) {
                    String teamId = posToId.get(selected);
                    queries.put("team", teamId);
                }
                ArrayList<String> positions = playerPositions.stream().map(Enum::toString).collect(Collectors.toCollection(ArrayList::new));
                ParcelableQuery parcelableQuery = new ParcelableQuery(queries);
                intent.putExtra("parcelable_q", parcelableQuery);
                intent.putStringArrayListExtra("playersIds", getIntent().getStringArrayListExtra("playerIds"));
                intent.putStringArrayListExtra("positions", positions);
                intent.putExtra("fixture_id", (String) getIntent().getExtras().get("fixture_id"));
                v.getContext().startActivity(intent);
            }
        });}
    }
}