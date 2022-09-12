package com.example.mychatapptutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

     BottomNavigationView navigationView;
     Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bottomNavBarRepair();



    }

    @SuppressLint("NonConstantResourceId")
    public void bottomNavBarRepair(){
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.page_3);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.page_1:
                    intent  = new Intent(SettingsActivity.this,HomeActivity.class);
                    intent .setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    break;
                case R.id.page_2:
                    intent  = new Intent(SettingsActivity.this, ChatActivity.class);
                    intent .setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return true;
        });
    }
}