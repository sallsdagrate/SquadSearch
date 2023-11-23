package com.drp52;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.drp52.data.database.Position;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class SelectPosition extends AppCompatActivity {
    List<Integer> ids = new ArrayList<>(List.of(
            R.id.switchGK,
            R.id.switchLB,
            R.id.switchCB,
            R.id.switchRB,
            R.id.switchLM,
            R.id.switchCM,
            R.id.switchRM,
            R.id.switchLW,
            R.id.switchST,
            R.id.switchRW));
    MaterialButton saveBtn, backBtn;

    Set<Position> playerPositions;
    DatabaseAdapter db = new FirebaseAdaptor();
    FirebaseUser f = FirebaseAuth.getInstance().getCurrentUser();


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_position);

        setPreviousPositions(); //toggles positions that were already selected before

        playerPositions = new HashSet<>();

// Create change listeners to all buttons
        int idInd = 0;
        for (Position p : Position.values()) {
            Integer id = ids.get(idInd); //get id of ui switch element
            @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s = findViewById(id); //get appropriate switch
            //add listener to switch
            s.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    playerPositions.add(p);
                }
                else {
                    playerPositions.remove(p);
                }
            });
            idInd ++;
        }


// find the save button and add click listener
        saveBtn = findViewById(R.id.savePositionsButton);
        saveBtn.setOnClickListener(v -> {
            StringBuilder s = new StringBuilder();
            for (Position p : playerPositions){
                s.append(p.toString());
                s.append(", ");
            }

//                User FirebaseAdaptor to interact with firebase, get user and set new positions
            assert f != null;
            if (db.setPlayerPosition(f.getUid(), playerPositions)) {
                Toast.makeText(SelectPosition.this, String.format("Set Positions to %s", s), Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(), PlayerProfile.class));
            }
            else {
                Toast.makeText(SelectPosition.this, "Error changing positions, try again", Toast.LENGTH_LONG).show();
            }
        });

        backBtn = findViewById(R.id.selectPositionsBackBtn);
        backBtn.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(getApplicationContext(), PlayerProfile.class));
        });
    }

    private void setPreviousPositions() {
        // TODO implement toggling positions that were already selected
        Function<Object, Void> func = (Object o) -> {
            List<String> pos = (List<String>) o;
            if(pos == null || pos.size() == 0) return null;
            if(pos.contains("ST")){
                @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s =
                        (Switch) findViewById(R.id.switchST);
                s.setChecked(true);
            }

            if(pos.contains("RW")){
                @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s =
                        (Switch) findViewById(R.id.switchRW);
                s.setChecked(true);
            }
            if(pos.contains("LW")){
                @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s =
                        (Switch) findViewById(R.id.switchLW);
                s.setChecked(true);
            }
            if(pos.contains("CM")){
                @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s =
                        (Switch) findViewById(R.id.switchCM);
                s.setChecked(true);
            }
            if(pos.contains("RM")){
                @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s =
                        (Switch) findViewById(R.id.switchRM);
                s.setChecked(true);
            }
            if(pos.contains("LM")){
                @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s =
                        (Switch) findViewById(R.id.switchLM);
                s.setChecked(true);
            }
            if(pos.contains("CB")){
                @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s =
                        (Switch) findViewById(R.id.switchCB);
                s.setChecked(true);
            }
            if(pos.contains("RB")){
                @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s =
                        (Switch) findViewById(R.id.switchRB);
                s.setChecked(true);
            }
            if(pos.contains("LB")){
                @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s =
                        (Switch) findViewById(R.id.switchLB);
                s.setChecked(true);
            }
            if(pos.contains("GK")){
                @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s =
                        (Switch) findViewById(R.id.switchGK);
                s.setChecked(true);
            }
            return null;
        };
        db.chainDocVal("players", f.getUid(), "positions", func);
    }
}