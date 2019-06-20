package org.anastdronina.gyperborea;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class DbThread extends Thread {

    private static DbThread mInstance;
    private static Boolean isDataLoaded = false;
    private static DbListener dbListener;

    private DbThread() {
    }

    public static DbThread getInstance() {
        if(mInstance == null){
            mInstance = new DbThread();
        }
        return mInstance;
    }

    public interface DbListener {
        void onDataLoaded();
    }

    public void setData() {
        if (isDataLoaded && dbListener != null) {
            dbListener.onDataLoaded();
        }
    }

    public void addListener(DbListener listener) {
        this.dbListener = listener;
    }

    public void removeListener(DbListener listener) {
        dbListener = null;
    }

    //...................всякие остальные методы..................................
    public void doQuery(String sqlQuery) {
        SQLiteDatabase db = SQLiteDatabase
                .openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        db.execSQL(sqlQuery);
    }

    public ArrayList<Person> loadAllPeopleData() {
        isDataLoaded = false;
        SQLiteDatabase db = SQLiteDatabase
                .openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        ArrayList<Person> populationList = new ArrayList<>();
        Cursor res = db.rawQuery("select * from " + "population", null);
        while (res.moveToNext()) {
            int id = Integer.parseInt(res.getString(0));
            String name = res.getString(1);
            String surname = res.getString(2);
            int job = Integer.parseInt(res.getString(3));
            int salary = Integer.parseInt(res.getString(4));
            int age = Integer.parseInt(res.getString(5));
            int building = Integer.parseInt(res.getString(6));
            int manufacture = Integer.parseInt(res.getString(7));
            int farm = Integer.parseInt(res.getString(8));
            int athletic = Integer.parseInt(res.getString(9));
            int learning = Integer.parseInt(res.getString(10));
            int talking = Integer.parseInt(res.getString(11));
            int strength = Integer.parseInt(res.getString(12));
            int art = Integer.parseInt(res.getString(13));
            String trait1 = res.getString(14);
            String trait2 = res.getString(15);
            String trait3 = res.getString(16);

            populationList.add(new Person(id, name, surname, job, salary, age, building, manufacture, farm, athletic, learning, talking, strength, art,
                    new ArrayList<>(Arrays.asList(trait1, trait2, trait3))));
        }

        if (populationList != null) {
            isDataLoaded = true;
        }

        return populationList;
    }

    public ArrayList<Tecnology> loadAllTechData() {
        isDataLoaded = false;
        SQLiteDatabase db = SQLiteDatabase
                .openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        ArrayList<Tecnology> tecs = new ArrayList<>();
        Cursor res = db.rawQuery("select * from " + "tecnologies", null);
        while (res.moveToNext()) {
            int id = res.getInt(0);
            String name = res.getString(1);
            String description = res.getString(2);
            int monthsToLearn = res.getInt(3);
            int price = res.getInt(4);
            int isLearnedInt = res.getInt(5);
            boolean isLearned = false;
            if (isLearnedInt == 1) {
                isLearned = true;
            }
            tecs.add(new Tecnology(id, name, description, monthsToLearn, price, isLearned));
        }
        if (tecs != null) {
            isDataLoaded = true;
        }
        return tecs;
    }

    public ArrayList<Product> loadAllStockData() {
        isDataLoaded = false;
        SQLiteDatabase db = SQLiteDatabase
                .openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        ArrayList<Product> products = new ArrayList<>();
        Cursor res = db.rawQuery("select * from " + "stock", null);
        while (res.moveToNext()) {
            int id = res.getInt(0);
            String name = res.getString(1);
            String type = res.getString(2);
            int amount = res.getInt(3);

            products.add(new Product(id, name, type, amount));
        }
        if (products != null) {
            isDataLoaded = true;
        }
        return products;
    }

    public ArrayList<Farm> loadAllFarmsData() {
        isDataLoaded = false;
        SQLiteDatabase db = SQLiteDatabase
                .openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        ArrayList<Farm> farms = new ArrayList<>();

        Cursor res = db.rawQuery("select * from " + "farms", null);
        while (res.moveToNext()) {
            int id = Integer.parseInt(res.getString(0));
            String name = res.getString(1);
            String crop = res.getString(2);
            int status = Integer.parseInt(res.getString(3));
            int farmerId = Integer.parseInt(res.getString(4));

            farms.add(new Farm(id, name, crop, status, farmerId));
        }

        if (farms != null) {
            isDataLoaded = true;
        }

        return farms;
    }

    public ArrayList<MarketItem> loadAllMarketData() {
        isDataLoaded = false;
        SQLiteDatabase db = SQLiteDatabase
                .openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        ArrayList<MarketItem> marketItems = new ArrayList<>();

        Cursor res = db.rawQuery("select * from " + "market", null);
        while (res.moveToNext()) {
            int id = res.getInt(0);
            String name = res.getString(1);
            int amount = res.getInt(2);
            int price = res.getInt(3);
            String currency = res.getString(4);
            String type = res.getString(5);

            marketItems.add(new MarketItem(id, name, amount, price, currency, type));
        }

        if (marketItems != null) {
            isDataLoaded = true;
        }

        return marketItems;
    }

    public String printCoefAsync(int personId) {
        SQLiteDatabase db = SQLiteDatabase
                .openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        String printCoef = "";
        Cursor res = db.rawQuery("select * from " + "population", null);
        while (res.moveToNext()) {
            int id = Integer.parseInt(res.getString(0));
            double finCoef = res.getDouble(18);
            if (id == personId) {
                printCoef = "\nКОЭФФИЦИЕНТ УЛУЧШЕНИЯ ФИНАНСОВ: " + finCoef;
            }

        }
        return printCoef;
    }

    public Double setCoefInFinancesActivity() {
        SQLiteDatabase db = SQLiteDatabase
                .openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        Double coef = 0.0;
        Cursor res = db.rawQuery("select * from " + "population", null);
        while (res.moveToNext()) {
            double finCoef = res.getDouble(18);
            coef += finCoef;
        }
        coef = Math.round(coef * 10.0) / 10.0;

        return coef;
    }

    public ArrayList<Integer> getFinIds() {
        ArrayList finIds = new ArrayList<Integer>();
        isDataLoaded = false;
        SQLiteDatabase db = SQLiteDatabase
                .openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        Cursor res = db.rawQuery("select * from " + "population", null);
        while (res.moveToNext()) {
            int finId = res.getInt(0);
            int job = Integer.parseInt(res.getString(3));
            if (job == 11) {
                finIds.add(finId);
            }
        }
        if (finIds != null) {
            isDataLoaded = true;
        }
        return finIds;
    }

    public ArrayList<Double> getFinCoefs() {
        ArrayList finCoefs = new ArrayList<Double>();
        isDataLoaded = false;
        SQLiteDatabase db = SQLiteDatabase
                .openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        Cursor res = db.rawQuery("select * from " + "population", null);
        while (res.moveToNext()) {
            double finCoef = res.getDouble(18);
            int job = Integer.parseInt(res.getString(3));
            if (job == 11) {
                finCoefs.add(finCoef);
            }
        }
        if (finCoefs != null) {
            isDataLoaded = true;
        }
        return finCoefs;
    }

    public ArrayList<Integer> getFinMonthsWorked() {
        ArrayList finMonthsWorked = new ArrayList<Integer>();
        isDataLoaded = false;
        SQLiteDatabase db = SQLiteDatabase
                .openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        Cursor res = db.rawQuery("select * from " + "population", null);
        while (res.moveToNext()) {
            int monthsWorked = res.getInt(17);
            int job = Integer.parseInt(res.getString(3));
            if (job == 11) {
                finMonthsWorked.add(monthsWorked);
            }
        }
        if (finMonthsWorked != null) {
            isDataLoaded = true;
        }
        return finMonthsWorked;
    }
}

