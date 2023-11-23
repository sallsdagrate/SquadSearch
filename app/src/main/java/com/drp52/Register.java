package com.drp52;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.drp52.data.database.Player;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;

public class Register extends AppCompatActivity {

    EditText newPsw, newEmail, newFirstName, newLastName;
    MaterialButton RegisterBtn;
    RadioGroup YearGroupSelection;
    CheckBox RightFoot, LeftFoot, CaptainBtn;
    DatabaseAdapter db;
    FirebaseAuth auth;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new FirebaseAdaptor();
        auth = FirebaseAuth.getInstance();

        newPsw = findViewById(R.id.pswText2);
        newEmail = findViewById(R.id.emailText2);
        newFirstName = findViewById(R.id.firstNameText);
        newLastName = findViewById(R.id.lastNameText);

        YearGroupSelection = findViewById(R.id.playerProfileYearRadioGroup);
        RightFoot = findViewById(R.id.rightFoorCheckBox);
        LeftFoot = findViewById(R.id.leftFootCheckBox);
        CaptainBtn = findViewById(R.id.isCaptainCheckBox);

        RegisterBtn = findViewById(R.id.registerButton2);
        RegisterBtn.setOnClickListener(v -> {

            String psw, email, firstName, lastName;
            RadioButton year;
            psw = newPsw.getText().toString();
            email = newEmail.getText().toString();
            firstName = newFirstName.getText().toString();
            lastName = newLastName.getText().toString();
            year = (findViewById(YearGroupSelection.getCheckedRadioButtonId()));

            if(psw.equals("") ||
                    email.equals("") ||
                    firstName.equals("") ||
                    lastName.equals("") ||
                    year == null ||
                    (!RightFoot.isChecked() && !LeftFoot.isChecked())) {
                Toast.makeText(Register.this, "Fill in all the fields before registering", Toast.LENGTH_LONG).show();
            }
            assert year != null;

            auth
                    .createUserWithEmailAndPassword(email, psw)
                    .addOnSuccessListener(authResult -> {
                        Toast.makeText(Register.this, "Created New User", Toast.LENGTH_SHORT).show();
                        String uid;
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null){
                            uid = user.getUid();
                            db.createPlayer(new Player(
                                    uid,
                                    firstName,
                                    lastName,
                                    Collections.emptyList(),
                                    RightFoot.isChecked(),
                                    LeftFoot.isChecked(),
                                    (String) year.getText(),
                                    CaptainBtn.isChecked()
                                    ));
                            Intent intent = CaptainBtn.isChecked() ?
                                    new Intent(getApplicationContext(), RegisterTeam.class) :
                                    new Intent(getApplicationContext(), SelectPosition.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(Register.this, "Error adding user to FireStore", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(Register.this, "Failed to create new user, Try again", Toast.LENGTH_LONG).show());

        });
    }
}