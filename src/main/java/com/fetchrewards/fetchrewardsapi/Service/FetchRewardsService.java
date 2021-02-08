package com.fetchrewards.fetchrewardsapi.Service;

import com.fetchrewards.fetchrewardsapi.Model.*;
import com.fetchrewards.fetchrewardsapi.Repository.UserAccountDetailsRepository;
import com.fetchrewards.fetchrewardsapi.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class FetchRewardsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserAccountDetailsRepository userAccountDetailsRepository;

    public boolean checkUserExistance(String hashValue) {
        return usersRepository.checkUserExistance(hashValue);
    }

    public void registerUser(String hashValue, User user) {
        usersRepository.registerUser(hashValue, user);
    }

    public List<User> getAllUsers() {
        return usersRepository.getAllUsers();
    }

    public boolean isUserPresent(String userID) {
        return userAccountDetailsRepository.isUserPresent(userID);
    }

    public String addPoints(AddPointsRequest request) {

        UserAccount userAccount = userAccountDetailsRepository.getUserAccount(request.getUserID());

        if(request.getPoints() < 0) {
            int payerPoints = userAccount.getPayerTotalPoints(request.getPayer());
            if(payerPoints < Math.abs(request.getPoints())) {
                return "Payer " + request.getPayer() + " has only " + payerPoints + " points";
            }
        }

        Payer payer = new Payer(request.getPayer(), request.getPoints(), request.getDate());

        userAccount.setUserID(request.getUserID());
        userAccount.addPoints(request.getPoints());
        userAccount.addPayerToList(request.getPayer(), request.getPoints());

        if(request.getPoints() > 0) {
            userAccount.additionsList(payer);
        }
        else if(request.getPoints() < 0) {
            userAccount.deductionsList(request.getPayer(), request.getPoints());
        }

        userAccountDetailsRepository.addPayer(userAccount);
        return "Points Updated Successfully";
    }


    public List<Payer> deductPoints(DeductPointsRequest request) {

        UserAccount userAccount = userAccountDetailsRepository.getUserDetails(request.getUserID());
        HashMap<String, Payer> response = new HashMap<>();

        if(request.getPoints() > userAccount.getTotalPoints()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough points to deduct");
        }

        int remainingPoints = request.getPoints();
        HashMap<String, Integer> deductions = userAccount.getDeductions();

        while(remainingPoints > 0) {
            Payer payer = userAccount.getFirstPayer();
            int deductPoints = 0;

            if(deductions.containsKey(payer.getPayer())) {
                deductPoints = checkDeduction(deductions, payer, remainingPoints);
            }
            else {
                deductPoints = Math.min(payer.getPoints(), remainingPoints);
            }

            if(payer.getPoints() > deductPoints) {
                payer.updatePoints(deductPoints * -1);
                userAccount.additionsList(payer);
            }

            userAccount.addPoints(deductPoints * -1);
            remainingPoints -= deductPoints;
            userAccount.addPayerToList(payer.getPayer(), deductPoints * -1);

            if(!response.containsKey(payer.getPayer())) {
                response.put(payer.getPayer(), new Payer(payer.getPayer(), deductPoints * -1, LocalDateTime.now()));
            }
            else {
                Payer existingPayer = response.get(payer.getPayer());
                existingPayer.updatePoints(deductPoints);
                response.put(payer.getPayer(), existingPayer);
            }
        }

        return new ArrayList<>(response.values());
    }


    public int checkDeduction(HashMap<String, Integer> deductions, Payer payer, int remainingPoints) {
        int deduction = 0;

        if(Math.abs(deductions.get(payer.getPayer())) <= payer.getPoints()) {
            deduction = Math.min(payer.getPoints() + deductions.get(payer.getPayer()), remainingPoints);
            payer.updatePoints(deductions.get(payer.getPayer()));
            deductions.remove(payer.getPayer());
        }
        else {
            deductions.put(payer.getPayer(), deductions.get(payer.getPayer()) + payer.getPoints());
        }

        return deduction;
    }

    public HashMap<String, Integer> getBalancePoints(String userID) {
        return userAccountDetailsRepository.getBalancePoints(userID);
    }
}
