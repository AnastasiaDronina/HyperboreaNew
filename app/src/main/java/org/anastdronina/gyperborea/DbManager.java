package org.anastdronina.gyperborea;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.Arrays;


public class DbManager {
    private Bundle mBundle;
    private Message mMessage;
    private Handler mHandler;

    public enum WhatData {POPULATION, TECH, STOCK, FARMS, MARKET}

    public void createNewDatabase() {
        mHandler = new Handler();
        mMessage = mHandler.obtainMessage(DbThread.CREATE_DATABASE);
        DbThread.getBackgroundHandler().sendMessage(mMessage);
    }

    public void performQuery(String query) {
        mBundle = new Bundle();
        mHandler = new Handler();
        mBundle.putString("query", query);
        mMessage = mHandler.obtainMessage(DbThread.PERFORM_SQL_QUERY);
        mMessage.setData(mBundle);
        DbThread.getBackgroundHandler().sendMessage(mMessage);
    }

    public void loadData(WhatData whatData) {
        mHandler = new Handler();
        switch (whatData) {
            case POPULATION:
                mMessage = mHandler.obtainMessage(DbThread.LOAD_POPULATION_DATA);
                DbThread.getBackgroundHandler().sendMessage(mMessage);
                break;
            case TECH:
                mMessage = mHandler.obtainMessage(DbThread.LOAD_TECH_DATA);
                DbThread.getBackgroundHandler().sendMessage(mMessage);
                break;
            case STOCK:
                mMessage = mHandler.obtainMessage(DbThread.LOAD_STOCK_DATA);
                DbThread.getBackgroundHandler().sendMessage(mMessage);
                break;
            case FARMS:
                mMessage = mHandler.obtainMessage(DbThread.LOAD_FARMS_DATA);
                DbThread.getBackgroundHandler().sendMessage(mMessage);
                break;
            case MARKET:
                mMessage = mHandler.obtainMessage(DbThread.LOAD_MARKET_DATA);
                DbThread.getBackgroundHandler().sendMessage(mMessage);
                break;
            default:
        }
    }

    public void printCoefAsync(int personId) {
        mHandler = new Handler();
        mMessage = mHandler.obtainMessage(DbThread.PRINT_COEF_ASYNC, personId, 0);
        DbThread.getBackgroundHandler().sendMessage(mMessage);
    }

    public void setCoefInFinances() {
        mHandler = new Handler();
        mMessage = mHandler.obtainMessage(DbThread.SET_COEF_IN_FINANCES_ACTIVITY);
        DbThread.getBackgroundHandler().sendMessage(mMessage);
    }

    public void countInfoForNextTurn() {
        mHandler = new Handler();
        mMessage = mHandler.obtainMessage(DbThread.COUNT_INFO_FOR_NEXT_TURN);
        DbThread.getBackgroundHandler().sendMessage(mMessage);
    }

    public void insertStockData(String productName, String productType, int productAmount) {
        mBundle = new Bundle();
        mHandler = new Handler();
        mBundle.putString("productName", productName);
        mBundle.putString("productType", productType);
        mBundle.putInt("productAmount", productAmount);
        mMessage = mHandler.obtainMessage(DbThread.INSERT_STOCK_DATA);
        mMessage.setData(mBundle);
        DbThread.getBackgroundHandler().sendMessage(mMessage);
    }

    public void insertPopulationData(String name, String surname, int job, int salary, int age, int building, int manufacture, int farm, int athletic, int learning, int talking, int strength, int art, String trait1, String trait2, String trait3, int finMonthWorked, Double finCoef) {
        mBundle = new Bundle();
        mHandler = new Handler();
        mBundle.putStringArrayList("stringValues", new ArrayList<>(Arrays.asList(name, surname, trait1, trait2, trait3)));
        mBundle.putIntegerArrayList("intValues",
                new ArrayList(Arrays.asList(job, salary, age, building, manufacture, farm, athletic, learning, talking, strength, art)));
        mBundle.putInt("finMonthsWorked", finMonthWorked);
        mBundle.putDouble("finCoef", finCoef);
        mMessage = mHandler.obtainMessage(DbThread.INSERT_POPULATION_DATA);
        mMessage.setData(mBundle);
        DbThread.getBackgroundHandler().sendMessage(mMessage);
    }

    public void insertTechData(String name, String description, int monthsToLearn, int price, int isLEarned) {
        mBundle = new Bundle();
        mHandler = new Handler();
        mBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList(name, description)));
        mBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(monthsToLearn, price, isLEarned)));
        mMessage = mHandler.obtainMessage(DbThread.INSERT_TECH_DATA);
        mMessage.setData(mBundle);
        DbThread.getBackgroundHandler().sendMessage(mMessage);
    }

    public void insertFarmsData(String name, String crop, int status, int farmerId) {
        mBundle = new Bundle();
        mHandler = new Handler();
        mBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList(name, crop)));
        mBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(status, farmerId)));
        mMessage = mHandler.obtainMessage(DbThread.INSERT_FARMS_DATA);
        mMessage.setData(mBundle);
        DbThread.getBackgroundHandler().sendMessage(mMessage);
    }

    public void insertMarketData(String name, String currency, String type, int amount, int price) {
        mBundle = new Bundle();
        mHandler = new Handler();
        mBundle.putStringArrayList("stringValues", new ArrayList<>(Arrays.asList(name, currency, type)));
        mBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(amount, price)));
        mMessage = mHandler.obtainMessage(DbThread.INSERT_MARKET_DATA);
        mMessage.setData(mBundle);
        DbThread.getBackgroundHandler().sendMessage(mMessage);
    }

}
