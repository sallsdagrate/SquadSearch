package com.drp52;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.drp52.data.database.FirebaseAdaptor;
import com.drp52.data.database.Fixture;
import com.drp52.data.database.FixtureDate;
import com.drp52.data.database.Player;
import com.drp52.data.database.Team;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FixturesWithTeamSelected extends AppCompatActivity {

   FirebaseAdaptor db = new FirebaseAdaptor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixtures_with_team_selected);

        String tid = getIntent().getStringExtra("tid");

        Context contextRef = this;
        RecyclerView fixturesView = findViewById(R.id.teamFixturesRecyclerView);

        Function<List<Object>, Void> func = (List<Object> objects) -> {
            List<Fixture> fixtures = new ArrayList<>();
            for (Object o : objects) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) o;
                Fixture.FixtureBuilder builder = new Fixture.FixtureBuilder();
                String teamId = (String) document.get("team");
                builder.opponent((String) document.get("opponent"))
                        .date(new FixtureDate((String) document.get("date")))
                        .pitch((String) document.get("pitch"))
                        .location((String) document.get("location"))
                        .formation((String) document.get("formation"))
                        .team(new Team("unknown", teamId))
                        .fixtureId(document.getId());
                Fixture f = builder.build();
                f.setPlayers((List<Player>) document.get("confirmed_players"));
                Log.d("team ids", tid + " " + teamId);
                if ((Boolean) document.get("team_picked") && teamId.equals(tid)) {
                    fixtures.add(f);
                }
            }

            class getFunc implements Function<Integer, Function<Object, Void>> {
                @Override
                public Function<Object, Void> apply(Integer ind) {
                    Function<Object, Void> f, g;
                    if (ind == 1) {
                        f = (Object o) -> {
                            fixtures.get(0).getTeam().setName((String) o);
                            FixturesTeamSelectedRecyclerViewAdaptor adaptor = new FixturesTeamSelectedRecyclerViewAdaptor(contextRef, fixtures, tid);
                            fixturesView.setAdapter(adaptor);
                            fixturesView.setLayoutManager(new LinearLayoutManager(contextRef));
                            return null;
                        };
                    } else {
                        g = this.apply(ind - 1);
                        f = (Object o) -> {
                            fixtures.get(ind - 1).getTeam().setName((String) o);
                            String tid = fixtures.get(ind - 2).getTeam().getCapUid();
                            db.chainDocVal("teams", tid, "name", g);
                            return null;
                        };
                    }
                    return f;
                }
            }

            if (fixtures.size() > 0) {
                Function<Object, Void> getNames = new getFunc().apply(fixtures.size());
                String tailName = fixtures.get(fixtures.size() - 1).getTeam().getCapUid();
                db.chainDocVal("teams", tailName, "name", getNames);
            }
            return null;
        };

        db.chainColVal("fixtures", func);

        @SuppressLint("WrongViewCast")
        MaterialButton back = findViewById(R.id.teamMadeBackBtn);
        back.setOnClickListener(v -> finish());
    }
}