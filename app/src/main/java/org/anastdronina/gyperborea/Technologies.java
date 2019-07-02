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

public class Technologies extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rvTechnologies;
    private RecyclerView rvChangeScientist;
    private AlertDialog dialogLearnTec;
    private AlertDialog dialogAboutTec;
    private AlertDialog dialogChangeScientist;
    private AlertDialog dialogStopLearning;
    private TextView tvLearnTech;
    private TextView tvAboutTech;
    private TextView tvLearningTechInfo;
    private TextView tvPinnedScientist;
    private TextView tvDate;
    private TextView tvMoneyR;
    private TextView tvMoneyD;
    private TextView tvForDialodStopLearning;
    private Button btnChangeScientist;
    private Button btnStopLearning;
    private ImageButton btnToPeople;
    private ArrayList<Technology> mTechsList;
    private ArrayList<Technology> mTechnologiesList;
    private TechAdapter mTechAdapter;
    private ArrayList<Person> mScientistsList;
    private SharedPreferences mSettings;
    private DbThread.DbListener mListener;
    private ArrayList<Person> mPopulationList;
    private DateAndMoney mDateAndMoney;
    private DbManager mDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnologies);

        mPopulationList = new ArrayList<>();

        mDbManager = new DbManager();
        dialogStopLearning = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogStopLearning.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        tvForDialodStopLearning = new TextView(getApplicationContext());
        tvForDialodStopLearning.setText(R.string.stop_learning_tech);
        mDateAndMoney = new DateAndMoney();
        dialogLearnTec = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogLearnTec.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        dialogAboutTec = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogAboutTec.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        dialogChangeScientist = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogChangeScientist.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        tvLearnTech = new TextView(this);
        tvAboutTech = new TextView(this);
        rvChangeScientist = new RecyclerView(this);
        tvPinnedScientist = findViewById(R.id.pinnedScientist);
        tvLearningTechInfo = findViewById(R.id.sellTecInfo);
        btnChangeScientist = findViewById(R.id.changeScientist);
        btnStopLearning = findViewById(R.id.btnStopLearning);
        tvDate = findViewById(R.id.date);
        tvMoneyR = findViewById(R.id.moneyR);
        tvMoneyD = findViewById(R.id.moneyD);
        btnToPeople = findViewById(R.id.btnToPeople);
        btnToPeople.setOnClickListener(this);
        mSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);

        tvDate.setText(mDateAndMoney.getDate(mSettings));
        tvMoneyD.setText(mDateAndMoney.getMoney(mSettings, "$"));
        tvMoneyR.setText(mDateAndMoney.getMoney(mSettings, "руб"));

        rvTechnologies = findViewById(R.id.tecnologies_list);

        dialogLearnTec.setTitle(R.string.learn_tech);
        dialogLearnTec.setView(tvLearnTech);
        dialogAboutTec.setTitle(R.string.tech_info);
        dialogAboutTec.setView(tvAboutTech);
        dialogChangeScientist.setTitle(R.string.choose_scientist);
        dialogChangeScientist.setView(rvChangeScientist);
        dialogStopLearning.setTitle(R.string.learning_will_be_stopped);
        dialogStopLearning.setView(tvForDialodStopLearning);


        dialogLearnTec.setButton(DialogInterface.BUTTON_POSITIVE, "Начать изучение", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int tecId = mSettings.getInt("CURRENT_TEC_ID", 0);
                String tecName = mSettings.getString("CURRENT_TEC_NAME", "");
                int tecMonths = mSettings.getInt("CURRENT_TEC_MONTHS", 0);
                long tecPrice = mSettings.getLong("CURRENT_TEC_PRICE", 0);
                mSettings.edit().putString("TEC_IS_BEEING_LEARNED", tecName).apply();
                mSettings.edit().putInt("TEC_IS_BEEING_LEARNED_ID", tecId).apply();
                mSettings.edit().putInt("MONTHS_LEFT_TO_LEARN", tecMonths).apply();
                mSettings.edit().putLong("TEC_PRICE", tecPrice).apply();
                for (int i = 0; i < mTechnologiesList.size(); i++) {
                    if (mTechnologiesList.get(i).getName().equals(tecName)) {
                        mTechnologiesList.remove(i);
                        break;
                    }
                }
                mTechAdapter = new TechAdapter(getApplicationContext(), R.layout.tecnologies_row, mTechnologiesList);
                rvTechnologies.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                rvTechnologies.setLayoutManager(layoutManager);
                rvTechnologies.setAdapter(mTechAdapter);

                tvLearningTechInfo.setText("В процессе изучения технология: " + tecName
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
                mSettings.edit().putInt("TEC_IS_BEEING_LEARNED_ID", 0).apply();
                mSettings.edit().putString("TEC_IS_BEEING_LEARNED", "").apply();
                mSettings.edit().putInt("SCIENTIST_IN_USE_ID", 0).apply();
                mSettings.edit().putString("SCIENTIST_IN_USE_NAME", "").apply();
                printTechs();
                tvLearningTechInfo.setText("");
            }
        });

        dialogStopLearning.setButton(DialogInterface.BUTTON_NEGATIVE, "Отмена",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        btnChangeScientist.setOnClickListener(this);
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
        DbThread.getInstance().addListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DbThread.getInstance().removeListener(mListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeScientist:
                if (mSettings.getString("TEC_IS_BEEING_LEARNED", "").length() == 0) {
                    mScientistsList = new ArrayList<>();
                    mDbManager.loadData(DbManager.WhatData.POPULATION);
                    mListener = new DbThread.DbListener() {
                        @Override
                        public void onDataLoaded(Bundle bundle) {
                            mPopulationList = bundle.getParcelableArrayList("allPopulation");
                            if (mPopulationList != null) {
                                for (int i = 0; i < mPopulationList.size(); i++) {
                                    if (mPopulationList.get(i).getJob() == 6) {
                                        mScientistsList.add(mPopulationList.get(i));

                                    }
                                }
                                ScientistAdapter scientistAdapter
                                        = new ScientistAdapter(getApplicationContext(), R.layout.scientists_row, mScientistsList);
                                rvChangeScientist.setHasFixedSize(true);
                                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                rvChangeScientist.setLayoutManager(mLayoutManager);
                                rvChangeScientist.setAdapter(scientistAdapter);
                                dialogChangeScientist.show();
                            }
                        }
                    };
                    DbThread.getInstance().addListener(mListener);
                } else
                    Toast.makeText(getApplicationContext(), R.string.cant_change_scientist, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnStopLearning:
                if (mSettings.getString("TEC_IS_BEEING_LEARNED", "").length() > 0
                        || mSettings.getString("SCIENTIST_IN_USE_NAME", "").length() > 0) {
                    dialogStopLearning.show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.nothing_to_stop, Toast.LENGTH_SHORT).show();
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

        private Technology tech;
        private Context context;

        public TecHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.tecName = itemView.findViewById(R.id.tec_name);
            this.tecMonths = itemView.findViewById(R.id.tec_months);
            this.tecPrice = itemView.findViewById(R.id.tec_price);

            itemView.setOnClickListener(this);
        }

        public void bindTech(Technology tech) {
            this.tech = tech;
            tecName.setText(tech.getName());
            tecMonths.setText(tech.getMonthsToLearn() + " мес");
            tecPrice.setText(tech.getPrice() + " $");
        }

        @Override
        public void onClick(View v) {
            if (mSettings.getInt("SCIENTIST_IN_USE_ID", 0) == 0) {
                Toast.makeText(getApplicationContext(), R.string.choose_scientist_first,
                        Toast.LENGTH_SHORT).show();
            } else {
                mSettings.edit().putInt("CURRENT_TEC_ID", tech.getId()).apply();
                mSettings.edit().putString("CURRENT_TEC_NAME", tech.getName()).apply();
                mSettings.edit().putString("CURRENT_TEC_DESCRIPTION", tech.getDescription()).apply();
                mSettings.edit().putInt("CURRENT_TEC_MONTHS", tech.getMonthsToLearn()).apply();
                mSettings.edit().putLong("CURRENT_TEC_PRICE", tech.getPrice()).apply();
                mSettings.edit().putBoolean("CURRENT_TEC_ISLEARNED", tech.isLearned()).apply();

                if (mSettings.getString("TEC_IS_BEEING_LEARNED", "").length() == 0) {
                    tvLearnTech.setText("\n" + mSettings.getString("CURRENT_TEC_NAME", "").toUpperCase()
                            + "\n\nОписание: \n" + mSettings.getString("CURRENT_TEC_DESCRIPTION", "")
                            + "\n\nДля изучения необходимо: " + mSettings.getInt("CURRENT_TEC_MONTHS", 0)
                            + " мес. \n\nЦена продажи: " + mSettings.getLong("CURRENT_TEC_PRICE", 0) + " $");
                    dialogLearnTec.show();
                } else {
                    tvAboutTech.setText("\n" + mSettings.getString("CURRENT_TEC_NAME", "").toUpperCase()
                            + "\n\nОписание: \n" + mSettings.getString("CURRENT_TEC_DESCRIPTION", "")
                            + "\n\nДля изучения необходимо: " + mSettings.getInt("CURRENT_TEC_MONTHS", 0)
                            + " мес. \n\nЦена продажи: " + mSettings.getLong("CURRENT_TEC_PRICE", 0) + " $");
                    dialogAboutTec.show();
                }
            }
        }
    }

    public class TechAdapter extends RecyclerView.Adapter<TecHolder> {
        private final List<Technology> techs;
        private Context context;
        private int itemResource;

        TechAdapter(Context context, int itemResourse, List<Technology> techs) {
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
            Technology tech = this.techs.get(position);
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

            this.sciName = itemView.findViewById(R.id.scientistName);
            this.sciLevel = itemView.findViewById(R.id.scientistLevel);

            itemView.setOnClickListener(this);
        }

        public void bindScientist(Person scientist) {
            this.scientist = scientist;
            sciName.setText(scientist.getName() + " \n" + scientist.getSurname());
            sciLevel.setText(String.valueOf(scientist.getLearning()));
        }

        @Override
        public void onClick(View v) {
            mSettings.edit().putInt("SCIENTIST_IN_USE_ID", scientist.getId()).apply();
            mSettings.edit().putString("SCIENTIST_IN_USE_NAME", scientist.getName() + " " + scientist.getSurname()).apply();
            tvPinnedScientist.setText("Для изучения закреплен ученый: "
                    + mSettings.getString("SCIENTIST_IN_USE_NAME", ""));
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

    public ArrayList<Technology> changeTecnologiesList(ArrayList<Technology> tecnologies) {
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
            if (tecnologies.get(i).getName().equals(mSettings.getString("TEC_IS_BEEING_LEARNED", ""))) {
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
        int sciId = mSettings.getInt("SCIENTIST_IN_USE_ID", -1);
        if (sciId != -1) {
            tvPinnedScientist.setText("Для изучения закреплен ученый: "
                    + mSettings.getString("SCIENTIST_IN_USE_NAME", ""));
        } else tvPinnedScientist.setText("Для изучения закреплен ученый: Не выбрано");
        mTechsList = new ArrayList<>();
        mDbManager.loadData(DbManager.WhatData.TECH);
        mListener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                mTechsList = bundle.getParcelableArrayList("techs");
                mTechnologiesList = changeTecnologiesList(mTechsList);
                if (mSettings.getString("TEC_IS_BEEING_LEARNED", "").length() > 0) {
                    String tecInLearning = mSettings.getString("TEC_IS_BEEING_LEARNED", "");
                    int monthsLeftToLearn = mSettings.getInt("MONTHS_LEFT_TO_LEARN", 0);
                    long price = mSettings.getLong("TEC_PRICE", 0);
                    tvLearningTechInfo.setText("В процессе изучения технология: " + tecInLearning
                            + " \nОсталось: " + monthsLeftToLearn + " мес \nЦена продажи: " + price + " $");
                }
                mTechAdapter = new TechAdapter(getApplicationContext(), R.layout.tecnologies_row, mTechnologiesList);
                rvTechnologies.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rvTechnologies.setLayoutManager(mLayoutManager);
                rvTechnologies.setAdapter(mTechAdapter);

            }
        };
        DbThread.getInstance().addListener(mListener);
    }
}
