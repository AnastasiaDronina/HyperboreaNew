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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Market extends AppCompatActivity {

    private RecyclerView rvMarket;
    private AlertDialog dialogBuyItem;
    private TextView tvForDialogBuyItem;
    private TextView tvDate;
    private TextView tvMoneyR;
    private TextView tvMoneyD;
    private DateAndMoney mDateAndMoney;
    private ArrayList<MarketItem> mMarketItemsList;
    private SharedPreferences mSettings;
    private DbThread.DbListener mListener;
    private DbManager mDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        mDbManager = new DbManager();
        rvMarket = findViewById(R.id.lvMarket);
        mSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        dialogBuyItem = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogBuyItem.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        tvForDialogBuyItem = new TextView(this);
        mDateAndMoney = new DateAndMoney();
        tvDate = findViewById(R.id.date);
        tvMoneyR = findViewById(R.id.moneyR);
        tvMoneyD = findViewById(R.id.moneyD);

        dialogBuyItem.setTitle(R.string.buy_item);
        dialogBuyItem.setView(tvForDialogBuyItem);


        dialogBuyItem.setButton(DialogInterface.BUTTON_POSITIVE, "Купить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int price = mSettings.getInt("CURRENT_ITEM_PRICE", 0);
                String currency = mSettings.getString("CURRENT_ITEM_CURRENCY", "");
                if (currency.equals("руб")) {
                    long moneyRubbles = mSettings.getLong("MONEY_RUBLES", 0);
                    mSettings.edit().putLong("MONEY_RUBLES", moneyRubbles - price).apply();
                } else {
                    long moneyDollars = mSettings.getLong("MONEY_DOLLARS", 0);
                    mSettings.edit().putLong("MONEY_DOLLARS", moneyDollars - price).apply();
                }

                mDbManager.insertStockData(mSettings.getString("CURRENT_ITEM_NAME", ""),
                        mSettings.getString("CURRENT_ITEM_TYPE", ""),
                        mSettings.getInt("CURRENT_ITEM_AMOUNT", 0));

                //delete from market
                mDbManager.performQuery("delete from market where ITEM_ID='"
                        + mSettings.getInt("CURRENT_ITEM_ID", 0) + "'");

                mMarketItemsList = new ArrayList<>();
                mDbManager.loadData(DbManager.WhatData.MARKET);

                mListener = new DbThread.DbListener() {
                    @Override
                    public void onDataLoaded(Bundle bundle) {
                        mMarketItemsList = bundle.getParcelableArrayList("marketItems");
                        MarketAdapter peopleAdapter
                                = new MarketAdapter(getApplicationContext(), R.layout.market_row, mMarketItemsList);
                        rvMarket.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        rvMarket.setLayoutManager(layoutManager);
                        rvMarket.setAdapter(peopleAdapter);
                    }
                };
                DbThread.getInstance().addListener(mListener);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMarketItemsList = new ArrayList<>();
        mDbManager.loadData(DbManager.WhatData.MARKET);

        mListener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                mMarketItemsList = bundle.getParcelableArrayList("marketItems");
                MarketAdapter marketAdapter
                        = new MarketAdapter(getApplicationContext(), R.layout.market_row, mMarketItemsList);
                rvMarket.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                rvMarket.setLayoutManager(layoutManager);
                rvMarket.setAdapter(marketAdapter);
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

    public class MarketHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView marketItemName;
        private final TextView marketItemAmount;
        private final TextView marketItemPrice;

        private MarketItem marketItem;
        private Context context;

        public MarketHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.marketItemName = itemView.findViewById(R.id.marketItemName);
            this.marketItemAmount = itemView.findViewById(R.id.marketItemAmount);
            this.marketItemPrice = itemView.findViewById(R.id.marketItemPrice);

            itemView.setOnClickListener(this);
        }

        public void bindMarketItem(MarketItem marketItem) {
            this.marketItem = marketItem;

            String amount;

            if (marketItem.getType().equals("Еда")) {
                amount = marketItem.getAmount() + " кг ";
            } else {
                amount = marketItem.getAmount() + " шт ";
            }

            marketItemName.setText(marketItem.getName());
            marketItemAmount.setText(amount);
            marketItemPrice.setText(String.valueOf(marketItem.getPrice()));

        }

        @Override
        public void onClick(View v) {
            boolean gotEnoughtMoney = false;
            if (marketItem.getCurrency().equals("руб")) {
                if (mSettings.getLong("MONEY_RUBLES", 0) >= marketItem.getPrice()) {
                    gotEnoughtMoney = true;
                }
            }
            if (marketItem.getCurrency().equals("$")) {
                if (mSettings.getLong("MONEY_DOLLARS", 0) >= marketItem.getPrice()) {
                    gotEnoughtMoney = true;
                }
            }
            if (!gotEnoughtMoney) {
                Toast.makeText(getApplicationContext(), R.string.not_enough_money_to_buy, Toast.LENGTH_SHORT).show();
            } else {
                mSettings.edit().putInt("CURRENT_ITEM_ID", marketItem.getId()).apply();
                mSettings.edit().putString("CURRENT_ITEM_NAME", marketItem.getName()).apply();
                mSettings.edit().putInt("CURRENT_ITEM_AMOUNT", marketItem.getAmount()).apply();
                mSettings.edit().putInt("CURRENT_ITEM_PRICE", marketItem.getPrice()).apply();
                mSettings.edit().putString("CURRENT_ITEM_CURRENCY", marketItem.getCurrency()).apply();
                mSettings.edit().putString("CURRENT_ITEM_TYPE", marketItem.getType()).apply();

                String type;

                if (mSettings.getString("CURRENT_ITEM_TYPE", "").equals("Еда")) {
                    type = "кг";
                } else {
                    type = "шт";
                }

                tvForDialogBuyItem.setText("\n" + mSettings.getString("CURRENT_ITEM_NAME", "")
                        + " " + mSettings.getInt("CURRENT_ITEM_AMOUNT", 0)
                        + " " + type + "\n\nЦена: "
                        + mSettings.getInt("CURRENT_ITEM_PRICE", 0) + " "
                        + mSettings.getString("CURRENT_ITEM_CURRENCY", ""));
                dialogBuyItem.show();

            }
        }
    }

    public class MarketAdapter extends RecyclerView.Adapter<MarketHolder> {
        private final List<MarketItem> marketItems;
        private Context context;
        private int itemResource;

        MarketAdapter(Context context, int itemResourse, List<MarketItem> marketItems) {
            this.context = context;
            this.marketItems = marketItems;
            this.itemResource = itemResourse;
        }

        @NonNull
        @Override
        public MarketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(this.itemResource, parent, false);
            return new MarketHolder(this.context, view);
        }

        @Override
        public void onBindViewHolder(@NonNull MarketHolder holder, int position) {
            MarketItem marketItem = this.marketItems.get(position);
            holder.bindMarketItem(marketItem);
        }

        @Override
        public int getItemCount() {
            return this.marketItems.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
