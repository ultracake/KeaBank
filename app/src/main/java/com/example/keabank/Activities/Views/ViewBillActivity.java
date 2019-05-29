package com.example.keabank.Activities.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.keabank.Activities.List.ListOfBillsActivity;
import com.example.keabank.Models.Bill;
import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.example.keabank.Services.BillRepo;

public class ViewBillActivity extends AppCompatActivity
{
    //class
    private User user;
    private Bill bill;
    private BillRepo billRepo;

    private Intent intent;

    //widgets
    private TextView textCurrentBillName;
    private TextView textBillVal;

    private Button butPayNow;
    private Button butAutoPay;

    //to next activity
    private static final String TAG = "MyTest";
    public static final String EXTRA_viewAccount = "User";

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

        textCurrentBillName = findViewById(R.id.textViewBillTitle);
        textCurrentBillName.setText(bill.getName());
        textBillVal = findViewById(R.id.textBillCurrentVal);
        textBillVal.setText(""+bill.getValue());

        //buttons
        butPayNow = findViewById(R.id.butPayBillNow);
        butPayNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               billRepo.payBill(bill.getName());
            }
        });
    }
}
