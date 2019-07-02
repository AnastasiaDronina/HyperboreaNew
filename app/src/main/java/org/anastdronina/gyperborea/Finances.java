package org.anastdronina.gyperborea;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Finances extends AppCompatActivity implements View.OnClickListener {

    private Button btnRubToDol;
    private Button btnDolToRub;
    private Button btnRubToDolCount;
    private Button btnDolToRubCount;
    private ImageButton btnToPeople;
    private EditText etRubToConvert;
    private EditText etDolToConvert;
    private EditText etRubConverted;
    private EditText etDolConverted;
    private AlertDialog dialogUSureRub;
    private AlertDialog dialogUSureDol;
    private TextView tvForDialogRub;
    private TextView tvForDialogDol;
    private TextView tvRulesFinances;
    private TextView tvDate;
    private TextView tvMoneyD;
    private TextView tvMoneyR;
    private SharedPreferences mSettings;
    private DateAndMoney mDateAndMoney;
    private double mCoef;
    private DbThread.DbListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finances);

        DbManager dbManager = new DbManager();
        dbManager.setCoefInFinances();

        mListener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                mCoef = bundle.getDouble("coef");
                tvRulesFinances.setText("Коэффициент улучшения курса: " + mCoef
                        + "\nПокупка $ - " + (70.0 - mCoef) + "0 руб \nПродажа $ - " + (60.0 + mCoef) + "0 руб");
            }
        };
        DbThread.getInstance().addListener(mListener);

        mSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);

        btnRubToDol = findViewById(R.id.btn_rub_to_dol);
        btnDolToRub = findViewById(R.id.btn_dol_to_rub);
        btnRubToDolCount = findViewById(R.id.btn_rub_to_dol_count);
        btnDolToRubCount = findViewById(R.id.btn_dol_to_rub_count);
        btnToPeople = findViewById(R.id.btnToPeople);
        etRubToConvert = findViewById(R.id.et_rub_to_convert);
        etDolToConvert = findViewById(R.id.et_dol_to_convert);
        etRubConverted = findViewById(R.id.et_rub_converted);
        etDolConverted = findViewById(R.id.et_dol_converted);
        dialogUSureRub = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogUSureRub.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        tvForDialogRub = new TextView(this);
        dialogUSureDol = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogUSureDol.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        tvForDialogDol = new TextView(this);
        tvRulesFinances = findViewById(R.id.tv_rules_finances);
        tvDate = findViewById(R.id.date);
        tvMoneyR = findViewById(R.id.moneyR);
        tvMoneyD = findViewById(R.id.moneyD);
        mDateAndMoney = new DateAndMoney();

        btnRubToDolCount.setOnClickListener(this);
        btnDolToRubCount.setOnClickListener(this);
        btnRubToDol.setOnClickListener(this);
        btnDolToRub.setOnClickListener(this);
        btnToPeople.setOnClickListener(this);

        dialogUSureRub.setTitle(R.string.confirm);
        dialogUSureRub.setView(tvForDialogRub);
        dialogUSureDol.setTitle(R.string.confirm);
        dialogUSureDol.setView(tvForDialogDol);

        dialogUSureRub.setButton(DialogInterface.BUTTON_POSITIVE, "Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String rubles = Long.toString(mSettings.getLong("MONEY_RUBLES", 20000000));
                String kop;
                if (mSettings.getInt("MONEY_KOP", 0) < 10) {
                    kop = "0" + mSettings.getInt("MONEY_KOP", 0);
                } else kop = Integer.toString(mSettings.getInt("MONEY_KOP", 0));
                BigDecimal currentRubbles = new BigDecimal(rubles + "." + kop);

                String dollars = Long.toString(mSettings.getLong("MONEY_DOLLARS", 20000));
                String cents;
                if (mSettings.getInt("MONEY_CENTS", 0) < 10) {
                    cents = "0" + mSettings.getInt("MONEY_CENTS", 0);
                } else cents = Integer.toString(mSettings.getInt("MONEY_CENTS", 0));
                BigDecimal currentDollars = new BigDecimal(dollars + "." + cents);

                BigDecimal rubblesFromEditText = new BigDecimal(etRubToConvert.getText().toString());
                BigDecimal dollarsFromEditText = new BigDecimal(etDolConverted.getText().toString());

                String[] partsResRub = currentRubbles.subtract(rubblesFromEditText).toString().split("\\.");
                String[] partsResDol = currentDollars.add(dollarsFromEditText).toString().split("\\.");

                mSettings.edit().putLong("MONEY_DOLLARS", Integer.parseInt(partsResDol[0])).apply();
                mSettings.edit().putInt("MONEY_CENTS", Integer.parseInt(partsResDol[1])).apply();
                mSettings.edit().putLong("MONEY_RUBLES", Integer.parseInt(partsResRub[0])).apply();
                mSettings.edit().putInt("MONEY_KOP", Integer.parseInt(partsResRub[1])).apply();

                tvMoneyD.setText(mDateAndMoney.getMoney(mSettings, "$"));
                tvMoneyR.setText(mDateAndMoney.getMoney(mSettings, "руб"));
            }
        });

        dialogUSureDol.setButton(DialogInterface.BUTTON_POSITIVE, "Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String rubles = Long.toString(mSettings.getLong("MONEY_RUBLES", 20000000));
                String kop;
                if (mSettings.getInt("MONEY_KOP", 0) < 10) {
                    kop = "0" + mSettings.getInt("MONEY_KOP", 0);
                } else kop = Integer.toString(mSettings.getInt("MONEY_KOP", 0));
                BigDecimal currentRubbles = new BigDecimal(rubles + "." + kop);

                String dollars = Long.toString(mSettings.getLong("MONEY_DOLLARS", 20000));
                String cents;
                if (mSettings.getInt("MONEY_CENTS", 0) < 10) {
                    cents = "0" + mSettings.getInt("MONEY_CENTS", 0);
                } else cents = Integer.toString(mSettings.getInt("MONEY_CENTS", 0));
                BigDecimal currentDollars = new BigDecimal(dollars + "." + cents);

                BigDecimal rubblesFromEditText = new BigDecimal(etRubConverted.getText().toString());
                BigDecimal dollarsFromEditText = new BigDecimal(etDolToConvert.getText().toString());

                String[] resRub = currentRubbles.add(rubblesFromEditText).toString().split("\\.");
                String[] resDol = currentDollars.subtract(dollarsFromEditText).toString().split("\\.");

                mSettings.edit().putLong("MONEY_DOLLARS", Integer.parseInt(resDol[0])).apply();
                mSettings.edit().putInt("MONEY_CENTS", Integer.parseInt(resDol[1])).apply();
                mSettings.edit().putLong("MONEY_RUBLES", Integer.parseInt(resRub[0])).apply();
                mSettings.edit().putInt("MONEY_KOP", Integer.parseInt(resRub[1])).apply();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvDate.setText(mDateAndMoney.getDate(mSettings));
        tvMoneyD.setText(mDateAndMoney.getMoney(mSettings, "$"));
        tvMoneyR.setText(mDateAndMoney.getMoney(mSettings, "руб"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        DbThread.getInstance().addListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DbThread.getInstance().removeListener(mListener);
    }

    @Override
    public void onClick(View v) {
        long moneyDollars = mSettings.getLong("MONEY_DOLLARS", 200000);
        long moneyRubles = mSettings.getLong("MONEY_RUBLES", 20000000);
        int moneyCents = mSettings.getInt("MONEY_CENTS", 00);
        int moneyKop = mSettings.getInt("MONEY_KOP", 00);
        String cents, kop;
        if (moneyCents < 10) {
            cents = "0" + moneyCents;
        } else cents = Integer.toString(moneyCents);
        if (moneyKop < 10) {
            kop = "0" + moneyKop;
        } else kop = Integer.toString(moneyKop);

        BigDecimal rub = new BigDecimal(moneyRubles + "." + kop);
        BigDecimal dol = new BigDecimal(moneyDollars + "." + cents);

        switch (v.getId()) {
            case R.id.btnToPeople:
                Intent intent = new Intent(this, People.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.btn_rub_to_dol_count:
                if (isNumeric(etRubToConvert.getText().toString())) {
                    if (new BigDecimal(etRubToConvert.getText().toString()).compareTo(rub) == 1) {
                        Toast.makeText(getApplicationContext(), "У Вас нет такой суммы. ", Toast.LENGTH_SHORT).show();
                    } else if (checkDecimalPart(etRubToConvert) == false) {
                        showErrorMsg("kop");
                    } else {
                        checkDecimalPart(etRubToConvert);
                    }
                } else showErrorMsg("kop");
                break;
            case R.id.btn_dol_to_rub_count:
                if (isNumeric(etDolToConvert.getText().toString())) {
                    if (new BigDecimal(etDolToConvert.getText().toString()).compareTo(dol) == 1) {
                        Toast.makeText(getApplicationContext(), "У Вас нет такой суммы. ", Toast.LENGTH_SHORT).show();
                    } else if (checkDecimalPart(etDolToConvert) == false) {
                        showErrorMsg("cent");
                    } else {
                        checkDecimalPart(etDolToConvert);
                    }
                } else showErrorMsg("cent");
                break;
            case R.id.btn_rub_to_dol:
                if (isNumeric(etRubToConvert.getText().toString())) {
                    if (new BigDecimal(etRubToConvert.getText().toString()).compareTo(rub) == 1) {
                        Toast.makeText(getApplicationContext(), "У Вас нет такой суммы. ", Toast.LENGTH_SHORT).show();
                    } else if (checkDecimalPart(etRubToConvert) == false) {
                        showErrorMsg("kop");
                    } else {
                        checkDecimalPart(etRubToConvert);
                        tvForDialogRub.setText(etRubToConvert.getText().toString() + "  \u20BD -----> " + etDolConverted.getText().toString()
                                + " $\n\nУверены, что хотите конвертировать?");
                        dialogUSureRub.show();
                    }
                } else {
                    showErrorMsg("kop");
                }
                break;
            case R.id.btn_dol_to_rub:
                if (isNumeric(etDolToConvert.getText().toString())) {
                    if (new BigDecimal(etDolToConvert.getText().toString()).compareTo(dol) == 1) {
                        Toast.makeText(getApplicationContext(), "У Вас нет такой суммы. ", Toast.LENGTH_SHORT).show();
                    } else if (checkDecimalPart(etDolToConvert) == false) {
                        showErrorMsg("cent");
                    } else {
                        checkDecimalPart(etDolToConvert);
                        tvForDialogDol.setText(etDolToConvert.getText().toString() + " $ -----> " + etRubConverted.getText().toString()
                                + " \u20BD\n\nУверены, что хотите конвертировать?");
                        dialogUSureDol.show();
                    }
                } else showErrorMsg("cent");
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public boolean checkDecimalPart(EditText et) {
        if (et.equals(etRubToConvert)) {
            String rubInput = etRubToConvert.getText().toString();
            if (isNumeric(rubInput)) {
                String[] parts = rubInput.split("\\.");
                if (parts.length <= 1) {
                    return false;
                } else if (new BigDecimal(parts[0]).compareTo(new BigDecimal(1)) == -1) {
                    return false;
                } else {
                    if (2 == parts[1].length()) {
                        BigDecimal resultRubToDol = new BigDecimal(rubInput).multiply(new BigDecimal(1 / (70.0 - mCoef))).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                        etDolConverted.setText(resultRubToDol.toString());
                        return true;
                    } else return false;
                }
            } else return false;
        } else if (et.equals(etDolToConvert)) {
            String dolInput = etDolToConvert.getText().toString();
            if (isNumeric(dolInput)) {
                String[] parts2 = dolInput.split("\\.");
                if (parts2.length <= 1) {
                    return false;
                } else {
                    if (2 == parts2[1].length()) {
                        BigDecimal resultDolToRub = new BigDecimal(dolInput).multiply(new BigDecimal(60.0 + mCoef)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                        etRubConverted.setText(resultDolToRub.toString());
                        return true;
                    } else return false;
                }
            } else {
                return false;
            }
        } else return false;
    }

    public boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }

    public void showErrorMsg(String currency) {
        if (currency.equals("cent")) {
            Toast.makeText(getApplicationContext(), R.string.not_right_format_cents, Toast.LENGTH_LONG).show();
        }
        if (currency.equals("kop")) {
            Toast.makeText(getApplicationContext(), R.string.not_right_format_kop, Toast.LENGTH_LONG).show();
        }
    }

}
