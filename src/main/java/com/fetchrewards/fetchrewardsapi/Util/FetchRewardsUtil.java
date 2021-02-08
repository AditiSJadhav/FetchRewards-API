package com.fetchrewards.fetchrewardsapi.Util;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;


@Component
public class FetchRewardsUtil {

    public static String getSHA(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return toHexString(md.digest(input.getBytes(StandardCharsets.UTF_8)));
    }


    public static String toHexString(byte[] hash)
    {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    public void validateDateTime(LocalDateTime date) {
        LocalDateTime currentTime = LocalDateTime.now();
        if(date.isBefore(currentTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot add points for a past date");
        };
    }
}
