package com.example.keabank.Activities.Create;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.keabank.Activities.List.ListOfBillsActivity;
import com.example.keabank.Activities.ProfileActivity;
import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.Bill;
import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.example.keabank.Services.Myfunktions;
import com.example.keabank.Services.BillRepo;


public class CreateBillActivity extends AppCompatActivity
{
    //class
    private BillRepo billRepo;
    private User user;
    private AccountNames accountNames;

    private Intent intent;
    private String name;
    private Double value = 0.00;
    private static final String TAG = "MyTest";

    private EditText editInsertNameBill;
    private EditText editInsertBillVal;

    private EditText editSelectedEmail;
    private Spinner spinnerToAccount;
    
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

        //class
        billRepo = new BillRepo();

        editInsertNameBill = findViewById(R.id.editNameBill);
        editInsertBillVal = findViewById(R.id.editInsertValueBill);

        butCreateBill = findViewById(R.id.butCreateBill);
        butCreateBill.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                value = Double.parseDouble(editInsertBillVal.getText().toString());
                name = editInsertNameBill.getText().toString();

                if (value != 0 || !name.isEmpty())
                {
                    //Creates bill in database
                    billRepo.createBill(user, name, value);

                    Toast.makeText(CreateBillActivity.this, R.string.suc_bill_register, Toast.LENGTH_LONG).show();
                    goToProfile();
                }else
                {
                    Toast.makeText(CreateBillActivity.this, R.string.have_to_insert, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void goToProfile()
    {
        intent = new Intent(CreateBillActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
