package com.example.keabank.Services;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.keabank.Activities.ProfileActivity;
import com.example.keabank.Activities.TransferActivity;
import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
This class is to help making a transaction that check if users email exist inDB.
Firebase can constant read because it use onDataChange() method to read data,
which require changes for read data,
which happens in OnCreate() and since firebase can not read on onClik listner button!

Documentation from Firebase:

Important: The onDataChange() method is called every time data is changed at the specified database reference,
including changes to children. To limit the size of your snapshots, attach only at the highest level needed for watching changes.
For example, attaching a listener to the root of your database is not recommended.

https://firebase.google.com/docs/database/android/read-and-write
 */

public class TransferServiceActivity extends AppCompatActivity
{
    //class
    private User user;
    private User selectedUser;
    private AccountRepo accountRepo;
    private Myfunktions myfunktions;

    private Intent intent;
    private int idYAForDB;
    private int idSelectedAForDB;
    private Double value = 0.00;
    private Double selectedsAccountsVal = 0.00;
    private int currentUserAccountVal = 0;

    private String selectedUserAccount = "";
    private String selectedEmail = "";

    private boolean showToast;
    private static final String TAG = "MyTest";

    //Firbase
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_service);

        myInit();
    }

    private void myInit()
    {
        intent = getIntent();
        if (intent != null)
        {
            if (intent.getParcelableExtra(TransferActivity.EXTRA_transferUser) != null)
            {
                user = intent.getParcelableExtra(TransferActivity.EXTRA_transferUser);
                selectedUserAccount = intent.getStringExtra(TransferActivity.EXTRA_transferUserAccount);
                value = intent.getDoubleExtra(TransferActivity.EXTRA_transferVal, 0.00);
                idYAForDB = intent.getIntExtra(TransferActivity.EXTRA_transferUserAccountID, 2);

                selectedEmail = intent.getStringExtra(TransferActivity.EXTRA_selectedEmailT);
                idSelectedAForDB = intent.getIntExtra(TransferActivity.EXTRA_selectedAccount, 3);

                Log.d(TAG, "onCreate: ");
            }
        }

        //class
        accountRepo = new AccountRepo();
        myfunktions = new Myfunktions();

        showToast = true;
        currentUserAccountVal = myfunktions.checkWhichAccountValToUse(user, selectedUserAccount);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot userSnapShot : dataSnapshot.getChildren())
                {
                    selectedUser = userSnapShot.getValue(User.class);

                    if (selectedEmail.equals(selectedUser.getEmail()))
                    {
                        selectedsAccountsVal = 0.00 + myfunktions.checkWhichAccountValueByIdToUse(selectedUser, idSelectedAForDB);

                        //from
                        accountRepo.transfer(user.getEmail(),idYAForDB, currentUserAccountVal - value);
                        //to
                        accountRepo.transfer(selectedEmail, idSelectedAForDB, value + selectedsAccountsVal);

                        showToast = false;
                        Toast.makeText(TransferServiceActivity.this, selectedEmail + " got: " + value, Toast.LENGTH_LONG).show();
                    }
                }

                if (showToast)
                {
                    Toast.makeText(TransferServiceActivity.this, R.string.doesnt_exist_email , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(TransferServiceActivity.this, databaseError.getMessage() , Toast.LENGTH_LONG).show();
            }
        });

        goToProfile();
    }

    private void goToProfile()
    {
        intent = new Intent(TransferServiceActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
