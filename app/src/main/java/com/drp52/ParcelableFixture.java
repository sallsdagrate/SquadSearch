package com.drp52;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.drp52.data.database.Fixture;
import com.drp52.data.database.FixtureDate;
import com.drp52.data.database.Team;

public class ParcelableFixture extends ParcelableMatch {

    public ParcelableFixture(String teamName, String teamTid, String teamCapUid, String opponentName, String date,
                             String pitch, String location, String formation, String fixtureId){
        super(
                teamName,
                teamTid,
                teamCapUid,
                opponentName,
                date,
                pitch,
                location,
                formation,
                fixtureId
        );
    }

    protected ParcelableFixture(Parcel in) {
        this(
                in.readString(),// teamName
                in.readString(),// teamTid
                in.readString(),// teamCapUid
                in.readString(),// opponentName
                in.readString(),// date
                in.readString(),// pitch
                in.readString(),// location
                in.readString(),// formation
                in.readString()// fixtureId
        );
    }

    public static final Creator<ParcelableFixture> CREATOR = new Creator<>() {
        @Override
        public ParcelableFixture createFromParcel(Parcel in) {
            return new ParcelableFixture(in);
        }

        @Override
        public ParcelableFixture[] newArray(int size) {
            return new ParcelableFixture[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

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
        dest.writeString(fixtureId);
    }

    public Fixture getFixture() {
        Team team = new Team(teamName, teamCapUid);
        team.setTid(teamTid);
        return new Fixture(team,
                opponentName,
                new FixtureDate(date),
                pitch,
                location,
                formation,
                fixtureId);
    }
}
