package com.example.financefinancer.model;

public class Member {

    public static final String TAG = "Member";

    private long mId;
    private String mName;
    private double mCredit;
    private double mDebit;
    private double mAmount;
    private long tripId;
    private double mBalance;

    public Member() {
    }

    public Member(String mName, int tripId) {
        this.mName = mName;
        this.mCredit = 0;
        this.mDebit = 0;
        this.tripId = tripId;
        this.mAmount = -1;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mFirstName) {
        this.mName = mFirstName;
    }

    public double getCredit() {
        return mCredit;
    }

    public void setCredit(double mCredit) {
        this.mCredit = mCredit;
    }

    public double getDebit() {
        return mDebit;
    }

    public void setDebit(double mDebit) {
        this.mDebit = mDebit;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double mAmount) {
        this.mAmount = mAmount;
    }

    public double getBalance() {
        return mBalance;
    }

    public void setBalance(double mBalance) {
        this.mBalance = mBalance;
    }
}