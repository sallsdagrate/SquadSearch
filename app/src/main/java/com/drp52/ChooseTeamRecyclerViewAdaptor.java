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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.drp52.data.database.Player;
import com.drp52.data.database.Position;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class ChooseTeamRecyclerViewAdaptor extends RecyclerView.Adapter<ChooseTeamRecyclerViewAdaptor.MyViewHolder> {

    Context context;
    String uid = FirebaseAuth.getInstance().getUid();
    List<Player> availablePlayers;
    Set<Player> chosenPlayers = new HashSet<>();
    String fid, tid;
    boolean isNewTeam;
    DatabaseAdapter db = new FirebaseAdaptor();

    public ChooseTeamRecyclerViewAdaptor(Context context, List<Player> players, String fid, String tid){
        this.context = context;
        this.availablePlayers = players;
        this.fid = fid;
        this.isNewTeam = (fid.equals("team_selection"));
        this.tid = tid;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.choose_player_list_view_item, parent, false);
        return new ChooseTeamRecyclerViewAdaptor.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Inside an activity or a context-aware class
        Player player = availablePlayers.get(position);
        String playerName = player.getFirstname()
                + " " + player.getSurname();

        holder.playerNameText.setText(playerName);
        holder.positionsText.setText(player.getPositions().toString());
        holder.statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player player = availablePlayers.get(position);
                Intent intent = new Intent(context.getApplicationContext(), Stats.class);
                intent.putExtra("uid", player.getUid());
                intent.putExtra("firstname", player.getFirstname());
                intent.putExtra("surname", player.getSurname());
                intent.putExtra("teamname", player.getTeam().getName());
                context.startActivity(intent);
            }
        });
        FirebaseAdaptor fs = new FirebaseAdaptor();

        Function<Object, Void> setChecked = (Object o) -> {
            if (o == null) return null;
            if (((List<String>) o).contains(availablePlayers.get(position).getUid())) {
                holder.pickPlayer.setChecked(true);

            }
            return null;
        };
        db.chainDocVal("fixtures", fid, "confirmed_playerIds", setChecked);

        holder.pickPlayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FirebaseAdaptor db = new FirebaseAdaptor();
                if (isNewTeam) {
                    db.updateField("players", player.getUid(), "teamUid", tid);
                } else {
                    db.updateList("fixtures", fid, "confirmed_players", player.getFirstname() + " " + player.getSurname(), isChecked);
                    db.updateList("fixtures", fid, "confirmed_playerIds", player.getUid(), isChecked);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return availablePlayers.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView playerNameText, positionsText;
        MaterialButton statsBtn;

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch pickPlayer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameText = itemView.findViewById(R.id.choosePlayerNameTxt);
            positionsText = itemView.findViewById(R.id.choosePlayerPositions);
            statsBtn = itemView.findViewById(R.id.choosePlayerStatsBtn);
            pickPlayer = itemView.findViewById(R.id.choosePlayerChooseSwitch);
        }
    }
}
