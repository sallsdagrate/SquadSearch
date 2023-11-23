package com.drp52;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class AnnRecyclerViewAdaptor extends RecyclerView.Adapter<AnnRecyclerViewAdaptor.MyViewHolder> {

    Context context;
    String uid = FirebaseAuth.getInstance().getUid();
    List<String> announcements;
    RecyclerInterface recyclerInterface;

    public AnnRecyclerViewAdaptor(Context context, List<String> announcements, RecyclerInterface recyclerInterface){
        this.context = context;
        this.announcements = announcements;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public AnnRecyclerViewAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.ann_list_view_item, parent, false);
        return new AnnRecyclerViewAdaptor.MyViewHolder(view, recyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnRecyclerViewAdaptor.MyViewHolder holder, int position) {
        String ann = announcements.get(position);
        if(ann.length() > 25){
            holder.text.setText(ann.substring(0, 25) + " ...");
        }
        else holder.text.setText(ann);

        holder.infoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, AnnouncementFullView.class);
            intent.putExtra("ann", ann);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        MaterialButton infoBtn;
        public MyViewHolder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
            super(itemView);
            infoBtn = itemView.findViewById(R.id.annInfoBtn);
            text = itemView.findViewById(R.id.annText);

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
