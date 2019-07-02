package org.anastdronina.gyperborea;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Person implements Parcelable {
    private int mId;
    private String mName;
    private String mSurname;
    private int mAge;
    private int mSalary;
    private int mJob;
    private int mBuilding;
    private int mManufacture;
    private int mFarm;
    private int mAthletic;
    private int mLearning;
    private int mTalking;
    private int mStrength;
    private int mArt;
    private ArrayList<String> mTraitsList;

    // JOBS //
    public static final int NOT_EMPLOYED = 0;
    public static final int BUILDER = 1;
    public static final int COLLECTOR = 2;
    public static final int MECHANIC = 3;
    public static final int FARMER = 4;
    public static final int COURIER = 5;
    public static final int SCIENTIST = 6;
    public static final int MINISTER = 7;
    public static final int SECURITY = 8;
    public static final int DEVELOPER = 9;
    public static final int CLEANER = 10;
    public static final int FINANSIST = 11;
    public static final int SALESMAN = 12;

    // SALARIES //
    public static final int NOT_EMPLOYED_SALARY = 0;
    public static final int BUILDER_SALARY = 10000;
    public static final int COLLECTOR_SALARY = 8000;
    public static final int MECHANIC_SALARY = 11000;
    public static final int FARMER_SALARY = 6000;
    public static final int COURIER_SALARY = 7000;
    public static final int SCIENTIST_SALARY = 12000;
    public static final int MINISTER_SALARY = 11000;
    public static final int SECURITY_SALARY = 7000;
    public static final int DEVELOPER_SALARY = 12000;
    public static final int CLEANER_SALARY = 6000;
    public static final int FINANSIST_SALARY = 10000;
    public static final int SALESMAN_SALARY = 11000;

    public Person(int id, String name, String surname, int job, int salary, int age, int building, int manufacture, int farm,
                  int athletic, int learning, int talking, int strength, int art, ArrayList<String> traits) {
        mId = id;
        mName = name;
        mSurname = surname;
        mJob = job;
        mSalary = salary;
        mAge = age;
        mBuilding = building;
        mManufacture = manufacture;
        mFarm = farm;
        mAthletic = athletic;
        mLearning = learning;
        mTalking = talking;
        mStrength = strength;
        mArt = art;
        this.mTraitsList = traits;
    }

    protected Person(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mSurname = in.readString();
        mAge = in.readInt();
        mSalary = in.readInt();
        mJob = in.readInt();
        mBuilding = in.readInt();
        mManufacture = in.readInt();
        mFarm = in.readInt();
        mAthletic = in.readInt();
        mLearning = in.readInt();
        mTalking = in.readInt();
        mStrength = in.readInt();
        mArt = in.readInt();
        mTraitsList = in.createStringArrayList();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getSalary() {
        return mSalary;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSurname() {
        return mSurname;
    }

    public int getJob() {
        return mJob;
    }

    public int getAge() {
        return mAge;
    }

    public int getBuilding() {
        return mBuilding;
    }

    public int getManufacture() {
        return mManufacture;
    }

    public int getFarm() {
        return mFarm;
    }

    public int getAthletic() {
        return mAthletic;
    }

    public int getLearning() {
        return mLearning;
    }

    public int getTalking() {
        return mTalking;
    }

    public int getStrength() {
        return mStrength;
    }

    public int getArt() {
        return mArt;
    }

    public ArrayList<String> getTraits() {
        return mTraitsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mSurname);
        dest.writeInt(mAge);
        dest.writeInt(mSalary);
        dest.writeInt(mJob);
        dest.writeInt(mBuilding);
        dest.writeInt(mManufacture);
        dest.writeInt(mFarm);
        dest.writeInt(mAthletic);
        dest.writeInt(mLearning);
        dest.writeInt(mTalking);
        dest.writeInt(mStrength);
        dest.writeInt(mArt);
        dest.writeStringList(mTraitsList);
    }
}
