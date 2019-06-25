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
                            int id = res.getInt(0);
                            String name = res.getString(1);
                            String type = res.getString(2);
                            int amount = res.getInt(3);
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
                            int id = Integer.parseInt(res.getString(0));
                            String name = res.getString(1);
                            String crop = res.getString(2);
                            int status = Integer.parseInt(res.getString(3));
                            int farmerId = Integer.parseInt(res.getString(4));
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
                            int id = res.getInt(0);
                            String name = res.getString(1);
                            int amount = res.getInt(2);
                            int price = res.getInt(3);
                            String currency = res.getString(4);
                            String type = res.getString(5);
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
                            int id = Integer.parseInt(res.getString(0));
                            double finCoef = res.getDouble(18);
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
                            double finCoef = res.getDouble(18);
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
                            int finId = res.getInt(0);
                            int job = Integer.parseInt(res.getString(3));
                            if (job == 11) {
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
                            double finCoef = res.getDouble(18);
                            int job = Integer.parseInt(res.getString(3));
                            if (job == 11) {
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
                            int monthsWorked = res.getInt(17);
                            int job = Integer.parseInt(res.getString(3));
                            if (job == 11) {
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

