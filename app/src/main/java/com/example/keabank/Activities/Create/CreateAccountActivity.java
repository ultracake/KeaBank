package com.example.keabank.Activities.Create;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.keabank.Activities.List.ListOfAccountsActivity;
import com.example.keabank.Activities.ProfileActivity;
import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.example.keabank.Services.Myfunktions;
import com.example.keabank.Services.WriteToDBAccount;

public class CreateAccountActivity extends AppCompatActivity
{
    //class
    private WriteToDBAccount writeToDBAccount;
    private Myfunktions myfunktions;
    private User user;
    private AccountNames accountNames;

    private Intent intent;
    private int id;
    private int idForDB;
    private Double value = 0.00;
    private static final String TAG = "MyTest";

    private Spinner spinner;
    private EditText editInsertValue;
    private Button butCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

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
            if (intent.getParcelableExtra(ListOfAccountsActivity.EXTRA_viewAccounts) != null)
            {
                user = intent.getParcelableExtra(ListOfAccountsActivity.EXTRA_viewAccounts);
                Log.d(TAG, "onCreate: ");
            }
        }

        //class
        writeToDBAccount = new WriteToDBAccount();
        myfunktions = new Myfunktions();
        accountNames = new AccountNames();

        editInsertValue = findViewById(R.id.editInsertValue);

        //setup for spinner
        spinner = findViewById(R.id.spinnerCreate);

        accountNames = myfunktions.checkIfExistForCreate(user,accountNames);
        if(accountNames.getAccountNamesList().isEmpty())
        {
            Toast.makeText(CreateAccountActivity.this, "You can´t make more accounts!", Toast.LENGTH_LONG).show();
            goToProfile();
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1 ,accountNames.getAccountNamesList());
        spinner.setAdapter(arrayAdapter);

        butCreateAccount = findViewById(R.id.butCreateAccount);
        butCreateAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                createAccount();
            }
        });
    }

    private void createAccount()
    {
        id = spinner.getSelectedItemPosition();

        if (!editInsertValue.getText().toString().isEmpty())
        {
            value = Double.parseDouble(editInsertValue.getText().toString());
        }
        if( value > 0)
        {
            idForDB = myfunktions.findAccountID(accountNames.getAccountNamesList().get(id));
            writeToDBAccount.transfer(user.getEmail(), idForDB, value);

            Toast.makeText(CreateAccountActivity.this, R.string.suc_register_account, Toast.LENGTH_LONG).show();
            editInsertValue.setText("");
            goToProfile();
        }else
        {
            Toast.makeText(CreateAccountActivity.this, R.string.have_to_insert, Toast.LENGTH_LONG).show();
        }
    }

    private void goToProfile()
    {
        intent = new Intent(CreateAccountActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
