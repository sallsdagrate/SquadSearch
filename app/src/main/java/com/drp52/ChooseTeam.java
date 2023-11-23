package com.drp52;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.drp52.data.database.Player;
import com.drp52.data.database.Position;
import com.drp52.data.database.Team;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ChooseTeam extends AppCompatActivity {

    MaterialButton backBtn, saveBtn, filterBtn;
    ParcelableQuery q = null;
    List<String> filterPositions;
    String fixtureId, teamId;

    ArrayList<String> playersIds;
    Set<Player> chosen;
    DatabaseAdapter db = new FirebaseAdaptor();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_team);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playersIds = extras.getStringArrayList("playersIds");
            filterPositions = extras.getStringArrayList("positions");
            fixtureId = (String) extras.get("fixture_id");
            teamId = (String) extras.get("team_id");
        }

        if (getIntent().hasExtra("parcelable_q")) {
            q = getIntent().getParcelableExtra("parcelable_q");
        }

        backBtn = findViewById(R.id.chooseTeamBackBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), Fixtures.class));
                finish();
            }
        });

        filterBtn = findViewById(R.id.filterChooseTeamPlayersBtn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FilterPlayers.class);
                intent.putStringArrayListExtra("playerIds", playersIds);
                intent.putExtra("fixture_id", fixtureId);
                startActivity(intent);
                finish();
            }
        });

        saveBtn = findViewById(R.id.chooseTeamSaveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fixtureId == "team_selection") {

                    finish();
                } else {
//                    startActivity(new Intent(getApplicationContext(), Fixtures.class));
                    db.updateField("fixtures", fixtureId, "team_picked", true);
                    finish();
                }
            }
        });



        Context contextRef = this;
        RecyclerView playerView = findViewById(R.id.chooseTeamRecyclerView);

        Function<List<Object>, Void> func = (List<Object> objects) -> {
            List<Player> availablePlayers = new ArrayList<>();
            for (Object o : objects) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) o;
                Player.PlayerBuilder builder = new Player.PlayerBuilder();
                String playerId = document.getId();
                String teamId = (String) document.get("teamUid");
                builder.rightFoot((Boolean) document.get("rightFoot"))
                        .leftFoot((Boolean) document.get("leftFoot"))
                        .firstName((String) document.get("firstname"))
                        .surname((String) document.get("surname"))
                        .position((List<Position>) document.get("positions"))
                        .year((String) document.get("year_group"))
                        .uid(playerId);
                Player p = builder.build();
                p.setTeam(new Team("unknown", teamId));
                availablePlayers.add(p);
            }

            class getFunc implements Function<Integer, Function<Object, Void>> {
                @Override
                public Function<Object, Void> apply(Integer ind) {
                    Function<Object, Void> f, g;
                    if (ind == 1) {
                        f = (Object o) -> {
                            availablePlayers.get(0).getTeam().setName((String) o);
                            ChooseTeamRecyclerViewAdaptor adaptor = new ChooseTeamRecyclerViewAdaptor(contextRef, availablePlayers, fixtureId, teamId);
                            playerView.setAdapter(adaptor);
                            playerView.setLayoutManager(new LinearLayoutManager(contextRef));
                            return null;
                        };
                    } else {
                        g = this.apply(ind - 1);
                        f = (Object o) -> {
                            availablePlayers.get(ind - 1).getTeam().setName((String) o);
                            String tid = availablePlayers.get(ind - 2).getTeam().getCapUid();
                            db.chainDocVal("teams", tid, "name", g);
                            return null;
                        };
                    }
                    return f;
                }
            }

            if (availablePlayers.size() > 0) {
                Function<Object, Void> getNames = new getFunc().apply(availablePlayers.size());
                String tailName = availablePlayers.get(availablePlayers.size() - 1).getTeam().getCapUid();
                db.chainDocVal("teams", tailName, "name", getNames);
            }
            return null;
        };
        Map<String, String> equalQueries = (q == null) ? Collections.emptyMap() : q.queries;
        Map<String, List<String>> inQueries = new HashMap<>(), containQueries = new HashMap<>();
        if (playersIds != null) inQueries.put("id", playersIds);
        if (filterPositions != null && !filterPositions.isEmpty())
            containQueries.put("positions", filterPositions);
        db.chainColVal("players", equalQueries, inQueries, containQueries, func);
    }
}