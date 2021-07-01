package com.example.financefinancer.model;

public class Bill {
    public static final String TAG = "Bill";

    private long id;
    private String billName;
    private double billAmount;
    private double billShare;
    private String billMembers;
    private long tripId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return billName;
    }

    public void setName(String billName) {
        this.billName = billName;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public double getBillShare() {
        return billShare;
    }

    public void setBillShare(double billAmount) {
        this.billShare = billAmount;
    }

    public String getBillMembers() {
        return billMembers;
    }

    public void setBillMembers(String billAmount) {
        this.billMembers = billAmount;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }
}
