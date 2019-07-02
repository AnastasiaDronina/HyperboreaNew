package org.anastdronina.gyperborea;

import android.os.Parcel;
import android.os.Parcelable;

public class Farm implements Parcelable {
    private int mId;
    private int mStatus;
    private int mFarmerId;
    private String mName;
    private String mCrop;

    // FARMS' STATUSES
    public static final int NOT_USED = 0;
    public static final int SOWING_COMPLETED = 1;
    public static final int SPROUTED = 2;
    public static final int BLOOM = 3;
    public static final int RIPENING = 4;
    public static final int HARVEST = 5;

    public Farm(int id, String name, String crop, int status, int farmerId) {
        mId = id;
        mStatus = status;
        mFarmerId = farmerId;
        mName = name;
        mCrop = crop;
    }

    protected Farm(Parcel in) {
        mId = in.readInt();
        mStatus = in.readInt();
        mFarmerId = in.readInt();
        mName = in.readString();
        mCrop = in.readString();
    }

    public static final Creator<Farm> CREATOR = new Creator<Farm>() {
        @Override
        public Farm createFromParcel(Parcel in) {
            return new Farm(in);
        }

        @Override
        public Farm[] newArray(int size) {
            return new Farm[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getStatus() {
        return mStatus;
    }

    public int getFarmerId() {
        return mFarmerId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCrop() {
        return mCrop;
    }

    public String statusString(int statusInt) {
        String statusStr = "";
        switch (statusInt) {
            case NOT_USED:
                statusStr = "Не используется";
                break;
            case SOWING_COMPLETED:
                statusStr = "Посев завершен";
                break;
            case SPROUTED:
                statusStr = "Взошли ростки";
                break;
            case BLOOM:
                statusStr = "Цветение";
                break;
            case RIPENING:
                statusStr = "Созревание";
                break;
            case HARVEST:
                statusStr = "Сбор урожая";
                break;
        }
        return statusStr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mStatus);
        dest.writeInt(mFarmerId);
        dest.writeString(mName);
        dest.writeString(mCrop);
    }
}
