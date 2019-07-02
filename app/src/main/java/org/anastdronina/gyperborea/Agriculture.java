package org.anastdronina.gyperborea;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class Agriculture extends AppCompatActivity {

    private RecyclerView rvAgriculture;
    private ArrayList<Farm> mFarmsList;
    private DbThread.DbListener mListener;
    private SharedPreferences mSettings;
    private TextView mDate;
    private TextView mMoneyR;
    private TextView mMoneyD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agriculture);

        mSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        mDate = findViewById(R.id.date);
        mMoneyR = findViewById(R.id.moneyR);
        mMoneyD = findViewById(R.id.moneyD);
        rvAgriculture = findViewById(R.id.agricultureList);
        mFarmsList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        DbManager dbManager = new DbManager();
        dbManager.loadData(DbManager.WhatData.FARMS);
        mListener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                mFarmsList = bundle.getParcelableArrayList("farms");
                FarmAdapter farmAdapter = new FarmAdapter(getApplicationContext(), R.layout.farms_row, mFarmsList);
                rvAgriculture.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                rvAgriculture.setLayoutManager(layoutManager);
                rvAgriculture.setAdapter(farmAdapter);
            }
        };
        DbThread.getInstance().addListener(mListener);

        DateAndMoney dateAndMoney = new DateAndMoney();
        mDate.setText(dateAndMoney.getDate(mSettings));
        mMoneyD.setText(dateAndMoney.getMoney(mSettings, "$"));
        mMoneyR.setText(dateAndMoney.getMoney(mSettings, "руб"));
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

    public class FarmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView farmName;
        private final TextView farmsCrop;
        private final TextView farmsStatus;

        private Farm farm;
        private Context context;

        public FarmHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.farmName = itemView.findViewById(R.id.farmName);
            this.farmsCrop = itemView.findViewById(R.id.farmsCrop);
            this.farmsStatus = itemView.findViewById(R.id.farmsStatus);

            itemView.setOnClickListener(this);
        }

        public void bindFarm(Farm farm) {
            this.farm = farm;
            farmName.setText(farm.getName());
            farmsCrop.setText(farm.getCrop());
            farmsStatus.setText(farm.statusString(farm.getStatus()));
        }

        @Override
        public void onClick(View v) {
            mSettings.edit().putInt("CURRENT_FARM_ID", farm.getId()).apply();
            mSettings.edit().putString("CURRENT_FARM_NAME", farm.getName()).apply();
            mSettings.edit().putString("CURRENT_FARM_CROP", farm.getCrop()).apply();
            mSettings.edit().putInt("CURRENT_FARM_STATUS", farm.getStatus()).apply();
            mSettings.edit().putInt("CURRENT_FARM_FARMER_ID", farm.getFarmerId()).apply();

            startActivity(new Intent(getApplicationContext(), FarmCard.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public class FarmAdapter extends RecyclerView.Adapter<FarmHolder> {
        private final List<Farm> farms;
        private Context context;
        private int itemResource;

        FarmAdapter(Context context, int itemResourse, List<Farm> farms) {
            this.context = context;
            this.farms = farms;
            this.itemResource = itemResourse;
        }

        @NonNull
        @Override
        public FarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(this.itemResource, parent, false);
            return new FarmHolder(this.context, view);
        }

        @Override
        public void onBindViewHolder(@NonNull FarmHolder holder, int position) {
            Farm farm = this.farms.get(position);
            holder.bindFarm(farm);
        }

        @Override
        public int getItemCount() {
            return this.farms.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
