package com.drp52;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.drp52.data.database.Player;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FixtureInfo extends AppCompatActivity {

    ParcelableFixture f = null;
    Boolean isCap = false;
    DatabaseAdapter db = new FirebaseAdaptor();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TextView fixtureNameText, formationText, dateTimeText, pitchText, locationText;
    MaterialButton teamBtn, backBtn, editFixture, makeResult;
    String tid = "";

    @SuppressLint({"SetTextI18n", "WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixture_info);

        fixtureNameText = findViewById(R.id.fixtureInfoTeamsPlaying);
        formationText = findViewById(R.id.fixtureInfoFormation);
        dateTimeText = findViewById(R.id.fixtureInfoDateTime);
        pitchText = findViewById(R.id.fixtureInfoPitch);
        locationText = findViewById(R.id.fixtureInfoLocation);

        backBtn = findViewById(R.id.fixtureInfoBackBtn);
        backBtn.setOnClickListener(v -> finish());



        if (getIntent().hasExtra("parcelable_f")){
            f = getIntent().getParcelableExtra("parcelable_f");
        }

        if (f != null){
            tid = f.teamCapUid;
            fixtureNameText.setText(f.teamName + " / " + f.opponentName);
            formationText.setText("formation:  " + f.formation);
            dateTimeText.setText(f.date);
            pitchText.setText("Pitch Type:  " + f.pitch);
            locationText.setText("Location:  " + f.location);
        }

        editFixture = findViewById(R.id.fixtureInfoEditBtn);
        makeResult = findViewById(R.id.fixtureInfoResultBtn);
        teamBtn = findViewById(R.id.selectTeamBtn);
        editFixture.setVisibility(View.INVISIBLE);
        makeResult.setVisibility(View.INVISIBLE);
        teamBtn.setVisibility(View.INVISIBLE);

        Function<List<Object>, Void> func = (List<Object> os) -> {
            if (os == null) return null;
            for (Object o : os) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) o;
                if (document.getId().equals(uid)){
                    if (document.get("teamUid") == null) return null;
                    if (((String) document.get("teamUid")).equals(tid)){
                        if ((Boolean) document.get("isCaptain")){
//                            editFixture.setVisibility(View.VISIBLE);
                            makeResult.setOnClickListener(v -> {
                                Intent intent = new Intent(getApplicationContext(), EnterResult.class);
                                intent.putExtra("parcelable_f", f);
                                v.getContext().startActivity(intent);
                            });
                            makeResult.setVisibility(View.VISIBLE);
                            teamBtn.setOnClickListener(v -> {
                                Intent intent = new Intent(getApplicationContext(), ChooseTeam.class);
                                Function<Object, Void> getPlayers = (Object players) -> {
                                    intent.putStringArrayListExtra("playersIds", (ArrayList<String>) players);
                                    intent.putExtra("fixture_id", f.fixtureId);
                                    v.getContext().startActivity(intent);
                                    return null;
                                };
                                db.chainDocVal("fixtures", f.fixtureId, "players", getPlayers);
                            });
                            teamBtn.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
            return null;
        };
        db.chainColVal("players", func);

    }
}