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
    private TextView tvStatus;
    private TextView tvFarmFarmer;
    private TextView tvFarmName;
    private Button btnChangeFarmer;
    private ImageButton btnToPeople;
    private EditText etFarmName;
    private Spinner spinnerCrops;
    private RecyclerView rvChangeFarmer;
    private AlertDialog dialogChangeFarmer;
    private AlertDialog dialogChangeFarmName;
    private ArrayAdapter<CharSequence> mAdapter;
    private SharedPreferences mSettings;
    private DbThread.DbListener mListener;
    private int mFarmId;
    private int mFarmFarmerId;
    private int mFarmStatus;
    private String mFarmName;
    private String mFarmCrop;
    private Person mFarmer;
    private ArrayList<Person> mFarmersAvailableList;
    private ArrayList<Person> mPeopleList;
    private DbManager mDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_card);

        mDbManager = new DbManager();
        mSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        tvStatus = findViewById(R.id.tvStatus);
        tvFarmFarmer = findViewById(R.id.tvFarmFarmer);
        tvFarmName = findViewById(R.id.tvFarmName);
        etFarmName = new EditText(this);
        btnChangeFarmer = findViewById(R.id.btnChangeFarmer);
        btnToPeople = findViewById(R.id.btnToPeople);
        spinnerCrops = findViewById(R.id.spinnerCrops);
        rvChangeFarmer = new RecyclerView(this);
        dialogChangeFarmer = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogChangeFarmName = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();

        dialogChangeFarmer.setTitle(R.string.choose_farmer);
        dialogChangeFarmer.setView(rvChangeFarmer);
        dialogChangeFarmName.setTitle(R.string.change_farm_name);
        dialogChangeFarmName.setView(etFarmName);

        dialogChangeFarmName.setButton(DialogInterface.BUTTON_POSITIVE, "Сохранить",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvFarmName.setText(etFarmName.getText());

                mDbManager.performQuery("UPDATE " + "farms" + " SET FARM_NAME='" + etFarmName.getText()
                        + "'WHERE FARM_ID='" + mFarmId + "'");
            }
        });
        tvFarmName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFarmName.setText(tvFarmName.getText());
                dialogChangeFarmName.show();
            }
        });

        mAdapter = ArrayAdapter.createFromResource(this, R.array.crops, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCrops.setAdapter(mAdapter);
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
                if (mSettings.getInt("CURRENT_FARM_STATUS", Farm.NOT_USED) != Farm.NOT_USED) {
                    Toast.makeText(getApplicationContext(), R.string.cant_change_farmer,
                            Toast.LENGTH_SHORT).show();
                } else {
                    mFarmersAvailableList = new ArrayList<>();
                    mPeopleList = new ArrayList<>();

                    mDbManager.loadData(DbManager.WhatData.POPULATION);

                    mListener = new DbThread.DbListener() {
                        @Override
                        public void onDataLoaded(Bundle bundle) {
                            mPeopleList = bundle.getParcelableArrayList("allPopulation");
                            if (mPeopleList != null) {
                                for (int i = 0; i < mPeopleList.size(); i++) {
                                    if (mPeopleList.get(i).getJob() == 4) {
                                        mFarmersAvailableList.add(mPeopleList.get(i));
                                    }
                                }
                                FarmersAdapter farmersAdapter
                                        = new FarmersAdapter(getApplicationContext(), R.layout.scientists_row, mFarmersAvailableList);
                                rvChangeFarmer.setHasFixedSize(true);
                                RecyclerView.LayoutManager layoutManager
                                        = new LinearLayoutManager(getApplicationContext());
                                rvChangeFarmer.setLayoutManager(layoutManager);
                                rvChangeFarmer.setAdapter(farmersAdapter);
                            }
                        }
                    };
                    DbThread.getInstance().addListener(mListener);

                    dialogChangeFarmer.show();
                }
            }
        });

        mFarmId = mSettings.getInt("CURRENT_FARM_ID", 0);
        mFarmName = mSettings.getString("CURRENT_FARM_NAME", "");
        mFarmCrop = mSettings.getString("CURRENT_FARM_CROP", "");
        mFarmFarmerId = mSettings.getInt("CURRENT_FARM_FARMER_ID", 0);
        mFarmStatus = mSettings.getInt("CURRENT_FARM_STATUS", Farm.NOT_USED);

        Farm currentFarm = new Farm(mFarmId, mFarmName, mFarmCrop, mFarmStatus, mFarmFarmerId);

        tvFarmName.setText(mFarmName);
        tvStatus.setText("Статус: " + currentFarm.statusString(mFarmStatus));
        int crop = -1;
        switch (mFarmCrop) {
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

        if (mFarmFarmerId == 0) {
            tvFarmFarmer.setText(R.string.farmer_not_pinned);
        } else {
            mDbManager.loadData(DbManager.WhatData.POPULATION);
            mListener = new DbThread.DbListener() {
                @Override
                public void onDataLoaded(Bundle bundle) {
                    mPeopleList = bundle.getParcelableArrayList("allPopulation");
                    if (mPeopleList != null) {
                        for (int i = 0; i < mPeopleList.size(); i++) {
                            if (mPeopleList.get(i).getId() == mFarmFarmerId) {
                                mFarmer = mPeopleList.get(i);
                            }
                        }
                    }
                    tvFarmFarmer.setText("Ответственный фермер: " + mFarmer.getName() + " " + mFarmer.getSurname());
                }
            };
            DbThread.getInstance().addListener(mListener);
        }
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
        if (mSettings.getInt("CURRENT_FARM_STATUS", 0) != 0) {
            if (!mSettings.getString("CURRENT_FARM_CROP", "").equals(text)) {
                Toast.makeText(getApplicationContext(), R.string.cant_change_crop,
                        Toast.LENGTH_SHORT).show();
                int previousPosition = Arrays.asList((getResources().getStringArray(R.array.crops)))
                        .indexOf(mSettings.getString("CURRENT_FARM_CROP", ""));
                spinnerCrops.setSelection(previousPosition);
            }
        } else {
            if (text.equals("Не выбрано")) {
                mDbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Не выбрано"
                        + "'WHERE FARM_ID='" + mFarmId + "'");
            }
            if (text.equals("Огурцы")) {
                mDbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Огурцы"
                        + "'WHERE FARM_ID='" + mFarmId + "'");
            }
            if (text.equals("Картофель")) {
                mDbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Картофель"
                        + "'WHERE FARM_ID='" + mFarmId + "'");
            }
            if (text.equals("Помидоры")) {
                mDbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Помидоры"
                        + "'WHERE FARM_ID='" + mFarmId + "'");
            }
            if (text.equals("Пшеница")) {
                mDbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Пшеница"
                        + "'WHERE FARM_ID='" + mFarmId + "'");
            }
            if (text.equals("Рожь")) {
                mDbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Рожь"
                        + "'WHERE FARM_ID='" + mFarmId + "'");
            }
            if (text.equals("Лук")) {
                mDbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Лук"
                        + "'WHERE FARM_ID='" + mFarmId + "'");
            }
            if (text.equals("Морковь")) {
                mDbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Морковь"
                        + "'WHERE FARM_ID='" + mFarmId + "'");
            }
            if (text.equals("Укроп")) {
                mDbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Укроп"
                        + "'WHERE FARM_ID='" + mFarmId + "'");
            }
            if (text.equals("Свёкла")) {
                mDbManager.performQuery("UPDATE " + "farms" + " SET FARM_CROP='" + "Свёкла"
                        + "'WHERE FARM_ID='" + mFarmId + "'");
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

            this.farmerName = itemView.findViewById(R.id.scientistName);
            this.farmerLevel = itemView.findViewById(R.id.scientistLevel);

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
            mSettings.edit().putString("CURRENT_FARM_FARMER_NAME", person.getName() + " " + person.getSurname()).apply();
            tvFarmFarmer.setText("Ответственный фермер: "
                    + mSettings.getString("CURRENT_FARM_FARMER_NAME", ""));
            mDbManager.performQuery("UPDATE " + "farms" + " SET FARM_FARMER_ID='" + pinnedFarmerId
                    + "'WHERE FARM_ID='" + mFarmId + "'");
            mSettings.edit().putInt("FARMER_IN_USE_ID", pinnedFarmerId).apply();

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
