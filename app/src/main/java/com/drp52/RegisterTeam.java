package com.drp52;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.drp52.data.database.Team;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterTeam extends AppCompatActivity {

    EditText teamName;
    MaterialButton RegisterBtn;
    DatabaseAdapter db;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_team);
        db = new FirebaseAdaptor();

        teamName = findViewById(R.id.teamNameInput);
        RegisterBtn = findViewById(R.id.teamRegisterButton);

        RegisterBtn.setOnClickListener(v -> {
            String name = teamName.getText().toString();
            if (name.equals("")) {
                Toast.makeText(RegisterTeam.this,"Invalid name", Toast.LENGTH_SHORT).show();
                return;
            }
            db.createTeam(new Team(name, FirebaseAuth.getInstance().getUid()));

            startActivity(new Intent(getApplicationContext(), SelectPosition.class));
            finish();
        });
    }
}