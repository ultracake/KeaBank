package com.example.keabank.Activities.Views;

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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keabank.Activities.List.ListOfBillsActivity;
import com.example.keabank.Activities.TransferActivity;
import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.Bill;
import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.example.keabank.Services.BillRepo;
import com.example.keabank.Services.Myfunktions;

public class ViewBillActivity extends AppCompatActivity
{
    //class
    private User user;
    private Bill bill;
    private BillRepo billRepo;
    private Myfunktions myfunktions;
    private AccountNames accountNamesUser;

    private Intent intent;

    //widgets
    private TextView textCurrentBillName;
    private TextView textBillVal;
    private TextView textHasPaid;

    private Button butPayNow;
    private Button butAutoPay;

    //hidden menu and scroll view
    private ScrollView scrollView;
    private TextView textSelectAccountHidden;
    private Spinner spinnerSelectAccount;
    private Button butConfirmPayNow;
    private int showPayNow = 1;

    private TextView textSelectDateHidden;
    private Button butConfirmPayAuto;
    private int showAutoPay = 0;

    //alertDialog for verifying
    AlertDialog.Builder alertDialogVerify;

    private LayoutInflater inflater;
    private View layout;
    private EditText editVerEmail;
    private EditText editVerPassword;
    private String email;
    private String password;

    //to next activity
    private static final String TAG = "MyTest";
    public static final String EXTRA_viewBill = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);

        myInit();
    }

    private void myInit()
    {
        intent = getIntent();
        if (intent != null)
        {
            if (intent.getParcelableExtra(ListOfBillsActivity.EXTRA_viewBills) != null)
            {
                user = intent.getParcelableExtra(ListOfBillsActivity.EXTRA_viewBills);
                bill = intent.getParcelableExtra(ListOfBillsActivity.EXTRA_bill);
                Log.d(TAG, "onCreate: ");
            }
        }

        //class
        billRepo = new BillRepo();
        myfunktions = new Myfunktions();
        accountNamesUser = new AccountNames();

        textCurrentBillName = findViewById(R.id.textViewBillTitle);
        textCurrentBillName.setText(bill.getName());
        textBillVal = findViewById(R.id.textBillCurrentVal);
        textBillVal.setText(""+bill.getValue());

        textHasPaid = findViewById(R.id.textViewsStatusVal);
        if(bill.getPaid() == 1)
        {
            textHasPaid.setText(R.string.paid);
        }

        //hidden menu
        scrollView = findViewById(R.id.HiddenScrollBill);
        textSelectAccountHidden = findViewById(R.id.textSelectAccountHidden);
        textSelectDateHidden = findViewById(R.id.textSelectDateHidden);

        //setup for spinners
        spinnerSelectAccount = findViewById(R.id.spinnerAccountToPayBill);
        accountNamesUser = myfunktions.checkIfExistForViews(user, accountNamesUser);
        accountNamesUser.getAccountNamesList().remove("Pension");
        ArrayAdapter arrayAdapterYA = new ArrayAdapter(this, android.R.layout.simple_list_item_1, accountNamesUser.getAccountNamesList());
        spinnerSelectAccount.setAdapter(arrayAdapterYA);

        //buttons
        butPayNow = findViewById(R.id.butPayBillNow);
        butPayNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showSideMenu(showPayNow);
            }
        });

        butConfirmPayNow = findViewById(R.id.butConfirmPayBillNow);
        butConfirmPayNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                resetAlertDialog();
                alertDialogVerify.show();
            }
        });
    }

    private void showSideMenu(int insertOrWithdraw)
    {

        if (insertOrWithdraw == 1 && !textSelectAccountHidden.isShown())
        {
            //textSelectDateHidden.setVisibility(View.GONE);
           // butConfirmPayAuto.setVisibility(View.GONE);

            spinnerSelectAccount.setVisibility(View.VISIBLE);
            textSelectAccountHidden.setVisibility(View.VISIBLE);
            butConfirmPayNow.setVisibility(View.VISIBLE);
        }
/*
        if (insertOrWithdraw == 0 && !textSelectDateHidden.isShown())
        {
            textSelectAccountHidden.setVisibility(View.GONE);
            butConfirmPayNow.setVisibility(View.GONE);

            textSelectDateHidden.setVisibility(View.VISIBLE);
            butConfirmPayAuto.setVisibility(View.VISIBLE);
        }*/

        if (scrollView.isShown())
        {
            scrollView.setVisibility(View.GONE);
        }
        else
        {
            scrollView.setVisibility(View.VISIBLE);
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
                    billRepo.payBill(bill.getName());
                } else
                {
                    Toast.makeText(ViewBillActivity.this, R.string.verify_wrong_login, Toast.LENGTH_LONG).show();
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
                Toast.makeText(ViewBillActivity.this, R.string.cancel, Toast.LENGTH_LONG).show();
            }
        });
    }
}
