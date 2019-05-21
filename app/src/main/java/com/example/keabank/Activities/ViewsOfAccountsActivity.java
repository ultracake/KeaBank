package com.example.keabank.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.example.keabank.Services.Myfunktions;

import java.util.ArrayList;

public class ViewsOfAccountsActivity extends AppCompatActivity
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
        setContentView(R.layout.activity_views_of_accounts);

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

        //snackbar
        snackbar = Snackbar.make(findViewById(R.id.ListviewId), R.string.show_cur_bal, Snackbar.LENGTH_LONG);

        //setup for listView
        accountNames = new AccountNames();
        listView = findViewById(R.id.ListviewId);
        accountNames = myfunktions.checkIfExistForViews(user, accountNames);


        ArrayAdapter arrayAdapter = new ArrayAdapter(ViewsOfAccountsActivity.this, android.R.layout.simple_list_item_1 ,accountNames.getAccountNamesList());
        listView.setAdapter(arrayAdapter);

        //action listener for viewList
        viewListListenerFunc(accountNames.getAccountNamesList());

        butHome = findViewById(R.id.butHome);
        butHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(ViewsOfAccountsActivity.this, ProfileActivity.class);
                intent.putExtra(EXTRA_viewAccounts, user);
                startActivity(intent);
            }
        });

        floatingActionButCreate = findViewById(R.id.floatingActionButCreateAccount);
        floatingActionButCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(ViewsOfAccountsActivity.this, CreateAccountActivity.class);
                intent.putExtra(EXTRA_viewAccounts,user);
                startActivity(intent);
            }
        });
    }

    private void viewListListenerFunc(final ArrayList list)
    {
        //go to view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(ViewsOfAccountsActivity.this, "you click: " + list.get(position).toString(), Toast.LENGTH_LONG).show();

                intent = new Intent(ViewsOfAccountsActivity.this, ViewAccountActivity.class);
                intent.putExtra(EXTRA_viewAccounts, user);
                intent.putExtra(EXTRA_ID, accountNames.getAccountNamesList().get(position));
                startActivity(intent);
            }
        });

        //show value for account
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                snackbar.setAction(R.string.show, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        nameForVal = accountNames.getAccountNamesList().get(position) ;
                        Toast.makeText(ViewsOfAccountsActivity.this, "Current value: " + myfunktions.checkWhichAccountValToUse(user, nameForVal), Toast.LENGTH_LONG).show();
                    }
                });
                snackbar.show();
                return true;
            }
        });

    }
}
