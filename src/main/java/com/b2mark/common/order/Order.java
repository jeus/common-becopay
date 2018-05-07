/*******************************************************
 * Copyright (C) 2018-2019 B2mark info@b2mark.com
 * 
 * This file is part of "B2mark Exchange".
 * 
 * "B2mark Exchange" can not be copied and/or distributed without the express
 * permission of B2mark
 *******************************************************/

package com.b2mark.common.order;

import com.b2mark.common.coin.Coin;
import com.b2mark.common.order.utils.OrderType;

/**
 * TODO: change this description in project
 */
public class Order {

    private final Coin giveCoin;
    private final Coin recieveCoin;
    private final long value;
    private final long price;
    private final long amount;
    private final OrderType orderType;

    public Order(Coin giveCoin, Coin recieveCoin, long unitPrice, long count) {
        this.price = unitPrice;
        this.recieveCoin = recieveCoin;
        this.giveCoin = giveCoin;
        this.amount = count;
        this.value = count * unitPrice;
        orderType = OrderType.UNKNOWN; 
    }

    public Coin getGiveCoin() {
        return giveCoin;
    }

    public Coin getRecieveCoin() {
        return recieveCoin;
    }

    public long getValue() {
        return value;
    }

    public long getPrice() {
        return price;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Give_Coin:").append(giveCoin.getName())
                .append("|Recieve_Coin:").append(recieveCoin.getName())
                .append("|Unit_pice:").append(price)
                .append("|Count:").append(amount)
                .append("|Amount:").append(value);
        return stringBuilder.toString();
    }
    
    
}
