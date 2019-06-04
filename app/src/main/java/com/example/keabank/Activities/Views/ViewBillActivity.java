package com.example.keabank.Activities.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.Bill;
import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.example.keabank.Services.BillRepo;
import com.example.keabank.Services.Myfunktions;
import com.example.keabank.Services.TransferServiceActivity;

import java.util.Date;

public class ViewBillActivity extends AppCompatActivity
{
    //class
    private User user;
    private Bill bill;
    private BillRepo billRepo;
    private Myfunktions myfunktions;
    private AccountNames accountNamesUser;

    private Intent intent;

    //Date
    private Date date;
    private Calendar cal;
    private int month;

    //widgets
    private TextView textCurrentBillName;
    private TextView textBillVal;
    private TextView textReceiverEmail;
    private TextView textReceiverAccount;
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
    private Button butConfirmAutoPay;
    private Spinner spinnerSelectDate;
    private int showAutoPay = 0;
    private Integer[] monthArray = {1,2,3,4,5,6,7,8,9,10,11,12};

    //alertDialog for verifying
    AlertDialog.Builder alertDialogVerify;
    private String curAccountName;
    private double curAccountVal;

    private LayoutInflater inflater;
    private View layout;
    private EditText editVerEmail;
    private EditText editVerPassword;
    private String email;
    private String password;

    //to next activity
    private static final String TAG = "MyTest";
    public static final String EXTRA_viewBillUser = "User";
    public static final String EXTRA_viewBillBill = "Bill";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);

        myInit();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

        textReceiverEmail = findViewById(R.id.textViewsInsertReceiverEmail);
        textReceiverEmail.setText(bill.getEmailTo());
        textReceiverAccount = findViewById(R.id.textViewsInsertReceiverAccount);
        textReceiverAccount.setText(accountNamesUser.getAccountNamesList().get(bill.getAccountIDTo()));


        // (Calendar.MONTH) is zero index based, need to plus with 1
        date= new Date();
        cal = Calendar.getInstance();
        cal.setTime(date);
        month = cal.get(Calendar.MONTH) + 1;

        textHasPaid = findViewById(R.id.textViewsStatusVal);
        if(bill.getPaid() == 1)
        {
            textHasPaid.setText(R.string.paid);
        }
        if (bill.getDate() == month && bill.getPaid() != 1)
        {
            if (bill.getValue() < user.getBalanceDefault())
            {
                payNow();
                textHasPaid.setText(R.string.paid);
                Toast.makeText(ViewBillActivity.this, R.string.suc_auto_pay, Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(ViewBillActivity.this, R.string.not_enough_val, Toast.LENGTH_LONG).show();
            }
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

        spinnerSelectDate = findViewById(R.id.spinnerMonthToPayBill);
        ArrayAdapter arrayAdapterDate = new ArrayAdapter(this, android.R.layout.simple_list_item_1, monthArray);
        spinnerSelectDate.setAdapter(arrayAdapterDate);

        //buttons
        butPayNow = findViewById(R.id.butPayBillNow);
        butPayNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (bill.getPaid() != 1)
                {
                    showSideMenu(showPayNow);
                }else
                {
                    Toast.makeText(ViewBillActivity.this, R.string.already_paid, Toast.LENGTH_LONG).show();
                }
            }
        });

        butConfirmPayNow = findViewById(R.id.butConfirmPayBillNow);
        butConfirmPayNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                curAccountName = spinnerSelectAccount.getSelectedItem().toString();
                curAccountVal = myfunktions.checkWhichAccountValToUse(user,curAccountName);
                if (bill.getValue() < curAccountVal)
                {
                    payNowAlertDialog();
                    alertDialogVerify.show();
                } else
                {
                    Toast.makeText(ViewBillActivity.this, R.string.not_enough_val, Toast.LENGTH_LONG).show();
                }
            }
        });

        butAutoPay = findViewById(R.id.butAutoPay);
        butAutoPay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (bill.getPaid() != 1)
                {
                    showSideMenu(showAutoPay);
                }else
                {
                    Toast.makeText(ViewBillActivity.this, R.string.already_paid, Toast.LENGTH_LONG).show();
                }
            }
        });

        butConfirmAutoPay = findViewById(R.id.butConfirmPayBillAuto);
        butConfirmAutoPay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (bill.getValue() < user.getBalanceDefault())
                {
                    payAutoAlertDialog();
                    alertDialogVerify.show();
                } else
                {
                    Toast.makeText(ViewBillActivity.this, R.string.not_enough_val, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showSideMenu(int insertOrWithdraw)
    {
        if (insertOrWithdraw == 1 && !textSelectAccountHidden.isShown())
        {
            textSelectDateHidden.setVisibility(View.GONE);
            spinnerSelectDate.setVisibility(View.GONE);
            butConfirmAutoPay.setVisibility(View.GONE);

            spinnerSelectAccount.setVisibility(View.VISIBLE);
            textSelectAccountHidden.setVisibility(View.VISIBLE);
            butConfirmPayNow.setVisibility(View.VISIBLE);
        }

        if (insertOrWithdraw == 0 && !textSelectDateHidden.isShown())
        {
            textSelectAccountHidden.setVisibility(View.GONE);
            spinnerSelectAccount.setVisibility(View.GONE);
            butConfirmPayNow.setVisibility(View.GONE);

            textSelectDateHidden.setVisibility(View.VISIBLE);
            spinnerSelectDate.setVisibility(View.VISIBLE);
            butConfirmAutoPay.setVisibility(View.VISIBLE);
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

    //if I don´t reset everything in alertDialog it will crash program after second call!
    private void payNowAlertDialog()
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
                    payNow();
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

    private void payNow()
    {
        intent = new Intent(ViewBillActivity.this, TransferServiceActivity.class);
        intent.putExtra(EXTRA_viewBillUser, user);
        intent.putExtra(EXTRA_viewBillBill, bill);
        startActivity(intent);

    }

    private void payAutoAlertDialog()
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
                    billRepo.setDateBill(bill.getName(), (Integer) spinnerSelectDate.getSelectedItem());
                    Toast.makeText(ViewBillActivity.this, R.string.suc_set_date_bill, Toast.LENGTH_LONG).show();
                    goToListOfBills();
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

    private void goToListOfBills()
    {
        intent = new Intent(ViewBillActivity.this, ListOfBillsActivity.class);
        intent.putExtra(EXTRA_viewBillUser, user);
        startActivity(intent);
    }
}
