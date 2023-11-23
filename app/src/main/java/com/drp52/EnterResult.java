package com.drp52;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.drp52.data.database.Fixture;
import com.drp52.data.database.GameResult;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class EnterResult extends AppCompatActivity {
    DatabaseAdapter db = new FirebaseAdaptor();
    ParcelableFixture f = null;

    TextView teamText, opponentText;
    MaterialButton backBtn, enterResultButton, enterScorer;
    Spinner playerMenu;
    Map<Integer, String> posToId;
    EditText inputTeamScore, inputOpponentScore, inputGoal;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_result);

        teamText = findViewById(R.id.enterResultTeam);
        opponentText = findViewById(R.id.enterResultOpponent);

        inputTeamScore = findViewById(R.id.enterResultInputTeamScore);
        inputOpponentScore = findViewById(R.id.enterResultInputOpponentScore);
        inputGoal = findViewById(R.id.goalNumber);

        backBtn = findViewById(R.id.enterResultBackBtn);
        backBtn.setOnClickListener(v -> finish());

        enterResultButton = findViewById(R.id.enterResultCreateBtn);
        enterScorer = findViewById(R.id.enterScorer);

        if (getIntent().hasExtra("parcelable_f")){
            f = getIntent().getParcelableExtra("parcelable_f");
        }

        if (f != null) {
            teamText.setText(f.teamName);
            opponentText.setText(f.opponentName);

            enterResultButton.setOnClickListener(v -> {
                if (inputTeamScore.getText().toString().equals("") ||
                inputOpponentScore.getText().toString().equals("")) {
                    Toast.makeText(EnterResult.this, "Please Enter Both Scores", Toast.LENGTH_SHORT).show();
                    return;
                }
                int teamScore = Integer.parseInt(inputTeamScore.getText().toString());
                int opponentScore = Integer.parseInt(inputOpponentScore.getText().toString());
                Fixture fixture = f.getFixture();
                GameResult result = fixture.createResult(teamScore, opponentScore);
                db.createResult(result);
                db.deleteFixture(fixture.getFixtureId());
                Toast.makeText(EnterResult.this, "Result Entered", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getBaseContext(), Fixtures.class));
            });

            enterScorer.setOnClickListener(v -> {
                int selected = playerMenu.getSelectedItemPosition();
                if (selected != 0) {
                    int goal = Integer.parseInt(inputGoal.getText().toString());
                    String id = posToId.get(selected);
                    Log.d("selected", String.valueOf(selected));
                    Log.d("id", id);
                    addGoals(id, goal);
                }
            });

            playerMenu = findViewById(R.id.goalScorer);
            posToId = new HashMap<>();
            Function<Object, Void> playerFunc = (Object objects) -> {
                List<String> players = (List<String>) objects;
                int cnt = 1;
                for (String s:players) {
                    posToId.put(cnt, s);
                    cnt ++;
                }
                players.add(0, "Select player");
                Context context = this;
                class getFunc implements Function<Integer, Function<Object, Void>> {
                    @Override
                    public Function<Object, Void> apply(Integer ind) {
                        Function<Object, Void> f, g;
                        if (ind == 2) {
                            f = (Object o) -> {
                                if (o != null) players.set(1, (String) o);
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                                        com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                                        players);
                                playerMenu.setAdapter(adapter);
                                playerMenu.setSelection(0);
                                return null;
                            };
                        } else {
                            g = this.apply(ind - 1);
                            f = (Object o) -> {
                                if (o != null) players.set(ind - 1, (String) o);
                                String pid = players.get(ind - 2);
                                db.chainDocVal("players", pid, "firstname", g);
                                return null;
                            };
                        }
                        return f;
                    }
                }
                if (players.size() > 1) {
                    Function<Object, Void> getNames = new getFunc().apply(players.size());
                    String tailId = players.get(players.size() - 1);
                    db.chainDocVal("players", tailId, "firstname", getNames);
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                            players);
                    playerMenu.setAdapter(adapter);
                    playerMenu.setSelection(0);
                }
                return null;
            };
            db.chainDocVal("fixtures", f.fixtureId, "players", playerFunc);
        }
    }

    private void addGoals(String uid, int goals) {
        Function<Object, Void> func = (Object o) -> {
            Long cur = (o == null) ? 0 : (Long) o;
            db.updateField("players", uid, "goals", cur + goals);
            Toast.makeText(getApplicationContext(), "Goals set", Toast.LENGTH_SHORT).show();
            return null;
        };
        db.chainDocVal("players", uid, "goals", func);
    }
}
