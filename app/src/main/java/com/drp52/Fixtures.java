package com.drp52;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drp52.data.database.DatabaseAdapter;
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

public class Fixtures extends AppCompatActivity implements RecyclerInterface {

//    List<Fixture> fs;
    DatabaseAdapter db = new FirebaseAdaptor();
    MaterialButton BackButton, FilterButton;
    ParcelableQuery q = null;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixtures);
        BackButton = findViewById(R.id.teamMadeBackBtn);
        BackButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        });

        FilterButton = findViewById(R.id.filterButton);
        FilterButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Filter.class));
            finish();
        });

        if (getIntent().hasExtra("parcelable_q")) {
            q = getIntent().getParcelableExtra("parcelable_q");
        }

        Context contextRef = this;
        RecyclerInterface recyclerRef = this;
        RecyclerView fixturesView = findViewById(R.id.requestsRecyclerView);

        Function<List<Object>, Void> func = (List<Object> objects) -> {
            List<Fixture> fixtures = new ArrayList<>();
            for (Object o : objects) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) o;
                String teamId = (String) document.get("team");
                Fixture f = new Fixture.FixtureBuilder()
                        .opponent((String) document.get("opponent"))
                        .date(new FixtureDate((String) document.get("date")))
                        .pitch((String) document.get("pitch"))
                        .location((String) document.get("location"))
                        .formation((String) document.get("formation"))
                        .team(new Team("unknown", teamId))
                        .fixtureId(document.getId())
                        .build();
                f.setPlayers((List<Player>) document.get("players"));
                fixtures.add(f);
            }

            class getFunc implements Function<Integer, Function<Object, Void>> {
                @Override
                public Function<Object, Void> apply(Integer ind) {
                    Function<Object, Void> f, g;
                    if (ind == 1) {
                        f = (Object o) -> {
                            if (o != null) fixtures.get(0).getTeam().setName((String) o);
                            FixturesRecyclerViewAdaptor adaptor = new FixturesRecyclerViewAdaptor(contextRef, fixtures, recyclerRef);
                            fixturesView.setAdapter(adaptor);
                            fixturesView.setLayoutManager(new LinearLayoutManager(contextRef));
                            return null;
                        };
                    } else {
                        g = this.apply(ind - 1);
                        f = (Object o) -> {
                            if (o != null) fixtures.get(ind - 1).getTeam().setName((String) o);
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
        if (q == null) {
            db.chainColVal("fixtures", func);
        } else {
            db.chainColVal("fixtures", q.queries, func);
        }
    }
}