package com.drp52.data.database;

public class Fixture extends Match {

    public Fixture(Team team, String opponent, FixtureDate date,
                   String pitch, String location, String formation, String fixtureId) {
        super(team, opponent, date, pitch, location, formation, fixtureId);
    }

    public GameResult createResult(int teamScore, int opponentScore) {
        GameResult result = new GameResult(team, opponent, date, pitch, location, formation, teamScore, opponentScore, fixtureId);
        result.setPlayers(players);
        return result;
    }

    public void setFixtureId(String fixtureId) {
        this.fixtureId = fixtureId;
    }

    public static class FixtureBuilder {
        private Team team;
        private String opponent;
        private FixtureDate date;
        private String pitch;
        private String location;
        private String formation;
        private String fixtureId;

        public FixtureBuilder() {
            this.pitch = "UNKNOWN";
            this.opponent = "tbc";
            this.location = "tbc";
            this.formation = "tbc";

        }

        public Fixture build() {
            return new Fixture(this.team, this.opponent, this.date,
                    this.pitch, this.location, this.formation, this.fixtureId);
        }

        public FixtureBuilder team(Team team) {
            this.team = team;
            return this;
        }

        public FixtureBuilder opponent(String opponent) {
            this.opponent = opponent;
            return this;
        }

        public FixtureBuilder date(FixtureDate date) {
            this.date = date;
            return this;
        }

        public FixtureBuilder pitch(String surface) {
            this.pitch = surface;
            return this;
        }

        public FixtureBuilder location(String location) {
            this.location = location;
            return this;
        }

        public FixtureBuilder formation(String formation) {
            this.formation = formation;
            return this;
        }

        public FixtureBuilder fixtureId(String fixtureId) {
            this.fixtureId = fixtureId;
            return this;
        }
    }
}
