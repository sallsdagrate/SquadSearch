package com.drp52;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.google.android.material.button.MaterialButton;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Announcements extends AppCompatActivity implements RecyclerInterface{

    MaterialButton backBtn;

    String teamUid = "";
    DatabaseAdapter db = new FirebaseAdaptor();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        backBtn = findViewById(R.id.annBackBtn);
        backBtn.setOnClickListener(v -> finish());

        Function<Object, Void> tidFunc = (Object o) -> {
            if (o != null) {
                teamUid = (String) o;
            }
            return null;
        };


        Context contextRef = this;
        RecyclerInterface recyclerRef = this;
        RecyclerView annView = findViewById(R.id.annRecyclerView);

        Function<Object, Void> func = (Object o) -> {

            List<String> anns = Collections.EMPTY_LIST;
            if (o != null) anns = (List<String>) o;
            AnnRecyclerViewAdaptor adaptor = new AnnRecyclerViewAdaptor(
                    contextRef,
                    anns,
                    recyclerRef
            );
            annView.setAdapter(adaptor);
            annView.setLayoutManager(new LinearLayoutManager(contextRef));

            return null;
        };


        if (getIntent().hasExtra("tid")){
            teamUid = getIntent().getStringExtra("tid");
            db.chainDocVal("teams",
                    teamUid,
                    "announcements",
                    func);
        }

    }

}