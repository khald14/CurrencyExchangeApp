package com.example.mychatapptutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

public class SearchActivity extends AppCompatActivity {

     TextInputLayout menu_from, menu_to;
     AutoCompleteTextView autoCompleteTextView, autoCompleteTextView2;
     ArrayAdapter<String> arrayAdapter1;
     ArrayAdapter<String> arrayAdapter2;
     BottomNavigationView navigationView;
     Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        repairDropDownMenus();
        bottomNavBarRepair();

        findViewById(R.id.SearchTopAppBar).setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this,HomeActivity.class);
            intent .setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });
        findViewById(R.id.search_search_btn).setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this,MapsActivity.class);
            String to =   autoCompleteTextView2.getText().toString();
            String from =  autoCompleteTextView.getText().toString();

            intent .setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra("required", to.substring(0,3) );
            startActivity(intent);
        });

    }

    public void repairDropDownMenus(){
        menu_from = findViewById(R.id.menu_from);
        menu_to = findViewById(R.id.menu_to);

        autoCompleteTextView = findViewById(R.id.AutoCompleteTextView);
        autoCompleteTextView2= findViewById(R.id.AutoCompleteTextView2);

        String[] items = getResources().getStringArray(R.array.currs);
        arrayAdapter1 = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_item, items);
        autoCompleteTextView.setAdapter(arrayAdapter1);

        arrayAdapter2 = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_item, items);
        autoCompleteTextView2.setAdapter(arrayAdapter2);

    }

    @SuppressLint("NonConstantResourceId")
    public void bottomNavBarRepair(){
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.page_1);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.page_1:
                    intent  = new Intent(SearchActivity.this,HomeActivity.class);
                    intent .setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    break;
                case R.id.page_2:
                    intent  = new Intent(SearchActivity.this, ChatActivity.class);
                    intent .setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    break;
                case R.id.page_3:
                    intent  = new Intent(SearchActivity.this,SettingsActivity.class);
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