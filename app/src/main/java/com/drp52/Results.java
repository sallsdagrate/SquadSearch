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
import com.drp52.data.database.GameResult;
import com.drp52.data.database.Player;
import com.drp52.data.database.Team;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Results extends AppCompatActivity implements RecyclerInterface {

    List<GameResult> results;
    DatabaseAdapter db = new FirebaseAdaptor();
    MaterialButton BackButton;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        BackButton = findViewById(R.id.backButtonResults);
        BackButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        });

        Context contextRef = this;
        RecyclerInterface recyclerRef = this;
        RecyclerView resultsView = findViewById(R.id.resultsRecyclerView);

        Function<List<Object>, Void> func = (List<Object> objects) -> {
            List<GameResult> results = new ArrayList<>();
            for (Object o : objects) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) o;
                GameResult result = new Fixture.FixtureBuilder()
                        .team(new Team("unknown", (String) document.get("team")))
                        .opponent((String) document.get("opponent"))
                        .date(new FixtureDate((String) document.get("date")))
                        .pitch((String) document.get("pitch"))
                        .location((String) document.get("location"))
                        .formation((String) document.get("formation"))
                        .fixtureId(document.getId())
                        .build()
                        .createResult(((Long) document.get("teamScore")).intValue(),
                                ((Long) document.get("opponentScore")).intValue());
                result.setPlayers((List<Player>) document.get("players"));
                results.add(result);
            }

            class getFunc implements Function<Integer, Function<Object, Void>> {
                @Override
                public Function<Object, Void> apply(Integer ind) {
                    Function<Object, Void> f, g;
                    if (ind == 1) {
                        f = (Object o) -> {
                            results.get(0).getTeam().setName((String) o);
                            ResultsRecyclerViewAdaptor adaptor = new ResultsRecyclerViewAdaptor(contextRef, results, recyclerRef);
                            resultsView.setAdapter(adaptor);
                            resultsView.setLayoutManager(new LinearLayoutManager(contextRef));
                            return null;
                        };
                    } else {
                        g = this.apply(ind - 1);
                        f = (Object o) -> {
                            results.get(ind - 1).getTeam().setName((String) o);
                            String tid = results.get(ind - 2).getTeam().getCapUid();
                            db.chainDocVal("teams", tid, "name", g);
                            return null;
                        };
                    }
                    return f;
                }
            }

            if (results.size() > 0) {
                Function<Object, Void> getNames = new getFunc().apply(results.size());
                String tailName = results.get(results.size() - 1).getTeam().getCapUid();
                db.chainDocVal("teams", tailName, "name", getNames);
            }
            return null;
        };
        db.chainColVal("result", func);
    }
}