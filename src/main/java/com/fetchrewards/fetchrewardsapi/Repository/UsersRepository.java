package com.fetchrewards.fetchrewardsapi.Repository;

import com.fetchrewards.fetchrewardsapi.Model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class UsersRepository {

    private Map<String, User> users = new HashMap<>();

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void registerUser(String userID, User user) {
        users.put(userID, user);
    }

    public boolean checkUserExistance(String hashValue) {
        return users.containsKey(hashValue);
    }

}
