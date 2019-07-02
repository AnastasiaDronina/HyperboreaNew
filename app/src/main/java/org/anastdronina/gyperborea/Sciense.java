package org.anastdronina.gyperborea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Sciense extends AppCompatActivity implements View.OnClickListener {
    private Button btnTecnologies;
    private Button btnSellTecnologies;
    private TextView tvDate;
    private TextView tvMoneyR;
    private TextView tvMoneyD;
    private DateAndMoney mDateAndMoney;
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sciense);

        mDateAndMoney = new DateAndMoney();
        mSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);

        btnTecnologies = findViewById(R.id.btn_tecnologies);
        btnSellTecnologies = findViewById(R.id.btn_sell_tecnologies);
        tvDate = findViewById(R.id.date);
        tvMoneyR = findViewById(R.id.moneyR);
        tvMoneyD = findViewById(R.id.moneyD);

        btnTecnologies.setOnClickListener(this);
        btnSellTecnologies.setOnClickListener(this);

        tvDate.setText(mDateAndMoney.getDate(mSettings));
        tvMoneyD.setText(mDateAndMoney.getMoney(mSettings, "$"));
        tvMoneyR.setText(mDateAndMoney.getMoney(mSettings, "руб"));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_tecnologies:
                intent = new Intent(this, Technologies.class);
                break;
            case R.id.btn_sell_tecnologies:
                intent = new Intent(this, SellTechnology.class);
                break;
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
