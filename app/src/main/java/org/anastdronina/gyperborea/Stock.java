package org.anastdronina.gyperborea;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Stock extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerPruductType;
    private RecyclerView rvStock;
    private TextView tvDate;
    private TextView tvMoneyD;
    private TextView tvMoneyR;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Product> mProductsList;
    private ArrayList<Product> mSubList;
    private ArrayAdapter<CharSequence> mStockArrayAdapter;
    private StockAdapter mStockAdapter;
    private SharedPreferences mSettings;
    private DateAndMoney mDateAndMoney;
    private DbThread.DbListener mListener;
    private DbManager mDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        mDbManager = new DbManager();
        mProductsList = new ArrayList<>();
        rvStock = findViewById(R.id.stockListView);
        rvStock.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        mDateAndMoney = new DateAndMoney();

        tvDate = findViewById(R.id.date);
        tvMoneyR = findViewById(R.id.moneyR);
        tvMoneyD = findViewById(R.id.moneyD);
        spinnerPruductType = findViewById(R.id.spinnerPruductType);
        mStockArrayAdapter = ArrayAdapter.createFromResource(this, R.array.product_types, R.layout.spinner_item);
        mStockArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPruductType.setAdapter(mStockArrayAdapter);
        spinnerPruductType.setOnItemSelectedListener(this);

        tvDate.setText(mDateAndMoney.getDate(mSettings));
        tvMoneyD.setText(mDateAndMoney.getMoney(mSettings, "$"));
        tvMoneyR.setText(mDateAndMoney.getMoney(mSettings, "руб"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDbManager.loadData(DbManager.WhatData.STOCK);
        mListener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                mProductsList = bundle.getParcelableArrayList("products");
            }
        };
        DbThread.getInstance().addListener(mListener);

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.equals("Не выбрано")) {
            mDbManager.loadData(DbManager.WhatData.STOCK);
            mListener = new DbThread.DbListener() {
                @Override
                public void onDataLoaded(Bundle bundle) {
                    mProductsList = bundle.getParcelableArrayList("products");
                    mStockAdapter = new StockAdapter(getApplicationContext(), R.layout.scientists_row, mProductsList);
                    rvStock.setLayoutManager(mLayoutManager);
                    rvStock.setAdapter(mStockAdapter);
                }
            };
            DbThread.getInstance().addListener(mListener);
        }
        if (text.equals("Еда")) {
            mDbManager.loadData(DbManager.WhatData.STOCK);
            mListener = new DbThread.DbListener() {
                @Override
                public void onDataLoaded(Bundle bundle) {
                    mProductsList = bundle.getParcelableArrayList("products");
                    mSubList = new ArrayList<>();
                    for (int i = 0; i < mProductsList.size(); i++) {
                        if (mProductsList.get(i).getType().equals("Еда")) {
                            mSubList.add(mProductsList.get(i));
                        }
                    }
                    mStockAdapter = new StockAdapter(getApplicationContext(), R.layout.scientists_row, mSubList);
                    rvStock.setLayoutManager(mLayoutManager);
                    rvStock.setAdapter(mStockAdapter);
                }
            };
            DbThread.getInstance().addListener(mListener);
        }
        if (text.equals("Ресурсы")) {
            mDbManager.loadData(DbManager.WhatData.STOCK);
            mListener = new DbThread.DbListener() {
                @Override
                public void onDataLoaded(Bundle bundle) {
                    mProductsList = bundle.getParcelableArrayList("products");
                    mSubList = new ArrayList<>();
                    for (int i = 0; i < mProductsList.size(); i++) {
                        if (mProductsList.get(i).getType().equals("Ресурсы")) {
                            mSubList.add(mProductsList.get(i));
                        }
                    }
                    mStockAdapter = new StockAdapter(getApplicationContext(), R.layout.scientists_row, mSubList);
                    rvStock.setLayoutManager(mLayoutManager);
                    rvStock.setAdapter(mStockAdapter);
                }
            };
            DbThread.getInstance().addListener(mListener);
        }
        if (text.equals("Оборудование")) {
            mDbManager.loadData(DbManager.WhatData.STOCK);
            mListener = new DbThread.DbListener() {
                @Override
                public void onDataLoaded(Bundle bundle) {
                    mProductsList = bundle.getParcelableArrayList("products");
                    mSubList = new ArrayList<>();
                    for (int i = 0; i < mProductsList.size(); i++) {
                        if (mProductsList.get(i).getType().equals("Оборудование")) {
                            mSubList.add(mProductsList.get(i));
                        }
                    }
                    mStockAdapter = new StockAdapter(getApplicationContext(), R.layout.scientists_row, mSubList);
                    rvStock.setLayoutManager(mLayoutManager);
                    rvStock.setAdapter(mStockAdapter);
                }
            };
            DbThread.getInstance().addListener(mListener);
        }
        if (text.equals("Транспорт")) {
            mDbManager.loadData(DbManager.WhatData.STOCK);
            mListener = new DbThread.DbListener() {
                @Override
                public void onDataLoaded(Bundle bundle) {
                    mProductsList = bundle.getParcelableArrayList("products");
                    mSubList = new ArrayList<>();
                    for (int i = 0; i < mProductsList.size(); i++) {
                        if (mProductsList.get(i).getType().equals("Транспорт")) {
                            mSubList.add(mProductsList.get(i));
                        }
                    }
                    mStockAdapter = new StockAdapter(getApplicationContext(), R.layout.scientists_row, mSubList);
                    rvStock.setLayoutManager(mLayoutManager);
                    rvStock.setAdapter(mStockAdapter);
                }
            };
            DbThread.getInstance().addListener(mListener);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class StockHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView productName;
        private final TextView productAmount;

        private Product product;
        private Context context;

        public StockHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;

            this.productName = itemView.findViewById(R.id.scientistName);
            this.productAmount = itemView.findViewById(R.id.scientistLevel);

            itemView.setOnClickListener(this);
        }

        public void bindProduct(Product product) {
            String amount;
            this.product = product;
            productName.setText(product.getName());
            if (product.getType().equals("Еда")) {
                amount = product.getAmount() + " кг ";
            } else {
                amount = product.getAmount() + " шт ";
            }
            productAmount.setText(amount);
        }

        @Override
        public void onClick(View v) {
        }
    }

    public class StockAdapter extends RecyclerView.Adapter<StockHolder> {
        private final List<Product> products;
        private Context context;
        private int itemResource;

        StockAdapter(Context context, int itemResourse, List<Product> products) {
            this.context = context;
            this.products = products;
            this.itemResource = itemResourse;
        }

        @NonNull
        @Override
        public StockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(this.itemResource, parent, false);
            return new StockHolder(this.context, view);
        }

        @Override
        public void onBindViewHolder(@NonNull StockHolder holder, int position) {
            Product product = this.products.get(position);
            holder.bindProduct(product);
        }

        @Override
        public int getItemCount() {
            return this.products.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
