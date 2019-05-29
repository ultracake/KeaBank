package com.example.keabank.Activities.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keabank.Activities.List.ListOfAccountsActivity;
import com.example.keabank.Activities.ProfileActivity;
import com.example.keabank.Activities.TransferActivity;
import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.example.keabank.Services.Myfunktions;
import com.example.keabank.Services.AccountRepo;

public class ViewAccountActivity extends AppCompatActivity
{
    //class
    private Myfunktions myfunktions;
    private AccountRepo accountRepo;
    private User user;
    private AccountNames accountNames;

    private Intent intent;
    private String currentAccountName;
    private double currentVal = 0.00;

    //widgets
    private TextView textCurrentAccount;
    private TextView textCurrentVal;

    //hidden menu and scroll view
    private ScrollView scrollView;
    private TextView textInsertHidden;
    private TextView textWithdrawHidden;
    private EditText editValHidDB;
    private Button butInsertDB;
    private Button butWithdrawDB;

    private int idToDB = 0;
    private double valueToDB = 0.00;
    private double valueFormEdit = 0.00;
    private int showInsert = 1;
    private int showWithdraw = 0;

    private Button butTransfer;
    private Button butInsert;
    private Button butwithdraw;

    //to next activity
    private static final String TAG = "MyTest";
    public static final String EXTRA_viewAccount = "User";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

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
                currentAccountName = intent.getStringExtra(ListOfAccountsActivity.EXTRA_ID);
                Log.d(TAG, "onCreate: ");
            }
        }

        //class
        accountRepo = new AccountRepo();
        myfunktions = new Myfunktions();
        accountNames = new AccountNames();

        currentVal = myfunktions.checkWhichAccountValToUse(user, currentAccountName);

        textCurrentAccount = findViewById(R.id.textAccountNameVA);
        textCurrentAccount.setText(currentAccountName);
        textCurrentVal = findViewById(R.id.textCurentValueVA);
        textCurrentVal.setText("" + currentVal);

        //hidden menu
        scrollView = findViewById(R.id.HiddenMenuScrollView);
        textInsertHidden = findViewById(R.id.textInsertHidden);
        textWithdrawHidden = findViewById(R.id.textWithdrawHidden);
        editValHidDB = findViewById(R.id.editInsertandWithdraw);

        //buttons
        butInsert = findViewById(R.id.butAddValVA);
        butInsert.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showSideMenu(showInsert);
            }
        });

        butwithdraw = findViewById(R.id.butWithdrawVA);
        butwithdraw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showSideMenu(showWithdraw);
            }
        });

        butInsertDB = findViewById(R.id.butAddValDB);
        butInsertDB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                idToDB = myfunktions.findAccountID(currentAccountName);
                valueFormEdit = Double.parseDouble(editValHidDB.getText().toString());

                valueToDB = valueFormEdit  + currentVal;
                accountRepo.transfer(user.getEmail(),idToDB, valueToDB);
                Toast.makeText(ViewAccountActivity.this, R.string.suc_insert_val, Toast.LENGTH_LONG).show();
                goToProfile();
            }
        });

        butWithdrawDB = findViewById(R.id.butWithdrawDB);
        butWithdrawDB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (currentAccountName.equals(accountNames.getAccountNamesList().get(3)) && user.getAge() < 70)
                {
                    Toast.makeText(ViewAccountActivity.this, R.string.not_old_enough, Toast.LENGTH_LONG).show();
                    return;
                }

                idToDB = myfunktions.findAccountID(currentAccountName);
                valueFormEdit = Double.parseDouble(editValHidDB.getText().toString());

                if (valueFormEdit <= currentVal)
                {
                    valueToDB = currentVal - valueFormEdit;
                    accountRepo.transfer(user.getEmail(),idToDB, valueToDB);
                    Toast.makeText(ViewAccountActivity.this, R.string.suc_withdraw_val, Toast.LENGTH_LONG).show();
                    goToProfile();
                }
            }
        });

        butTransfer = findViewById(R.id.butTransferValVA);
        butTransfer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(ViewAccountActivity.this, TransferActivity.class);
                intent.putExtra(EXTRA_viewAccount , user);
                startActivity(intent);
            }
        });
    }

    public void showSideMenu(int insertOrWithdraw)
    {
        if (insertOrWithdraw == 1 && !textInsertHidden.isShown())
        {
            textWithdrawHidden.setVisibility(View.GONE);
            butWithdrawDB.setVisibility(View.GONE);

            textInsertHidden.setVisibility(View.VISIBLE);
            butInsertDB.setVisibility(View.VISIBLE);
        }

        if (insertOrWithdraw == 0 && !textWithdrawHidden.isShown())
        {
            textInsertHidden.setVisibility(View.GONE);
            butInsertDB.setVisibility(View.GONE);

            textWithdrawHidden.setVisibility(View.VISIBLE);
            butWithdrawDB.setVisibility(View.VISIBLE);
        }

        if (scrollView.isShown())
        {
            scrollView.setVisibility(View.GONE);
        }
        else
        {
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void goToProfile()
    {
        intent = new Intent(ViewAccountActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

}
