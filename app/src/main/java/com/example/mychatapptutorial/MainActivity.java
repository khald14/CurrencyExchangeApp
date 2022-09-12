package com.example.mychatapptutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        findViewById(R.id.login_btn).setOnClickListener(v->{
//            Intent intent  = new Intent(MainActivity.this,LoginActivity.class);
//            startActivity(intent);
//        });
        findViewById(R.id.register_btn).setOnClickListener(v -> {
            Intent intent  = new Intent(MainActivity.this,PhoneVerActivity.class);
            startActivity(intent);

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}