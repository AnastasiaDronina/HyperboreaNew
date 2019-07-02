package org.anastdronina.gyperborea;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class NewGame extends AppCompatActivity implements View.OnClickListener {

    private Boolean ifNewGame;

    private Button btnPopulation;
    private Button btnFinances;
    private Button btnScience;
    private Button btnSupply;
    private FloatingActionButton btnNextTurn;
    private TextView tvMoneyD;
    private TextView tvMoneyR;
    private TextView tvDate;
    private TextView tvForDialogNextTurn;
    private TextView tvNotEnoughtMoney;
    private AlertDialog dialogNextTurn;
    private AlertDialog dialogGameOver;
    private AlertDialog dialogNotEnoughtMoney;
    private DateAndMoney mDateAndMoney;
    private long mSalaries;
    private SharedPreferences mSettings;
    private DbThread.DbListener mListener;
    private DbManager mDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        mDbManager = new DbManager();
        mSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        mDateAndMoney = new DateAndMoney();
        ifNewGame = mSettings.getBoolean("NEW_GAME", true);
        if (ifNewGame) {
            Intent popupIntent = new Intent(this, Popup.class);
            startActivity(popupIntent);
        }

        btnPopulation = findViewById(R.id.btnPopulation);
        btnFinances = findViewById(R.id.btnFinances);
        btnScience = findViewById(R.id.btnScience);
        btnSupply = findViewById(R.id.btnSupply);
        btnNextTurn = findViewById(R.id.btnNextTurn);
        tvNotEnoughtMoney = new TextView(getApplicationContext());
        tvNotEnoughtMoney.setText(R.string.not_enough_money_to_pay_salaries);
        tvMoneyD = findViewById(R.id.moneyD);
        tvMoneyR = findViewById(R.id.moneyR);
        tvDate = findViewById(R.id.date);

        dialogNotEnoughtMoney = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogNotEnoughtMoney.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        dialogNotEnoughtMoney.setView(tvNotEnoughtMoney);
        dialogNotEnoughtMoney.setButton(DialogInterface.BUTTON_POSITIVE, "Исправить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogNotEnoughtMoney.dismiss();
            }
        });
        dialogNotEnoughtMoney.setButton(DialogInterface.BUTTON_NEUTRAL, "Начать заново", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startNewGame();
                dialogNotEnoughtMoney.dismiss();
            }
        });

        dialogNextTurn = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogNextTurn.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        tvForDialogNextTurn = new TextView(this);
        tvForDialogNextTurn.setText(getString(R.string.next_turn_info));
        dialogNextTurn.setView(tvForDialogNextTurn);
        dialogNextTurn.setButton(DialogInterface.BUTTON_POSITIVE, "Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nextTurn();
            }
        });

        dialogGameOver = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogGameOver.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        LayoutInflater inflater = this.getLayoutInflater();
        dialogGameOver.setView(inflater.inflate(R.layout.game_over, null));
        dialogGameOver.setCancelable(false);
        dialogGameOver.setCanceledOnTouchOutside(false);
        dialogGameOver.setButton(DialogInterface.BUTTON_POSITIVE, "Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startNewGame();
            }
        });

        btnPopulation.setOnClickListener(this);
        btnFinances.setOnClickListener(this);
        btnScience.setOnClickListener(this);
        btnSupply.setOnClickListener(this);
        btnNextTurn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSettings.getInt("TURN", 0) == 9) {
            dialogGameOver.show();
        }
        ifNewGame = mSettings.getBoolean("NEW_GAME", true);
        if (ifNewGame) {
            Intent popupIntent = new Intent(this, Popup.class);
            startActivity(popupIntent);
        }

        tvDate.setText(mDateAndMoney.getDate(mSettings));
        tvMoneyD.setText(mDateAndMoney.getMoney(mSettings, "$"));
        tvMoneyR.setText(mDateAndMoney.getMoney(mSettings, "руб"));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnPopulation:
                intent = new Intent(this, People.class);
                break;
            case R.id.btnFinances:
                intent = new Intent(this, Finances.class);
                break;
            case R.id.btnScience:
                intent = new Intent(this, Sciense.class);
                break;
            case R.id.btnSupply:
                intent = new Intent(this, Supply.class);
                break;
            case R.id.btnNextTurn:
                dialogNextTurn.show();
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

    public void nextTurn() {

        //paying salaris and changing finances coef
        mListener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                mSalaries = bundle.getLong("salaries");
                ArrayList<Integer> finIdsList = bundle.getIntegerArrayList("financeIds");
                ArrayList<Double> finCoefsList = (ArrayList<Double>) bundle.getSerializable("financeCoefs");
                ArrayList<Integer> finMonthsWorkedList = bundle.getIntegerArrayList("financeMonthsWorked");
                ArrayList<Farm> farmsList = bundle.getParcelableArrayList("farms");

                if (mSettings.getLong("MONEY_RUBLES", 0) < mSalaries) {
                    dialogNotEnoughtMoney.show();
                } else {
                    //changing month and year if it needs
                    int currentMonth = mSettings.getInt("MONTH_ID", 0);
                    if (currentMonth == 12) {
                        mSettings.edit().putInt("MONTH_ID", 1).apply();
                        mSettings.edit().putInt("YEAR", mSettings.getInt("YEAR", 0) + 1).apply();
                    } else {
                        mSettings.edit().putInt("MONTH_ID", currentMonth + 1).apply();
                    }

                    //changing money value and finanses coef
                    mSettings.edit().putLong("MONEY_RUBLES", mSettings.getLong("MONEY_RUBLES", 0) - mSalaries).apply();


                    for (int i = 0; i < finIdsList.size(); i++) {
                        double newCoef = finCoefsList.get(i) + 0.2;
                        newCoef = Math.round(newCoef * 10.0) / 10.0;
                        if (newCoef > 3.0) {
                            newCoef = 3.0;
                        }

                        mDbManager.performQuery("UPDATE " + "population" + " SET FIN_MONTHS_WORKED='" + finMonthsWorkedList.get(i) + 1
                                + "'WHERE ID='" + finIdsList.get(i) + "'");
                        mDbManager.performQuery("UPDATE " + "population" + " SET FIN_COEF='" + newCoef
                                + "'WHERE ID='" + finIdsList.get(i) + "'");
                    }


                    tvDate.setText(mDateAndMoney.getDate(mSettings));
                    tvMoneyD.setText(mDateAndMoney.getMoney(mSettings, "$"));
                    tvMoneyR.setText(mDateAndMoney.getMoney(mSettings, "руб"));

                    //learning current technology
                    if (mSettings.getString("TEC_IS_BEEING_LEARNED", "").length() > 0) {
                        int tecId = mSettings.getInt("TEC_IS_BEEING_LEARNED_ID", 0);
                        int monthsLeft = mSettings.getInt("MONTHS_LEFT_TO_LEARN", 0) - 1;
                        if (monthsLeft == 0) {
                            mDbManager.performQuery("UPDATE " + "tecnologies" + " SET TEC_IS_LEARNED='" + 1 + "'WHERE TEC_ID='" + tecId + "'");
                            mSettings.edit().putString("TEC_IS_BEEING_LEARNED", "").apply();
                            mSettings.edit().putInt("TEC_IS_BEEING_LEARNED_ID", 0).apply();
                            mSettings.edit().putInt("MONTHS_LEFT_TO_LEARN", 0).apply();
                        } else {
                            mSettings.edit().putInt("MONTHS_LEFT_TO_LEARN", monthsLeft).apply();
                        }
                    }

                    //changing farms statuses
                    for (int i = 0; i < farmsList.size(); i++) {
                        if ((farmsList.get(i).getFarmerId() != 0) && !(farmsList.get(i).getCrop().equals("Не выбрано"))) {
                            if (farmsList.get(i).getStatus() == Farm.HARVEST) {
                                mDbManager.insertStockData(farmsList.get(i).getCrop(), "Еда", 100);
                                mDbManager.performQuery("UPDATE " + "farms" + " SET FARM_STATUS='" + 0
                                        + "'WHERE FARM_ID='" + farmsList.get(i).getId() + "'");

                            } else {
                                mDbManager.performQuery("UPDATE " + "farms" + " SET FARM_STATUS='" + farmsList.get(i).getStatus() + 1
                                        + "'WHERE FARM_ID='" + farmsList.get(i).getId() + "'");
                            }
                        }
                    }

                    //changing turn value
                    int turnValue = mSettings.getInt("TURN", 0);
                    if (turnValue == 9) {
                        dialogGameOver.show();
                    } else {
                        turnValue++;
                        mSettings.edit().putInt("TURN", turnValue).apply();
                    }
                }
            }
        };
        DbThread.getInstance().addListener(mListener);
        mDbManager.countInfoForNextTurn();
    }

    public void startNewGame() {
        mSettings.edit().putInt("TURN", 0).apply();
        mSettings.edit().putBoolean("NEW_GAME", true).apply();
        mSettings.edit().putString("PRESIDENT_NAME", "").apply();
        mSettings.edit().putString("COUNTRY_NAME", "").apply();
        mSettings.edit().putString("LEVEL", "Middle").apply();
        mSettings.edit().putLong("MONEY_DOLLARS", 200000).apply();
        mSettings.edit().putInt("MONEY_CENTS", 0).apply();
        mSettings.edit().putLong("MONEY_RUBLES", 20000000).apply();
        mSettings.edit().putInt("MONEY_KOP", 0).apply();
        mSettings.edit().putInt("MONTH_ID", 1).apply();
        mSettings.edit().putInt("YEAR", 2019).apply();
        mSettings.edit().putString("TEC_IS_BEEING_LEARNED", "").apply();
        mSettings.edit().putInt("TEC_IS_BEEING_LEARNED_ID", 0).apply();
        mSettings.edit().putInt("SCIENTIST_IN_USE_ID", -1).apply();
        mSettings.edit().putString("SCIENTIST_IN_USE_NAME", "").apply();
        mSettings.edit().putString("SOLD_TECHNOLOGIES", "").apply();

        getApplicationContext().deleteDatabase("hyperborea.db");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
