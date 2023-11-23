package com.drp52;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.function.Function;

public class PlayerRequests extends AppCompatActivity implements RecyclerInterface{

    MaterialButton backBtn;
    DatabaseAdapter db = new FirebaseAdaptor();
    String tid = null;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_requests);

        if(getIntent().hasExtra("tid")){
            tid = getIntent().getStringExtra("tid");
        }

        backBtn = findViewById(R.id.teamMadeBackBtn);
        backBtn.setOnClickListener(v -> {
            finish();
        });

        RecyclerView recyclerView = findViewById(R.id.requestsRecyclerView);
        Function<Object, Void> getReqs = (Object o) -> {
            if (o == null) return null;
            List<String> reqs = (List<String>) o;
            if (tid != null){
                RequestsRecyclerViewAdaptor adaptor = new RequestsRecyclerViewAdaptor(this, reqs, tid, this);
                recyclerView.setAdapter(adaptor);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
            return null;
        };

        if (tid != null){
            db.chainDocVal("teams", tid, "playerRequests", getReqs);
        }
    }
}