package com.drp52;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ResultInfo extends AppCompatActivity {

    ParcelableResult r = null;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TextView resultNameText, formationText, dateTimeText, pitchText, locationText;
    MaterialButton backBtn;
    String tid = "";

    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_info);

        resultNameText = findViewById(R.id.resultInfoTeamsPlaying);
        formationText = findViewById(R.id.resultInfoFormation);
        dateTimeText = findViewById(R.id.resultInfoDateTime);
        pitchText = findViewById(R.id.resultInfoPitch);
        locationText = findViewById(R.id.resultInfoLocation);

        backBtn = findViewById(R.id.resultInfoBackBtn);
        backBtn.setOnClickListener(v -> finish());

        if (getIntent().hasExtra("parcelable_r")){
            r = getIntent().getParcelableExtra("parcelable_r");
        }

        if (r != null){
            tid = r.teamCapUid;
            resultNameText.setText(r.teamName + " " + r.teamScore + " - " + r.opponentScore + " " + r.opponentName);
            formationText.setText("formation:  " + r.formation);
            dateTimeText.setText(r.date);
            pitchText.setText("Pitch Type:  " + r.pitch);
            locationText.setText("Location:  " + r.location);
        }

    }
}