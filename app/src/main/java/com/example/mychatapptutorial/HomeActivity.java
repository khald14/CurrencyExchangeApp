package com.example.mychatapptutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<String> users = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    BottomNavigationView navigationView;
     Intent intent;
    private FirebaseAuth firebaseAuth;

    TextView currenciesTV;
    boolean[] selectedCurrency;
    ArrayList<Integer> currencyList = new ArrayList<>();
    String[] currencies;
    HashMap<String,String> currenciesHave = new HashMap<>();
    HashMap<String,String> temp = new HashMap<>();




    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        bottomNavBarRepair();
        buttonsRepair();
        currenciesTV = findViewById(R.id.currencies_tv);
        currencies = getResources().getStringArray(R.array.currs);
        selectedCurrency = new boolean[currencies.length];

        currenciesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Select Currency");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(currencies, selectedCurrency, (dialog, which, isChecked) -> {
                    //check condition
                    if(isChecked){
                        //when checkbox selected;
                        //add position in currency list;
                        currencyList.add(which);
                        Collections.sort(currencyList);
                    }else{
                        //when checkbox unselected
                        //remove position from currency list
                        currencyList.remove(which);
                    }
                });
                builder.setPositiveButton("OK", (dialog, which) -> {
                    //initialize string builder
                    currenciesHave.clear();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int j = 0 ; j<currencyList.size() ; j++){
                        stringBuilder.append(currencies[currencyList.get(j)].substring(0,3));
                        currenciesHave.put(j+"",currencies[currencyList.get(j)].substring(0,3));
                        //check condition
                        if(j != currencyList.size()-1){
                            //when j value not equal to currency list size -1
                            //add coma
                            stringBuilder.append(", ");
                        }
                    }

                    firebaseFirestore.collection("Users")
                    .document(firebaseAuth.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        temp = (HashMap<String, String>)document.get("currencies_i_have");
                    }).addOnCompleteListener(task -> {
                                if(temp!=null) {
                                    currenciesHave.putAll(temp);
                                }
                                firebaseFirestore
                                .collection("Users")
                                .document(firebaseAuth.getUid())
                                .update("currencies_i_have",currenciesHave);
                            });






                    //set text on text view
                    currenciesTV.setText(stringBuilder.toString());
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                builder.setNeutralButton("Clear All", (dialog, which) -> {
                    for (int j = 0 ; j<selectedCurrency.length ; j++) {
                        //Remove all selections
                        selectedCurrency[j] = false;
                        //Clear currency list
                        currencyList.clear();
                        //Clear textview value
                        currenciesTV.setText("");
                    }
                    currenciesHave.clear();
                    firebaseFirestore
                            .collection("Users")
                            .document(firebaseAuth.getUid())
                            .update("currencies_i_have",currenciesHave);
                });
                builder.show();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    public void bottomNavBarRepair(){
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.page_1);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.page_2:
                    firebaseFirestore.collection("Users")
                            .document(firebaseAuth.getUid())
                            .get()
                            .addOnCompleteListener(task -> {
                                DocumentSnapshot document = task.getResult();
                                users.addAll((ArrayList<String>) document.get("users_i_messaged"));
                                if(users.isEmpty()){
                                    Toast.makeText(getApplicationContext(), "You don't have any messages", Toast.LENGTH_SHORT).show();
                                }else{
                                    intent  = new Intent(HomeActivity.this, ChatActivity.class);
                                    intent .setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intent);
                                }
                            });

                    break;
                case R.id.page_3:
                    intent  = new Intent(HomeActivity.this,SettingsActivity.class);
                    intent .setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    public void buttonsRepair(){
        findViewById(R.id.search_btn).setOnClickListener(v -> {
            Intent intent  = new Intent(HomeActivity.this,SearchActivity.class);
            intent .setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });
        findViewById(R.id.currency_convertor_btn).setOnClickListener(v -> {
            Intent intent  = new Intent(HomeActivity.this,CurrencyConvertorActivity.class);
            intent .setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });
        findViewById(R.id.contact_us_btn).setOnClickListener(v -> {
            Intent intent  = new Intent(HomeActivity.this,HomeActivity.class);
            intent .setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });
        findViewById(R.id.calculator_btn).setOnClickListener(v -> {
            Intent intent  = new Intent(HomeActivity.this,CalculatorActivity.class);
            intent .setFlags(intent .getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });
    }

}