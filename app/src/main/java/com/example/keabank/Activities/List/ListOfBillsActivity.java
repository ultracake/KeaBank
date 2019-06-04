package com.example.keabank.Activities.List;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.keabank.Activities.Create.CreateBillActivity;
import com.example.keabank.Activities.ProfileActivity;
import com.example.keabank.Activities.Views.ViewBillActivity;
import com.example.keabank.Models.Bill;
import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfBillsActivity extends AppCompatActivity
{
    //class
    private User user;
    private Bill bill;

    private Intent intent;
    private ListView listView;
    private ArrayList<Bill>  myBillList;
    private ArrayList<String> billNameList;

    //to next activity
    private static final String TAG = "MyTest";
    public static final String EXTRA_viewBills = "User";
    public static final String EXTRA_bill = "model";

    //snackbar
    private Snackbar snackbar;

    //Buttons
    private Button butHome;
    private FloatingActionButton floatingActionButCreate;

    //Firbase
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_bills);

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
            if (intent.getParcelableExtra(ListOfBillsActivity.EXTRA_viewBills) != null)
            {
                user = intent.getParcelableExtra(ListOfBillsActivity.EXTRA_viewBills);
                Log.d(TAG, "reload: ");
            }
            if (intent.getParcelableExtra(ViewBillActivity.EXTRA_viewBillUser) != null)
            {
                user = intent.getParcelableExtra(ViewBillActivity.EXTRA_viewBillUser);
                Log.d(TAG, "from ViewBill: ");
            }
        }



        //snackbar
        snackbar = Snackbar.make(findViewById(R.id.ListviewBillsId), R.string.want_to_delete, Snackbar.LENGTH_LONG);

        //setup for listView
        myBillList = new ArrayList<Bill>();
        billNameList = new ArrayList<String>();
        listView = findViewById(R.id.ListviewBillsId);

        //gets all user´s bills
        databaseReference = FirebaseDatabase.getInstance().getReference("Bills");
        if(myBillList.isEmpty())
        {
            databaseReference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    for (DataSnapshot billSnapShot : dataSnapshot.getChildren())
                    {
                        bill = billSnapShot.getValue(Bill.class);

                        if(user.getId().equals(bill.getId()))
                        {
                            myBillList.add(bill);
                            billNameList.add(bill.getName());
                        }
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(ListOfBillsActivity.this, android.R.layout.simple_list_item_1 , billNameList);
                    listView.setAdapter(arrayAdapter);

                    //listener for listview
                    viewListListenerFunc(billNameList);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    Toast.makeText(ListOfBillsActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        butHome = findViewById(R.id.butHomeBills);
        butHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(ListOfBillsActivity.this, ProfileActivity.class);
                intent.putExtra(EXTRA_viewBills, user);
                startActivity(intent);
            }
        });

        floatingActionButCreate = findViewById(R.id.floatingActionButCreateBill);
        floatingActionButCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(ListOfBillsActivity.this, CreateBillActivity.class);
                intent.putExtra(EXTRA_viewBills, user);
                startActivity(intent);
            }
        });
    }

    private void viewListListenerFunc(final ArrayList list)
    {
        //go to view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(ListOfBillsActivity.this, "you click: " + list.get(position).toString(), Toast.LENGTH_LONG).show();

                intent = new Intent(ListOfBillsActivity.this, ViewBillActivity.class);
                intent.putExtra(EXTRA_viewBills, user);
                intent.putExtra(EXTRA_bill, myBillList.get(position));
                startActivity(intent);
            }
        });

        //show value for account
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                snackbar.setAction(R.string.delete_bill, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        databaseReference.child(myBillList.get(position).getName()).removeValue();
                        Toast.makeText(ListOfBillsActivity.this,  myBillList.get(position).getName() + " is deleted!", Toast.LENGTH_LONG).show();
                        reloadAct();
                    }
                });
                snackbar.show();
                return true;
            }
        });
    }

    private void reloadAct()
    {
        intent = new Intent(ListOfBillsActivity.this, ListOfBillsActivity.class);
        intent.putExtra(EXTRA_viewBills, user);
        startActivity(intent);
    }
}
