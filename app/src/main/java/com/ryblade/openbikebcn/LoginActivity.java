package com.ryblade.openbikebcn;

import android.content.Intent;

import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


import com.ryblade.openbikebcn.Fragments.LoginFragment;
import com.ryblade.openbikebcn.Fragments.FirstScreenFragment;


public class LoginActivity extends AppCompatActivity implements OnPageLoaded, FirstScreenFragment.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if (savedInstanceState == null) {
            FirstScreenFragment mapFragment = new FirstScreenFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mapFragment)
                    .commit();
        }

    }
    public void OnPageLoaded() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }


    @Override
    public void loginBtnClicked() {

        LoginFragment newFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }
}




