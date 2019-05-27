package com.example.keabank.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Bill implements Parcelable
{
    //user info
    private String id;
    //bill info
    private String name;
    private Double value;
    private int date;
    private int paid;

    //empty constructor for Firebase
    public Bill()
    {
    }

    public Bill(String id, String name, Double value)
    {
        this.id = id;
        this.name = name;
        this.value = value;
        this.date = 0;
        this.paid = 0;
    }

    protected Bill(Parcel in)
    {
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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeDouble(value);
        dest.writeInt(paid);
        dest.writeInt(date);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }
}