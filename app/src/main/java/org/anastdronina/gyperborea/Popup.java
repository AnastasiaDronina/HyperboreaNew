package org.anastdronina.gyperborea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Popup extends AppCompatActivity implements View.OnClickListener {
    private EditText editCountryName;
    private EditText editPresidentName;

    private Button popupApply, popupBack;
    private RadioGroup levels;
    private RadioButton chekedRadioButton;
    private RadioButton easyLevel, middleLevel, hardLevel;
    private SharedPreferences allSettings;
    private DbThread.DbListener listener;
    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        dbManager = new DbManager();
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

        dbManager.createNewDatabase();

        listener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                //inserting data in db
                dbManager.insertPopulationData("Дмитрий", "Аксенов", 0, 0, 21, 0, 0, 0, 1, 0,
                        0, 1, 0, "Жаворонок", "Торопыжка", "Некультурный", 0, 0.0);
                dbManager.insertPopulationData("Денис", "Сергеев", 0, 0, 53, 0, 1, 0, 0, 1,
                        0, 0, 0, "Способный ученик", "Технарь", "Бездонный желудок", 0, 0.0);
                dbManager.insertPopulationData("Степан", "Одинцов", 0, 0, 28, 0, 0, 0, 0, 1,
                        1, 0, 0, "Декоратор", "Гурман", "Брезгливый", 0, 0.0);
                dbManager.insertPopulationData("Вадим", "Дьячков", 0, 0, 34, 0, 0, 1, 0, 0,
                        1, 0, 0, "Веган", "Болтун", "Громкий храп", 0, 0.0);
                dbManager.insertPopulationData("Владислав", "Кошелев", 0, 0, 30, 1, 0, 1, 0, 0,
                        0, 0, 0, "Способный ученик", "Декоратор", "Пацифист", 0, 0.0);
                dbManager.insertPopulationData("Святослав", "Гуляев", 0, 0, 25, 0, 1, 0, 0, 0,
                        0, 1, 0, "Анемия", "Гурман", "Неконструктивный", 0, 0.0);
                dbManager.insertPopulationData("Артём", "Тетерин", 0, 0, 28, 0, 0, 0, 1, 0,
                        1, 0, 0, "Руки из лапши", "Бездонный желудок", "Жаворонок", 0, 0.0);
                dbManager.insertPopulationData("Эдуард", "Лыткин", 0, 0, 18, 0, 0, 0, 0, 1,
                        0, 0, 1, "Буйвол", "Декоратор", "Нарколепсия", 0, 0.0);
                dbManager.insertPopulationData("Иннокентий", "Харитонов", 0, 0, 34, 0, 1, 1, 0, 0,
                        0, 0, 0, "Медленная обучаемость", "Брезгливый", "Торопыжка", 0, 0.0);
                dbManager.insertPopulationData("Илья", "Некрасов", 0, 0, 23, 1, 0, 0, 0, 0,
                        1, 0, 1, "Трипофобия", "Технарь", "Декоратор", 0, 0.0);

                dbManager.insertTechData("tec1", "desc1", 3, 300, 0);
                dbManager.insertTechData("tec2", "desc2", 10, 1000, 1);
                dbManager.insertTechData("tec3", "desc3", 5, 500, 0);
                dbManager.insertTechData("tec4", "desc4", 8, 800, 0);
                dbManager.insertTechData("tec5", "desc5", 12, 1200, 0);
                dbManager.insertTechData("tec1.1", "desc1.1", 2, 200, 0);
                dbManager.insertTechData("tec2.1", "desc2.1", 7, 700, 0);
                dbManager.insertTechData("tec3.1", "desc3.1", 9, 900, 0);
                dbManager.insertTechData("tec4.1", "desc4.1", 4, 400, 0);
                dbManager.insertTechData("tec5.1", "desc5.1", 18, 1800, 0);

                dbManager.insertStockData("Самосвал", "Транспорт", 2);
                dbManager.insertStockData("Огурцы", "Еда", 10);
                dbManager.insertStockData("Ресурс1", "Ресурсы", 2);
                dbManager.insertStockData("Лук", "Еда", 30);
                dbManager.insertStockData("Ресурс2", "Ресурсы", 10);
                dbManager.insertStockData("Помидоры", "Еда", 10);
                dbManager.insertStockData("Оборудование3", "Оборудование", 2);
                dbManager.insertStockData("Ресурс3", "Ресурсы", 6);
                dbManager.insertStockData("Оборудование2", "Оборудование", 7);
                dbManager.insertStockData("Легковой автомобиль", "Транспорт", 5);
                dbManager.insertStockData("Оборудование1", "Оборудование", 10);
                dbManager.insertStockData("Трактор", "Транспорт", 3);

                dbManager.insertFarmsData("Теплица 1", "Не выбрано", 0, 0);
                dbManager.insertFarmsData("Теплица 2", "Не выбрано", 0, 0);
                dbManager.insertFarmsData("Теплица 3", "Не выбрано", 0, 0);
                dbManager.insertFarmsData("Теплица 4", "Не выбрано", 0, 0);
                dbManager.insertFarmsData("Теплица 5", "Не выбрано", 0, 0);

                dbManager.insertMarketData("Картофель", "руб", "Еда", 10, 300);
                dbManager.insertMarketData("Оборудование42", "руб", "Оборудование", 5, 50000);
                dbManager.insertMarketData("Бетономешалка", "$", "Транспорт", 1, 50000);
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
                if (editCountryName.getText().toString().length() > 0 && editPresidentName.getText().toString().length() > 0) {
                    SharedPreferences allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
                    allSettings.edit().putBoolean("NEW_GAME", false).apply();
                    allSettings.edit().putString("COUNTRY_NAME", editCountryName.getText().toString()).apply();
                    allSettings.edit().putString("PRESIDENT_NAME", editPresidentName.getText().toString()).apply();

                    int checkedId = levels.getCheckedRadioButtonId();
                    chekedRadioButton = findViewById(checkedId);
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
                            R.string.define_country_or_president, Toast.LENGTH_LONG).show();
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
