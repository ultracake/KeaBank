package com.example.keabank.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.keabank.Models.User;
import com.example.keabank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
{
    private Intent intent;

    //class
    private User user;

    //widgets
    private Toolbar toolbar;
    private ProgressBar progressBar;

    private EditText editEmail;
    private EditText editPassword;
    private EditText editAge;

    private Button butSignup;
    private Button butLogin;
    private Button butForgotPassword;

    //Firbase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private int textToToast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myInit();
    }

    private void myInit()
    {
        //for create user in database
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        progressBar = findViewById(R.id.progressBar);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editAge = findViewById(R.id.editAge);

        butSignup = findViewById(R.id.butSignup);
        butSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                final String email = editEmail.getText().toString();
                final String password = editPassword.getText().toString();
                final int  age = Integer.parseInt(editAge.getText().toString());

                if (!email.isEmpty() && !password.isEmpty())
                {
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                //Creates user in database
                                String id = firebaseAuth.getUid();
                                user = new User(id,password,email,100, age);
                                databaseReference.child(id).setValue(user);
                                editEmail.setText("");
                                editPassword.setText("");
                                editAge.setText("");
                                Toast.makeText(MainActivity.this, R.string.register_success, Toast.LENGTH_LONG).show();

                            }else
                            {
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    progressBar.setVisibility(View.GONE);
                } else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, R.string.have_to_insert , Toast.LENGTH_LONG).show();
                }
            }
        });

        butLogin = findViewById(R.id.butLogin);
        butLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        butForgotPassword = findViewById(R.id.butForgotPassword);
        butForgotPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
