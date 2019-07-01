package org.anastdronina.gyperborea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Popup extends AppCompatActivity implements View.OnClickListener {
    private EditText editCountryName;
    private EditText editPresidentName;
    private String presidentName = "";
    private String countryName = "";

    private Button popupApply, popupBack;
    private RadioGroup levels;
    private RadioButton chekedRadioButton;
    private RadioButton easyLevel, middleLevel, hardLevel;
    private SharedPreferences allSettings;
    private Bundle innerBundle;
    private DbThread.DbListener listener;
    Handler handler, innerHandler;
    Message message, innerMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        popupApply = findViewById(R.id.popupApply);
        popupBack = findViewById(R.id.popupBack);
        levels = findViewById(R.id.levels);
        easyLevel = findViewById(R.id.easyLevel);
        middleLevel = findViewById(R.id.middleLevel);
        hardLevel = findViewById(R.id.hardLevel);
        editCountryName = findViewById(R.id.editCountryName);
        editPresidentName = findViewById(R.id.editPresidentName);

        popupApply.setOnClickListener(this);
        popupBack.setOnClickListener(this);

        handler = new Handler();
        message = handler.obtainMessage(DbThread.CREATE_DATABASE);
        DbThread.getBackgroundHandler().sendMessage(message);

        listener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                //inserting data in db
                innerHandler = new Handler();
                innerBundle = new Bundle();

                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Дмитрий", "Аксенов", "Жаворонок", "Торопыжка", "Некультурный")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0, 21, 0, 0, 0, 1, 0, 0, 1, 0)));
                innerBundle.putInt("finMonthsWorked", 0);
                innerBundle.putDouble("finCoef", 0.0);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_POPULATION_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Денис", "Сергеев", "Способный ученик", "Технарь", "Бездонный желудок")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0, 53, 0, 1, 0, 0, 1, 0, 0, 0)));
                innerBundle.putInt("finMonthsWorked", 0);
                innerBundle.putDouble("finCoef", 0.0);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_POPULATION_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Степан", "Одинцов", "Декоратор", "Гурман", "Брезгливый")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0, 28, 0, 0, 0, 0, 1, 1, 0, 0)));
                innerBundle.putInt("finMonthsWorked", 0);
                innerBundle.putDouble("finCoef", 0.0);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_POPULATION_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Вадим", "Дьячков", "Веган", "Болтун", "Громкий храп")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0, 34, 0, 0, 1, 0, 0, 1, 0, 0)));
                innerBundle.putInt("finMonthsWorked", 0);
                innerBundle.putDouble("finCoef", 0.0);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_POPULATION_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Владислав", "Кошелев", "Способный ученик", "Декоратор", "Пацифист")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0, 30, 1, 0, 1, 0, 0, 0, 0, 0)));
                innerBundle.putInt("finMonthsWorked", 0);
                innerBundle.putDouble("finCoef", 0.0);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_POPULATION_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Святослав", "Гуляев", "Анемия", "Гурман", "Неконструктивный")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0, 25, 0, 1, 0, 0, 0, 0, 1, 0)));
                innerBundle.putInt("finMonthsWorked", 0);
                innerBundle.putDouble("finCoef", 0.0);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_POPULATION_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Артём", "Тетерин", "Руки из лапши", "Бездонный желудок", "Жаворонок")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0, 28, 0, 0, 0, 1, 0, 1, 0, 0)));
                innerBundle.putInt("finMonthsWorked", 0);
                innerBundle.putDouble("finCoef", 0.0);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_POPULATION_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Эдуард", "Лыткин", "Буйвол", "Декоратор", "Нарколепсия")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0, 18, 0, 0, 0, 0, 1, 0, 0, 1)));
                innerBundle.putInt("finMonthsWorked", 0);
                innerBundle.putDouble("finCoef", 0.0);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_POPULATION_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Иннокентий", "Харитонов", "Медленная обучаемость", "Брезгливый", "Торопыжка")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0, 34, 0, 1, 1, 0, 0, 0, 0, 0)));
                innerBundle.putInt("finMonthsWorked", 0);
                innerBundle.putDouble("finCoef", 0.0);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_POPULATION_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Илья", "Некрасов", "Трипофобия", "Технарь", "Декоратор")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0, 23, 1, 0, 0, 0, 0, 1, 0, 1)));
                innerBundle.putInt("finMonthsWorked", 0);
                innerBundle.putDouble("finCoef", 0.0);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_POPULATION_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("tec1", "desc1")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(3, 300, 0)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_TECH_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("tec2", "desc2")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(10, 1000, 1)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_TECH_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("tec3", "desc3")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(5, 500, 0)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_TECH_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("tec4", "desc4")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(8, 800, 0)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_TECH_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("tec5", "desc5")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(12, 1200, 0)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_TECH_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("tec1.1", "desc1.1")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(2, 200, 0)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_TECH_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("tec2.1", "desc2.1")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(7, 700, 0)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_TECH_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("tec3.1", "desc3.1")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(9, 900, 0)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_TECH_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("tec4.1", "desc4.1")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(4, 400, 0)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_TECH_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("tec5.1", "desc5.1")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(18, 1800, 0)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_TECH_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putString("productName", "Самосвал");
                innerBundle.putString("productType", "Транспорт");
                innerBundle.putInt("productAmount", 2);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_STOCK_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putString("productName", "Огурцы");
                innerBundle.putString("productType", "Еда");
                innerBundle.putInt("productAmount", 10);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_STOCK_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putString("productName", "Ресурс1");
                innerBundle.putString("productType", "Ресурсы");
                innerBundle.putInt("productAmount", 2);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_STOCK_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putString("productName", "Лук");
                innerBundle.putString("productType", "Еда");
                innerBundle.putInt("productAmount", 30);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_STOCK_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putString("productName", "Ресурс2");
                innerBundle.putString("productType", "Ресурсы");
                innerBundle.putInt("productAmount", 10);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_STOCK_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putString("productName", "Помидоры");
                innerBundle.putString("productType", "Еда");
                innerBundle.putInt("productAmount", 10);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_STOCK_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putString("productName", "Помидоры");
                innerBundle.putString("productType", "Еда");
                innerBundle.putInt("productAmount", 10);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_STOCK_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putString("productName", "Оборудование3");
                innerBundle.putString("productType", "Оборудование");
                innerBundle.putInt("productAmount", 2);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_STOCK_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putString("productName", "Ресурс3");
                innerBundle.putString("productType", "Ресурсы");
                innerBundle.putInt("productAmount", 6);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_STOCK_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putString("productName", "Оборудование2");
                innerBundle.putString("productType", "Оборудование");
                innerBundle.putInt("productAmount", 7);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_STOCK_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putString("productName", "Легковой автомобиль");
                innerBundle.putString("productType", "Транспорт");
                innerBundle.putInt("productAmount", 5);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_STOCK_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putString("productName", "Оборудование1");
                innerBundle.putString("productType", "Оборудование");
                innerBundle.putInt("productAmount", 10);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_STOCK_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putString("productName", "Трактор");
                innerBundle.putString("productType", "Транспорт");
                innerBundle.putInt("productAmount", 3);
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_STOCK_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Теплица 1", "Не выбрано")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_FARMS_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Теплица 2", "Не выбрано")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_FARMS_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Теплица 3", "Не выбрано")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_FARMS_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Теплица 4", "Не выбрано")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_FARMS_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Теплица 5", "Не выбрано")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(0, 0)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_FARMS_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Картофель", "руб", "Еда")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(10, 300)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_MARKET_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Оборудование42", "руб", "Оборудование")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(5, 50000)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_MARKET_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);

                innerBundle = new Bundle();
                innerBundle.putStringArrayList("stringValues", new ArrayList<String>(Arrays.asList("Бетономешалка", "$", "Транспорт")));
                innerBundle.putIntegerArrayList("intValues", new ArrayList(Arrays.asList(1, 50000)));
                innerMessage = innerHandler.obtainMessage(DbThread.INSERT_MARKET_DATA);
                innerMessage.setData(innerBundle);
                DbThread.getBackgroundHandler().sendMessage(innerMessage);


            }
        };
        DbThread.getInstance().addListener(listener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!allSettings.getBoolean("NEW_GAME", true)) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        DbThread.getInstance().removeListener(listener);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.popupApply:
                countryName = editCountryName.getText().toString();
                presidentName = editPresidentName.getText().toString();

                if (countryName.length() > 0 && presidentName.length() > 0) {
                    SharedPreferences allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
                    allSettings.edit().putBoolean("NEW_GAME", false).apply();
                    allSettings.edit().putString("COUNTRY_NAME", countryName).apply();
                    allSettings.edit().putString("PRESIDENT_NAME", presidentName).apply();

                    int checkedId = levels.getCheckedRadioButtonId();
                    chekedRadioButton = (RadioButton) findViewById(checkedId);
                    if (chekedRadioButton.equals(easyLevel)) {
                        allSettings.edit().putString("LEVEL", "Easy").apply();
                        allSettings.edit().putLong("MONEY_DOLLARS", 300000).apply();
                        allSettings.edit().putLong("MONEY_RUBLES", 30000000).apply();
                    }
                    if (chekedRadioButton.equals(hardLevel)) {
                        allSettings.edit().putString("LEVEL", "Hard").apply();
                        allSettings.edit().putLong("MONEY_DOLLARS", 100000).apply();
                        allSettings.edit().putLong("MONEY_RUBLES", 10000000).apply();
                    }
                    if (chekedRadioButton.equals(middleLevel)) {
                        allSettings.edit().putString("LEVEL", "Middle");
                        allSettings.edit().putLong("MONEY_DOLLARS", 200000).apply();
                        allSettings.edit().putLong("MONEY_RUBLES", 20000000).apply();
                    }

                    intent = new Intent(this, NewGame.class);
                } else {
                    Toast.makeText(this,
                            "Введите название государства и имя правителя. ", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.popupBack:
                intent = new Intent(this, MainActivity.class);
                break;
            default:
        }
        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
