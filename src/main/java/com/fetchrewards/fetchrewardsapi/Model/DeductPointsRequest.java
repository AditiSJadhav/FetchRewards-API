package com.fetchrewards.fetchrewardsapi.Model;


public class DeductPointsRequest {

    private String userID;
    private int points;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
