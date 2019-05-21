package com.example.keabank.Services;

import android.app.Application;
import android.content.Intent;

import com.example.keabank.Activities.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//This class makes sure if the User is login he can´t go to login page again before logout!
public class Home extends Application
{
    //Firbase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    public void onCreate()
    {
        super.onCreate();

        myInit();
    }

    private void myInit()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null)
        {
            startActivity(new Intent(Home.this, ProfileActivity.class));
        }
    }
}
