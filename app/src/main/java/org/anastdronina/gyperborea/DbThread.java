package org.anastdronina.gyperborea;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.Arrays;

public class DbThread extends Thread {

    private static DbThread sInstance;
    private static DbListener sListener;
    private static SQLiteDatabase sDb;
    private static Handler sBackgoundHandler;
    private static Message sUiMessage;
    private static Bundle sBundle;
    private static ContentValues sContentValues;

    public static final int PERFORM_SQL_QUERY = 0;
    public static final int LOAD_POPULATION_DATA = 1;
    public static final int LOAD_TECH_DATA = 2;
    public static final int LOAD_STOCK_DATA = 3;
    public static final int LOAD_FARMS_DATA = 4;
    public static final int LOAD_MARKET_DATA = 5;
    public static final int PRINT_COEF_ASYNC = 6;
    public static final int SET_COEF_IN_FINANCES_ACTIVITY = 7;

    public static final int COUNT_INFO_FOR_NEXT_TURN = 11;
    public static final int CREATE_DATABASE = 12;
    public static final int INSERT_STOCK_DATA = 13;
    public static final int INSERT_POPULATION_DATA = 14;
    public static final int INSERT_TECH_DATA = 15;
    public static final int INSERT_FARMS_DATA = 16;
    public static final int INSERT_MARKET_DATA = 17;

    private DbThread(Context context) {
    }

    public static void init(Context context) {
        if (sInstance == null) {
            sInstance = new DbThread(context);
            sInstance.start();
        }
    }

    public static DbThread getInstance() {
        return sInstance;
    }

    public interface DbListener {
        void onDataLoaded(Bundle bundle);
    }

    public void addListener(DbListener listener) {
        this.sListener = listener;
    }

    public void removeListener(DbListener listener) {
        sListener = null;
    }

