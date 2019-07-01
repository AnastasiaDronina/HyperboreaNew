package org.anastdronina.gyperborea;

import android.os.Parcel;
import android.os.Parcelable;

public class Farm implements Parcelable {
    private int id, status, farmerId;
    private String name, crop;

    // FARMS' STATUSES
    public static final int NOT_USED = 0;
    public static final int SOWING_COMPLETED = 1;
    public static final int SPROUTED = 2;
    public static final int BLOOM = 3;
    public static final int RIPENING = 4;
    public static final int HARVEST = 5;

    public Farm(int id, String name, String crop, int status, int farmerId) {
        this.id = id;
        this.status = status;
        this.farmerId = farmerId;
        this.name = name;
        this.crop = crop;
    }

    protected Farm(Parcel in) {
        id = in.readInt();
        status = in.readInt();
        farmerId = in.readInt();
        name = in.readString();
        crop = in.readString();
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
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCrop() {
        return crop;
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
        dest.writeInt(id);
        dest.writeInt(status);
        dest.writeInt(farmerId);
        dest.writeString(name);
        dest.writeString(crop);
    }
}
