package com.example.mychatapptutorial;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.example.mychatapptutorial.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,LocationListener{


    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView courseRV;
    UserAdapter courseAdapter ;
    List<Entry<String, Double>> list =  new LinkedList<>();
    BottomNavigationView navigationView;
    Intent intent;
    String required;
    private ArrayList<UserModel> arraylist = new ArrayList<>();
    UserModel user;
    LatLng sydney;
    String name;
    double distance;
    String img;
    String id;
    HashMap<String,String> currs;
    double bb;
    double aa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        required = getIntent().getStringExtra("required");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);}
        bottomNavBarRepair();


         courseAdapter = new UserAdapter(MapsActivity.this, arraylist);


    }
    private void updateLocation( Double a , Double b) {

        sendDataTocloudFirestore(a,b);

    }
    private void sendDataTocloudFirestore(Double a ,Double b) {
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("a",a);
        documentReference.update("b",b);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Double a =location.getLatitude();
        Double b = location.getLongitude();
        updateLocation(a ,b);

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Double a =null;
        Double b = null;

        firebaseFirestore.collection("Users")
               .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String[] usersUid = new String[task.getResult().size()];

                double max = 0;
                int i = 0;

                Double aCurentUser = null;
                Double bCurentUser = null;

                    for (QueryDocumentSnapshot document1 : task.getResult()) {
                        if (document1.getString("uid").equals(firebaseAuth.getUid())) {
                            aCurentUser = document1.getDouble("a");
                            bCurentUser = document1.getDouble("b");
                        }
                    }

                for (QueryDocumentSnapshot document : task.getResult()) {

                    if ((!document.getString("uid").equals(firebaseAuth.getUid()))) {
                        Double a = document.getDouble("a");
                        Double b = document.getDouble("b");

                        usersUid[i] = document.get("uid").toString();
                        if ((b != null && bCurentUser != null)) {
                            double theta = b - bCurentUser;
                            double dist = Math.sin((a * Math.PI / 180.0)) * Math.sin((aCurentUser * Math.PI / 180.0))
                                    + Math.cos((a * Math.PI / 180.0)) * Math.cos((aCurentUser * Math.PI / 180.0))
                                    * Math.cos((theta * Math.PI / 180.0));
                            dist = Math.acos(dist);
                            dist = (dist * 180.0 / Math.PI);
                            dist = dist * 60 * 1.1515;
                            // distance in kilometer
                            dist = dist * 1.609344;
                            arraylist.add(new UserModel("",dist,"",usersUid[i],null));
                        }
                        i++;
                    }
                }
                if (arraylist != null) {
                    Collections.sort(arraylist);

                    for(int k = 0 ; k<arraylist.size();k++){
                        System.out.println("--------------------------------------"+arraylist.get(k).getName() +" " + arraylist.get(k).getDistance()  +" " +  arraylist.get(k).getimage()  +" " +  arraylist.get(k).getId());
                    }

                }

                //sorted
                for (UserModel usermodel : arraylist) {
                    final UserModel userModel = usermodel;
                    DocumentReference docRef = firebaseFirestore.collection("Users").document(userModel.getId());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                            DocumentSnapshot document = task.getResult();

                            name = document.get("name").toString();
                            distance = userModel.getDistance();
                            img = document.get("image").toString();
                            id = document.getId();
                            currs = (HashMap<String, String>) document.get("currencies_i_have");
                            aa = document.getDouble("a");
                            bb = document.getDouble("b");
                        }}).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            user = new UserModel(name,distance,img,id,currs);
                            if(user.getCurrencies()!=null) {
                                if (user.getCurrencies().containsValue(required)) {
                                    arraylist.remove(userModel);
                                    arraylist.add(user);
                                    sydney = new LatLng(aa, bb);
                                    mMap.addMarker(new MarkerOptions().position(sydney).title(user.getName()));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                }
                            }
                            else {
                                arraylist.remove(userModel);
                            }

                            courseRV = findViewById(R.id.idRVCourse);
                            courseRV.setAdapter(courseAdapter);
                            courseAdapter.notifyDataSetChanged();
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MapsActivity.this, LinearLayoutManager.VERTICAL, false);
                            courseRV.setLayoutManager(linearLayoutManager);
                        }

                    });
                }

            }
        });


    }
    @SuppressLint("NonConstantResourceId")
    public void bottomNavBarRepair() {
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.page_2);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.page_1:
                    intent = new Intent(MapsActivity.this, HomeActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    break;
                case R.id.page_2:
                    intent = new Intent(MapsActivity.this, ChatActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    break;
                case R.id.page_3:
                    intent = new Intent(MapsActivity.this, SettingsActivity.class);
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