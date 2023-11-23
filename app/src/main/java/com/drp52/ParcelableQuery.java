package com.drp52;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ParcelableQuery implements  Parcelable {
    Map<String, String> queries;

    public ParcelableQuery(Map<String, String> queries) {
        this.queries = queries;
    }

    public ParcelableQuery(Parcel in) {
        Map<String, String> queries = new HashMap<>();
        int n = in.readInt();
        String key, value;
        for (int i = 0; i < n; i++) {
            key = in.readString();
            value = in.readString();
            queries.put(key, value);
        }

        this.queries = queries;
    }

    public static final Parcelable.Creator<ParcelableQuery> CREATOR = new Parcelable.Creator<>() {
        @Override
        public ParcelableQuery createFromParcel(Parcel in) {
            return new ParcelableQuery(in);
        }

        @Override
        public ParcelableQuery[] newArray(int size) {
            return new ParcelableQuery[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(this.queries.size());
        for (Map.Entry<String, String> entry : this.queries.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }
}
