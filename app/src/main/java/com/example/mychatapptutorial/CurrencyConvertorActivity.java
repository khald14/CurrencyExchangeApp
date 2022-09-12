package com.example.mychatapptutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CurrencyConvertorActivity extends AppCompatActivity {

    TextInputLayout menu_from, menu_to;
    AutoCompleteTextView autoCompleteTextView, autoCompleteTextView2;
    ArrayAdapter<String> arrayAdapter1;
    ArrayAdapter<String> arrayAdapter2;
    BottomNavigationView navigationView;
    EditText converted_value;
    EditText amount_field;
    Intent intent;
    Button convert_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_convertor);
        convert_btn = findViewById(R.id.convert_btn);
        amount_field = findViewById(R.id.amount_field);
        converted_value = findViewById(R.id.converted_value);
        bottomNavBarRepair();

        amount_field.setEnabled(false);

        findViewById(R.id.CurrencyTopAppBar).setOnClickListener(v -> {
            Intent intent = new Intent(CurrencyConvertorActivity.this, HomeActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });

        repairDropDownMenus();
        convert_btn.setOnClickListener(view -> {
            String amount = amount_field.getText().toString();
            if (!amount.matches("")) {
                String from = autoCompleteTextView.getText().toString();
                String to = autoCompleteTextView2.getText().toString();

                if (from.equals(""))
                    Toast.makeText(CurrencyConvertorActivity.this, "Please enter from which currency you wish to convert", Toast.LENGTH_SHORT).show();
                else {
                    if (to.equals(""))
                        Toast.makeText(CurrencyConvertorActivity.this, "Please enter for which currency you wish to convert", Toast.LENGTH_SHORT).show();
                    else {
                        String url = "https://api.currencyapi.com/v3/latest?apikey=dESAX0jWTO8JFAUaJVKQiHUNLnMhjt3c4AUCW4RI";

                        StringBuffer response ;
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                        StrictMode.setThreadPolicy(policy);
                        try {
                            URL obj = new URL(url);
                            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                            // optional default is GET
                            con.setRequestMethod("GET");
                            //add request header
                            int responseCode = con.getResponseCode();
                            // System.out.println(responseCode);
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(con.getInputStream()));
                            String inputLine;
                            response = new StringBuffer();
                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();
                            JSONObject myResponse = new JSONObject(response.toString());

                            Double input1InDollar = myResponse.getJSONObject("data").getJSONObject(from.substring(0, 3)).getDouble("value");
                            Double input2InDollar = myResponse.getJSONObject("data").getJSONObject(to.substring(0, 3)).getDouble("value");
                            double result = ((input2InDollar / input1InDollar) * Double.parseDouble(amount));
                            converted_value.setText(Double.toString(result));
                        }
                        catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
            else {
                Toast.makeText(CurrencyConvertorActivity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void repairDropDownMenus() {
        menu_from = findViewById(R.id.menu_from);
        menu_to = findViewById(R.id.menu_to);

        autoCompleteTextView = findViewById(R.id.AutoCompleteTextView);
        autoCompleteTextView2 = findViewById(R.id.AutoCompleteTextView2);

        String[] items = getResources().getStringArray(R.array.currs);
        arrayAdapter1 = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_item, items);
        autoCompleteTextView.setAdapter(arrayAdapter1);


        arrayAdapter2 = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_item, items);
        autoCompleteTextView2.setAdapter(arrayAdapter2);

    }

    @SuppressLint("NonConstantResourceId")
    public void bottomNavBarRepair() {
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.page_1:
                    intent = new Intent(CurrencyConvertorActivity.this, HomeActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    break;
                case R.id.page_2:
                    intent = new Intent(CurrencyConvertorActivity.this, ChatActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    break;
                case R.id.page_3:
                    intent = new Intent(CurrencyConvertorActivity.this, SettingsActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return true;
        });
    }
}