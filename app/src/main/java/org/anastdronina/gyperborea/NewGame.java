package org.anastdronina.gyperborea;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class NewGame extends AppCompatActivity implements View.OnClickListener {

    private Boolean ifNewGame;

    private Button btnPopulation, btnFinances, btnScience, btnSupply;
    private FloatingActionButton btnNextTurn;
    private TextView moneyD, moneyR, date, tvForDialogNextTurn, tvNotEnoughtMoney;
    private DateAndMoney dateAndMoney;
    private long salaries;
    private SharedPreferences allSettings;
    private DbThread.DbListener listener;
    private List<Person> population;
    private AlertDialog dialogNextTurn, dialogGameOver, dialogNotEnoughtMoney;
    private ArrayList<Integer> finIds;
    private ArrayList<Double> finCoefs;
    private ArrayList<Integer> finMonthsWorked;
    private boolean result;
    private ArrayList<Farm> farms;
    private Handler handler;
    private Message message;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        dateAndMoney = new DateAndMoney();
        ifNewGame = allSettings.getBoolean("NEW_GAME", true);
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
        moneyD = findViewById(R.id.moneyD);
        moneyR = findViewById(R.id.moneyR);
        date = findViewById(R.id.date);

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
                if (nextTurn() == false) {
                    dialogNotEnoughtMoney.show();
                }
            }
        });

        TextView tvGameOver = findViewById(R.id.tvGameOver);

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
        if (allSettings.getInt("TURN", 0) == 9) {
            dialogGameOver.show();
        }
        ifNewGame = allSettings.getBoolean("NEW_GAME", true);
        if (ifNewGame) {
            Intent popupIntent = new Intent(this, Popup.class);
            startActivity(popupIntent);
        }

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public boolean nextTurn() {
        result = true;

        //paying salaris and changing finances coef

        population = new ArrayList<Person>();

        listener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                salaries = bundle.getLong("salaries");
                finIds = bundle.getIntegerArrayList("financeIds");
                finCoefs = (ArrayList<Double>) bundle.getSerializable("financeCoefs");
                finMonthsWorked = bundle.getIntegerArrayList("financeMonthsWorked");
                farms = bundle.getParcelableArrayList("farms");

                if (allSettings.getLong("MONEY_RUBLES", 0) < salaries) {
                    result = false;
                } else {
                    //changing month and year if it needs
                    int currentMonth = allSettings.getInt("MONTH_ID", 0);
                    if (currentMonth == 12) {
                        allSettings.edit().putInt("MONTH_ID", 1).apply();
                        allSettings.edit().putInt("YEAR", allSettings.getInt("YEAR", 0) + 1).apply();
                    } else {
                        allSettings.edit().putInt("MONTH_ID", currentMonth + 1).apply();
                    }

                    //changing money value and finanses coef
                    allSettings.edit().putLong("MONEY_RUBLES", allSettings.getLong("MONEY_RUBLES", 0) - salaries).apply();


                    for (int i = 0; i < finIds.size(); i++) {
                        double newCoef = finCoefs.get(i) + 0.2;
                        newCoef = Math.round(newCoef * 10.0) / 10.0;
                        if (newCoef > 3.0) {
                            newCoef = 3.0;
                        }
                        bundle = new Bundle();
                        bundle.putString("query", "UPDATE " + "population" + " SET FIN_MONTHS_WORKED='" + finMonthsWorked.get(i) + 1 + "'WHERE ID='" + finIds.get(i) + "'");
                        message = handler.obtainMessage(DbThread.PERFORM_SQL_QUERY);
                        message.setData(bundle);
                        DbThread.getBackgroundHandler().sendMessage(message);

                        bundle = new Bundle();
                        bundle.putString("query", "UPDATE " + "population" + " SET FIN_COEF='" + newCoef + "'WHERE ID='" + finIds.get(i) + "'");
                        message = handler.obtainMessage(DbThread.PERFORM_SQL_QUERY);
                        message.setData(bundle);
                        DbThread.getBackgroundHandler().sendMessage(message);
                    }


                    date.setText(dateAndMoney.getDate(allSettings));
                    moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
                    moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));

                    //learning current technology
                    if (allSettings.getString("TEC_IS_BEEING_LEARNED", "").length() > 0) {
                        int tecId = allSettings.getInt("TEC_IS_BEEING_LEARNED_ID", 0);
                        int monthsLeft = allSettings.getInt("MONTHS_LEFT_TO_LEARN", 0) - 1;
                        if (monthsLeft == 0) {
                            bundle = new Bundle();
                            bundle.putString("query", "UPDATE " + "tecnologies" + " SET TEC_IS_LEARNED='" + 1 + "'WHERE TEC_ID='" + tecId + "'");
                            message = handler.obtainMessage(DbThread.PERFORM_SQL_QUERY);
                            message.setData(bundle);
                            DbThread.getBackgroundHandler().sendMessage(message);


                            allSettings.edit().putString("TEC_IS_BEEING_LEARNED", "").apply();
                            allSettings.edit().putInt("TEC_IS_BEEING_LEARNED_ID", 0).apply();
                            allSettings.edit().putInt("MONTHS_LEFT_TO_LEARN", 0).apply();
                        } else {
                            allSettings.edit().putInt("MONTHS_LEFT_TO_LEARN", monthsLeft).apply();
                        }
                    }

                    //changing farms statuses
                    for (int i = 0; i < farms.size(); i++) {
                        if ((farms.get(i).getFarmerId() != 0) && !(farms.get(i).getCrop().equals("Не выбрано"))) {
                            if (farms.get(i).getStatus() == Farm.HARVEST) {
                                bundle = new Bundle();
                                bundle.putString("productName", farms.get(i).getCrop());
                                bundle.putString("productType", "Еда");
                                bundle.putInt("productAmount", 100);
                                message = handler.obtainMessage(DbThread.INSERT_STOCK_DATA);
                                message.setData(bundle);
                                DbThread.getBackgroundHandler().sendMessage(message);

                                bundle = new Bundle();
                                bundle.putString("query", "UPDATE " + "farms" + " SET FARM_STATUS='" + 0 + "'WHERE FARM_ID='" + farms.get(i).getId() + "'");
                                message = handler.obtainMessage(DbThread.PERFORM_SQL_QUERY);
                                message.setData(bundle);
                                DbThread.getBackgroundHandler().sendMessage(message);

                            } else {
                                int statusChanged = farms.get(i).getStatus() + 1;

                                bundle = new Bundle();
                                bundle.putString("query", "UPDATE " + "farms" + " SET FARM_STATUS='" + statusChanged + "'WHERE FARM_ID='" + farms.get(i).getId() + "'");
                                message = handler.obtainMessage(DbThread.PERFORM_SQL_QUERY);
                                message.setData(bundle);
                                DbThread.getBackgroundHandler().sendMessage(message);
                            }
                        }
                    }

                    //changing turn value
                    int turnValue = allSettings.getInt("TURN", 0);
                    if (turnValue == 9) {
                        dialogGameOver.show();
                    } else {
                        turnValue++;
                        allSettings.edit().putInt("TURN", turnValue).apply();
                    }
                }
            }
        };
        DbThread.getInstance().addListener(listener);

        handler = new Handler();
        message = handler.obtainMessage(DbThread.COUNT_INFO_FOR_NEXT_TURN);
        DbThread.getBackgroundHandler().sendMessage(message);

        return result;
    }

    public void startNewGame() {
        allSettings.edit().putInt("TURN", 0).apply();
        allSettings.edit().putBoolean("NEW_GAME", true).apply();
        allSettings.edit().putString("PRESIDENT_NAME", "").apply();
        allSettings.edit().putString("COUNTRY_NAME", "").apply();
        allSettings.edit().putString("LEVEL", "Middle").apply();
        allSettings.edit().putLong("MONEY_DOLLARS", 200000).apply();
        allSettings.edit().putInt("MONEY_CENTS", 0).apply();
        allSettings.edit().putLong("MONEY_RUBLES", 20000000).apply();
        allSettings.edit().putInt("MONEY_KOP", 0).apply();
        allSettings.edit().putInt("MONTH_ID", 1).apply();
        allSettings.edit().putInt("YEAR", 2019).apply();
        allSettings.edit().putString("TEC_IS_BEEING_LEARNED", "").apply();
        allSettings.edit().putInt("TEC_IS_BEEING_LEARNED_ID", 0).apply();
        allSettings.edit().putInt("SCIENTIST_IN_USE_ID", -1).apply();
        allSettings.edit().putString("SCIENTIST_IN_USE_NAME", "").apply();
        allSettings.edit().putString("SOLD_TECHNOLOGIES", "").apply();

        getApplicationContext().deleteDatabase("hyperborea.db");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
