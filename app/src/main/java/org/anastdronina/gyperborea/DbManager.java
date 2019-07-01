package org.anastdronina.gyperborea;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.Arrays;


public class DbManager {
    private Bundle bundle;
    private Message message;
    private Handler handler;
    public enum WhatData {population, tech, stock, farms, market}

    public void createNewDatabase() {
        handler = new Handler();
        message = handler.obtainMessage(DbThread.CREATE_DATABASE);
        DbThread.getBackgroundHandler().sendMessage(message);
    }

    public void performQuery(String query) {
        bundle = new Bundle();
        handler = new Handler();
        bundle.putString("query", query);
        message = handler.obtainMessage(DbThread.PERFORM_SQL_QUERY);
        message.setData(bundle);
        DbThread.getBackgroundHandler().sendMessage(message);
    }

    public void loadData(WhatData whatData) {
        handler = new Handler();
        switch (whatData) {
            case population:
                message = handler.obtainMessage(DbThread.LOAD_POPULATION_DATA);
                DbThread.getBackgroundHandler().sendMessage(message);
                break;
            case tech:
                message = handler.obtainMessage(DbThread.LOAD_TECH_DATA);
                DbThread.getBackgroundHandler().sendMessage(message);
                break;
            case stock:
                message = handler.obtainMessage(DbThread.LOAD_STOCK_DATA);
                DbThread.getBackgroundHandler().sendMessage(message);
                break;
            case farms:
                message = handler.obtainMessage(DbThread.LOAD_FARMS_DATA);
                DbThread.getBackgroundHandler().sendMessage(message);
                break;
            case market:
                message = handler.obtainMessage(DbThread.LOAD_MARKET_DATA);
                DbThread.getBackgroundHandler().sendMessage(message);
                break;
            default:
        }
    }

    public void printCoefAsync(int personId) {
        handler = new Handler();
        message = handler.obtainMessage(DbThread.PRINT_COEF_ASYNC, personId, 0);
        DbThread.getBackgroundHandler().sendMessage(message);
    }

    public void setCoefInFinances() {
        handler = new Handler();
        message = handler.obtainMessage(DbThread.SET_COEF_IN_FINANCES_ACTIVITY);
        DbThread.getBackgroundHandler().sendMessage(message);
    }

    public void countInfoForNextTurn() {
        handler = new Handler();
        message = handler.obtainMessage(DbThread.COUNT_INFO_FOR_NEXT_TURN);
        DbThread.getBackgroundHandler().sendMessage(message);
    }

    public void insertStockData(String productName, String productType, int productAmount) {
        bundle = new Bundle();
        handler = new Handler();
        bundle.putString("productName", productName);
        bundle.putString("productType", productType);
        bundle.putInt("productAmount", productAmount);
        message = handler.obtainMessage(DbThread.INSERT_STOCK_DATA);
        message.setData(bundle);
        DbThread.getBackgroundHandler().sendMessage(message);
    }

    public void insertPopulationData(String name, String surname, int job, int salary, int age, int building, int manufacture, int farm, int athletic, int learning, int talking, int strength, int art, String trait1, String trait2, String trait3, int finMonthWorked, Double finCoef) {
        bundle = new Bundle();
        handler = new Handler();
        bundle.putStringArrayList("stringValues", new ArrayList<>(Arrays.asList(name, surname, trait1, trait2, trait3)));
        bundle.putIntegerArrayList("intValues",
                new ArrayList(Arrays.asList(job, salary, age, building, manufacture, farm, athletic, learning, talking, strength, art)));
        bundle.putInt("finMonthsWorked", finMonthWorked);
        bundle.putDouble("finCoef", finCoef);
        message = handler.obtainMessage(DbThread.INSERT_POPULATION_DATA);
        message.setData(bundle);
        DbThread.getBackgroundHandler().sendMessage(message);
    }

    public void insertTechData(String name, String description, int monthsToLearn, int price, int isLEarned) {
        bundle = new Bundle();
        handler = new Handler();
        bundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList(name, description)));
        bundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(monthsToLearn, price, isLEarned)));
        message = handler.obtainMessage(DbThread.INSERT_TECH_DATA);
        message.setData(bundle);
        DbThread.getBackgroundHandler().sendMessage(message);
    }

    public void insertFarmsData(String name, String crop, int status, int farmerId) {
        bundle = new Bundle();
        handler = new Handler();
        bundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList(name, crop)));
        bundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(status, farmerId)));
        message = handler.obtainMessage(DbThread.INSERT_FARMS_DATA);
        message.setData(bundle);
        DbThread.getBackgroundHandler().sendMessage(message);
    }

    public void insertMarketData(String name, String currency, String type, int amount, int price) {
        bundle = new Bundle();
        handler = new Handler();
        bundle.putStringArrayList("stringValues", new ArrayList<>(Arrays.asList(name, currency, type)));
        bundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(amount, price)));
        message = handler.obtainMessage(DbThread.INSERT_MARKET_DATA);
        message.setData(bundle);
        DbThread.getBackgroundHandler().sendMessage(message);
    }

}
