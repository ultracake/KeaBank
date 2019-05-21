package com.example.keabank.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.keabank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity
{
    //widgets
    private Toolbar toolbar;
    private ProgressBar progressBar;

    private EditText userEmail;
    private Button butSendNewPassword;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        myInit();
    }

    private void myInit()
    {
        firebaseAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbarForgotPassword);
        toolbar.setTitle(R.string.forgot_password);

        progressBar = findViewById(R.id.progressBarForgotPassword);
        userEmail = findViewById(R.id.editEmailForgotPassword);

        butSendNewPassword = findViewById(R.id.butSendNewPassword);
        butSendNewPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful())
                        {
                            Toast.makeText(ForgotPasswordActivity.this, "Password send to your email", Toast.LENGTH_LONG).show();
                        }else
                        {
                            Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });
    }
}
