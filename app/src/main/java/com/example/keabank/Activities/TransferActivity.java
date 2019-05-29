package com.example.keabank.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.keabank.Activities.Views.ViewAccountActivity;
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

    //alertDialog for verifying
    AlertDialog.Builder alertDialogVerify;
   //private static final int myIDPopup = 3;
    private LayoutInflater inflater;
    private View layout;
    private EditText editVerEmail;
    private EditText editVerPassword;
    private String email;
    private String password;


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
            else if(intent.getParcelableExtra(TransferServiceActivity.EXTRA_transferServiceUser) != null)
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
        final ArrayAdapter arrayAdapterSelectedA = new ArrayAdapter(this, android.R.layout.simple_list_item_1, accountNamesSelectedTaget.getAccountNamesList());
        spinnerToAccount.setAdapter(arrayAdapterSelectedA);


        butTransfer = findViewById(R.id.butTransferT);
        butTransfer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!editInsertValue.getText().toString().isEmpty() && !editSelectedEmail.getText().toString().isEmpty())
                {
                    resetAlertDialog();
                    alertDialogVerify.show();
                } else
                {
                    Toast.makeText(TransferActivity.this, R.string.have_to_insert, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void transferVal()
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
    }

    //if I don´t reset everything in alertDialog it will crash program after second call!
    private void resetAlertDialog()
    {
        //popup window
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.popup_verify_with_login, (ViewGroup) findViewById(R.id.root));
        editVerEmail = layout.findViewById(R.id.editVerifyEmail);
        editVerPassword = layout.findViewById(R.id.editVerifyPassword);

        //alertDialog
        alertDialogVerify = new AlertDialog.Builder(this);
        alertDialogVerify.setView(layout);
        alertDialogVerify.setMessage(R.string.verify_transfer);

        //on confirm
        alertDialogVerify.setPositiveButton(R.string.verify, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                email = editVerEmail.getText().toString();
                password = editVerPassword.getText().toString();

                if (myfunktions.verifyWithLogin(email, password, user))
                {
                    transferVal();
                } else
                {
                    Toast.makeText(TransferActivity.this, R.string.verify_wrong_login, Toast.LENGTH_LONG).show();
                    alertDialogVerify.setCancelable(true);
                }
            }
        });
        //on cancel
        alertDialogVerify.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(TransferActivity.this, R.string.cancel, Toast.LENGTH_LONG).show();
            }
        });
    }


}
