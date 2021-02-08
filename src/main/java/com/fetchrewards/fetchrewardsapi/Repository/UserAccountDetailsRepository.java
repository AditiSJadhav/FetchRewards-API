package com.fetchrewards.fetchrewardsapi.Repository;

import com.fetchrewards.fetchrewardsapi.Model.UserAccount;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UserAccountDetailsRepository {

    private HashMap<String, UserAccount> userAccounts = new HashMap<>();


    public void addPayer(UserAccount userAccount) {
        userAccounts.put(userAccount.getUserID(), userAccount);
    }

    public UserAccount getUserAccount(String userID) {
        if(userAccounts.containsKey(userID)){
            return userAccounts.get(userID);
        }

        return new UserAccount();
    }

    public boolean isUserPresent(String userID) {
        return userAccounts.containsKey(userID);
    }

    public UserAccount getUserDetails(String userID) {
        return userAccounts.get(userID);
    }

    public HashMap<String, Integer> getBalancePoints(String userID) {
        return userAccounts.get(userID).getPayersList();
    }
}
