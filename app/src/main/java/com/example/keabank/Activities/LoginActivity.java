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

import com.example.keabank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    //widgets
    private Toolbar toolbar;
    private ProgressBar progressBar;

    private EditText editEmail;
    private EditText editPassword;
    private Button butLogin;

    //Firbase
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myInit();
    }
    private void myInit()
    {
        toolbar = findViewById(R.id.toolbarLogin);
        toolbar.setTitle(R.string.Login);
        progressBar = findViewById(R.id.progressBarlogin);

        editEmail = findViewById(R.id.editEmailLogin);
        editPassword = findViewById(R.id.editPasswordLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        butLogin = findViewById(R.id.butUserLogin);
        butLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty())
                {
                    progressBar.setVisibility(View.GONE);
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this, "You´re logged in", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                                editEmail.setText("");
                                editPassword.setText("");

                            }else
                            {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, R.string.have_to_insert, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
