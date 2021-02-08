package com.fetchrewards.fetchrewardsapi.Model;

import com.fetchrewards.fetchrewardsapi.Util.PayerComparator;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserAccount {

    private String userID;
    private int totalPoints = 0;
    private HashMap<String, Integer> payersList = new HashMap<>();
    private HashMap<String, Integer> deductions = new HashMap<>();
    private PriorityQueue<Payer> additions = new PriorityQueue<>(new PayerComparator());

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void addPoints(int points) {
        this.totalPoints += points;
    }

    public int getTotalPoints() {
        return this.totalPoints;
    }


    public void addPayerToList(String payerName, int points) {
        payersList.put(payerName, payersList.getOrDefault(payerName, 0) + points);
    }


    public void additionsList(Payer payer) {
        additions.add(payer);
    }

    public void deductionsList(String payerName, int points) {
        deductions.put(payerName, deductions.getOrDefault(payerName, 0) + points);
    }

    public int getPayerTotalPoints(String payer) {
        return payersList.getOrDefault(payer, 0);
    }

    public Payer getFirstPayer() {
        return additions.poll();
    }

    public HashMap<String, Integer> getDeductions() {
        return deductions;
    }

    public HashMap<String, Integer> getPayersList() {
        return payersList;
    }
}
