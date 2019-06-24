package org.anastdronina.gyperborea;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class DbThread extends Thread {

    private static DbThread mInstance;
    private static Boolean isDataLoaded;
    private static DbListener dbListener;
    private static SQLiteDatabase db;

    private DbThread(Context context) {

    }

    public static void init(Context context) {
        if (mInstance == null) {
            mInstance = new DbThread(context);
            mInstance.start();
        }
    }

    @Override
    public void run() {
        super.run();
    }

    public static DbThread getInstance() {
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
        db = SQLiteDatabase.openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        db.execSQL(sqlQuery);
    }

    public ArrayList<Person> loadAllPeopleData() {
        isDataLoaded = false;
        db = SQLiteDatabase.openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
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
        db = SQLiteDatabase.openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);

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
        db = SQLiteDatabase.openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);

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
        db = SQLiteDatabase.openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);

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
        db = SQLiteDatabase.openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);

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
        db = SQLiteDatabase.openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
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
        db = SQLiteDatabase.openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
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
        db = SQLiteDatabase.openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        ArrayList finIds = new ArrayList<Integer>();
        isDataLoaded = false;
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
        db = SQLiteDatabase.openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        ArrayList finCoefs = new ArrayList<Double>();
        isDataLoaded = false;
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

    public void insertData(DatabaseHelper myDb) {
        //filling population table in db
        myDb.insertPeopleData("Дмитрий", "Аксенов", 0, 0, 21, 0, 0, 0, 1, 0,
                0, 1, 0, "Жаворонок", "Торопыжка", "Некультурный", 0, 0.0);
        myDb.insertPeopleData("Денис", "Сергеев", 0, 0, 53, 0, 1, 0, 0, 1,
                0, 0, 0, "Способный ученик", "Технарь", "Бездонный желудок", 0, 0.0);
        myDb.insertPeopleData("Степан", "Одинцов", 0, 0, 28, 0, 0, 0, 0, 1,
                1, 0, 0, "Декоратор", "Гурман", "Брезгливый", 0, 0.0);
        myDb.insertPeopleData("Вадим", "Дьячков", 0, 0, 34, 0, 0, 1, 0, 0,
                1, 0, 0, "Веган", "Болтун", "Громкий храп", 0, 0.0);
        myDb.insertPeopleData("Владислав", "Кошелев", 0, 0, 30, 1, 0, 1, 0, 0,
                0, 0, 0, "Способный ученик", "Декоратор", "Пацифист", 0, 0.0);
        myDb.insertPeopleData("Святослав", "Гуляев", 0, 0, 25, 0, 1, 0, 0, 0,
                0, 1, 0, "Анемия", "Гурман", "Неконструктивный", 0, 0.0);
        myDb.insertPeopleData("Артём", "Тетерин", 0, 0, 28, 0, 0, 0, 1, 0,
                1, 0, 0, "Руки из лапши", "Бездонный желудок", "Жаворонок", 0, 0.0);
        myDb.insertPeopleData("Эдуард", "Лыткин", 0, 0, 18, 0, 0, 0, 0, 1,
                0, 0, 1, "Буйвол", "Декоратор", "Нарколепсия", 0, 0.0);
        myDb.insertPeopleData("Иннокентий", "Харитонов", 0, 0, 34, 0, 1, 1, 0, 0,
                0, 0, 0, "Медленная обучаемость", "Брезгливый", "Торопыжка", 0, 0.0);
        myDb.insertPeopleData("Илья", "Некрасов", 0, 0, 23, 1, 0, 0, 0, 0,
                1, 0, 1, "Трипофобия", "Технарь", "Декоратор", 0, 0.0);

        //filling tecnologies table in db
        myDb.insertTecnologiesData("tec1", "desc1", 3, 300, 0);
        myDb.insertTecnologiesData("tec2", "desc2", 10, 1000, 1);
        myDb.insertTecnologiesData("tec3", "desc3", 5, 500, 0);
        myDb.insertTecnologiesData("tec4", "desc4", 8, 800, 0);
        myDb.insertTecnologiesData("tec5", "desc5", 12, 1200, 0);
        myDb.insertTecnologiesData("tec1.1", "desc1.1", 2, 200, 0);
        myDb.insertTecnologiesData("tec2.1", "desc2.1", 7, 700, 0);
        myDb.insertTecnologiesData("tec3.1", "desc3.1", 9, 900, 0);
        myDb.insertTecnologiesData("tec4.1", "desc4.1", 4, 400, 0);
        myDb.insertTecnologiesData("tec5.1", "desc5.1", 18, 1800, 0);

        myDb.insertStockData("Самосвал", "Транспорт", 2);
        myDb.insertStockData("Огурцы", "Еда", 10);
        myDb.insertStockData("Ресурс1", "Ресурсы", 2);
        myDb.insertStockData("Лук", "Еда", 30);
        myDb.insertStockData("Ресурс2", "Ресурсы", 10);
        myDb.insertStockData("Помидоры", "Еда", 10);
        myDb.insertStockData("Оборудование3", "Оборудование", 2);
        myDb.insertStockData("Ресурс3", "Ресурсы", 6);
        myDb.insertStockData("Оборудование2", "Оборудование", 7);
        myDb.insertStockData("Легковой автомобиль", "Транспорт", 5);
        myDb.insertStockData("Оборудование1", "Оборудование", 10);
        myDb.insertStockData("Трактор", "Транспорт", 3);

        myDb.insertFarmsData("Теплица 1", "Не выбрано", 0, 0);
        myDb.insertFarmsData("Теплица 2", "Не выбрано", 0, 0);
        myDb.insertFarmsData("Теплица 3", "Не выбрано", 0, 0);
        myDb.insertFarmsData("Теплица 4", "Не выбрано", 0, 0);
        myDb.insertFarmsData("Теплица 5", "Не выбрано", 0, 0);

        myDb.insertMarketData("Картофель", 10, 300, "руб", "Еда");
        myDb.insertMarketData("Оборудование42", 5, 50000, "руб", "Оборудование");
        myDb.insertMarketData("Бетономешалка", 1, 50000, "$", "Транспорт");
    }

    public ArrayList<Integer> getFinMonthsWorked() {
        db = SQLiteDatabase.openDatabase("/data/data/org.anastdronina.gyperborea/databases/hyperborea.db", null, 0, null);
        ArrayList finMonthsWorked = new ArrayList<Integer>();
        isDataLoaded = false;
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

