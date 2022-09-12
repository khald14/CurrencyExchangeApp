package com.example.mychatapptutorial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetProfileActivity extends AppCompatActivity implements LocationListener {

    private CardView mgetuserimage;
    private ImageView mgetuserimageinimageview;
    private static int PICK_IMAGE = 123;
    private Uri imagepath;

    private EditText mgetusername;

    private android.widget.Button msaveprofile;

    private FirebaseAuth firebaseAuth;
    private String name;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private String ImageUriAcessToken;

    private FirebaseFirestore firebaseFirestore;

    ProgressBar mprogressbarofsetprofile;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReferenceFromUrl("gs://currency-exchange-projec-663b4.appspot.com");
        firebaseFirestore = FirebaseFirestore.getInstance();


        mgetusername = findViewById(R.id.get_user_name);
        mgetuserimage = findViewById(R.id.get_user_image);
        mgetuserimageinimageview = findViewById(R.id.get_user_image_in_image_view);
        msaveprofile = findViewById(R.id.saveProfile);
        mprogressbarofsetprofile = findViewById(R.id.progress_bar_of_set_profile);


        mgetuserimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });

        if (ContextCompat.checkSelfPermission(SetProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SetProfileActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        msaveprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();


                name = mgetusername.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Name is Empty", Toast.LENGTH_SHORT).show();
                } else if (imagepath == null) {
                    Toast.makeText(getApplicationContext(), "Image is Empty", Toast.LENGTH_SHORT).show();
                } else {

                    mprogressbarofsetprofile.setVisibility(View.VISIBLE);
                    sendDataForNewUser();
                    mprogressbarofsetprofile.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(SetProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();


                }
            }
        });
    }


    private void sendDataForNewUser() {

        sendDataToRealTimeDatabase();

    }

    private void sendDataToRealTimeDatabase() {


        name = mgetusername.getText().toString().trim();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        UserProfile muserprofile = new UserProfile(name, firebaseAuth.getUid());
        databaseReference.setValue(muserprofile);
        Toast.makeText(getApplicationContext(), "User Profile Added Sucessfully", Toast.LENGTH_SHORT).show();
        sendImagetoStorage();


    }
    StorageReference imageref ;
    UploadTask uploadTask;


    private void sendImagetoStorage() {

         imageref = storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Pic");

        //Image compresesion

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagepath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        ///putting image to storage

        uploadTask = imageref.putBytes(data);




    }


    private void updateAll(StorageReference imageref, UploadTask uploadTask , Double a , Double b) {
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()  {
                    @Override
                    public void onSuccess(Uri uri)  {
                        ImageUriAcessToken = uri.toString();
                        Toast.makeText(getApplicationContext(), "URI get sucess", Toast.LENGTH_SHORT).show();
                        sendDataTocloudFirestore(a,b);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "URI get Failed", Toast.LENGTH_SHORT).show();
                    }


                });
                Toast.makeText(getApplicationContext(), "Image is uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Image Not UPdloaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendDataTocloudFirestore(Double a ,Double b) {
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name", name);
        userdata.put("image", ImageUriAcessToken);
        userdata.put("uid", firebaseAuth.getUid());
        userdata.put("status", "Online");
        userdata.put("a", a);
        userdata.put("b", b);
        userdata.put("users_i_messaged", new ArrayList<String>());
        userdata.put("currencies_i_have", new HashMap<String,String>());


        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Data on Cloud Firestore send success", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imagepath = data.getData();
            mgetuserimageinimageview.setImageURI(imagepath);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, SetProfileActivity.this);


        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Double a =location.getLatitude();
        Double b = location.getLongitude();
        updateAll(imageref, uploadTask ,a ,b);

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



}