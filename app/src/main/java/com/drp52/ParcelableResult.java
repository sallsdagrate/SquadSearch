package com.drp52;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.drp52.data.database.Fixture;
import com.drp52.data.database.FixtureDate;
import com.drp52.data.database.GameResult;
import com.drp52.data.database.Team;

public class ParcelableResult extends ParcelableMatch {
    final String teamScore;
    final String opponentScore;


    public ParcelableResult(String teamName, String teamTid, String teamCapUid, String opponentName, String date,
                            String pitch, String location, String formation, String teamScore, String opponentScore, String fixtureId){
        super(
                teamName,
                teamTid,
                teamCapUid,
                opponentName,
                date,
                pitch,
                location,
                formation,
                fixtureId);
        this.teamScore = teamScore;
        this.opponentScore = opponentScore;
    }

    protected ParcelableResult(Parcel in) {
        this(
                in.readString(),// teamName
                in.readString(),// teamTid
                in.readString(),// teamCapUid
                in.readString(),// opponentName
                in.readString(),// date
                in.readString(),// pitch
                in.readString(),// location
                in.readString(),// formation
                in.readString(),// teamScore
                in.readString(),// opponentScore
                in.readString()// fixtureId
        );
    }

    public static final Creator<ParcelableResult> CREATOR = new Creator<>() {
        @Override
        public ParcelableResult createFromParcel(Parcel in) {
            return new ParcelableResult(in);
        }

        @Override
        public ParcelableResult[] newArray(int size) {
            return new ParcelableResult[size];
        }
    };

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(teamName);
        dest.writeString(teamTid);
        dest.writeString(teamCapUid);
        dest.writeString(opponentName);
        dest.writeString(date);
        dest.writeString(pitch);
        dest.writeString(location);
        dest.writeString(formation);
        dest.writeString(teamScore);
        dest.writeString(opponentScore);
        dest.writeString(fixtureId);
    }

    public GameResult getResult() {
        Team team = new Team(teamName, teamCapUid);
        team.setTid(teamTid);
        return new Fixture(team,
                opponentName,
                new FixtureDate(date),
                pitch,
                location,
                formation,
                fixtureId).createResult(Integer.parseInt(teamScore), Integer.parseInt(opponentScore));
    }
}
