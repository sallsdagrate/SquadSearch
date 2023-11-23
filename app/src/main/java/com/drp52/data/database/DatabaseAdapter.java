package com.drp52.data.database;

import com.google.firebase.firestore.Query;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface DatabaseAdapter {

    boolean updateList(String collection, String id, String field, Object o, boolean isAdding);
    boolean confirmAvailability(String uid, String fixtureId, boolean isAvailable);
    boolean updateField(String collection, String uid, String field, Object value);
    boolean setPlayerPosition(String uid, Collection<Position> positions);
    boolean createFixture(Fixture fixture);
    boolean deleteFixture(String fixtureId);
    boolean createResult(GameResult result);
    boolean createTeam(Team team);
    boolean createPlayer(Player player);
    boolean createTeamAnnouncement(String teamUid, String text);
    String create(String collection, String uid, Map<String, Object> data);
    String addFields(String collection, String uid, Map<String, Object> data);
    boolean deleteById(String collections, String uid);

    boolean chainDocVal(String collection, String uid, String field,
                     Function<Object, Void> function);

    boolean chainColVal(String collection,
                        Function<List<Object>, Void> function);

    boolean chainColVal(String collection, Map<String, String> queries,
                        Function<List<Object>, Void> function);

    boolean chainColVal(String collection,
                        Map<String, String> equalQueries, Map<String, List<String>> inQueries, Map<String, List<String>> contain,
                        Function<List<Object>, Void> function);
}
