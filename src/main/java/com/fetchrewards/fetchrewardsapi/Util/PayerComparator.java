package com.fetchrewards.fetchrewardsapi.Util;

import com.fetchrewards.fetchrewardsapi.Model.Payer;

import java.util.Comparator;

public class PayerComparator implements Comparator<Payer> {

    @Override
    public int compare(Payer payer1, Payer payer2) {
        if(payer1.getDate().isBefore(payer2.getDate())) {
            return -1;
        }
        else if(payer1.getDate().isAfter(payer2.getDate())) {
            return 1;
        }

        return 0;
    }
}
