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
import java.util.function.Function;

public class FixturesRecyclerViewAdaptor extends RecyclerView.Adapter<FixturesRecyclerViewAdaptor.MyViewHolder> {

    Context context;
    String uid = FirebaseAuth.getInstance().getUid();
    DatabaseAdapter db = new FirebaseAdaptor();
    List<Fixture> fixtures;
    RecyclerInterface recyclerInterface;

    public FixturesRecyclerViewAdaptor(Context context, List<Fixture> fixtures, RecyclerInterface recyclerInterface){
        this.context = context;
        this.fixtures = fixtures;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fixtures_list_view_item, parent, false);
        return new FixturesRecyclerViewAdaptor.MyViewHolder(view, recyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String teamsPlaying =
                fixtures.get(position).getTeamName() +
                        " / " +
                        fixtures.get(position).getOpponent();
        Log.d("binding", "teams playing: " + teamsPlaying +
                " date: " + fixtures.get(position).getDate().toString());
        holder.teamsPlayingText.setText(teamsPlaying);
        holder.dateText.setText(fixtures.get(position).getDate().toString());

        holder.infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChooseTeam.class);
                List<Player> players = fixtures.get(position).getPlayers();
                intent.putParcelableArrayListExtra("playersIds", (ArrayList<? extends Parcelable>) players);
                intent.putExtra("fixture_id", fixtures.get(position).getFixtureId());
                v.getContext().startActivity(intent);
            }
        });

        holder.infoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, FixtureInfo.class);
            List<Fixture> fs = fixtures;
            ParcelableFixture parcelableFixture = new ParcelableFixture(
                    fs.get(position).getTeamName(),
                    fs.get(position).getTeamId(),
                    fs.get(position).getTeam().getCapUid(),
                    fs.get(position).getOpponent(),
                    fs.get(position).getDate().toString().trim(),
                    fs.get(position).getPitch(),
                    fs.get(position).getLocation(),
                    fs.get(position).getFormation(),
                    fs.get(position).getFixtureId()
            );

            intent.putExtra("parcelable_f", parcelableFixture);

            Function<Object, Void> func2 = (Object o) -> {
                if (o == null) return null;
                intent.putExtra("isCap", (Boolean) o);
                v.getContext().startActivity(intent);
                return null;
            };

            db.chainDocVal("players", uid, "isCaptain", func2);
        });

        holder.availableCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            db.confirmAvailability(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                fixtures.get(position).getFixtureId(),
                isChecked
            );
        });
        if (fixtures.get(position).getPlayers() != null){
            if (fixtures.get(position).getPlayers().contains(uid)){
                holder.availableCheck.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return fixtures.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView teamsPlayingText, dateText;
        MaterialButton infoBtn, manageTeamBtn;
        CheckBox availableCheck;
        public MyViewHolder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
            super(itemView);
            teamsPlayingText = itemView.findViewById(R.id.requestName);
            dateText = itemView.findViewById(R.id.fixtureDateView);
            infoBtn = itemView.findViewById(R.id.requestDel);
            availableCheck = itemView.findViewById(R.id.availabilityCheck);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (recyclerInterface != null){
//                        int pos = getAdapterPosition();
//
//                        if (pos != RecyclerView.NO_POSITION){
//                            recyclerInterface.onItemClick(pos);
//                        }
//                    }
//                }
//            });
        }
    }
}
