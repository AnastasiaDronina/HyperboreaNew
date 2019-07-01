package org.anastdronina.gyperborea;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class Stock extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerPruductType;
    private ArrayAdapter<CharSequence> stockArrayAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private StockAdapter stockAdapter;
    private ArrayList<Product> products;
    private ArrayList<Product> subList;
    private RecyclerView stockListView;
    private SharedPreferences allSettings;
    private DateAndMoney dateAndMoney;
    private TextView date, moneyD, moneyR;
    private DbThread.DbListener listener;
    private Handler handler;
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        handler = new Handler();
        products = new ArrayList<>();
        stockListView = findViewById(R.id.stockListView);
        stockListView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());

        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        dateAndMoney = new DateAndMoney();

        date = findViewById(R.id.date);
        moneyR = findViewById(R.id.moneyR);
        moneyD = findViewById(R.id.moneyD);
        spinnerPruductType = findViewById(R.id.spinnerPruductType);
        stockArrayAdapter = ArrayAdapter.createFromResource(this, R.array.product_types, R.layout.spinner_item);
        stockArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPruductType.setAdapter(stockArrayAdapter);
        spinnerPruductType.setOnItemSelectedListener(this);

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        message = handler.obtainMessage(DbThread.LOAD_STOCK_DATA);
        DbThread.getBackgroundHandler().sendMessage(message);
        listener = new DbThread.DbListener() {
            @Override
            public void onDataLoaded(Bundle bundle) {
                products = bundle.getParcelableArrayList("products");
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.equals("Не выбрано")) {
            message = handler.obtainMessage(DbThread.LOAD_STOCK_DATA);
            DbThread.getBackgroundHandler().sendMessage(message);
            listener = new DbThread.DbListener() {
                @Override
                public void onDataLoaded(Bundle bundle) {
                    products = bundle.getParcelableArrayList("products");
                    stockAdapter = new StockAdapter(getApplicationContext(), R.layout.scientists_row, products);
                    stockListView.setLayoutManager(layoutManager);
                    stockListView.setAdapter(stockAdapter);
                }
            };
            DbThread.getInstance().addListener(listener);
        }
        if (text.equals("Еда")) {
            message = handler.obtainMessage(DbThread.LOAD_STOCK_DATA);
            DbThread.getBackgroundHandler().sendMessage(message);
            listener = new DbThread.DbListener() {
                @Override
                public void onDataLoaded(Bundle bundle) {
                    products = bundle.getParcelableArrayList("products");
                    subList = new ArrayList<>();
                    for (int i = 0; i < products.size(); i++) {
                        if (products.get(i).getType().equals("Еда")) {
                            subList.add(products.get(i));
                        }
                    }
                    stockAdapter = new StockAdapter(getApplicationContext(), R.layout.scientists_row, subList);
                    stockListView.setLayoutManager(layoutManager);
                    stockListView.setAdapter(stockAdapter);
                }
            };
            DbThread.getInstance().addListener(listener);
        }
        if (text.equals("Ресурсы")) {
            message = handler.obtainMessage(DbThread.LOAD_STOCK_DATA);
            DbThread.getBackgroundHandler().sendMessage(message);
            listener = new DbThread.DbListener() {
                @Override
                public void onDataLoaded(Bundle bundle) {
                    products = bundle.getParcelableArrayList("products");
                    subList = new ArrayList<>();
                    for (int i = 0; i < products.size(); i++) {
                        if (products.get(i).getType().equals("Ресурсы")) {
                            subList.add(products.get(i));
                        }
                    }
                    stockAdapter = new StockAdapter(getApplicationContext(), R.layout.scientists_row, subList);
                    stockListView.setLayoutManager(layoutManager);
                    stockListView.setAdapter(stockAdapter);
                }
            };
            DbThread.getInstance().addListener(listener);
        }
        if (text.equals("Оборудование")) {
            message = handler.obtainMessage(DbThread.LOAD_STOCK_DATA);
            DbThread.getBackgroundHandler().sendMessage(message);
            listener = new DbThread.DbListener() {
                @Override
                public void onDataLoaded(Bundle bundle) {
                    products = bundle.getParcelableArrayList("products");
                    subList = new ArrayList<>();
                    for (int i = 0; i < products.size(); i++) {
                        if (products.get(i).getType().equals("Оборудование")) {
                            subList.add(products.get(i));
                        }
                    }
                    stockAdapter = new StockAdapter(getApplicationContext(), R.layout.scientists_row, subList);
                    stockListView.setLayoutManager(layoutManager);
                    stockListView.setAdapter(stockAdapter);
                }
            };
            DbThread.getInstance().addListener(listener);
        }
        if (text.equals("Транспорт")) {
            message = handler.obtainMessage(DbThread.LOAD_STOCK_DATA);
            DbThread.getBackgroundHandler().sendMessage(message);
            listener = new DbThread.DbListener() {
                @Override
                public void onDataLoaded(Bundle bundle) {
                    products = bundle.getParcelableArrayList("products");
                    subList = new ArrayList<>();
                    for (int i = 0; i < products.size(); i++) {
                        if (products.get(i).getType().equals("Транспорт")) {
                            subList.add(products.get(i));
                        }
                    }
                    stockAdapter = new StockAdapter(getApplicationContext(), R.layout.scientists_row, subList);
                    stockListView.setLayoutManager(layoutManager);
                    stockListView.setAdapter(stockAdapter);
                }
            };
            DbThread.getInstance().addListener(listener);
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

            this.productName = (TextView) itemView.findViewById(R.id.scientistName);
            this.productAmount = (TextView) itemView.findViewById(R.id.scientistLevel);

            itemView.setOnClickListener(this);
        }

        public void bindProduct(Product product) {
            String amount;
            this.product = product;
            productName.setText(product.getName());
            if (product.getType().equals("Еда")) {
                amount = Integer.toString(product.getAmount()) + " кг ";
            } else {
                amount = Integer.toString(product.getAmount()) + " шт ";
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
