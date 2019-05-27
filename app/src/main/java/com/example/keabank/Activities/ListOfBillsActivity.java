package com.example.keabank.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

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
    public static final String EXTRA_viewAccounts = "User";
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

    }
}
