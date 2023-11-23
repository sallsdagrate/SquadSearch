package com.drp52;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public abstract class ParcelableMatch implements Parcelable {
    final String teamName;
    final String teamTid;
    final String teamCapUid;
    final String opponentName;
    final String date;
    final String pitch;
    final String location;
    final String formation;
    final String fixtureId;

    ParcelableMatch(String teamName, String teamTid, String teamCapUid, String opponentName, String date,
                            String pitch, String location, String formation, String fixtureId){
        this.teamName = teamName;
        this.teamTid = teamTid;
        this.teamCapUid = teamCapUid;
        this.opponentName = opponentName;
        this.date = date;
        this.pitch = pitch;
        this.location = location;
        this.formation = formation;
        this.fixtureId = fixtureId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public abstract void writeToParcel(@NonNull Parcel dest, int flags);
}
