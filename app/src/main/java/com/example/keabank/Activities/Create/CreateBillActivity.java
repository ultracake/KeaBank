package com.example.keabank.Activities.Create;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.keabank.Activities.List.ListOfBillsActivity;
import com.example.keabank.Activities.MainActivity;
import com.example.keabank.Activities.ProfileActivity;
import com.example.keabank.Models.AccountNames;
import com.example.keabank.Models.Bill;
import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.example.keabank.Services.Myfunktions;
import com.example.keabank.Services.WriteToDBAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateBillActivity extends AppCompatActivity
{
    //class
    private WriteToDBAccount writeToDBAccount;
    private Myfunktions myfunktions;
    private User user;
    private Bill bill;
    private AccountNames accountNames;

    private Intent intent;
    private int id;
    private int idForDB;
    private Double value = 0.00;
    private static final String TAG = "MyTest";

    private EditText editInsertNameBill;
    private EditText editInsertBillVal;
    private Button butCreateBill;

    //Firbase
    private DatabaseReference databaseReference;

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

        //Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Bills");

        editInsertNameBill = findViewById(R.id.editNameBill);
        editInsertBillVal = findViewById(R.id.editInsertValueBill);

        butCreateBill = findViewById(R.id.butCreateBill);
        butCreateBill.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                value = Double.parseDouble(editInsertBillVal.getText().toString());
                //Creates bill in database
                String id = user.getId();
                bill = new Bill(user.getEmail(),id,"test",value);
                //user = new User(id,password,email,100, age);
                databaseReference.child(id).setValue(bill);

                Toast.makeText(CreateBillActivity.this, R.string.suc_bill_register, Toast.LENGTH_LONG).show();
                goToProfile();

            }
        });

    }

    private void goToProfile()
    {
        intent = new Intent(CreateBillActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
