package org.anastdronina.gyperborea;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

    private RecyclerView lvMarket;
    private DateAndMoney dateAndMoney;
    private ArrayList<MarketItem> marketItems;
    private SharedPreferences allSettings;
    private AlertDialog dialogBuyItem;
    private TextView tvForDialogBuyItem, date, moneyR, moneyD;
    private DbThread.DbListener listener;
    private Handler handler;
    private Message message;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        lvMarket = findViewById(R.id.lvMarket);
        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        dialogBuyItem = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogBuyItem.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        tvForDialogBuyItem = new TextView(this);
        dateAndMoney = new DateAndMoney();
        date = findViewById(R.id.date);
        moneyR = findViewById(R.id.moneyR);
        moneyD = findViewById(R.id.moneyD);

        dialogBuyItem.setTitle("Купить товар? ");
        dialogBuyItem.setView(tvForDialogBuyItem);


        dialogBuyItem.setButton(DialogInterface.BUTTON_POSITIVE, "Купить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int price = allSettings.getInt("CURRENT_ITEM_PRICE", 0);
                String currency = allSettings.getString("CURRENT_ITEM_CURRENCY", "");
                if (currency.equals("руб")) {
                    long moneyRubbles = allSettings.getLong("MONEY_RUBLES", 0);
                    allSettings.edit().putLong("MONEY_RUBLES", moneyRubbles - price).apply();
                } else {
                    long moneyDollars = allSettings.getLong("MONEY_DOLLARS", 0);
                    allSettings.edit().putLong("MONEY_DOLLARS", moneyDollars - price).apply();
                }

                //add to stock
                DatabaseThread databaseThread = new DatabaseThread();
                databaseThread.start();

                //delete from market
                bundle = new Bundle();
                bundle.putString("query", "delete from market where ITEM_ID='" + allSettings.getInt("CURRENT_ITEM_ID", 0) + "'");
                handler = new Handler();
                message = handler.obtainMessage(0);
                message.setData(bundle);
                DbThread.getBackgroundHandler().sendMessage(message);

                marketItems = new ArrayList<>();

                message = handler.obtainMessage(5);
                DbThread.getBackgroundHandler().sendMessage(message);
                listener = new DbThread.DbListener() {
                    @Override
                    public void onDataLoaded(Bundle bundle) {
                        marketItems = bundle.getParcelableArrayList("marketItems");
                        MarketAdapter peopleAdapter = new MarketAdapter(getApplicationContext(), R.layout.market_row, marketItems);
                        lvMarket.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        lvMarket.setLayoutManager(layoutManager);
                        lvMarket.setAdapter(peopleAdapter);
                    }
                };
                DbThread.getInstance().addListener(listener);

                date.setText(dateAndMoney.getDate(allSettings));
                moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
                moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        marketItems = new ArrayList<>();

        message = handler.obtainMessage(5);
        DbThread.getBackgroundHandler().sendMessage(message);
        listener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                marketItems = bundle.getParcelableArrayList("marketItems");
                MarketAdapter marketAdapter = new MarketAdapter(getApplicationContext(), R.layout.market_row, marketItems);
                lvMarket.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                lvMarket.setLayoutManager(layoutManager);
                lvMarket.setAdapter(marketAdapter);
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

    public class MarketHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView marketItemName;
        private final TextView marketItemAmount;
        private final TextView marketItemPrice;

        private MarketItem marketItem;
        private Context context;

        public MarketHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.marketItemName = (TextView) itemView.findViewById(R.id.marketItemName);
            this.marketItemAmount = (TextView) itemView.findViewById(R.id.marketItemAmount);
            this.marketItemPrice = (TextView) itemView.findViewById(R.id.marketItemPrice);

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
                if (allSettings.getLong("MONEY_RUBLES", 0) >= marketItem.getPrice()) {
                    gotEnoughtMoney = true;
                }
            }
            if (marketItem.getCurrency().equals("$")) {
                if (allSettings.getLong("MONEY_DOLLARS", 0) >= marketItem.getPrice()) {
                    gotEnoughtMoney = true;
                }
            }
            if (!gotEnoughtMoney) {
                Toast.makeText(getApplicationContext(), "Недостаточно денег", Toast.LENGTH_SHORT).show();
            } else {
                allSettings.edit().putInt("CURRENT_ITEM_ID", marketItem.getId()).apply();
                allSettings.edit().putString("CURRENT_ITEM_NAME", marketItem.getName()).apply();
                allSettings.edit().putInt("CURRENT_ITEM_AMOUNT", marketItem.getAmount()).apply();
                allSettings.edit().putInt("CURRENT_ITEM_PRICE", marketItem.getPrice()).apply();
                allSettings.edit().putString("CURRENT_ITEM_CURRENCY", marketItem.getCurrency()).apply();
                allSettings.edit().putString("CURRENT_ITEM_TYPE", marketItem.getType()).apply();

                String type;

                if (allSettings.getString("CURRENT_ITEM_TYPE", "").equals("Еда")) {
                    type = "кг";
                } else {
                    type = "шт";
                }

                tvForDialogBuyItem.setText("\n" + allSettings.getString("CURRENT_ITEM_NAME", "") + " "
                        + allSettings.getInt("CURRENT_ITEM_AMOUNT", 0)
                        + " " + type + "\n\nЦена: "
                        + allSettings.getInt("CURRENT_ITEM_PRICE", 0) + " "
                        + allSettings.getString("CURRENT_ITEM_CURRENCY", ""));
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

    private class DatabaseThread extends Thread {
        @Override
        public void run() {
            super.run();
            DatabaseHelper myDb = new DatabaseHelper(getApplicationContext());
            myDb.insertStockData(
                    allSettings.getString("CURRENT_ITEM_NAME", ""),
                    allSettings.getString("CURRENT_ITEM_TYPE", ""),
                    allSettings.getInt("CURRENT_ITEM_AMOUNT", 0));
        }
    }
}
