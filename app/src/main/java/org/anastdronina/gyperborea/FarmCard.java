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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class FarmCard extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SharedPreferences allSettings;
    private TextView tvStatus, tvFarmFarmer, tvFarmName;
    private Button btnChangeFarmer;
    private ImageButton btnToPeople;
    private EditText editFarmName;
    private Spinner spinnerCrops;
    private ArrayAdapter<CharSequence> adapter;
    private DbThread.DbListener listener;
    private int farmId, farmFarmerId, farmStatus;
    private String farmName, farmCrop;
    private Person farmer;
    private ArrayList<Person> farmersAvailable;
    private ArrayList<Person> allPeople;
    private RecyclerView lvChangeFarmer;
    private AlertDialog dialogChangeFarmer, dialogChangeFarmName;
    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_card);

        dbManager = new DbManager();
        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        tvStatus = findViewById(R.id.tvStatus);
        tvFarmFarmer = findViewById(R.id.tvFarmFarmer);
        tvFarmName = findViewById(R.id.tvFarmName);
        editFarmName = new EditText(this);
        btnChangeFarmer = findViewById(R.id.btnChangeFarmer);
        btnToPeople = findViewById(R.id.btnToPeople);
        spinnerCrops = findViewById(R.id.spinnerCrops);
        lvChangeFarmer = new RecyclerView(this);
        dialogChangeFarmer = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogChangeFarmName = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();

        dialogChangeFarmer.setTitle(R.string.choose_farmer);
        dialogChangeFarmer.setView(lvChangeFarmer);
        dialogChangeFarmName.setTitle(R.string.change_farm_name);
        dialogChangeFarmName.setView(editFarmName);

        dialogChangeFarmName.setButton(DialogInterface.BUTTON_POSITIVE, "Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvFarmName.setText(editFarmName.getText());

                dbManager.performQuery("UPDATE " + "farms" + " SET FARM_NAME='" + editFarmName.getText() + "'WHERE FARM_ID='" + farmId + "'");
            }
        });
        tvFarmName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFarmName.setText(tvFarmName.getText());
                dialogChangeFarmName.show();
            }
        });

        adapter = ArrayAdapter.createFromResource(this, R.array.crops, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCrops.setAdapter(adapter);
        spinnerCrops.setOnItemSelectedListener(this);
        btnToPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), People.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        btnChangeFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allSettings.getInt("CURRENT_FARM_STATUS", Farm.NOT_USED) != Farm.NOT_USED) {
                    Toast.makeText(getApplicationContext(), "Переназначить фермера можно будет после сбора урожая ", Toast.LENGTH_SHORT).show();
                } else {
                    farmersAvailable = new ArrayList<>();
                    allPeople = new ArrayList<>();

                    dbManager.loadData(DbManager.WhatData.population);

                    listener = new DbThread.DbListener() {
                        @Override
                        public void onDataLoaded(Bundle bundle) {
                            allPeople = bundle.getParcelableArrayList("allPopulation");
                            if (allPeople != null) {
                                for (int i = 0; i < allPeople.size(); i++) {
                                    if (allPeople.get(i).getJob() == 4) {
                                        farmersAvailable.add(allPeople.get(i));
                                    }
                                }
                                FarmersAdapter farmersAdapter = new FarmersAdapter(getApplicationContext(), R.layout.scientists_row, farmersAvailable);
                                lvChangeFarmer.setHasFixedSize(true);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                lvChangeFarmer.setLayoutManager(layoutManager);
                                lvChangeFarmer.setAdapter(farmersAdapter);
                            }
                        }
                    };
                    DbThread.getInstance().addListener(listener);

                    dialogChangeFarmer.show();
                }
            }
        });

        farmId = allSettings.getInt("CURRENT_FARM_ID", 0);
        farmName = allSettings.getString("CURRENT_FARM_NAME", "");
        farmCrop = allSettings.getString("CURRENT_FARM_CROP", "");
        farmFarmerId = allSettings.getInt("CURRENT_FARM_FARMER_ID", 0);
        farmStatus = allSettings.getInt("CURRENT_FARM_STATUS", Farm.NOT_USED);

        Farm currentFarm = new Farm(farmId, farmName, farmCrop, farmStatus, farmFarmerId);

        tvFarmName.setText(farmName);
        tvStatus.setText("Статус: " + currentFarm.statusString(farmStatus));
        int crop = -1;
        switch (farmCrop) {
            case "Не выбрано":
                crop = 0;
                break;
            case "Огурцы":
                crop = 1;
                break;
            case "Картофель":
                crop = 2;
                break;
            case "Помидоры":
                crop = 3;
                break;
            case "Пшеница":
                crop = 4;
                break;
            case "Рожь":
                crop = 5;
                break;
            case "Лук":
                crop = 6;
                break;
            case "Морковь":
                crop = 7;
                break;
            case "Укроп":
                crop = 8;
                break;
            case "Свёкла":
                crop = 9;
                break;
        }
        spinnerCrops.setSelection(crop);

        if (farmFarmerId == 0) {
            tvFarmFarmer.setText(R.string.farmer_not_pinned);
        } else {
            dbManager.loadData(DbManager.WhatData.population);
            listener = new DbThread.DbListener() {
                @Override
                public void onDataLoaded(Bundle bundle) {
                    allPeople = bundle.getParcelableArrayList("allPopulation");
                    if (allPeople != null) {
                        for (int i = 0; i < allPeople.size(); i++) {
                            if (allPeople.get(i).getId() == farmFarmerId) {
                                farmer = allPeople.get(i);
                            }
                        }
                    }
                    tvFarmFarmer.setText("Ответственный фермер: " + farmer.getName() + " " + farmer.getSurname());
                }
            };
            DbThread.getInstance().addListener(listener);
        }
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("farmer", tvFarmFarmer.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvFarmFarmer.setText(savedInstanceState.getString("farmer"));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (allSettings.getInt("CURRENT_FARM_STATUS", 0) != 0) {
            if (!allSettings.getString("CURRENT_FARM_CROP", "").equals(text)) {
                Toast.makeText(getApplicationContext(), R.string.cant_change_crop, Toast.LENGTH_SHORT).show();
                int previousPosition = Arrays.asList((getResources().getStringArray(R.array.crops))).indexOf(allSettings.getString("CURRENT_FARM_CROP", ""));
                spinnerCrops.setSelection(previousPosition);
            }
        } else {
            if (text.equals("Не выбрано")) {
                dbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Не выбрано" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Огурцы")) {
                dbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Огурцы" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Картофель")) {
                dbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Картофель" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Помидоры")) {
                dbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Помидоры" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Пшеница")) {
                dbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Пшеница" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Рожь")) {
                dbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Рожь" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Лук")) {
                dbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Лук" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Морковь")) {
                dbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Морковь" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Укроп")) {
                dbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Укроп" + "'WHERE FARM_ID='" + farmId + "'");
            }
            if (text.equals("Свёкла")) {
                dbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Свёкла" + "'WHERE FARM_ID='" + farmId + "'");
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class FarmersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView farmerName;
        private final TextView farmerLevel;

        private Person person;
        private Context context;

        public FarmersHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.farmerName = (TextView) itemView.findViewById(R.id.scientistName);
            this.farmerLevel = (TextView) itemView.findViewById(R.id.scientistLevel);

            itemView.setOnClickListener(this);
        }

        public void bindFarmer(Person person) {
            this.person = person;
            farmerName.setText(person.getName() + " \n" + person.getSurname());
            farmerLevel.setText(String.valueOf(person.getFarm()));
        }

        @Override
        public void onClick(View v) {
            int pinnedFarmerId = person.getId();
            allSettings.edit().putString("CURRENT_FARM_FARMER_NAME", person.getName() + " " + person.getSurname()).apply();
            tvFarmFarmer.setText("Ответственный фермер: "
                    + allSettings.getString("CURRENT_FARM_FARMER_NAME", ""));
            dbManager.performQuery("UPDATE " + "farms" + " SET FARM_FARMER_ID='" + pinnedFarmerId + "'WHERE FARM_ID='" + farmId + "'");
            allSettings.edit().putInt("FARMER_IN_USE_ID", pinnedFarmerId).apply();

            dialogChangeFarmer.dismiss();
        }
    }

    public class FarmersAdapter extends RecyclerView.Adapter<FarmersHolder> {
        private final List<Person> farmers;
        private Context context;
        private int itemResource;

        FarmersAdapter(Context context, int itemResourse, List<Person> farmers) {
            this.context = context;
            this.farmers = farmers;
            this.itemResource = itemResourse;
        }

        @NonNull
        @Override
        public FarmersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(this.itemResource, parent, false);
            return new FarmersHolder(this.context, view);
        }

        @Override
        public void onBindViewHolder(@NonNull FarmersHolder holder, int position) {
            Person person = this.farmers.get(position);
            holder.bindFarmer(person);
        }

        @Override
        public int getItemCount() {
            return this.farmers.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
