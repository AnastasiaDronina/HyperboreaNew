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
import java.util.List;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Agriculture extends AppCompatActivity {

    private RecyclerView agricultureLv;
    private ArrayList<Farm> farmsList;
    private DbThread.DbListener listener;
    private SharedPreferences allSettings;
    private DateAndMoney dateAndMoney;
    private TextView date, moneyR, moneyD;
    private Handler handler;
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agriculture);

        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        dateAndMoney = new DateAndMoney();
        date = findViewById(R.id.date);
        moneyR = findViewById(R.id.moneyR);
        moneyD = findViewById(R.id.moneyD);

        agricultureLv = findViewById(R.id.agricultureList);

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));

        farmsList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler = new Handler();
        message = handler.obtainMessage(4);
        DbThread.getBackgroundHandler().sendMessage(message);

        listener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                farmsList = bundle.getParcelableArrayList("farms");
                FarmAdapter farmAdapter = new FarmAdapter(getApplicationContext(), R.layout.farms_row, farmsList);
                agricultureLv.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                agricultureLv.setLayoutManager(layoutManager);
                agricultureLv.setAdapter(farmAdapter);
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

    public class FarmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView farmName;
        private final TextView farmsCrop;
        private final TextView farmsStatus;

        private Farm farm;
        private Context context;

        public FarmHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.farmName = (TextView) itemView.findViewById(R.id.farmName);
            this.farmsCrop = (TextView) itemView.findViewById(R.id.farmsCrop);
            this.farmsStatus = (TextView) itemView.findViewById(R.id.farmsStatus);

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
            allSettings.edit().putInt("CURRENT_FARM_ID", farm.getId()).apply();
            allSettings.edit().putString("CURRENT_FARM_NAME", farm.getName()).apply();
            allSettings.edit().putString("CURRENT_FARM_CROP", farm.getCrop()).apply();
            allSettings.edit().putInt("CURRENT_FARM_STATUS", farm.getStatus()).apply();
            allSettings.edit().putInt("CURRENT_FARM_FARMER_ID", farm.getFarmerId()).apply();

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
