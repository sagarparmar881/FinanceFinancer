package com.example.financefinancer.model;

public class Trip {
    public static final String TAG = "Trip";
    private long tripId;
    private String tripName;

    public Trip() {
    }

    public Trip(String tripName) {
        this.tripName = tripName;
    }

    public long getId() {
        return tripId;
    }

    public void setId(long mId) {
        this.tripId = mId;
    }

    public String getName() {
        return tripName;
    }

    public void setName(String mFirstName) {
        this.tripName = mFirstName;
    }

}
