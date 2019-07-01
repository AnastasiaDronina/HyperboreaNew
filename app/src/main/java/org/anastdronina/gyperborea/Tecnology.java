package org.anastdronina.gyperborea;

import android.os.Parcel;
import android.os.Parcelable;

public class Tecnology implements Parcelable {
    private int id, monthsToLearn;
    long price;
    private String name, description;
    private boolean isLearned;

    public Tecnology(int id, String name, String description, int monthsToLearn, long price, boolean isLearned) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.monthsToLearn = monthsToLearn;
        this.price = price;
        this.isLearned = isLearned;
    }

    protected Tecnology(Parcel in) {
        id = in.readInt();
        monthsToLearn = in.readInt();
        price = in.readLong();
        name = in.readString();
        description = in.readString();
        isLearned = in.readByte() != 0;
    }

    public static final Creator<Tecnology> CREATOR = new Creator<Tecnology>() {
        @Override
        public Tecnology createFromParcel(Parcel in) {
            return new Tecnology(in);
        }

        @Override
        public Tecnology[] newArray(int size) {
            return new Tecnology[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonthsToLearn() {
        return monthsToLearn;
    }

    public long getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isLearned() {
        return isLearned;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(monthsToLearn);
        dest.writeLong(price);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeByte((byte) (isLearned ? 1 : 0));
    }
}
