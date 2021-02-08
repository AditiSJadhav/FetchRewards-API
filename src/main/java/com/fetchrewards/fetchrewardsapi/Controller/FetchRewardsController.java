package com.fetchrewards.fetchrewardsapi.Controller;

import com.fetchrewards.fetchrewardsapi.Model.AddPointsRequest;
import com.fetchrewards.fetchrewardsapi.Model.DeductPointsRequest;
import com.fetchrewards.fetchrewardsapi.Model.Payer;
import com.fetchrewards.fetchrewardsapi.Model.User;
import com.fetchrewards.fetchrewardsapi.Service.FetchRewardsService;
import com.fetchrewards.fetchrewardsapi.Util.FetchRewardsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;


@RestController
public class FetchRewardsController {

    @Autowired
    private FetchRewardsUtil fetchRewardsUtil;

    @Autowired
    private FetchRewardsService fetchRewardsService;


    @RequestMapping("/register")
    public String register(@RequestParam String username, HttpServletResponse response) {

        String hashValue = null;
        try
        {
            hashValue = fetchRewardsUtil.getSHA(username);

            if(fetchRewardsService.checkUserExistance(hashValue)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user is already registered");
            }

            fetchRewardsService.registerUser(hashValue, new User(username, hashValue));
        }
        catch (NoSuchAlgorithmException e) {
        }

        response.setHeader("userID", hashValue);
        return "User successfully registered";
    }


    @RequestMapping("/get_users")
    public List<User> getAllUsers() {
        return fetchRewardsService.getAllUsers();
    }


    @RequestMapping(value = "/add_points", method = RequestMethod.POST)
    public String addPoints(@RequestBody AddPointsRequest request) {
        if(!fetchRewardsService.checkUserExistance(request.getUserID())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user is not registered");
        }

        try {
            fetchRewardsUtil.validateDateTime(request.getDate());
        }
        catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getReason());
        }

        return fetchRewardsService.addPoints(request);
    }


    @RequestMapping(value = "/deduct_points", method = RequestMethod.POST)
    public List<Payer> deductPoints(@RequestBody DeductPointsRequest request) {
        if(!fetchRewardsService.isUserPresent(request.getUserID())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user is not registered");
        }

        List<Payer> response;
        try {
            response = fetchRewardsService.deductPoints(request);
        }
        catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }

        return response;
    }


    @RequestMapping("/points_balance")
    public HashMap<String, Integer> pointsBalance(@RequestParam String userID) {
        if(!fetchRewardsService.isUserPresent(userID)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user is not registered");
        }

        return fetchRewardsService.getBalancePoints(userID);
    }

}
