/*******************************************************
 * Copyright (C) 2018-2019 B2mark info@b2mark.com
 * 
 * This file is part of "B2mark Exchange".
 * 
 * "B2mark Exchange" can not be copied and/or distributed without the express
 * permission of B2mark
 *******************************************************/

package com.b2mark.common.coin;

/**
 * Coin is currency that work in system 
 *TODO: have to change description in this project
 */
public final class Bitcoin implements Coin {

    private final String name;
    private final String shortName;
    private final int numberOfFloat;
    private final String property;

    public Bitcoin(String name, String shortName, int numberOfFloat) {
        this.name = name;
        this.shortName = name;
        this.numberOfFloat = numberOfFloat;
        property = "not set"; //TODO: have to change
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public String getProperty() {
        return property;
    }

    @Override
    public int numberOfFloat() {
        return numberOfFloat;
    }

}
