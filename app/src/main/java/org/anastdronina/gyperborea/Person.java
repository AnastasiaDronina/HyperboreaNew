package org.anastdronina.gyperborea;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Person implements Parcelable {
    private int id;
    private String name, surname;
    private int age, salary, job;
    private int building, manufacture, farm, athletic, learning, talking, strength, art;
    private ArrayList<String> traits;

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
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.job = job;
        this.salary = salary;
        this.age = age;
        this.building = building;
        this.manufacture = manufacture;
        this.farm = farm;
        this.athletic = athletic;
        this.learning = learning;
        this.talking = talking;
        this.strength = strength;
        this.art = art;
        this.traits = traits;
    }

    protected Person(Parcel in) {
        id = in.readInt();
        name = in.readString();
        surname = in.readString();
        age = in.readInt();
        salary = in.readInt();
        job = in.readInt();
        building = in.readInt();
        manufacture = in.readInt();
        farm = in.readInt();
        athletic = in.readInt();
        learning = in.readInt();
        talking = in.readInt();
        strength = in.readInt();
        art = in.readInt();
        traits = in.createStringArrayList();
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
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public int getManufacture() {
        return manufacture;
    }

    public void setManufacture(int manufacture) {
        this.manufacture = manufacture;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public int getAthletic() {
        return athletic;
    }

    public void setAthletic(int athletic) {
        this.athletic = athletic;
    }

    public int getLearning() {
        return learning;
    }

    public void setLearning(int learning) {
        this.learning = learning;
    }

    public int getTalking() {
        return talking;
    }

    public void setTalking(int talking) {
        this.talking = talking;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getArt() {
        return art;
    }

    public void setArt(int art) {
        this.art = art;
    }

    public ArrayList<String> getTraits() {
        return traits;
    }

    public void setTraits(ArrayList<String> traits) {
        this.traits = traits;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeInt(age);
        dest.writeInt(salary);
        dest.writeInt(job);
        dest.writeInt(building);
        dest.writeInt(manufacture);
        dest.writeInt(farm);
        dest.writeInt(athletic);
        dest.writeInt(learning);
        dest.writeInt(talking);
        dest.writeInt(strength);
        dest.writeInt(art);
        dest.writeStringList(traits);
    }
}
