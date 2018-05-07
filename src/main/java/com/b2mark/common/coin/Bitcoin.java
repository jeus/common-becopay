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
