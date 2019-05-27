package com.example.keabank.Activities.Create;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.keabank.Activities.List.ListOfBillsActivity;
import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.example.keabank.Services.Myfunktions;
import com.example.keabank.Services.WriteToDBAccount;

public class CreateBillActivity extends AppCompatActivity
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

    private EditText editInsertNameBill;
    private EditText editInsertBillVal;
    private Button butCreateBill;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);

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
                Log.d(TAG, "onCreate: ");
            }
        }

        editInsertNameBill = findViewById(R.id.editNameBill);
        editInsertBillVal = findViewById(R.id.editInsertValueBill);

        butCreateBill = findViewById(R.id.butCreateBill);
        butCreateBill.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


            }
        });

    }
}
