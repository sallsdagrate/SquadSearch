package com.drp52;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.drp52.data.database.Fixture;
import com.drp52.data.database.Player;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FixturesTeamSelectedRecyclerViewAdaptor extends RecyclerView.Adapter<FixturesTeamSelectedRecyclerViewAdaptor.MyViewHolder> {

    Context context;
    String uid = FirebaseAuth.getInstance().getUid();
    DatabaseAdapter db = new FirebaseAdaptor();
    List<Fixture> fixtures;
    String tid;

    public FixturesTeamSelectedRecyclerViewAdaptor(Context context, List<Fixture> fixtures, String tid){
        this.context = context;
        this.fixtures = fixtures;
        this.tid = tid;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_fixtures_team_selected_list_view, parent, false);
        return new FixturesTeamSelectedRecyclerViewAdaptor.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String teamsPlaying =
                fixtures.get(position).getTeamName() +
                        " / " +
                        fixtures.get(position).getOpponent();
        holder.teamsPlayingText.setText(teamsPlaying);
        holder.dateText.setText(fixtures.get(position).getDate().toString());


        holder.makeAnnouncementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MakeAnnouncement.class);
                List<Fixture> fs = fixtures;
                List<String> players = new ArrayList<>();
                for (Object p : fixtures.get(position).getPlayers()) {
                    players.add(p.toString());
                }
                String announcementMessage = "The following players are picked " +
                        String.join(", ", players) +
                        "\n for the game on " + fs.get(position).getDate().toString().trim() +
                        "against " +  fs.get(position).getOpponent() +
                        "at " + fs.get(position).getLocation() +
                        "on a " + fs.get(position).getPitch() +
                        "with formation " + fs.get(position).getFormation();

                intent.putExtra("tid", tid);
                intent.putExtra("type", tid);
                intent.putExtra("message", announcementMessage);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return fixtures.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView teamsPlayingText, dateText;
        MaterialButton makeAnnouncementBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            teamsPlayingText = itemView.findViewById(R.id.teamsPlayingView1);
            dateText = itemView.findViewById(R.id.fixtureDateView1);
            makeAnnouncementBtn = itemView.findViewById(R.id.makeTeamAnnBtn);
        }
    }
}
