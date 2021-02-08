package com.fetchrewards.fetchrewardsapi.Model;

import java.time.LocalDateTime;


public class Payer {

    private String payer;
    private int points;
    private LocalDateTime date;

    public Payer(String payer, int points, LocalDateTime date) {
        this.payer = payer;
        this.points = points;
        this.date = date;
    }


    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public int getPoints() {
        return points;
    }

    public void updatePoints(int points) {
        this.points += points;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
