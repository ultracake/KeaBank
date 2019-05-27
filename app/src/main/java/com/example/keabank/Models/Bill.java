package com.example.keabank.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Bill implements Parcelable
{
    //user info
    private String email;
    //bill info
    private String id;
    private String name;
    private Double value;
    private int paid;

    //empty constructor for Firebase
    public Bill()
    {
    }

    public Bill(String email, String id, String name, Double value)
    {
        this.email = email;
        this.id = id;
        this.name = name;
        this.value = value;
        this.paid = 0;
    }

    protected Bill(Parcel in)
    {
        email = in.readString();
        id = in.readString();
        name = in.readString();
        value = in.readDouble();
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>()
    {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(email);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeDouble(value);
        dest.writeInt(paid);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}
