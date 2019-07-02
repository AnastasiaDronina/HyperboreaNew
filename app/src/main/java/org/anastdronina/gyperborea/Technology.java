package org.anastdronina.gyperborea;

import android.os.Parcel;
import android.os.Parcelable;

public class Technology implements Parcelable {
    private int mId;
    private int mMonthsToLearn;
    private long mPrice;
    private String mName;
    private String mDescription;
    private boolean mIsLearned;

    public Technology(int id, String name, String description, int monthsToLearn, long price, boolean isLearned) {
        mId = id;
        mName = name;
        mDescription = description;
        mMonthsToLearn = monthsToLearn;
        mPrice = price;
        mIsLearned = isLearned;
    }

    protected Technology(Parcel in) {
        mId = in.readInt();
        mMonthsToLearn = in.readInt();
        mPrice = in.readLong();
        mName = in.readString();
        mDescription = in.readString();
        mIsLearned = in.readByte() != 0;
    }

    public static final Creator<Technology> CREATOR = new Creator<Technology>() {
        @Override
        public Technology createFromParcel(Parcel in) {
            return new Technology(in);
        }

        @Override
        public Technology[] newArray(int size) {
            return new Technology[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getMonthsToLearn() {
        return mMonthsToLearn;
    }

    public long getPrice() {
        return mPrice;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public boolean isLearned() {
        return mIsLearned;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mMonthsToLearn);
        dest.writeLong(mPrice);
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeByte((byte) (mIsLearned ? 1 : 0));
    }
}
