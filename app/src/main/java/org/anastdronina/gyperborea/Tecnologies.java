package org.anastdronina.gyperborea;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Tecnologies extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Tecnology> tecs, tecnologies;
    private TecAdapter tecAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Person> scientists;
    private RecyclerView tecnologiesList, lvChangeScientist;
    private AlertDialog dialogLearnTec, dialogAboutTec, dialogChangeScientist, dialogStopLearning;
    private TextView learnTecView, aboutTecView, learningTecInfo, pinnedScientist, date, moneyR, moneyD, tvForDialodStopLearning;
    private Button changeScientist, btnStopLearning;
    private SharedPreferences allSettings;
    private DbThread.DbListener listener;
    private ArrayList<Person> population;
    private DateAndMoney dateAndMoney;
    private ImageButton btnToPeople;
    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnologies);

        population = new ArrayList<>();

        dbManager = new DbManager();
        dialogStopLearning = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogStopLearning.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        tvForDialodStopLearning = new TextView(getApplicationContext());
        tvForDialodStopLearning.setText(R.string.stop_learning_tech);
        dateAndMoney = new DateAndMoney();
        dialogLearnTec = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogLearnTec.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        dialogAboutTec = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogAboutTec.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        dialogChangeScientist = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogChangeScientist.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        learnTecView = new TextView(this);
        aboutTecView = new TextView(this);
        lvChangeScientist = new RecyclerView(this);
        pinnedScientist = findViewById(R.id.pinnedScientist);
        learningTecInfo = findViewById(R.id.sellTecInfo);
        changeScientist = findViewById(R.id.changeScientist);
        btnStopLearning = findViewById(R.id.btnStopLearning);
        date = findViewById(R.id.date);
        moneyR = findViewById(R.id.moneyR);
        moneyD = findViewById(R.id.moneyD);
        btnToPeople = findViewById(R.id.btnToPeople);
        btnToPeople.setOnClickListener(this);
        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));

        tecnologiesList = findViewById(R.id.tecnologies_list);

        dialogLearnTec.setTitle("Изучить технологию?");
        dialogLearnTec.setView(learnTecView);
        dialogAboutTec.setTitle("Информация о технологии");
        dialogAboutTec.setView(aboutTecView);
        dialogChangeScientist.setTitle("Выберите ученого из списка: ");
        dialogChangeScientist.setView(lvChangeScientist);
        dialogStopLearning.setTitle("Изучения технологии будет прервано ");
        dialogStopLearning.setView(tvForDialodStopLearning);


        dialogLearnTec.setButton(DialogInterface.BUTTON_POSITIVE, "Начать изучение", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int tecId = allSettings.getInt("CURRENT_TEC_ID", 0);
                String tecName = allSettings.getString("CURRENT_TEC_NAME", "");
                int tecMonths = allSettings.getInt("CURRENT_TEC_MONTHS", 0);
                long tecPrice = allSettings.getLong("CURRENT_TEC_PRICE", 0);
                allSettings.edit().putString("TEC_IS_BEEING_LEARNED", tecName).apply();
                allSettings.edit().putInt("TEC_IS_BEEING_LEARNED_ID", tecId).apply();
                allSettings.edit().putInt("MONTHS_LEFT_TO_LEARN", tecMonths).apply();
                allSettings.edit().putLong("TEC_PRICE", tecPrice).apply();
                for (int i = 0; i < tecnologies.size(); i++) {
                    if (tecnologies.get(i).getName().equals(tecName)) {
                        tecnologies.remove(i);
                        break;
                    }
                }

                tecAdapter = new TecAdapter(getApplicationContext(), R.layout.tecnologies_row, tecnologies);
                tecnologiesList.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getApplicationContext());
                tecnologiesList.setLayoutManager(layoutManager);
                tecnologiesList.setAdapter(tecAdapter);

                learningTecInfo.setText("В процессе изучения технология: " + tecName
                        + " \nОсталось: " + tecMonths + " мес \nЦена продажи: " + tecPrice + " $");
            }
        });

        dialogAboutTec.setButton(DialogInterface.BUTTON_POSITIVE, "Закрыть", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialogStopLearning.setButton(DialogInterface.BUTTON_POSITIVE, "Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                allSettings.edit().putInt("TEC_IS_BEEING_LEARNED_ID", 0).apply();
                allSettings.edit().putString("TEC_IS_BEEING_LEARNED", "").apply();
                allSettings.edit().putInt("SCIENTIST_IN_USE_ID", 0).apply();
                allSettings.edit().putString("SCIENTIST_IN_USE_NAME", "").apply();
                printTechs();
                learningTecInfo.setText("");
            }
        });

        dialogStopLearning.setButton(DialogInterface.BUTTON_NEGATIVE, "Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        changeScientist.setOnClickListener(this);
        btnStopLearning.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        printTechs();
    }

    @Override
    protected void onStart() {
        super.onStart();
        DbThread.getInstance().addListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DbThread.getInstance().removeListener(listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeScientist:
                if (allSettings.getString("TEC_IS_BEEING_LEARNED", "").length() == 0) {
                    scientists = new ArrayList<>();
                    dbManager.loadData(DbManager.WhatData.population);
                    listener = new DbThread.DbListener() {
                        @Override
                        public void onDataLoaded(Bundle bundle) {
                            population = bundle.getParcelableArrayList("allPopulation");
                            if (population != null) {
                                for (int i = 0; i < population.size(); i++) {
                                    if (population.get(i).getJob() == 6) {
                                        scientists.add(population.get(i));

                                    }
                                }
                                ScientistAdapter scientistAdapter = new ScientistAdapter(getApplicationContext(), R.layout.scientists_row, scientists);
                                lvChangeScientist.setHasFixedSize(true);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                lvChangeScientist.setLayoutManager(layoutManager);
                                lvChangeScientist.setAdapter(scientistAdapter);
                                dialogChangeScientist.show();
                            }
                        }
                    };
                    DbThread.getInstance().addListener(listener);
                } else
                    Toast.makeText(getApplicationContext(), "Изменение ученого в момент изучения технологии невозможно.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnStopLearning:
                if (allSettings.getString("TEC_IS_BEEING_LEARNED", "").length() > 0
                        || allSettings.getString("SCIENTIST_IN_USE_NAME", "").length() > 0) {
                    dialogStopLearning.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Пока сбрасывать нечего ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnToPeople:
                Intent intent = new Intent(this, People.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            default:
        }
    }

    public class TecHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tecName;
        private final TextView tecMonths;
        private final TextView tecPrice;

        private Tecnology tech;
        private Context context;

        public TecHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.tecName = (TextView) itemView.findViewById(R.id.tec_name);
            this.tecMonths = (TextView) itemView.findViewById(R.id.tec_months);
            this.tecPrice = (TextView) itemView.findViewById(R.id.tec_price);

            itemView.setOnClickListener(this);
        }

        public void bindTech(Tecnology tech) {
            this.tech = tech;
            tecName.setText(tech.getName());
            tecMonths.setText(tech.getMonthsToLearn() + " мес");
            tecPrice.setText(tech.getPrice() + " $");
        }

        @Override
        public void onClick(View v) {
            if (allSettings.getInt("SCIENTIST_IN_USE_ID", 0) == 0) {
                Toast.makeText(getApplicationContext(), "Для изучения необходимо сначала выбрать ученого! ", Toast.LENGTH_SHORT).show();
            } else {
                allSettings.edit().putInt("CURRENT_TEC_ID", tech.getId()).apply();
                allSettings.edit().putString("CURRENT_TEC_NAME", tech.getName()).apply();
                allSettings.edit().putString("CURRENT_TEC_DESCRIPTION", tech.getDescription()).apply();
                allSettings.edit().putInt("CURRENT_TEC_MONTHS", tech.getMonthsToLearn()).apply();
                allSettings.edit().putLong("CURRENT_TEC_PRICE", tech.getPrice()).apply();
                allSettings.edit().putBoolean("CURRENT_TEC_ISLEARNED", tech.isLearned()).apply();

                if (allSettings.getString("TEC_IS_BEEING_LEARNED", "").length() == 0) {
                    learnTecView.setText("\n" + allSettings.getString("CURRENT_TEC_NAME", "").toUpperCase() + "\n\nОписание: \n"
                            + allSettings.getString("CURRENT_TEC_DESCRIPTION", "") + "\n\nДля изучения необходимо: "
                            + allSettings.getInt("CURRENT_TEC_MONTHS", 0) + " мес. \n\nЦена продажи: "
                            + allSettings.getLong("CURRENT_TEC_PRICE", 0) + " $");
                    dialogLearnTec.show();
                } else {
                    aboutTecView.setText("\n" + allSettings.getString("CURRENT_TEC_NAME", "").toUpperCase() + "\n\nОписание: \n"
                            + allSettings.getString("CURRENT_TEC_DESCRIPTION", "") + "\n\nДля изучения необходимо: "
                            + allSettings.getInt("CURRENT_TEC_MONTHS", 0) + " мес. \n\nЦена продажи: "
                            + allSettings.getLong("CURRENT_TEC_PRICE", 0) + " $");
                    dialogAboutTec.show();
                }
            }
        }
    }

    public class TecAdapter extends RecyclerView.Adapter<TecHolder> {
        private final List<Tecnology> techs;
        private Context context;
        private int itemResource;

        TecAdapter(Context context, int itemResourse, List<Tecnology> techs) {
            this.context = context;
            this.techs = techs;
            this.itemResource = itemResourse;
        }

        @NonNull
        @Override
        public TecHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(this.itemResource, parent, false);
            return new TecHolder(this.context, view);
        }

        @Override
        public void onBindViewHolder(@NonNull TecHolder holder, int position) {
            Tecnology tech = this.techs.get(position);
            holder.bindTech(tech);
        }

        @Override
        public int getItemCount() {
            return this.techs.size();
        }
    }

    public class ScientistHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView sciName;
        private final TextView sciLevel;

        private Person scientist;
        private Context context;

        public ScientistHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.sciName = (TextView) itemView.findViewById(R.id.scientistName);
            this.sciLevel = (TextView) itemView.findViewById(R.id.scientistLevel);

            itemView.setOnClickListener(this);
        }

        public void bindScientist(Person scientist) {
            this.scientist = scientist;
            sciName.setText(scientist.getName() + " \n" + scientist.getSurname());
            sciLevel.setText(String.valueOf(scientist.getLearning()));
        }

        @Override
        public void onClick(View v) {
            allSettings.edit().putInt("SCIENTIST_IN_USE_ID", scientist.getId()).apply();
            allSettings.edit().putString("SCIENTIST_IN_USE_NAME", scientist.getName() + " " + scientist.getSurname()).apply();
            pinnedScientist.setText("Для изучения закреплен ученый: "
                    + allSettings.getString("SCIENTIST_IN_USE_NAME", ""));
            dialogChangeScientist.dismiss();
        }
    }

    public class ScientistAdapter extends RecyclerView.Adapter<ScientistHolder> {
        private final List<Person> scientists;
        private Context context;
        private int itemResource;

        ScientistAdapter(Context context, int itemResourse, List<Person> scientists) {
            this.context = context;
            this.scientists = scientists;
            this.itemResource = itemResourse;
        }

        @NonNull
        @Override
        public ScientistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(this.itemResource, parent, false);
            return new ScientistHolder(this.context, view);
        }

        @Override
        public void onBindViewHolder(@NonNull ScientistHolder holder, int position) {
            Person scientist = this.scientists.get(position);
            holder.bindScientist(scientist);
        }

        @Override
        public int getItemCount() {
            return this.scientists.size();
        }
    }

    public ArrayList<Tecnology> changeTecnologiesList(ArrayList<Tecnology> tecnologies) {
        boolean tec1IsLearned = false, tec2IsLearned = false, tec3IsLearned = false, tec4IsLearned = false, tec5IsLearned = false;

        int i;

        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec1") && tecnologies.get(i).isLearned() == true) {
                tec1IsLearned = true;
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec2") && tecnologies.get(i).isLearned() == true) {
                tec2IsLearned = true;
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec3") && tecnologies.get(i).isLearned() == true) {
                tec3IsLearned = true;
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec4") && tecnologies.get(i).isLearned() == true) {
                tec4IsLearned = true;
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec5") && tecnologies.get(i).isLearned() == true) {
                tec5IsLearned = true;
                tecnologies.remove(i);
            }
        }

        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec1.1") && !tec1IsLearned) {
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec2.1") && !tec2IsLearned) {
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec3.1") && !tec3IsLearned) {
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec4.1") && !tec4IsLearned) {
                tecnologies.remove(i);
            }
        }
        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals("tec5.1") && !tec5IsLearned) {
                tecnologies.remove(i);
            }
        }

        for (i = 0; i < tecnologies.size(); i++) {
            if (tecnologies.get(i).getName().equals(allSettings.getString("TEC_IS_BEEING_LEARNED", ""))) {
                tecnologies.remove(i);
            }
        }
        return tecnologies;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void printTechs() {
        int sciId = allSettings.getInt("SCIENTIST_IN_USE_ID", -1);
        if (sciId != -1) {
            pinnedScientist.setText("Для изучения закреплен ученый: "
                    + allSettings.getString("SCIENTIST_IN_USE_NAME", ""));
        } else pinnedScientist.setText("Для изучения закреплен ученый: Не выбрано");
        tecs = new ArrayList<>();
        dbManager.loadData(DbManager.WhatData.tech);
        listener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                tecs = bundle.getParcelableArrayList("techs");
                //убираем из списка уже изученные технологии и заменяем их на следующие этого же типа
                tecnologies = changeTecnologiesList(tecs);
                if (allSettings.getString("TEC_IS_BEEING_LEARNED", "").length() > 0) {
                    String tecInLearning = allSettings.getString("TEC_IS_BEEING_LEARNED", "");
                    int monthsLeftToLearn = allSettings.getInt("MONTHS_LEFT_TO_LEARN", 0);
                    long price = allSettings.getLong("TEC_PRICE", 0);
                    learningTecInfo.setText("В процессе изучения технология: " + tecInLearning
                            + " \nОсталось: " + monthsLeftToLearn + " мес \nЦена продажи: " + price + " $");
                }
                tecAdapter = new TecAdapter(getApplicationContext(), R.layout.tecnologies_row, tecnologies);
                tecnologiesList.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getApplicationContext());
                tecnologiesList.setLayoutManager(layoutManager);
                tecnologiesList.setAdapter(tecAdapter);

            }
        };
        DbThread.getInstance().addListener(listener);
    }
}
