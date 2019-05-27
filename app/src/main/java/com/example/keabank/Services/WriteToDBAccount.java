package com.example.keabank.Services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WriteToDBAccount
{
    //class
    private User user;
    private AccountNames accountNames;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    //to next activity
    private static final String TAG = "debug";

    public WriteToDBAccount()
    {
        this.accountNames = new  AccountNames();

        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference().child("User");
    }

    public void transfer(final String selectedEmail, final int id, final double value)
    {
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot userSnapShot : dataSnapshot.getChildren())
                {
                    user = userSnapShot.getValue(User.class);
                    if(selectedEmail.equals(user.getEmail()))
                    {
                        databaseReference.child(user.getId()).child(accountNames.getAccountDBlist().get(id)).setValue(value);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.d(TAG, "transfer Method: " + databaseError.getMessage());
            }
        });
    }
}
