package com.drp52.data.database;

public class Team {
    private String tid = "";
    private String name;
    private String capUid = "";

    public Team(String name) {
        this.name = name;
    }

    public Team(String name, String capUid) {
        this.name = name;
        this.capUid = capUid;
    }
    public String getTid() {
        return this.tid;
    }

    public String getName() {
        return name;
    }

    public String getCapUid() { return capUid; }
    public void setName(String name) {
        this.name = name;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
