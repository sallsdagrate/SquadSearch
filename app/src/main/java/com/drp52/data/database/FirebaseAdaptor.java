package com.drp52.data.database;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FirebaseAdaptor implements DatabaseAdapter {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public boolean updateList(String collection, String id, String field, Object o, boolean isAdding) {
        DocumentReference ref = db.collection(collection).document(id);
        if (isAdding) {
            ref.update(field, FieldValue.arrayUnion(o));
        } else {
            ref.update(field, FieldValue.arrayRemove(o));
        }
        return true;
    }
    @Override
    public boolean confirmAvailability(String uid, String fixtureId, boolean isAvailable) {
        updateList("players", uid, "fixtures", fixtureId, isAvailable);
        updateList("fixtures", fixtureId, "players", uid, isAvailable);
        return true;
    }

    @Override
    public boolean updateField(String collection, String uid, String field, Object value) {
        DocumentReference ref = db.collection(collection).document(uid);
        ref.update(field, value);
        return true;
    }

    @Override
    public boolean setPlayerPosition(String uid, Collection<Position> positions) {
        Function<Object, Void> func = (Object o) -> {
            for (String p: (List<String>) o) {
                updateList("players", uid, "positions", p, false);
            }
            for (Position p: positions) {
                updateList("players", uid, "positions", p, true);
            }
            return null;
        };
        return chainDocVal("players", uid, "positions", func);
    }

    @Override
    public boolean createFixture(Fixture fixture) throws IllegalArgumentException {
        if (fixture == null) return false;
        String tid = fixture.getTeam().getCapUid();
        if (tid == null || tid.equals("")) throw new IllegalArgumentException("Invalid team, Register Team first");
        Map<String, Object> data = new HashMap<>();
        data.put("team", tid);
        data.put("opponent", fixture.getOpponent());
        data.put("date", fixture.getDate().toString());
        data.put("pitch", fixture.getPitch());
        data.put("location", fixture.getLocation());
        data.put("formation", fixture.getFormation());
        data.put("players", fixture.getPlayers());
        data.put("team_picked", false);

        create("fixtures", fixture.getId(), data);
        return true;
    }

    @Override
    public boolean deleteFixture(String fixtureId) {
        return deleteById("fixtures", fixtureId);
    }

    @Override
    public boolean createResult(GameResult result) {
        if (result == null) return false;
        String tid = result.getTeam().getCapUid();
        if (tid == null || tid.equals("")) throw new IllegalArgumentException("Invalid team, Register Team first");
        Map<String, Object> data = new HashMap<>();
        data.put("team", tid);
        data.put("opponent", result.getOpponent());
        data.put("date", result.getDate().toString());
        data.put("pitch", result.getPitch());
        data.put("location", result.getLocation());
        data.put("formation", result.getFormation());
        data.put("players", result.getPlayers());
        data.put("teamScore", result.getTeamScore());
        data.put("opponentScore", result.getOpponentScore());

        create("result", result.getId(), data);
        return true;
    }

    @Override
    public boolean createTeam(Team team) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", team.getName());
        if(!team.getCapUid().equals("")){
            data.put("captain", team.getCapUid());
        }
        String tid = create("teams", "", data);
        team.setTid(tid);
        updateField("players",
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                "teamUid", tid);
        return true;
    }

    @Override
    public boolean createPlayer(Player player) {
        if (player == null) return false;
        Map<String, Object> data = new HashMap<>();
        data.put("firstname", player.getFirstname());
        data.put("surname", player.getSurname());
        data.put("positions", player.getPositions());
        data.put("fixtures", player.getFixtures());
        data.put("rightFoot", player.isRightFooted());
        data.put("leftFoot", player.isLeftFooted());
        data.put("year_group", player.getYear());
        data.put("isCaptain", player.isCaptain());
        data.put("teamUid", "unassigned");

        create("players", player.getUid(), data);
        return true;
    }

    @Override
    public boolean createTeamAnnouncement(String teamUid, String text) {
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        updateList("teams", teamUid, "announcements", text, true);
        return true;
    }

    @Override
    public String create(String collection, String uid, Map<String, Object> data) {
        DocumentReference ref = (uid == null || uid.equals("")) ?
                db.collection(collection).document() :
                db.collection(collection).document(uid);
        ref.set(data)
                .addOnSuccessListener(aVoid -> Log.i(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
        return ref.getId();
    }

    @Override
    public String addFields(String collection, String uid, Map<String, Object> data) {
        DocumentReference ref = (uid == null || uid.equals("")) ?
                db.collection(collection).document() :
                db.collection(collection).document(uid);
        ref.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.i(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
        return ref.getId();
    }


    @Override
    public boolean deleteById(String collections, String uid) {
        db.collection(collections).document(uid)
                .delete()
                .addOnSuccessListener(aVoid -> Log.i(TAG, "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
        return true;
    }

    @Override
    public boolean chainDocVal(String collection, String uid, String field,
                            Function<Object, Void> function) {
        DocumentReference ref = db.collection(collection).document(uid);
        ref
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Object o = document.get(field);
                            function.apply(o);
                        } else {
                            function.apply(null);
                        }
                    }
                });
        return true;
    }

    public boolean chainColVal(String collection,
                               Map<String, String> equalQueries, Map<String, List<String>> inQueries, Map<String, List<String>> contain,
                               Function<List<Object>, Void> function) {
        CollectionReference ref = db.collection(collection);
        Query q = ref;
        for (Map.Entry<String, String> entry : equalQueries.entrySet()) {
            q = q.whereEqualTo(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, List<String>> entry: contain.entrySet()) {
            q = q.whereArrayContainsAny(entry.getKey(), entry.getValue());
        }
        final List<String> ids;
        ids =  (inQueries.containsKey("id")) ? inQueries.get("id") : null;
        for (Map.Entry<String, List<String>> entry: inQueries.entrySet()) {
            if (entry.getKey() != "id") q = q.whereIn(entry.getKey(), entry.getValue());
        }
        q
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Object> objects = new ArrayList<>();
                        if (ids == null) { for (QueryDocumentSnapshot document : task.getResult()) {
                            objects.add(document);
                        } } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (ids.contains(document.getId())) {
                                    objects.add(document);
                                }
                            }
                        }

                        function.apply(objects);
                    }
                });
        return true;
    }

    public boolean chainColVal(String collection, Map<String, String> queries,
                               Function<List<Object>, Void> function) {
        return chainColVal(collection, queries, Collections.emptyMap(), Collections.emptyMap(), function);
    }

    @Override
    public boolean chainColVal(String collection,
                               Function<List<Object>, Void> function) {
        return chainColVal(collection, Collections.emptyMap(), function);
    }
}
