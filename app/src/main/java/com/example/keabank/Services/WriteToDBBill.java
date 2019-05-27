package com.example.keabank.Services;

import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.Bill;
import com.example.keabank.Models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WriteToDBBill
{
    //class
    private User user;
    private Bill bill;
    private AccountNames accountNames;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    //to next activity
    private static final String TAG = "debug";

    public WriteToDBBill()
    {
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference().child("Bills");
    }

    public void createBill(User user, String name, Double value)
    {
        bill = new Bill(user.getId(),name, value);
        databaseReference.child(name).setValue(bill);
    }
}
