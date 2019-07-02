package org.anastdronina.gyperborea;

import android.os.Parcel;
import android.os.Parcelable;

public class MarketItem implements Parcelable {

    private int mId;
    private int mAmount;
    private int mPrice;
    private String mName;
    private String mCurrency;
    private String mType;

    public MarketItem(int id, String name, int amount, int price, String currency, String type) {
        mId = id;
        mAmount = amount;
        mPrice = price;
        mName = name;
        mCurrency = currency;
        mType = type;
    }

    protected MarketItem(Parcel in) {
        mId = in.readInt();
        mAmount = in.readInt();
        mPrice = in.readInt();
        mName = in.readString();
        mCurrency = in.readString();
        mType = in.readString();
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
        return mType;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getAmount() {
        return mAmount;
    }

    public int getPrice() {
        return mPrice;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCurrency() {
        return mCurrency;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mAmount);
        dest.writeInt(mPrice);
        dest.writeString(mName);
        dest.writeString(mCurrency);
        dest.writeString(mType);
    }
}
