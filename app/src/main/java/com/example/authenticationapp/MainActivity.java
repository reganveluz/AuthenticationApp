package com.example.authenticationapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.authenticationapp.DriverInterface.CarInformation;
import com.example.authenticationapp.DriverInterface.Driver;
import com.example.authenticationapp.OwnerInterface.Owner;
import com.example.authenticationapp.UserLogin.Login;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();


    }

    public void parkingowner(View view) {
        FirebaseAuth.getInstance().getCurrentUser();
        startActivity(new Intent(getApplicationContext(), Owner.class));
        finish();
    }

    public void driver(View view) {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String carmodel = documentSnapshot.getString("Car Model");
                String license = documentSnapshot.getString("License Plate");

                if(carmodel!=null && license!=null){
                    startActivity(new Intent(getApplicationContext(), Driver.class));
                    finish();
                }
                else{
                    startActivity(new Intent(getApplicationContext(), CarInformation.class));
                    finish();
                }

            }
        });


    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit Application")
                .setMessage("Are you sure you want to close Parking Boy?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}