    public static Handler getBackgroundHandler() {
        return sBackgoundHandler;
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        sBackgoundHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                sDb = Hyperborea.getAppContext().openOrCreateDatabase("hyperborea.db", Context.MODE_PRIVATE, null);
                Cursor res;
                sBundle = new Bundle();
                switch (msg.what) {
                    case PERFORM_SQL_QUERY:
                        sDb.execSQL(msg.getData().getString("query"));
                        break;
                    case LOAD_POPULATION_DATA:
                        res = sDb.rawQuery("select * from " + "population", null);
                        ArrayList<Person> allPopulation = new ArrayList<>();
                        while (res.moveToNext()) {
                            int id = Integer.parseInt(res.getString(res.getColumnIndex("ID")));
                            String name = res.getString(res.getColumnIndex("NAME"));
                            String surname = res.getString(res.getColumnIndex("SURNAME"));
                            int job = Integer.parseInt(res.getString(res.getColumnIndex("JOB")));
                            int salary = Integer.parseInt(res.getString(res.getColumnIndex("SALARY")));
                            int age = Integer.parseInt(res.getString(res.getColumnIndex("AGE")));
                            int building = Integer.parseInt(res.getString(res.getColumnIndex("BUILDING")));
                            int manufacture = Integer.parseInt(res.getString(res.getColumnIndex("MANUFACTURE")));
                            int farm = Integer.parseInt(res.getString(res.getColumnIndex("FARM")));
                            int athletic = Integer.parseInt(res.getString(res.getColumnIndex("ATHLETIC")));
                            int learning = Integer.parseInt(res.getString(res.getColumnIndex("LEARNING")));
                            int talking = Integer.parseInt(res.getString(res.getColumnIndex("TALKING")));
                            int strength = Integer.parseInt(res.getString(res.getColumnIndex("STRENGTH")));
                            int art = Integer.parseInt(res.getString(res.getColumnIndex("ART")));
                            String trait1 = res.getString(res.getColumnIndex("TRAIT1"));
                            String trait2 = res.getString(res.getColumnIndex("TRAIT2"));
                            String trait3 = res.getString(res.getColumnIndex("TRAIT3"));

                            allPopulation.add(new Person(id, name, surname, job, salary, age, building,
                                    manufacture, farm, athletic, learning, talking, strength, art,
                                    new ArrayList<>(Arrays.asList(trait1, trait2, trait3))));
                        }
                        res.close();
                        sBundle.putParcelableArrayList("allPopulation", allPopulation);
                        sUiMessage = uiHandler.obtainMessage(1);
                        sUiMessage.setData(sBundle);
                        uiHandler.sendMessage(sUiMessage);
                        break;
                    case LOAD_TECH_DATA:
                        res = sDb.rawQuery("select * from " + "tecnologies", null);
                        ArrayList<Technology> techs = new ArrayList<>();
                        while (res.moveToNext()) {
                            int id = res.getInt(res.getColumnIndex("TEC_ID"));
                            String name = res.getString(res.getColumnIndex("TEC_NAME"));
                            String description = res.getString(res.getColumnIndex("TEC_DESCRIPTION"));
                            int monthsToLearn = res.getInt(res.getColumnIndex("TEC_MONTHS_TO_LEARN"));
                            int price = res.getInt(res.getColumnIndex("TEC_PRICE"));
                            int isLearnedInt = res.getInt(res.getColumnIndex("TEC_IS_LEARNED"));
                            boolean isLearned = false;
                            if (isLearnedInt == 1) {
                                isLearned = true;
                            }
                            techs.add(new Technology(id, name, description, monthsToLearn, price, isLearned));
                        }
                        res.close();
                        sBundle.putParcelableArrayList("techs", techs);
                        sUiMessage = uiHandler.obtainMessage(1);
                        sUiMessage.setData(sBundle);
                        uiHandler.sendMessage(sUiMessage);
                        break;
                    case LOAD_STOCK_DATA:
                        res = sDb.rawQuery("select * from " + "stock", null);
                        ArrayList<Product> products = new ArrayList<>();
                        while (res.moveToNext()) {
                            int id = res.getInt(res.getColumnIndex("PRODUCT_ID"));
                            String name = res.getString(res.getColumnIndex("PRODUCT_NAME"));
                            String type = res.getString(res.getColumnIndex("PRODUCT_TYPE"));
                            int amount = res.getInt(res.getColumnIndex("PRODUCT_AMOUNT"));
                            products.add(new Product(id, name, type, amount));
                        }
                        res.close();
                        sBundle.putParcelableArrayList("products", products);
                        sUiMessage = uiHandler.obtainMessage(1);
                        sUiMessage.setData(sBundle);
                        uiHandler.sendMessage(sUiMessage);
                        break;
                    case LOAD_FARMS_DATA:
                        res = sDb.rawQuery("select * from " + "farms", null);
                        ArrayList<Farm> farms = new ArrayList<>();
                        while (res.moveToNext()) {
                            int id = Integer.parseInt(res.getString(res.getColumnIndex("FARM_ID")));
                            String name = res.getString(res.getColumnIndex("FARM_NAME"));
                            String crop = res.getString(res.getColumnIndex("FARM_CROP"));
                            int status = Integer.parseInt(res.getString(res.getColumnIndex("FARM_STATUS")));
                            int farmerId = Integer.parseInt(res.getString(res.getColumnIndex("FARM_FARMER_ID")));
                            farms.add(new Farm(id, name, crop, status, farmerId));
                        }
                        res.close();
                        sBundle.putParcelableArrayList("farms", farms);
                        sUiMessage = uiHandler.obtainMessage(1);
                        sUiMessage.setData(sBundle);
                        uiHandler.sendMessage(sUiMessage);
                        break;
                    case LOAD_MARKET_DATA:
                        res = sDb.rawQuery("select * from " + "market", null);
                        ArrayList<MarketItem> marketItems = new ArrayList<>();
                        while (res.moveToNext()) {
                            int id = res.getInt(res.getColumnIndex("ITEM_ID"));
                            String name = res.getString(res.getColumnIndex("ITEM_NAME"));
                            int amount = res.getInt(res.getColumnIndex("ITEM_AMOUNT"));
                            int price = res.getInt(res.getColumnIndex("ITEM_PRICE"));
                            String currency = res.getString(res.getColumnIndex("ITEM_CURRENCY"));
                            String type = res.getString(res.getColumnIndex("ITEM_TYPE"));
                            marketItems.add(new MarketItem(id, name, amount, price, currency, type));
                        }
                        res.close();
                        sBundle.putParcelableArrayList("marketItems", marketItems);
                        sUiMessage = uiHandler.obtainMessage(1);
                        sUiMessage.setData(sBundle);
                        uiHandler.sendMessage(sUiMessage);
                        break;
                    case PRINT_COEF_ASYNC:
                        res = sDb.rawQuery("select * from " + "population", null);
                        String printCoef = "";
                        while (res.moveToNext()) {
                            int id = Integer.parseInt(res.getString(res.getColumnIndex("ID")));
                            double finCoef = res.getDouble(res.getColumnIndex("FIN_COEF"));
                            if (id == msg.arg1) { //arg1 - person id
                                printCoef = "\nКОЭФФИЦИЕНТ УЛУЧШЕНИЯ ФИНАНСОВ: " + finCoef;
                            }
                        }
                        res.close();
                        sBundle.putString("printCoef", printCoef);
                        sUiMessage = uiHandler.obtainMessage(1);
                        sUiMessage.setData(sBundle);
                        uiHandler.sendMessage(sUiMessage);
                        break;
                    case SET_COEF_IN_FINANCES_ACTIVITY:
                        res = sDb.rawQuery("select * from " + "population", null);
                        Double coef = 0.0;
                        while (res.moveToNext()) {
                            double finCoef = res.getDouble(res.getColumnIndex("FIN_COEF"));
                            coef += finCoef;
                        }
                        res.close();
                        coef = Math.round(coef * 10.0) / 10.0;
                        sBundle.putDouble("coef", coef);
                        sUiMessage = uiHandler.obtainMessage(1);
                        sUiMessage.setData(sBundle);
                        uiHandler.sendMessage(sUiMessage);
                        break;
                    case COUNT_INFO_FOR_NEXT_TURN:
                        res = sDb.rawQuery("select * from " + "population", null);
                        long salaries = 0;
                        ArrayList<Integer> financeIds = new ArrayList<>();
                        ArrayList<Double> financeCoefs = new ArrayList<>();
                        ArrayList<Integer> financeMonthsWorked = new ArrayList<>();
                        ArrayList<Farm> farmss = new ArrayList<>();
                        while (res.moveToNext()) {
                            int salary = Integer.parseInt(res.getString(res.getColumnIndex("SALARY")));
                            salaries = salaries + salary;

                            Integer finId = res.getInt(res.getColumnIndex("ID"));
                            double finCoef = res.getDouble(res.getColumnIndex("FIN_COEF"));
                            int job = Integer.parseInt(res.getString(res.getColumnIndex("JOB")));
                            int monthsWorked = res.getInt(res.getColumnIndex("FIN_MONTHS_WORKED"));
                            if (job == Person.FINANSIST) {
                                financeIds.add(finId);
                                financeCoefs.add(finCoef);
                                financeMonthsWorked.add(monthsWorked);
                            }
                        }
                        res.close();
                        sBundle.putLong("salaries", salaries);
                        sBundle.putIntegerArrayList("financeIds", financeIds);
                        sBundle.putSerializable("financeCoefs", financeCoefs);
                        sBundle.putIntegerArrayList("financeMonthsWorked", financeMonthsWorked);

                        res = sDb.rawQuery("select * from " + "farms", null);
                        while (res.moveToNext()) {
                            int id = Integer.parseInt(res.getString(res.getColumnIndex("FARM_ID")));
                            String name = res.getString(res.getColumnIndex("FARM_NAME"));
                            String crop = res.getString(res.getColumnIndex("FARM_CROP"));
                            int status = Integer.parseInt(res.getString(res.getColumnIndex("FARM_STATUS")));
                            int farmerId = Integer.parseInt(res.getString(res.getColumnIndex("FARM_FARMER_ID")));
                            farmss.add(new Farm(id, name, crop, status, farmerId));
                        }
                        res.close();
                        sBundle.putParcelableArrayList("farms", farmss);
                        sUiMessage = uiHandler.obtainMessage(1);
                        sUiMessage.setData(sBundle);
                        uiHandler.sendMessage(sUiMessage);
                        break;
                    case CREATE_DATABASE:
                        sDb.execSQL("create table " + "population" + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, SURNAME TEXT, " +
                                "JOB INTEGER, SALARY INTEGER, AGE INTEGER, BUILDING INTEGER, MANUFACTURE INTEGER, FARM INTEGER, " +
                                "ATHLETIC INTEGER, LEARNING INTEGER, TALKING INTEGER, STRENGTH INTEGER, ART INTEGER, TRAIT1 TEXT, " +
                                "TRAIT2 TEXT, TRAIT3 TEXT, FIN_MONTHS_WORKED INTEGER, FIN_COEF REAL)");
                        sDb.execSQL("create table " + "tecnologies" + " (TEC_ID INTEGER PRIMARY KEY AUTOINCREMENT, TEC_NAME TEXT, TEC_DESCRIPTION TEXT, " +
                                "TEC_MONTHS_TO_LEARN INTEGER, TEC_PRICE INTEGER, TEC_IS_LEARNED INTEGER)");
                        sDb.execSQL("create table " + "stock" + " (PRODUCT_ID INTEGER PRIMARY KEY AUTOINCREMENT, PRODUCT_NAME TEXT, " +
                                "PRODUCT_TYPE TEXT, PRODUCT_AMOUNT INTEGER)");
                        sDb.execSQL("create table " + "farms" + " (FARM_ID INTEGER PRIMARY KEY AUTOINCREMENT, FARM_NAME TEXT, " +
                                "FARM_CROP TEXT, FARM_STATUS INTEGER, FARM_FARMER_ID INTEGER)");
                        sDb.execSQL("create table " + "market" + " (ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT, ITEM_NAME TEXT, " +
                                "ITEM_AMOUNT INTEGER, ITEM_PRICE INTEGER, ITEM_CURRENCY TEXT, ITEM_TYPE TEXT)");
                        sUiMessage = uiHandler.obtainMessage(1);
                        uiHandler.sendMessage(sUiMessage);
                        break;
                    case INSERT_STOCK_DATA:
                        sContentValues = new ContentValues();
                        sContentValues.put("PRODUCT_NAME", msg.getData().getString("productName"));
                        sContentValues.put("PRODUCT_TYPE", msg.getData().getString("productType"));
                        sContentValues.put("PRODUCT_AMOUNT", msg.getData().getInt("productAmount"));
                        sDb.insert("stock", null, sContentValues);
                        break;
                    case INSERT_POPULATION_DATA:
                        sContentValues = new ContentValues();
                        sContentValues.put("NAME", msg.getData().getStringArrayList("stringValues").get(0));
                        sContentValues.put("SURNAME", msg.getData().getStringArrayList("stringValues").get(1));
                        sContentValues.put("JOB", msg.getData().getIntegerArrayList("intValues").get(0));
                        sContentValues.put("SALARY", msg.getData().getIntegerArrayList("intValues").get(1));
                        sContentValues.put("AGE", msg.getData().getIntegerArrayList("intValues").get(2));
                        sContentValues.put("BUILDING", msg.getData().getIntegerArrayList("intValues").get(3));
                        sContentValues.put("MANUFACTURE", msg.getData().getIntegerArrayList("intValues").get(4));
                        sContentValues.put("FARM", msg.getData().getIntegerArrayList("intValues").get(5));
                        sContentValues.put("ATHLETIC", msg.getData().getIntegerArrayList("intValues").get(6));
                        sContentValues.put("LEARNING", msg.getData().getIntegerArrayList("intValues").get(7));
                        sContentValues.put("TALKING", msg.getData().getIntegerArrayList("intValues").get(8));
                        sContentValues.put("STRENGTH", msg.getData().getIntegerArrayList("intValues").get(9));
                        sContentValues.put("ART", msg.getData().getIntegerArrayList("intValues").get(10));
                        sContentValues.put("TRAIT1", msg.getData().getStringArrayList("stringValues").get(2));
                        sContentValues.put("TRAIT2", msg.getData().getStringArrayList("stringValues").get(3));
                        sContentValues.put("TRAIT3", msg.getData().getStringArrayList("stringValues").get(4));
                        sContentValues.put("FIN_MONTHS_WORKED", msg.getData().getInt("finMonthsWorked"));
                        sContentValues.put("FIN_COEF", msg.getData().getDouble("finCoef"));
                        sDb.insert("population", null, sContentValues);
                        break;
                    case INSERT_TECH_DATA:
                        sContentValues = new ContentValues();
                        sContentValues.put("TEC_NAME", msg.getData().getStringArrayList("stringValues").get(0));
                        sContentValues.put("TEC_DESCRIPTION", msg.getData().getStringArrayList("stringValues").get(1));
                        sContentValues.put("TEC_MONTHS_TO_LEARN", msg.getData().getIntegerArrayList("intValues").get(0));
                        sContentValues.put("TEC_PRICE", msg.getData().getIntegerArrayList("intValues").get(1));
                        sContentValues.put("TEC_IS_LEARNED", msg.getData().getIntegerArrayList("intValues").get(2));
                        sDb.insert("tecnologies", null, sContentValues);
                        break;
                    case INSERT_FARMS_DATA:
                        sContentValues = new ContentValues();
                        sContentValues.put("FARM_NAME", msg.getData().getStringArrayList("stringValues").get(0));
                        sContentValues.put("FARM_CROP", msg.getData().getStringArrayList("stringValues").get(1));
                        sContentValues.put("FARM_STATUS", msg.getData().getIntegerArrayList("intValues").get(0));
                        sContentValues.put("FARM_FARMER_ID", msg.getData().getIntegerArrayList("intValues").get(1));
                        sDb.insert("farms", null, sContentValues);
                        break;
                    case INSERT_MARKET_DATA:
                        sContentValues = new ContentValues();
                        sContentValues.put("ITEM_NAME", msg.getData().getStringArrayList("stringValues").get(0));
                        sContentValues.put("ITEM_AMOUNT", msg.getData().getIntegerArrayList("intValues").get(0));
                        sContentValues.put("ITEM_PRICE", msg.getData().getIntegerArrayList("intValues").get(1));
                        sContentValues.put("ITEM_CURRENCY", msg.getData().getStringArrayList("stringValues").get(1));
                        sContentValues.put("ITEM_TYPE", msg.getData().getStringArrayList("stringValues").get(2));
                        sDb.insert("market", null, sContentValues);
                        break;
                    default:
                }
            }
        };
        Looper.loop();
    }

    public Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) { //data successfully loaded
                sListener.onDataLoaded(msg.getData());
            }
        }
    };
}

