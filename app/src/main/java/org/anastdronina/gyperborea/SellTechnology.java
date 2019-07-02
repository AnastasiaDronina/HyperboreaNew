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
    private TextView tvSellTecInfo;
    private TextView tvSellTecView;
    private TextView tvDate;
    private TextView tvMoneyD;
    private TextView tvMoneyR;
    private RecyclerView rvLearnedTechs;
    private AlertDialog dialogSellTec;
    private DbThread.DbListener mListener;
    private ArrayList<Technology> mTechsList;
    private String[] mSoldTechsArray;
    private ArrayList<Technology> mLearnedTechsList;
    private SharedPreferences mSettings;
    private DateAndMoney mDateAndMoney;
    private DbManager mDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_technology);

        mDbManager = new DbManager();
        mSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        tvSellTecInfo = findViewById(R.id.sellTecInfo);
        rvLearnedTechs = findViewById(R.id.learnedTecList);
        dialogSellTec = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogSellTec.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        tvSellTecView = new TextView(this);
        tvDate = findViewById(R.id.date);
        tvMoneyD = findViewById(R.id.moneyD);
        tvMoneyR = findViewById(R.id.moneyR);
        mDateAndMoney = new DateAndMoney();

        dialogSellTec.setTitle(R.string.sell_tech);
        dialogSellTec.setView(tvSellTecView);

        dialogSellTec.setButton(DialogInterface.BUTTON_POSITIVE, "Продать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tecName = mSettings.getString("CURRENT_TEC_NAME", "");
                long tecPrice = mSettings.getLong("CURRENT_TEC_PRICE", 0);
                long res = mSettings.getLong("MONEY_DOLLARS", 0) + tecPrice;
                mSettings.edit().putLong("MONEY_DOLLARS", res).apply();
                for (int i = 0; i < mLearnedTechsList.size(); i++) {
                    if (mLearnedTechsList.get(i).getName().equals(tecName)) {
                        int id = mLearnedTechsList.get(i).getId();
                        mSettings.edit().putString("SOLD_TECHNOLOGIES", mSettings.getString("SOLD_TECHNOLOGIES", "") + id + ",").apply();
                        mLearnedTechsList.remove(i);
                        break;
                    }
                }

                mDbManager.loadData(DbManager.WhatData.TECH);

                mListener = new DbThread.DbListener() {
                    @Override
                    public void onDataLoaded(Bundle bundle) {
                        mTechsList = bundle.getParcelableArrayList("techs");
                        mSoldTechsArray = mSettings.getString("SOLD_TECHNOLOGIES", "").split(",");
                        for (int i = 0; i < mTechsList.size(); i++) {
                            Boolean isSold = false;
                            for (int j = 0; j < mSoldTechsArray.length; j++) {
                                if (mSoldTechsArray[j].equals(Integer.toString(mTechsList.get(i).getId()))) {
                                    isSold = true;
                                }
                            }
                            if (mTechsList.get(i).isLearned() && !isSold)
                                mLearnedTechsList.add(mTechsList.get(i));
                        }
                        if (mLearnedTechsList.size() > 0) {
                            tvSellTecInfo.setText("");
                            LearnedTechAdapter peopleAdapter
                                    = new LearnedTechAdapter(getApplicationContext(), R.layout.learned_technology_row, mLearnedTechsList);
                            rvLearnedTechs.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            rvLearnedTechs.setLayoutManager(layoutManager);
                            rvLearnedTechs.setAdapter(peopleAdapter);
                        } else {
                            tvSellTecInfo.setText(R.string.you_dont_have_techs_to_sell);
                        }
                    }
                };
                DbThread.getInstance().addListener(mListener);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSoldTechsArray = mSettings.getString("SOLD_TECHNOLOGIES", "").split(",");
        mLearnedTechsList = new ArrayList<>();

        mDbManager.loadData(DbManager.WhatData.TECH);

        mListener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                mTechsList = bundle.getParcelableArrayList("techs");
                for (int i = 0; i < mTechsList.size(); i++) {
                    Boolean isSold = false;
                    for (int j = 0; j < mSoldTechsArray.length; j++) {
                        if (mSoldTechsArray[j].equals(Integer.toString(mTechsList.get(i).getId()))) {
                            isSold = true;
                        }
                    }
                    if (mTechsList.get(i).isLearned() && !isSold)
                        mLearnedTechsList.add(mTechsList.get(i));
                }
                if (mLearnedTechsList.size() > 0) {
                    tvSellTecInfo.setText("");
                    LearnedTechAdapter peopleAdapter
                            = new LearnedTechAdapter(getApplicationContext(), R.layout.learned_technology_row, mLearnedTechsList);
                    rvLearnedTechs.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    rvLearnedTechs.setLayoutManager(layoutManager);
                    rvLearnedTechs.setAdapter(peopleAdapter);
                } else {
                    tvSellTecInfo.setText(R.string.you_dont_have_techs_to_sell);
                }
            }
        };
        DbThread.getInstance().addListener(mListener);

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

    public class LearnedTechHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tecName;
        private final TextView tecPrice;
        private Technology learnedTech;
        private Context context;

        public LearnedTechHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.tecName = itemView.findViewById(R.id.tec_name);
            this.tecPrice = itemView.findViewById(R.id.tec_price);

            itemView.setOnClickListener(this);
        }

        public void bindLearnedTech(Technology learnedTech) {
            this.learnedTech = learnedTech;
            tecName.setText(learnedTech.getName());
            tecPrice.setText(learnedTech.getPrice() + " $");
        }

        @Override
        public void onClick(View v) {
            mSettings.edit().putInt("CURRENT_TEC_ID", learnedTech.getId()).apply();
            mSettings.edit().putString("CURRENT_TEC_NAME", learnedTech.getName()).apply();
            mSettings.edit().putString("CURRENT_TEC_DESCRIPTION", learnedTech.getDescription()).apply();
            mSettings.edit().putInt("CURRENT_TEC_MONTHS", learnedTech.getMonthsToLearn()).apply();
            mSettings.edit().putLong("CURRENT_TEC_PRICE", learnedTech.getPrice()).apply();
            mSettings.edit().putBoolean("CURRENT_TEC_ISLEARNED", learnedTech.isLearned()).apply();

            tvSellTecView.setText("\n" + mSettings.getString("CURRENT_TEC_NAME", "")
                    .toUpperCase() + "\n\nОписание: \n" + mSettings.getString("CURRENT_TEC_DESCRIPTION", "")
                    + "\n\nДля изучения необходимо: " + mSettings.getInt("CURRENT_TEC_MONTHS", 0)
                    + " мес. \n\nЦена продажи: " + mSettings.getLong("CURRENT_TEC_PRICE", 0) + " $");
            dialogSellTec.show();
        }
    }

    public class LearnedTechAdapter extends RecyclerView.Adapter<LearnedTechHolder> {
        private final List<Technology> learnedTechs;
        private Context context;
        private int itemResource;

        LearnedTechAdapter(Context context, int itemResourse, List<Technology> learnedTechs) {
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
            Technology learnedTech = this.learnedTechs.get(position);
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
