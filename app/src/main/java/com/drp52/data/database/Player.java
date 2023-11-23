package com.drp52.data.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Player implements Parcelable {
    private final String uid;
    private final String firstname;
    private final String surname;
    private final List<String> fixtures;
    private Team team;
    private Collection<Position> positions;

    private Boolean rightFoot;
    private Boolean leftFoot;
    private String year;
    private boolean isCaptain;
    //private Stats stats; TODO:add stats

    public Player(String uid, String firstname, String surname, Collection<Position> positions, boolean rightFoot, boolean leftFoot, String year, boolean isCaptain) {
        this.uid = uid;
        this.firstname = firstname;
        this.surname = surname;
        this.positions = positions;
        this.fixtures = Collections.emptyList();
        this.rightFoot = rightFoot;
        this.leftFoot = leftFoot;
        this.year = year;
        this.isCaptain = isCaptain;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public static class PlayerBuilder {
        private String uid;
        private String firstname;
        private String surname;
        private Team team;
        private Collection<Position> positions;

        private Boolean rightFoot = false;
        private Boolean leftFoot = false;
        private String year;
        private boolean isCaptain = false;
        public PlayerBuilder() {

        }
        public Player build(){
            return new Player(this.uid, this.firstname, this.surname, this.positions, this.rightFoot, this.leftFoot, this.year, this.isCaptain);
        }
        public PlayerBuilder isCaptain(Boolean captain){
            this.isCaptain = captain;
            return this;
        }
        public PlayerBuilder rightFoot(Boolean captain){
            this.isCaptain = captain;
            return this;
        }
        public PlayerBuilder leftFoot(Boolean captain){
            this.isCaptain = captain;
            return this;
        }
        public PlayerBuilder firstName(String firstname){
            this.firstname = firstname;
            return this;
        }
        public PlayerBuilder surname(String surname){
            this.surname = surname;
            return this;
        }
        public PlayerBuilder position(List<Position> positions){
            this.positions = positions;
            return this;
        }
        public PlayerBuilder team(Team team){
            this.team = team;
            return this;
        }
        public PlayerBuilder uid(String uid){
            this.uid = uid;
            return this;
        }
        public PlayerBuilder year(String year){
            this.year = year;
            return this;
        }

    }

    public Player(String uid, String firstname, String surname, Collection<Position> positions, List<String> fixtures) {
        this.uid = uid;
        this.firstname = firstname;
        this.surname = surname;
        this.positions = positions;
        this.fixtures = fixtures;
    }

    protected Player(Parcel in) {
        uid = in.readString();
        firstname = in.readString();
        surname = in.readString();
        fixtures = in.createStringArrayList();
        byte tmpRightFoot = in.readByte();
        rightFoot = tmpRightFoot == 0 ? null : tmpRightFoot == 1;
        byte tmpLeftFoot = in.readByte();
        leftFoot = tmpLeftFoot == 0 ? null : tmpLeftFoot == 1;
        year = in.readString();
        isCaptain = in.readByte() != 0;
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public String getFirstname() {
        return this.firstname;
    }

    public String getSurname() {
        return this.surname;
    }

    public Collection<Position> getPositions() {
        return this.positions;
    }

    public List<String> getFixtures() {
        return this.fixtures;
    }

    public String getUid() {
        return this.uid;
    }

    public boolean isLeftFooted() { return this.leftFoot; }

    public boolean isRightFooted() { return this.rightFoot; }

    public boolean isCaptain() { return this.isCaptain; }

    public String getYear() { return this.year; }

    public void setPositions(Collection<Position> positions) {
        this.positions = positions;
    }

    public void addFixture(String fixture) {
        this.fixtures.add(fixture);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(firstname);
        dest.writeString(surname);
        dest.writeStringList(fixtures);
        dest.writeByte((byte) (rightFoot == null ? 0 : rightFoot ? 1 : 2));
        dest.writeByte((byte) (leftFoot == null ? 0 : leftFoot ? 1 : 2));
        dest.writeString(year);
        dest.writeByte((byte) (isCaptain ? 1 : 0));
    }
}
