package com.drp52;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class PlayerProfile extends AppCompatActivity {

    MaterialButton BackBtn, EditPositionsBtn, SaveBtn;
    TextView PositionsText, EmailText, UidText, GoalScored;
    RadioGroup YearGroup;
    RadioButton y1, y2, y3, y4;
    CheckBox RF, LF;
    EditText Name1, Name2;
    String currUid;
    FirebaseUser usr;

    HashMap<String, Object> changes = new HashMap<>();

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        SaveBtn = findViewById(R.id.playerProfileSaveBtn);
        SaveBtn.setVisibility(View.INVISIBLE);

        BackBtn = findViewById(R.id.statsBackButton);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        });

        Name1 = findViewById(R.id.playerProfileNameText);
        Name2 = findViewById(R.id.playerProfileNameText2);
        EditPositionsBtn = findViewById(R.id.playerProfileEditPositions);
        PositionsText = findViewById(R.id.playerProfilePositions);
        EmailText = findViewById(R.id.textView34);
        UidText = findViewById(R.id.textView36);
        YearGroup = findViewById(R.id.playerProfileYearRadioGroup);
        GoalScored = findViewById(R.id.goalsScored2);

        RF = findViewById(R.id.playerProfileRightFootCheckBox);
        LF = findViewById(R.id.playerProfileLeftFootCheckBox);

        y1 = findViewById(R.id.playerProfileyear1RadioButton);
        y2 = findViewById(R.id.playerProfileyear2RadioButton);
        y3 = findViewById(R.id.playerProfileyear3RadioButton);
        y4 = findViewById(R.id.playerProfileyear4RadioButton);

        DatabaseAdapter db = new FirebaseAdaptor();
        usr = FirebaseAuth.getInstance().getCurrentUser();
        currUid = usr.getUid();

        UidText.setText(currUid);
        EmailText.setText(usr.getEmail());

        Function<Object, Void> funcName1 = (Object o) -> {
            if (!Objects.equals(o, "")){
                Name1.setText((CharSequence) o);
                Name1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        SaveBtn.setVisibility(View.VISIBLE);
                    }
                });
            }
            return null;
        };
        Function<Object, Void> funcName2 = (Object o) -> {
            if (!Objects.equals(o, "")){
                Name2.setText((CharSequence) o);
                Name2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        SaveBtn.setVisibility(View.VISIBLE);
                    }
                });
            }
            return null;
        };
        @SuppressLint("SetTextI18n") Function<Object, Void> funcPos = (Object o) -> {
            List<String> positions = (List<String>) o;
            if (positions == null || positions.size() == 0){
                PositionsText.setText("no selected positions");
            }
            else if (positions.size() == 1){
                PositionsText.setText(positions.get(0));
            }
            else{
                StringBuilder sb = new StringBuilder();
                sb.append(positions.get(0));
                positions.remove(0);
                for (String s:positions) {
                    sb.append(", ");
                    sb.append(s);
                }
                PositionsText.setText(sb.toString());
            }
            return null;
        };
        Function<Object, Void> funcYear = (Object o) -> {
            switch ((String) o){
                case "Year 1":
                    y1.setChecked(true);
                    break;
                case "Year 2":
                    y2.setChecked(true);
                    break;
                case "Year 3":
                    y3.setChecked(true);
                    break;
                case "Year 4":
                    y4.setChecked(true);
                    break;
            }
            YearGroup.setOnCheckedChangeListener((group, checkedId) -> SaveBtn.setVisibility(View.VISIBLE));
            return null;
        };
        Function<Object, Void> funcRf = (Object o) -> {
            RF.setChecked((Boolean) o);
            RF.setOnCheckedChangeListener((buttonView, isChecked) -> SaveBtn.setVisibility(View.VISIBLE));
            return null;
        };
        Function<Object, Void> funcLf = (Object o) -> {
            LF.setChecked((Boolean) o);
            LF.setOnCheckedChangeListener((buttonView, isChecked) -> SaveBtn.setVisibility(View.VISIBLE));
            return null;
        };
        Function<Object, Void> getGoals = (Object o) -> {
            Long goals = (o == null) ? 0 : (Long) o;
            GoalScored.setText(goals.toString());
            return null;
        };

        db.chainDocVal("players", currUid, "firstname", funcName1);
        db.chainDocVal("players", currUid, "surname", funcName2);
        db.chainDocVal("players", currUid, "positions", funcPos);
        db.chainDocVal("players", currUid, "year_group", funcYear);
        db.chainDocVal("players", currUid, "rightFoot", funcRf);
        db.chainDocVal("players", currUid, "leftFoot", funcLf);
        db.chainDocVal("players", currUid, "goals", getGoals);

        EditPositionsBtn.setOnClickListener(v -> {
            SaveBtn.setVisibility(View.VISIBLE);
            startActivity(new Intent(getApplicationContext(), SelectPosition.class));
            finish();
        });

        SaveBtn.setOnClickListener(v -> {
            RadioButton year = (findViewById(YearGroup.getCheckedRadioButtonId()));
            assert year != null;
            db.updateField("players", currUid, "firstname", Name1.getText().toString());
            db.updateField("players", currUid, "surname", Name2.getText().toString());
            db.updateField("players", currUid, "year_group", year.getText().toString());
            db.updateField("players", currUid, "rightFoot", RF.isChecked());
            db.updateField("players", currUid, "leftFoot", LF.isChecked());

            Toast.makeText(PlayerProfile.this, "Player profile updated", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        });
    }
}