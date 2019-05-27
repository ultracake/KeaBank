package com.example.keabank.Services;

import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WriteToDBBill
{
    //class
    private User user;
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

    public void createBill()
    {
        
    }
}
