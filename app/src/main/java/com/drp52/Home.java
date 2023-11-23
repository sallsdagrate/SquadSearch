package com.drp52;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Home extends AppCompatActivity {

    MaterialButton LogOutButton, FixturesButton, ResultsButton, AnnouncementsBtn, ManageTeamButton, ViewProfileBtn, RequestButton;

    String teamUid = "";

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        DatabaseAdapter db = new FirebaseAdaptor();
        LogOutButton = findViewById(R.id.logOutButton);
        LogOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });
        FixturesButton = findViewById(R.id.fixturesButton);
        FixturesButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Fixtures.class)));
        TextView userEmail = findViewById(R.id.textView4);
        Function<Object, Void> getName = (Object o) -> {
            userEmail.setText((String) o);
            return null;
        };
        db.chainDocVal("players", FirebaseAuth.getInstance().getUid(), "firstname", getName);

//        EditPosButton = findViewById(R.id.editPositionsButton);
//        EditPosButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), SelectPosition.class));
//                finish();
//            }
//        });

        ResultsButton = findViewById(R.id.resultsButton);
        ResultsButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Results.class)));

        ManageTeamButton = findViewById(R.id.manageTeamBtn);
        RequestButton = findViewById(R.id.requestButton);
        Function<Object, Void> checkCaptain = (Object o) -> {
            if ((Boolean) o) {
                RequestButton.setVisibility(View.INVISIBLE);
                ManageTeamButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ManageTeam.class)));
                ManageTeamButton.setVisibility(View.VISIBLE);
                RequestButton.setText("Add Players");
                Intent intent = new Intent(getApplicationContext(), ChooseTeam.class);
                RequestButton.setOnClickListener(v -> startActivity(intent));
                Function<List<Object>, Void> getPlayers = (List<Object> objects) -> {
                    ArrayList<String> players = new ArrayList<>();
                    for (Object object: objects) players.add(((DocumentSnapshot) object).getId());
                    intent.putStringArrayListExtra("playerIds", players);
                    intent.putExtra("fixture_id", "team_selection");
//                    RequestButton.setVisibility(View.VISIBLE);

                    String userId = FirebaseAuth.getInstance().getUid();
                    Function<List<Object>, Void> getTeamId = (List<Object> teams) -> {
                        for (Object team: teams) {
                            DocumentSnapshot doc = (DocumentSnapshot) team;
                            String captain = (String) doc.get("captain");
                            if (captain.equals(userId)) {
                                intent.putExtra("team_id", doc.getId());
                            }
                        }
                        return null;
                    };
                    db.chainColVal("teams", getTeamId);
                    return null;
                };
                db.chainColVal("players", getPlayers);
            } else {
                // TODO: This else statement is currently unreached
                Log.d("visibility", "making invisible for players");
                ManageTeamButton.setVisibility(View.GONE);
                RequestButton.setText("Join Team");
                RequestButton.setVisibility(View.VISIBLE);
                RequestButton.setOnClickListener(v ->
                        startActivity(new Intent(getApplicationContext(), RequestTeam.class)));
            }
            return null;
        };
        db.chainDocVal("players",
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                "isCaptain",
                checkCaptain);

        ViewProfileBtn = findViewById(R.id.ViewProfileBtn);
        ViewProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PlayerProfile.class));
                finish();
            }
        });

        Function<Object, Void> tidFunc = (Object o) -> {
            if (o != null) {
                teamUid = (String) o;
            }
            return null;
        };
        db.chainDocVal("players",
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                "teamUid",
                tidFunc);

        AnnouncementsBtn = findViewById(R.id.announcementsBtn);
        AnnouncementsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Announcements.class);
            if (!teamUid.equals(""))  intent.putExtra("tid", teamUid);
            startActivity(intent);
        });
    }
}