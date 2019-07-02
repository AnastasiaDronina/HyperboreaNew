package org.anastdronina.gyperborea;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int mId;
    private String mName;
    private String mType;
    private int mAmount;

    public Product(int id, String name, String type, int amount) {
        mId = id;
        mName = name;
        mType = type;
        mAmount = amount;
    }

    protected Product(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mType = in.readString();
        mAmount = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getType() {
        return mType;
    }

    public int getAmount() {
        return mAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mType);
        dest.writeInt(mAmount);
    }
}
