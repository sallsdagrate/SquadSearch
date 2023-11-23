package com.drp52.data.database;

import java.util.ArrayList;
import java.util.List;

public abstract class Match {
    String id;
    Team team;
    String opponent;
    FixtureDate date;
    String pitch;
    String location;
    String formation;
    List<Player> players;
    String fixtureId;

    Match(Team team, String opponent, FixtureDate date,
          String pitch, String location, String formation, String fixtureId) {
        this.team = team;
        this.opponent = opponent;
        this.date = date;
        this.pitch = pitch;
        this.location = location;
        this.formation = formation;
        this.players = new ArrayList<>();
        this.fixtureId = fixtureId;
    }

    public String getTeamName() {
        return this.team.getName();
    }

    public Team getTeam() {
        return this.team;
    }

    public String getTeamId() { return this.team.getTid(); }
    public String getCapId() {return this.team.getCapUid();}

    public String getOpponent() {
        return this.opponent;
    }

    public FixtureDate getDate() {
        return this.date;
    }

    public String getPitch() { return this.pitch; }

    public String getLocation() { return this.location; }

    public String getFormation() { return formation; }

    public List<Player> getPlayers() { return this.players; }

    public void setPlayers(List<Player> players) {this.players = players; }

    public String getFixtureId() {
        return this.fixtureId;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setUid(String id) {this.id = id;}

    public String getId() {
        return this.id;
    }
}
