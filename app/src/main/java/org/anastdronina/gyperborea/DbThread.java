package org.anastdronina.gyperborea;

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

    private static DbThread mInstance;
    private static DbListener dbListener;
    private static SQLiteDatabase db;
    private static Handler backgroundHandler;
    private static Message uiMessage;
    private static Bundle bundle;

    private DbThread(Context context) {
    }

    public static void init(Context context) {
        if (mInstance == null) {
            mInstance = new DbThread(context);
            mInstance.start();
        }
    }

    public static DbThread getInstance() {
        return mInstance;
    }

    public interface DbListener {
        void onDataLoaded(Bundle bundle);
    }

    public void addListener(DbListener listener) {
        this.dbListener = listener;
    }

    public void removeListener(DbListener listener) {
        dbListener = null;
    }

    public static Handler getBackgroundHandler() {
        return backgroundHandler;
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        backgroundHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                db = SQLiteDatabase.openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
                Cursor res;
                bundle = new Bundle();

                switch (msg.what) {
                    case 0: //perform sql query
                        db.execSQL(msg.getData().getString("query"));
                        break;
                    case 1: //load popultion data
                        res = db.rawQuery("select * from " + "population", null);
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

                            allPopulation.add(new Person(id, name, surname, job, salary, age, building, manufacture, farm, athletic, learning, talking, strength, art,
                                    new ArrayList<>(Arrays.asList(trait1, trait2, trait3))));
                        }
                        bundle.putParcelableArrayList("allPopulation", allPopulation);
                        uiMessage = uiHandler.obtainMessage(1);
                        uiMessage.setData(bundle);
                        uiHandler.sendMessage(uiMessage);
                        break;
                    case 2: //load tech data
                        res = db.rawQuery("select * from " + "tecnologies", null);
                        ArrayList<Tecnology> techs = new ArrayList<>();
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
                            techs.add(new Tecnology(id, name, description, monthsToLearn, price, isLearned));
                        }
                        bundle.putParcelableArrayList("techs", techs);
                        uiMessage = uiHandler.obtainMessage(1);
                        uiMessage.setData(bundle);
                        uiHandler.sendMessage(uiMessage);
                        break;
                    case 3: //load stock data
                        res = db.rawQuery("select * from " + "stock", null);
                        ArrayList<Product> products = new ArrayList<>();
                        while (res.moveToNext()) {
                            int id = res.getInt(res.getColumnIndex("PRODUCT_ID"));
                            String name = res.getString(res.getColumnIndex("PRODUCT_NAME"));
                            String type = res.getString(res.getColumnIndex("PRODUCT_TYPE"));
                            int amount = res.getInt(res.getColumnIndex("PRODUCT_AMOUNT"));
                            products.add(new Product(id, name, type, amount));
                        }
                        bundle.putParcelableArrayList("products", products);
                        uiMessage = uiHandler.obtainMessage(1);
                        uiMessage.setData(bundle);
                        uiHandler.sendMessage(uiMessage);
                        break;
                    case 4: //load farms data
                        res = db.rawQuery("select * from " + "farms", null);
                        ArrayList<Farm> farms = new ArrayList<>();
                        while (res.moveToNext()) {
                            int id = Integer.parseInt(res.getString(res.getColumnIndex("FARM_ID")));
                            String name = res.getString(res.getColumnIndex("FARM_NAME"));
                            String crop = res.getString(res.getColumnIndex("FARM_CROP"));
                            int status = Integer.parseInt(res.getString(res.getColumnIndex("FARM_STATUS")));
                            int farmerId = Integer.parseInt(res.getString(res.getColumnIndex("FARM_FARMER_ID")));
                            farms.add(new Farm(id, name, crop, status, farmerId));
                        }
                        bundle.putParcelableArrayList("farms", farms);
                        uiMessage = uiHandler.obtainMessage(1);
                        uiMessage.setData(bundle);
                        uiHandler.sendMessage(uiMessage);
                        break;
                    case 5: //load market data
                        res = db.rawQuery("select * from " + "market", null);
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
                        bundle.putParcelableArrayList("marketItems", marketItems);
                        uiMessage = uiHandler.obtainMessage(1);
                        uiMessage.setData(bundle);
                        uiHandler.sendMessage(uiMessage);
                        break;
                    case 6: //printCoefAsync
                        res = db.rawQuery("select * from " + "population", null);
                        String printCoef = "";
                        while (res.moveToNext()) {
                            int id = Integer.parseInt(res.getString(res.getColumnIndex("ID")));
                            double finCoef = res.getDouble(res.getColumnIndex("FIN_COEF"));
                            if (id == msg.arg1) { //arg1 - person id
                                printCoef = "\nКОЭФФИЦИЕНТ УЛУЧШЕНИЯ ФИНАНСОВ: " + finCoef;
                            }
                        }
                        bundle.putString("printCoef", printCoef);
                        uiMessage = uiHandler.obtainMessage(1);
                        uiMessage.setData(bundle);
                        uiHandler.sendMessage(uiMessage);
                        break;
                    case 7: //setCoefInFinancesActivity
                        res = db.rawQuery("select * from " + "population", null);
                        Double coef = 0.0;
                        while (res.moveToNext()) {
                            double finCoef = res.getDouble(res.getColumnIndex("FIN_COEF"));
                            coef += finCoef;
                        }
                        coef = Math.round(coef * 10.0) / 10.0;
                        bundle.putDouble("coef", coef);
                        uiMessage = uiHandler.obtainMessage(1);
                        uiMessage.setData(bundle);
                        uiHandler.sendMessage(uiMessage);
                        break;
                    case 8: //getFinIds
                        res = db.rawQuery("select * from " + "population", null);
                        ArrayList finIds = new ArrayList<Integer>();
                        while (res.moveToNext()) {
                            Integer finId = res.getInt(res.getColumnIndex("ID"));
                            int job = Integer.parseInt(res.getString(res.getColumnIndex("JOB")));
                            if (job == Person.FINANSIST) {
                                finIds.add(finId);
                            }
                        }
                        bundle.putIntegerArrayList("finIds", finIds);
                        uiMessage = uiHandler.obtainMessage(1);
                        uiMessage.setData(bundle);
                        uiHandler.sendMessage(uiMessage);
                        break;
                    case 9: //getFinCoefs
                        res = db.rawQuery("select * from " + "population", null);
                        ArrayList<Double> finCoefs = new ArrayList<>();
                        while (res.moveToNext()) {
                            double finCoef = res.getDouble(res.getColumnIndex("FIN_COEF"));
                            int job = Integer.parseInt(res.getString(res.getColumnIndex("JOB")));
                            if (job == Person.FINANSIST) {
                                finCoefs.add(finCoef);
                            }
                        }
                        bundle.putSerializable("finCoefs", finCoefs);
                        uiMessage = uiHandler.obtainMessage(1);
                        uiMessage.setData(bundle);
                        uiHandler.sendMessage(uiMessage);
                        break;
                    case 10: //getFinMonthsWorked
                        res = db.rawQuery("select * from " + "population", null);
                        ArrayList finMonthsWorked = new ArrayList<Integer>();
                        while (res.moveToNext()) {
                            int monthsWorked = res.getInt(res.getColumnIndex("FIN_MONTHS_WORKED"));
                            int job = Integer.parseInt(res.getString(res.getColumnIndex("JOB")));
                            if (job == Person.FINANSIST) {
                                finMonthsWorked.add(monthsWorked);
                            }
                        }
                        bundle.putIntegerArrayList("finMonthsWorked", finMonthsWorked);
                        uiMessage = uiHandler.obtainMessage(1);
                        uiMessage.setData(bundle);
                        uiHandler.sendMessage(uiMessage);
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
                dbListener.onDataLoaded(msg.getData());
            }
        }
    };
}

