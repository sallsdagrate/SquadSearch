package com.drp52;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    MaterialButton RegisterBtn;
    EditText emailText, pswText;
    MaterialButton LoginButton;

   @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        if (auth != null){
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        RegisterBtn = findViewById(R.id.registerButton);
        RegisterBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
            finish();
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.emailText);
        pswText = findViewById(R.id.pswText);
        LoginButton = findViewById(R.id.loginButton);
        LoginButton.setOnClickListener(v -> {
            String email, psw;
            email = emailText.getText().toString();
            psw = pswText.getText().toString();
            auth.signInWithEmailAndPassword(email, psw).addOnSuccessListener(authResult -> {
                Toast.makeText(Login.this, "Logged In", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(Login.this, "Log In Failed", Toast.LENGTH_LONG).show();
                pswText.setText("");
            });
        });
    }
}