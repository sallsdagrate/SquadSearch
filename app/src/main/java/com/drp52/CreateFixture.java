package com.drp52;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.drp52.data.database.Fixture;
import com.drp52.data.database.FixtureDate;
import com.drp52.data.database.Team;
import com.google.android.material.button.MaterialButton;

public class CreateFixture extends AppCompatActivity {

    String currentTid;

    EditText opponent, location, date, time, pitch, formation;

    MaterialButton backBtn, createBtn;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_fixture);

        if (getIntent().hasExtra("currentTid")){
            currentTid = getIntent().getStringExtra("currentTid");
        }

        backBtn = findViewById(R.id.createFixtureBackBtn);
        backBtn.setOnClickListener(v -> finish());

        opponent = findViewById(R.id.createFixtureOpponentText);
        location = findViewById(R.id.createFixtureLocationText);
        date = findViewById(R.id.createFixtureDateText);
        time = findViewById(R.id.createFixtureTimeText);
        time.setOnClickListener(v -> {
            @SuppressLint("SetTextI18n") TimePickerDialog t = new TimePickerDialog(
                    CreateFixture.this,
                    (view, hourOfDay, minute) -> {
//                                time.setText(String.format("%02d:%02d", hourOfDay, minute));
                        time.setText(hourOfDay + ":" + minute);
                    }, 0, 0, true);
            t.show();
        });
        pitch = findViewById(R.id.createFixturePitchText);
        formation = findViewById(R.id.createFixtureFormationText);

        createBtn = findViewById(R.id.createFixtureCreateBtn);
        createBtn.setOnClickListener(v -> {
            String opp, loc, pit, form;
            String[] splitTime, splitDate;
            FixtureDate fixtureDate;
            opp = opponent.getText().toString();
            loc = location.getText().toString();
            splitDate = date.getText().toString().split("/", 4);
            splitTime = time.getText().toString().split(":", 3);
            pit = pitch.getText().toString();
            form = formation.getText().toString();

            if ( //Invalid conditions
                    opp.equals("") ||
                    loc.equals("") ||
                    splitDate.length < 3 ||
                    splitTime.length < 2 ||
                    pit.equals("") ||
                    form.equals("")
            ) {
                Toast.makeText(CreateFixture.this, "Please fill in all the fields properly", Toast.LENGTH_LONG).show();
            }
            else{
                fixtureDate = new FixtureDate(
                        Integer.parseInt(splitDate[2]),
                        Integer.parseInt(splitDate[1]),
                        Integer.parseInt(splitDate[0]),
                        Integer.parseInt(splitTime[0]),
                        Integer.parseInt(splitTime[1])
                );

                    DatabaseAdapter db = new FirebaseAdaptor();
                    db.createFixture(new Fixture(
                            new Team("team name not needed", currentTid),
                            opp,
                            fixtureDate,
                            pit,
                            loc,
                            form,
                            ""
                    ));
                    db.createTeamAnnouncement(currentTid, String.format(
                            "NEW FIXTURE against %s\n%s.\nLocation is %s.\nPitch type is %s.\nFormation will be %s",
                            opp,
                            fixtureDate,
                            loc,
                            pit,
                            form));
                    Toast.makeText(CreateFixture.this, "Fixture Created", Toast.LENGTH_SHORT).show();
                    finish();
                }


            });
    }
}