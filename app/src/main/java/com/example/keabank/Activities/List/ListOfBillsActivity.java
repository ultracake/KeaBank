package com.example.keabank.Activities.List;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.keabank.Activities.Create.CreateAccountActivity;
import com.example.keabank.Activities.Create.CreateBillActivity;
import com.example.keabank.Activities.ProfileActivity;
import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.example.keabank.Services.Myfunktions;

public class ListOfBillsActivity extends AppCompatActivity
{
    //class
    private User user;
    private Myfunktions myfunktions;
    private AccountNames accountNames;

    private Intent intent;
    private ListView listView;

    //to next activity
    private static final String TAG = "MyTest";
    public static final String EXTRA_viewBills = "User";
    public static final String EXTRA_ID = "model id";

    //snackbar
    private Snackbar snackbar;
    private String nameForVal;

    //Buttons
    private Button butHome;
    private FloatingActionButton floatingActionButCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_bills);

        myInit();
    }

    private void myInit()
    {
        intent = getIntent();
        if (intent != null)
        {
            if (intent.getParcelableExtra(ProfileActivity.EXTRA_Profile) != null)
            {
                user = intent.getParcelableExtra(ProfileActivity.EXTRA_Profile);
                Log.d(TAG, "onCreate: ");
            }
        }

        //class
        myfunktions = new Myfunktions();

        butHome = findViewById(R.id.butHomeBills);
        butHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(ListOfBillsActivity.this, ProfileActivity.class);
                intent.putExtra(EXTRA_viewBills, user);
                startActivity(intent);
            }
        });

        floatingActionButCreate = findViewById(R.id.floatingActionButCreateBill);
        floatingActionButCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(ListOfBillsActivity.this, CreateBillActivity.class);
                intent.putExtra(EXTRA_viewBills, user);
                startActivity(intent);
            }
        });
    }
}
