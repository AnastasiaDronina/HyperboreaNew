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
    private EditText etCountryName;
    private EditText etPresidentName;
    private Button btnPopupApply;
    private Button btnPopupBack;
    private RadioGroup radioGroupLevels;
    private RadioButton radioBtnCheked;
    private RadioButton radioBtnEasyLevel;
    private RadioButton radioBtnMiddleLevel;
    private RadioButton RadioBtnHardLevel;
    private SharedPreferences mSettings;
    private DbThread.DbListener mListener;
    private DbManager mDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        mDbManager = new DbManager();
        mSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        btnPopupApply = findViewById(R.id.popupApply);
        btnPopupBack = findViewById(R.id.popupBack);
        radioGroupLevels = findViewById(R.id.levels);
        radioBtnEasyLevel = findViewById(R.id.easyLevel);
        radioBtnMiddleLevel = findViewById(R.id.middleLevel);
        RadioBtnHardLevel = findViewById(R.id.hardLevel);
        etCountryName = findViewById(R.id.editCountryName);
        etPresidentName = findViewById(R.id.editPresidentName);

        btnPopupApply.setOnClickListener(this);
        btnPopupBack.setOnClickListener(this);

        mDbManager.createNewDatabase();

        mListener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                //inserting data in db
                mDbManager.insertPopulationData("Дмитрий", "Аксенов", 0, 0, 21, 0, 0, 0, 1, 0,
                        0, 1, 0, "Жаворонок", "Торопыжка", "Некультурный", 0, 0.0);
                mDbManager.insertPopulationData("Денис", "Сергеев", 0, 0, 53, 0, 1, 0, 0, 1,
                        0, 0, 0, "Способный ученик", "Технарь", "Бездонный желудок", 0, 0.0);
                mDbManager.insertPopulationData("Степан", "Одинцов", 0, 0, 28, 0, 0, 0, 0, 1,
                        1, 0, 0, "Декоратор", "Гурман", "Брезгливый", 0, 0.0);
                mDbManager.insertPopulationData("Вадим", "Дьячков", 0, 0, 34, 0, 0, 1, 0, 0,
                        1, 0, 0, "Веган", "Болтун", "Громкий храп", 0, 0.0);
                mDbManager.insertPopulationData("Владислав", "Кошелев", 0, 0, 30, 1, 0, 1, 0, 0,
                        0, 0, 0, "Способный ученик", "Декоратор", "Пацифист", 0, 0.0);
                mDbManager.insertPopulationData("Святослав", "Гуляев", 0, 0, 25, 0, 1, 0, 0, 0,
                        0, 1, 0, "Анемия", "Гурман", "Неконструктивный", 0, 0.0);
                mDbManager.insertPopulationData("Артём", "Тетерин", 0, 0, 28, 0, 0, 0, 1, 0,
                        1, 0, 0, "Руки из лапши", "Бездонный желудок", "Жаворонок", 0, 0.0);
                mDbManager.insertPopulationData("Эдуард", "Лыткин", 0, 0, 18, 0, 0, 0, 0, 1,
                        0, 0, 1, "Буйвол", "Декоратор", "Нарколепсия", 0, 0.0);
                mDbManager.insertPopulationData("Иннокентий", "Харитонов", 0, 0, 34, 0, 1, 1, 0, 0,
                        0, 0, 0, "Медленная обучаемость", "Брезгливый", "Торопыжка", 0, 0.0);
                mDbManager.insertPopulationData("Илья", "Некрасов", 0, 0, 23, 1, 0, 0, 0, 0,
                        1, 0, 1, "Трипофобия", "Технарь", "Декоратор", 0, 0.0);

                mDbManager.insertTechData("tec1", "desc1", 3, 300, 0);
                mDbManager.insertTechData("tec2", "desc2", 10, 1000, 1);
                mDbManager.insertTechData("tec3", "desc3", 5, 500, 0);
                mDbManager.insertTechData("tec4", "desc4", 8, 800, 0);
                mDbManager.insertTechData("tec5", "desc5", 12, 1200, 0);
                mDbManager.insertTechData("tec1.1", "desc1.1", 2, 200, 0);
                mDbManager.insertTechData("tec2.1", "desc2.1", 7, 700, 0);
                mDbManager.insertTechData("tec3.1", "desc3.1", 9, 900, 0);
                mDbManager.insertTechData("tec4.1", "desc4.1", 4, 400, 0);
                mDbManager.insertTechData("tec5.1", "desc5.1", 18, 1800, 0);

                mDbManager.insertStockData("Самосвал", "Транспорт", 2);
                mDbManager.insertStockData("Огурцы", "Еда", 10);
                mDbManager.insertStockData("Ресурс1", "Ресурсы", 2);
                mDbManager.insertStockData("Лук", "Еда", 30);
                mDbManager.insertStockData("Ресурс2", "Ресурсы", 10);
                mDbManager.insertStockData("Помидоры", "Еда", 10);
                mDbManager.insertStockData("Оборудование3", "Оборудование", 2);
                mDbManager.insertStockData("Ресурс3", "Ресурсы", 6);
                mDbManager.insertStockData("Оборудование2", "Оборудование", 7);
                mDbManager.insertStockData("Легковой автомобиль", "Транспорт", 5);
                mDbManager.insertStockData("Оборудование1", "Оборудование", 10);
                mDbManager.insertStockData("Трактор", "Транспорт", 3);

                mDbManager.insertFarmsData("Теплица 1", "Не выбрано", 0, 0);
                mDbManager.insertFarmsData("Теплица 2", "Не выбрано", 0, 0);
                mDbManager.insertFarmsData("Теплица 3", "Не выбрано", 0, 0);
                mDbManager.insertFarmsData("Теплица 4", "Не выбрано", 0, 0);
                mDbManager.insertFarmsData("Теплица 5", "Не выбрано", 0, 0);

                mDbManager.insertMarketData("Картофель", "руб", "Еда", 10, 300);
                mDbManager.insertMarketData("Оборудование42", "руб", "Оборудование", 5, 50000);
                mDbManager.insertMarketData("Бетономешалка", "$", "Транспорт", 1, 50000);
            }
        };
        DbThread.getInstance().addListener(mListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mSettings.getBoolean("NEW_GAME", true)) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        DbThread.getInstance().removeListener(mListener);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.popupApply:
                if (etCountryName.getText().toString().length() > 0 && etPresidentName.getText().toString().length() > 0) {
                    mSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
                    mSettings.edit().putBoolean("NEW_GAME", false).apply();
                    mSettings.edit().putString("COUNTRY_NAME", etCountryName.getText().toString()).apply();
                    mSettings.edit().putString("PRESIDENT_NAME", etPresidentName.getText().toString()).apply();

                    int checkedId = radioGroupLevels.getCheckedRadioButtonId();
                    radioBtnCheked = findViewById(checkedId);
                    if (radioBtnCheked.equals(radioBtnEasyLevel)) {
                        mSettings.edit().putString("LEVEL", "Easy").apply();
                        mSettings.edit().putLong("MONEY_DOLLARS", 300000).apply();
                        mSettings.edit().putLong("MONEY_RUBLES", 30000000).apply();
                    }
                    if (radioBtnCheked.equals(RadioBtnHardLevel)) {
                        mSettings.edit().putString("LEVEL", "Hard").apply();
                        mSettings.edit().putLong("MONEY_DOLLARS", 100000).apply();
                        mSettings.edit().putLong("MONEY_RUBLES", 10000000).apply();
                    }
                    if (radioBtnCheked.equals(radioBtnMiddleLevel)) {
                        mSettings.edit().putString("LEVEL", "Middle");
                        mSettings.edit().putLong("MONEY_DOLLARS", 200000).apply();
                        mSettings.edit().putLong("MONEY_RUBLES", 20000000).apply();
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
