package com.example.keabank.Services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.Bill;
import com.example.keabank.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BillRepo
{
    //class
    private Bill bill;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    //to next activity
    private static final String TAG = "debug";

    public BillRepo()
    {
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference().child("Bills");
    }

    public void createBill(User user, String name, Double value)
    {
        bill = new Bill(user.getId(),name, value);
        databaseReference.child(name).setValue(bill);
    }

    public void payBill(final String selectedbillName)
    {
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot billSnapShot : dataSnapshot.getChildren())
                {
                    bill = billSnapShot.getValue(Bill.class);
                    if(selectedbillName.equals(bill.getName()))
                    {
                        databaseReference.child(bill.getName()).child("paid").setValue(1);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.d(TAG, "pay bill Method: " + databaseError.getMessage());
            }
        });
    }

    public void setDateBill(final String selectedbillName, final int date)
    {
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot billSnapShot : dataSnapshot.getChildren())
                {
                    bill = billSnapShot.getValue(Bill.class);
                    if(selectedbillName.equals(bill.getName()))
                    {
                        databaseReference.child(bill.getName()).child("date").setValue(date);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.d(TAG, "set date bill Method: " + databaseError.getMessage());
            }
        });
    }
}
