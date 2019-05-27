package com.example.keabank.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity
{
    //class
    private User user;


    private Intent intent;

    //to next activity
    private static final String TAG = "MyTest";
    public static final String EXTRA_Profile = "User";

    //widgets
    private TextView userEmail;
    private ProgressBar progressBar;

    private Button butDeleteUser;
    private Button butLogout;
    private Button butCreateAccount;
    private Button butViewlistAccounts;
    private Button butViewlistBills;

    //Firbase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        myInit();
    }

    private void myInit()
    {
        intent = getIntent();
        if (intent != null)
        {
            if (intent.getParcelableExtra(ListOfAccountsActivity.EXTRA_viewAccounts) != null)
            {
                user = intent.getParcelableExtra(ListOfAccountsActivity.EXTRA_viewAccounts);
                Log.d(TAG, "onCreate: ");
            }
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        if(user == null)
        {
            databaseReference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    for (DataSnapshot userSnapShot : dataSnapshot.getChildren())
                    {
                        if(firebaseUser.getUid().equals(userSnapShot.getKey()))
                        {
                            user = userSnapShot.getValue(User.class);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }


        progressBar = findViewById(R.id.progressBarProfile);
        userEmail = findViewById(R.id.textViewUser);
        userEmail.setText(firebaseUser.getEmail());

        butDeleteUser = findViewById(R.id.butDeleteUser);
        butDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
                dialog.setTitle("Are you sure you want to Delete this Account?");
                dialog.setMessage("It will be remove completely from this system!"
                                   + "\n\n" + "You can´t undo this later on!");

                dialog.setPositiveButton(R.string.delete_user, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful())
                                {
                                    databaseReference.child(user.getId()).removeValue();
                                    Toast.makeText(ProfileActivity.this, "Your Account is now deleted", Toast.LENGTH_LONG).show();

                                    intent = new Intent(ProfileActivity.this, MainActivity.class);
                                    //clean logout
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }else
                                {
                                    Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        butLogout = findViewById(R.id.butLogout);
        butLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(ProfileActivity.this, MainActivity.class);
                //clean logout
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        butCreateAccount = findViewById(R.id.butCreateAccount);
        butCreateAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(ProfileActivity.this, CreateAccountActivity.class);
                intent.putExtra(EXTRA_Profile , user);
                startActivity(intent);
            }
        });

        butViewlistAccounts = findViewById(R.id.butViewlistAcount);
        butViewlistAccounts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(ProfileActivity.this, ListOfAccountsActivity.class);
                intent.putExtra(EXTRA_Profile , user);
                startActivity(intent);
            }
        });


    }
}
