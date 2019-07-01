package org.anastdronina.gyperborea;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class SellTechnology extends AppCompatActivity {

    private TextView sellTecInfo, sellTecView, date, moneyD, moneyR;
    private DbThread.DbListener listener;
    private ArrayList<Tecnology> tecs;
    private String[] soldTechnologies;
    private RecyclerView learnedTecList;
    private ArrayList<Tecnology> learnedTechs;
    private AlertDialog dialogSellTec;
    private SharedPreferences allSettings;
    private DateAndMoney dateAndMoney;
    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_technology);

        dbManager = new DbManager();
        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        sellTecInfo = findViewById(R.id.sellTecInfo);
        learnedTecList = findViewById(R.id.learnedTecList);
        dialogSellTec = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogSellTec.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        sellTecView = new TextView(this);
        date = findViewById(R.id.date);
        moneyD = findViewById(R.id.moneyD);
        moneyR = findViewById(R.id.moneyR);
        dateAndMoney = new DateAndMoney();

        dialogSellTec.setTitle(R.string.sell_tech);
        dialogSellTec.setView(sellTecView);

        dialogSellTec.setButton(DialogInterface.BUTTON_POSITIVE, "Продать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tecName = allSettings.getString("CURRENT_TEC_NAME", "");
                long tecPrice = allSettings.getLong("CURRENT_TEC_PRICE", 0);
                long res = allSettings.getLong("MONEY_DOLLARS", 0) + tecPrice;
                allSettings.edit().putLong("MONEY_DOLLARS", res).apply();
                for (int i = 0; i < learnedTechs.size(); i++) {
                    if (learnedTechs.get(i).getName().equals(tecName)) {
                        int id = learnedTechs.get(i).getId();
                        allSettings.edit().putString("SOLD_TECHNOLOGIES", allSettings.getString("SOLD_TECHNOLOGIES", "") + id + ",").apply();
                        learnedTechs.remove(i);
                        break;
                    }
                }

                dbManager.loadData(DbManager.WhatData.tech);

                listener = new DbThread.DbListener() {
                    @Override
                    public void onDataLoaded(Bundle bundle) {
                        tecs = bundle.getParcelableArrayList("techs");
                        soldTechnologies = allSettings.getString("SOLD_TECHNOLOGIES", "").split(",");
                        for (int i = 0; i < tecs.size(); i++) {
                            Boolean isSold = false;
                            for (int j = 0; j < soldTechnologies.length; j++) {
                                if (soldTechnologies[j].equals(Integer.toString(tecs.get(i).getId()))) {
                                    isSold = true;
                                }
                            }
                            if (tecs.get(i).isLearned() && !isSold) learnedTechs.add(tecs.get(i));
                        }
                        if (learnedTechs.size() > 0) {
                            sellTecInfo.setText("");
                            LearnedTechAdapter peopleAdapter = new LearnedTechAdapter(getApplicationContext(), R.layout.learned_technology_row, learnedTechs);
                            learnedTecList.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            learnedTecList.setLayoutManager(layoutManager);
                            learnedTecList.setAdapter(peopleAdapter);
                        } else {
                            sellTecInfo.setText(R.string.you_dont_have_techs_to_sell);
                        }
                    }
                };
                DbThread.getInstance().addListener(listener);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        soldTechnologies = allSettings.getString("SOLD_TECHNOLOGIES", "").split(",");
        learnedTechs = new ArrayList<>();

        dbManager.loadData(DbManager.WhatData.tech);

        listener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                tecs = bundle.getParcelableArrayList("techs");
                for (int i = 0; i < tecs.size(); i++) {
                    Boolean isSold = false;
                    for (int j = 0; j < soldTechnologies.length; j++) {
                        if (soldTechnologies[j].equals(Integer.toString(tecs.get(i).getId()))) {
                            isSold = true;
                        }
                    }
                    if (tecs.get(i).isLearned() && !isSold) learnedTechs.add(tecs.get(i));
                }
                if (learnedTechs.size() > 0) {
                    sellTecInfo.setText("");
                    LearnedTechAdapter peopleAdapter = new LearnedTechAdapter(getApplicationContext(), R.layout.learned_technology_row, learnedTechs);
                    learnedTecList.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    learnedTecList.setLayoutManager(layoutManager);
                    learnedTecList.setAdapter(peopleAdapter);
                } else {
                    sellTecInfo.setText(R.string.you_dont_have_techs_to_sell);
                }
            }
        };
        DbThread.getInstance().addListener(listener);

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
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

    public class LearnedTechHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tecName;
        private final TextView tecPrice;

        private Tecnology learnedTech;
        private Context context;

        public LearnedTechHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.tecName = itemView.findViewById(R.id.tec_name);
            this.tecPrice = itemView.findViewById(R.id.tec_price);

            itemView.setOnClickListener(this);
        }

        public void bindLearnedTech(Tecnology learnedTech) {
            this.learnedTech = learnedTech;
            tecName.setText(learnedTech.getName());
            tecPrice.setText(learnedTech.getPrice() + " $");
        }

        @Override
        public void onClick(View v) {
            allSettings.edit().putInt("CURRENT_TEC_ID", learnedTech.getId()).apply();
            allSettings.edit().putString("CURRENT_TEC_NAME", learnedTech.getName()).apply();
            allSettings.edit().putString("CURRENT_TEC_DESCRIPTION", learnedTech.getDescription()).apply();
            allSettings.edit().putInt("CURRENT_TEC_MONTHS", learnedTech.getMonthsToLearn()).apply();
            allSettings.edit().putLong("CURRENT_TEC_PRICE", learnedTech.getPrice()).apply();
            allSettings.edit().putBoolean("CURRENT_TEC_ISLEARNED", learnedTech.isLearned()).apply();

            sellTecView.setText("\n" + allSettings.getString("CURRENT_TEC_NAME", "").toUpperCase() + "\n\nОписание: \n"
                    + allSettings.getString("CURRENT_TEC_DESCRIPTION", "") + "\n\nДля изучения необходимо: "
                    + allSettings.getInt("CURRENT_TEC_MONTHS", 0) + " мес. \n\nЦена продажи: "
                    + allSettings.getLong("CURRENT_TEC_PRICE", 0) + " $");
            dialogSellTec.show();
        }
    }

    public class LearnedTechAdapter extends RecyclerView.Adapter<LearnedTechHolder> {
        private final List<Tecnology> learnedTechs;
        private Context context;
        private int itemResource;

        LearnedTechAdapter(Context context, int itemResourse, List<Tecnology> learnedTechs) {
            this.context = context;
            this.learnedTechs = learnedTechs;
            this.itemResource = itemResourse;
        }

        @NonNull
        @Override
        public LearnedTechHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(this.itemResource, parent, false);
            return new LearnedTechHolder(this.context, view);
        }

        @Override
        public void onBindViewHolder(@NonNull LearnedTechHolder holder, int position) {
            Tecnology learnedTech = this.learnedTechs.get(position);
            holder.bindLearnedTech(learnedTech);
        }

        @Override
        public int getItemCount() {
            return this.learnedTechs.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
