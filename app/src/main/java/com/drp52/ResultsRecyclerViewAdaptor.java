package com.drp52;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.drp52.data.database.GameResult;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ResultsRecyclerViewAdaptor extends RecyclerView.Adapter<ResultsRecyclerViewAdaptor.MyViewHolder> {

    Context context;
    String uid = FirebaseAuth.getInstance().getUid();
    DatabaseAdapter db = new FirebaseAdaptor();
    List<GameResult> results;
    RecyclerInterface recyclerInterface;

    public ResultsRecyclerViewAdaptor(Context context, List<GameResult> results, RecyclerInterface recyclerInterface){
        this.context = context;
        this.results = results;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.results_list_view_item, parent, false);
        return new ResultsRecyclerViewAdaptor.MyViewHolder(view, recyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String teamsPlaying =
                results.get(position).getTeamName() +
                        " " +
                        results.get(position).getTeamScore() +
                        " - " +
                        results.get(position).getOpponentScore() +
                        " " +
                        results.get(position).getOpponent();
        holder.teamsPlayingText.setText(teamsPlaying);
        holder.dateText.setText(results.get(position).getDate().toString());
        holder.infoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, ResultInfo.class);
            List<GameResult> rs = results;
            ParcelableResult parcelableResult = new ParcelableResult(
                    rs.get(position).getTeamName(),
                    rs.get(position).getTeamId(),
                    rs.get(position).getTeam().getCapUid(),
                    rs.get(position).getOpponent(),
                    rs.get(position).getDate().toString().trim(),
                    rs.get(position).getPitch(),
                    rs.get(position).getLocation(),
                    rs.get(position).getFormation(),
                    ((Integer) rs.get(position).getTeamScore()).toString(),
                    ((Integer) rs.get(position).getOpponentScore()).toString(),
                    rs.get(position).getFixtureId()
            );

            intent.putExtra("parcelable_r", parcelableResult);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView teamsPlayingText, dateText;
        MaterialButton infoBtn;
        public MyViewHolder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
            super(itemView);
            teamsPlayingText = itemView.findViewById(R.id.requestName);
            dateText = itemView.findViewById(R.id.resultDateView);
            infoBtn = itemView.findViewById(R.id.resultInfoBtn);
        }
    }
}
