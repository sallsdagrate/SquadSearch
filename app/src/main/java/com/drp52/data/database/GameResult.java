package com.drp52.data.database;

public class GameResult extends Match {
    private final int teamScore;
    private final int opponentScore;

    GameResult(Team team, String opponent, FixtureDate date,
               String pitch, String location, String formation,
               int teamScore, int opponentScore, String fixtureId) {
        super(team, opponent, date, pitch, location, formation, fixtureId);
        this.teamScore = teamScore;
        this.opponentScore = opponentScore;
    }

    public int getTeamScore() {
        return teamScore;
    }

    public int getOpponentScore() {
        return opponentScore;
    }
}
