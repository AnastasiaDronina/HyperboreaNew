package org.anastdronina.gyperborea;

import android.os.Parcel;
import android.os.Parcelable;

public class MarketItem implements Parcelable {

    private int id, amount, price;
    String name, currency, type;

    public MarketItem(int id, String name, int amount, int price, String currency, String type) {
        this.id = id;
        this.amount = amount;
        this.price = price;
        this.name = name;
        this.currency = currency;
        this.type = type;
    }

    protected MarketItem(Parcel in) {
        id = in.readInt();
        amount = in.readInt();
        price = in.readInt();
        name = in.readString();
        currency = in.readString();
        type = in.readString();
    }

    public static final Creator<MarketItem> CREATOR = new Creator<MarketItem>() {
        @Override
        public MarketItem createFromParcel(Parcel in) {
            return new MarketItem(in);
        }

        @Override
        public MarketItem[] newArray(int size) {
            return new MarketItem[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(amount);
        dest.writeInt(price);
        dest.writeString(name);
        dest.writeString(currency);
        dest.writeString(type);
    }
}
