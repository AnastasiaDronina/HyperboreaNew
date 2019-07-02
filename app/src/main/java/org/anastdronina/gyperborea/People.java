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

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class People extends AppCompatActivity {

    private TextView tvDate;
    private TextView tvMoneyD;
    private TextView tvMoneyR;
    private RecyclerView rvPeopleList;
    private ArrayList<Person> mPopulationList;
    private SharedPreferences mSettings;
    private DbThread.DbListener mListener;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        tvDate = findViewById(R.id.date);
        tvMoneyD = findViewById(R.id.moneyD);
        tvMoneyR = findViewById(R.id.moneyR);
        rvPeopleList = findViewById(R.id.peopleList);

        mSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        mPopulationList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DbManager mDbManager = new DbManager();
        mDbManager.loadData(DbManager.WhatData.POPULATION);
        mListener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                mPopulationList = bundle.getParcelableArrayList("allPopulation");
                PeopleAdapter peopleAdapter
                        = new PeopleAdapter(getApplicationContext(), R.layout.people_row, mPopulationList);
                rvPeopleList.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                rvPeopleList.setLayoutManager(layoutManager);
                rvPeopleList.setAdapter(peopleAdapter);
            }
        };
        DbThread.getInstance().addListener(mListener);

        DateAndMoney mDateAndMoney = new DateAndMoney();
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public class PeopleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView rowName;
        private final TextView rowJob;
        private final TextView rowSalary;
        private final TextView rowAge;

        private Person person;
        private Context context;

        public PeopleHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.rowName = itemView.findViewById(R.id.personName);
            this.rowJob = itemView.findViewById(R.id.personJob);
            this.rowSalary = itemView.findViewById(R.id.personSalary);
            this.rowAge = itemView.findViewById(R.id.personAge);

            itemView.setOnClickListener(this);
        }

        public void bindPerson(Person person) {
            this.person = person;
            rowName.setText(person.getName() + " \n" + person.getSurname());
            rowJob.setText(getResources().getStringArray(R.array.jobs)[person.getJob()]);
            rowSalary.setText(String.valueOf(person.getSalary()));
            rowAge.setText(String.valueOf(person.getAge()));
        }

        @Override
        public void onClick(View v) {

            mSettings.edit().putInt("CURRENT_PERS_ID", person.getId()).apply();
            mSettings.edit().putString("CURRENT_PERS_NAME", person.getName()).apply();
            mSettings.edit().putString("CURRENT_PERS_SURNAME", person.getSurname()).apply();
            mSettings.edit().putInt("CURRENT_PERS_JOB", person.getJob()).apply();
            mSettings.edit().putInt("CURRENT_PERS_SALARY", person.getSalary()).apply();
            mSettings.edit().putInt("CURRENT_PERS_AGE", person.getAge()).apply();
            mSettings.edit().putInt("CURRENT_PERS_BUILDING", person.getBuilding()).apply();
            mSettings.edit().putInt("CURRENT_PERS_MANUFACTURE", person.getManufacture()).apply();
            mSettings.edit().putInt("CURRENT_PERS_FARM", person.getFarm()).apply();
            mSettings.edit().putInt("CURRENT_PERS_ATHLETIC", person.getAthletic()).apply();
            mSettings.edit().putInt("CURRENT_PERS_LEARNING", person.getLearning()).apply();
            mSettings.edit().putInt("CURRENT_PERS_TALKING", person.getTalking()).apply();
            mSettings.edit().putInt("CURRENT_PERS_STRENGTH", person.getStrength()).apply();
            mSettings.edit().putInt("CURRENT_PERS_ART", person.getArt()).apply();
            mSettings.edit().putString("CURRENT_PERS_TRAIT_1", person.getTraits().get(0)).apply();
            mSettings.edit().putString("CURRENT_PERS_TRAIT_2", person.getTraits().get(1)).apply();
            mSettings.edit().putString("CURRENT_PERS_TRAIT_3", person.getTraits().get(2)).apply();

            startActivity(new Intent(getApplicationContext(), PersonCard.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public class PeopleAdapter extends RecyclerView.Adapter<PeopleHolder> {
        private ArrayList<Person> people;
        private Context context;
        private int itemResource;

        PeopleAdapter(Context context, int itemResourse, ArrayList<Person> people) {
            this.context = context;
            this.people = people;
            this.itemResource = itemResourse;
        }

        @NonNull
        @Override
        public PeopleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(this.itemResource, parent, false);
            return new PeopleHolder(this.context, view);
        }

        @Override
        public void onBindViewHolder(@NonNull PeopleHolder holder, int position) {
            Person person = this.people.get(position);
            holder.bindPerson(person);
        }

        @Override
        public int getItemCount() {
            return this.people.size();
        }
    }
}