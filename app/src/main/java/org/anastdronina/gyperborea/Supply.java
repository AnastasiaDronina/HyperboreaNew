package org.anastdronina.gyperborea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Supply extends AppCompatActivity implements View.OnClickListener {

    private Button btnStock;
    private Button btnAgriculture;
    private Button btnMarket;
    private Button btnNutrition;
    private Button btnDelivery;
    private TextView tvDate;
    private TextView tvMoneyD;
    private TextView tvMoneyR;
    private SharedPreferences mSettings;
    private DateAndMoney mDateAndMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply);

        btnStock = findViewById(R.id.btnStock);
        btnAgriculture = findViewById(R.id.btnAgriculture);
        btnMarket = findViewById(R.id.btnMarket);
        btnNutrition = findViewById(R.id.btnNutrition);
        btnDelivery = findViewById(R.id.btnDelivery);
        tvDate = findViewById(R.id.date);
        tvMoneyD = findViewById(R.id.moneyD);
        tvMoneyR = findViewById(R.id.moneyR);
        mSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        mDateAndMoney = new DateAndMoney();

        btnStock.setOnClickListener(this);
        btnAgriculture.setOnClickListener(this);
        btnMarket.setOnClickListener(this);
        btnNutrition.setOnClickListener(this);
        btnDelivery.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvDate.setText(mDateAndMoney.getDate(mSettings));
        tvMoneyD.setText(mDateAndMoney.getMoney(mSettings, "$"));
        tvMoneyR.setText(mDateAndMoney.getMoney(mSettings, "руб"));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnStock:
                intent = new Intent(this, Stock.class);
                break;
            case R.id.btnAgriculture:
                intent = new Intent(this, Agriculture.class);
                break;
            case R.id.btnMarket:
                intent = new Intent(this, Market.class);
                break;
            case R.id.btnNutrition:
                Toast.makeText(getApplicationContext(), R.string.not_available_in_demo, Toast.LENGTH_LONG).show();
                break;
            case R.id.btnDelivery:
                Toast.makeText(getApplicationContext(), R.string.not_available_in_demo, Toast.LENGTH_LONG).show();
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
