package com.example.keabank.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable
{
    //user info
    private String id;
    //the password is use as verifier like NemId
    private String password;
    private String email;
    private int age;

    //acounts
    private int balanceBudget;
    private int balanceBusiness;
    private int balanceDefault;
    private int balancePension;
    private int balanceSavings;


    //firebase database need a empty constructor
    public User()
    {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String password, String email, int startValue, int age)
    {
        this.id = id;
        this.password = password;
        this.email = email;
        this.age = age;
        this.balanceDefault = startValue;
        this.balanceBudget = startValue;
        this.balanceBusiness = 0;
        this.balanceSavings = 0;
        this.balancePension = 0;
    }

    protected User(Parcel in)
    {
        id = in.readString();
        password = in.readString();
        email = in.readString();
        age = in.readInt();
        balanceBudget = in.readInt();
        balanceBusiness = in.readInt();
        balanceDefault = in.readInt();
        balancePension = in.readInt();
        balanceSavings = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>()
    {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    //getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public int getBalanceBudget() {
        return balanceBudget;
    }

    public void setBalanceBudget(int balanceBudget) {
        this.balanceBudget = balanceBudget;
    }

    public int getBalanceBusiness() {
        return balanceBusiness;
    }

    public void setBalanceBusiness(int balanceBusiness) {
        this.balanceBusiness = balanceBusiness;
    }

    public int getBalanceDefault() {
        return balanceDefault;
    }

    public void setBalanceDefault(int balanceDefault) {
        this.balanceDefault = balanceDefault;
    }

    public int getBalancePension() {
        return balancePension;
    }

    public void setBalancePension(int balancePension) {
        this.balancePension = balancePension;
    }

    public int getBalanceSavings() {
        return balanceSavings;
    }

    public void setBalanceSavings(int balanceSavings) {
        this.balanceSavings = balanceSavings;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeInt(age);
        dest.writeInt(balanceBudget);
        dest.writeInt(balanceBusiness);
        dest.writeInt(balanceDefault);
        dest.writeInt(balancePension);
        dest.writeInt(balanceSavings);
    }
}
