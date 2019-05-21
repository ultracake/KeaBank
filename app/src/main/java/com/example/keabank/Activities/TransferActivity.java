package com.example.keabank.Activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.example.keabank.Services.Myfunktions;
import com.example.keabank.Services.TransferServiceActivity;

public class TransferActivity extends AppCompatActivity
{
    //class
    private User user;
    private AccountNames accountNamesUser;
    private AccountNames accountNamesSelectedTaget;
    private Myfunktions myfunktions;

    private Intent intent;
    private int idYA;
    private int idYAForDB;
    private int idSelectedToAccountForDB;
    private Double value = 0.00;
    private String selectedUserAccount = "";
    private String selectedEmail = "";

    private static final String TAG = "MyTest";
    public static final String EXTRA_transferUser = "user";
    public static final String EXTRA_transferUserAccount = "userAccount";
    public static final String EXTRA_transferVal = "value";
    public static final String EXTRA_transferUserAccountID = "account";

    public static final String EXTRA_selectedEmailT = "email";
    public static final String EXTRA_selectedAccount = "selectAccount id";

    //widgets
    private Spinner spinnerYourAccount;
    private Spinner spinnerToAccount;

    private EditText editInsertValue;
    private EditText editSelectedEmail;
    private Button butTransfer;

    //hidden menu and scroll view
    private ScrollView scrollViewVerifyT;
    private EditText editVerEmail;
    private EditText editVerPassword;
    private Button butVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        myInit();
    }

    private void myInit()
    {
        intent = getIntent();
        if (intent != null)
        {
            if (intent.getParcelableExtra(ViewAccountActivity.EXTRA_viewAccount) != null)
            {
                user = intent.getParcelableExtra(ViewAccountActivity.EXTRA_viewAccount);
                Log.d(TAG, "onCreate: ");
            }
            if (intent.getParcelableExtra(TransferServiceActivity.EXTRA_transferServiceUser) != null)
            {
                Toast.makeText(TransferActivity.this, R.string.doesnt_exist_email , Toast.LENGTH_LONG).show();
                user = intent.getParcelableExtra(TransferServiceActivity.EXTRA_transferServiceUser);
                Log.d(TAG, "onCreate back form transfer: ");
            }
        }

        //class
        accountNamesUser = new AccountNames();
        accountNamesSelectedTaget = new AccountNames();
        myfunktions = new Myfunktions();

        editInsertValue = findViewById(R.id.editTransferVal);
        editSelectedEmail = findViewById(R.id.editSelectedEmail);

        //setup for spinners
        spinnerYourAccount = findViewById(R.id.spinnerYourAccountT);
        accountNamesUser = myfunktions.checkIfExistForViews(user, accountNamesUser);
        accountNamesUser.getAccountNamesList().remove("Pension");
        ArrayAdapter arrayAdapterYA = new ArrayAdapter(this, android.R.layout.simple_list_item_1, accountNamesUser.getAccountNamesList());
        spinnerYourAccount.setAdapter(arrayAdapterYA);

        spinnerToAccount = findViewById(R.id.spinnerToAccountT);
        ArrayAdapter arrayAdapterSelectedA = new ArrayAdapter(this, android.R.layout.simple_list_item_1, accountNamesSelectedTaget.getAccountNamesList());
        spinnerToAccount.setAdapter(arrayAdapterSelectedA);

        //hidden menu
        scrollViewVerifyT = findViewById(R.id.scrollVerify);
        editVerEmail = findViewById(R.id.editVerifyEmail);
        editVerPassword = findViewById(R.id.editVerifyPassword);

        //alertDialog for verifying
        AlertDialog.Builder alertDialogVerify = new AlertDialog.Builder(this);
        alertDialogVerify.setMessage(R.string.verify_transfer);

        butTransfer = findViewById(R.id.butTransferT);
        butTransfer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                spinnerToAccount.setVisibility(View.GONE);
                spinnerYourAccount.setVisibility(View.GONE);
                butTransfer.setVisibility(View.GONE);
                scrollViewVerifyT.setVisibility(View.VISIBLE);
            }
        });

        butVerify = findViewById(R.id.butVerify);
        butVerify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                scrollViewVerifyT.setVisibility(View.GONE);
                transferVal();
            }
        });
    }

    private void transferVal()
    {
        if (!editInsertValue.getText().toString().isEmpty() && !editSelectedEmail.getText().toString().isEmpty())
        {
            value = Double.parseDouble(editInsertValue.getText().toString());
            idYA = spinnerYourAccount.getSelectedItemPosition();
            idYAForDB = myfunktions.findAccountID(accountNamesUser.getAccountNamesList().get(idYA));

            if(value < myfunktions.checkWhichAccountValueByIdToUse(user,idYAForDB))
            {
                selectedUserAccount = spinnerYourAccount.getSelectedItem().toString();

                idSelectedToAccountForDB = spinnerToAccount.getSelectedItemPosition();
                selectedEmail = editSelectedEmail.getText().toString();


                intent = new Intent(TransferActivity.this, TransferServiceActivity.class);
                intent.putExtra(EXTRA_transferUser , user);
                intent.putExtra(EXTRA_transferUserAccount, selectedUserAccount);
                intent.putExtra(EXTRA_transferVal, value);
                intent.putExtra(EXTRA_transferUserAccountID, idYAForDB );

                intent.putExtra(EXTRA_selectedEmailT, selectedEmail);
                intent.putExtra(EXTRA_selectedAccount, idSelectedToAccountForDB);
                startActivity(intent);
            }else
            {
                Toast.makeText(TransferActivity.this, R.string.not_enough_val, Toast.LENGTH_LONG).show();
            }

        } else
        {
            Toast.makeText(TransferActivity.this, R.string.have_to_insert, Toast.LENGTH_LONG).show();
        }
    }


}
