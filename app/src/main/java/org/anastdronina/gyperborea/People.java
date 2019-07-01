package org.anastdronina.gyperborea;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

    private ArrayList<Person> population;
    private RecyclerView peopleList;
    private SharedPreferences allSettings;
    private DateAndMoney dateAndMoney;
    private TextView date, moneyD, moneyR;
    private DbThread.DbListener listener;
    private Handler handler;
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        handler = new Handler();
        date = findViewById(R.id.date);
        moneyD = findViewById(R.id.moneyD);
        moneyR = findViewById(R.id.moneyR);

        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        dateAndMoney = new DateAndMoney();

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));

        population = new ArrayList<>();
        peopleList = findViewById(R.id.peopleList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        message = handler.obtainMessage(DbThread.LOAD_POPULATION_DATA);
        DbThread.getBackgroundHandler().sendMessage(message);

        listener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                population = bundle.getParcelableArrayList("allPopulation");
                PeopleAdapter peopleAdapter = new PeopleAdapter(getApplicationContext(), R.layout.people_row, population);
                peopleList.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                peopleList.setLayoutManager(layoutManager);
                peopleList.setAdapter(peopleAdapter);
            }
        };
        DbThread.getInstance().addListener(listener);
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

            this.rowName = (TextView) itemView.findViewById(R.id.personName);
            this.rowJob = (TextView) itemView.findViewById(R.id.personJob);
            this.rowSalary = (TextView) itemView.findViewById(R.id.personSalary);
            this.rowAge = (TextView) itemView.findViewById(R.id.personAge);

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

            allSettings.edit().putInt("CURRENT_PERS_ID", person.getId()).apply();
            allSettings.edit().putString("CURRENT_PERS_NAME", person.getName()).apply();
            allSettings.edit().putString("CURRENT_PERS_SURNAME", person.getSurname()).apply();
            allSettings.edit().putInt("CURRENT_PERS_JOB", person.getJob()).apply();
            allSettings.edit().putInt("CURRENT_PERS_SALARY", person.getSalary()).apply();
            allSettings.edit().putInt("CURRENT_PERS_AGE", person.getAge()).apply();
            allSettings.edit().putInt("CURRENT_PERS_BUILDING", person.getBuilding()).apply();
            allSettings.edit().putInt("CURRENT_PERS_MANUFACTURE", person.getManufacture()).apply();
            allSettings.edit().putInt("CURRENT_PERS_FARM", person.getFarm()).apply();
            allSettings.edit().putInt("CURRENT_PERS_ATHLETIC", person.getAthletic()).apply();
            allSettings.edit().putInt("CURRENT_PERS_LEARNING", person.getLearning()).apply();
            allSettings.edit().putInt("CURRENT_PERS_TALKING", person.getTalking()).apply();
            allSettings.edit().putInt("CURRENT_PERS_STRENGTH", person.getStrength()).apply();
            allSettings.edit().putInt("CURRENT_PERS_ART", person.getArt()).apply();
            allSettings.edit().putString("CURRENT_PERS_TRAIT_1", person.getTraits().get(0)).apply();
            allSettings.edit().putString("CURRENT_PERS_TRAIT_2", person.getTraits().get(1)).apply();
            allSettings.edit().putString("CURRENT_PERS_TRAIT_3", person.getTraits().get(2)).apply();

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