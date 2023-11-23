package com.drp52;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drp52.data.database.DatabaseAdapter;
import com.drp52.data.database.FirebaseAdaptor;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class RequestsRecyclerViewAdaptor extends RecyclerView.Adapter<RequestsRecyclerViewAdaptor.MyViewHolder> {

    Context context;
    List<String> reqs;
    String tid;
    DatabaseAdapter db = new FirebaseAdaptor();
    RecyclerInterface recyclerInterface;

    public RequestsRecyclerViewAdaptor (Context context, List<String> reqs, String tid, RecyclerInterface recyclerInterface) {
        this.context = context;
        this.reqs = reqs;
        this.tid = tid;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.request_list_item, parent, false);
        return new RequestsRecyclerViewAdaptor.MyViewHolder(view, recyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        @SuppressLint("SetTextI18n") Function<Object, Void> getName = (Object o) -> {
            if (o == null) return null;
//            AtomicReference<String> name = new AtomicReference<String>("");
//            Function<Object, Void> getSur = (Object p) -> {
//                if (p == null) return null;
//                name.set((String) p);
////                holder.nameText.setText((String) p);
//                return null;
//            };
//            db.chainDocVal("players", reqs.get(position), "surname", getSur);
//            try {
//                TimeUnit.MILLISECONDS.sleep(100);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            String full = (String) o; // + " " + name.get();
            holder.nameText.setText(full);
            return null;
        };
        db.chainDocVal("players", reqs.get(position), "firstname", getName);

        holder.delBtn.setOnClickListener(v -> {
            manageRequest(v, tid, "Request Deleted", position);
        });

        holder.accBtn.setOnClickListener(v -> {
            db.updateField("players", reqs.get(position), "teamUid", tid);
            manageRequest(v, tid, "Request Accepted", position);
        });
    }

    private void manageRequest(View v, String tid, String message, int position) {
        db.updateList("teams", tid, "playerRequests", reqs.get(position), false);
        reqs.remove(position);
        notifyItemRemoved(position);
        Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return reqs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        MaterialButton accBtn, delBtn;

        public MyViewHolder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
            super(itemView);
            nameText = itemView.findViewById(R.id.requestName);
            accBtn = itemView.findViewById(R.id.requestAcc);
            delBtn = itemView.findViewById(R.id.requestDel);
        }
    }
}
