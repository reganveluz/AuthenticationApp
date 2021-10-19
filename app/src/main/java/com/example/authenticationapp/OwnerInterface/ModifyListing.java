package com.example.authenticationapp.OwnerInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.authenticationapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModifyListing extends AppCompatActivity {
    CheckBox mSingle, mMulti, mUnder, mStreet;
    CheckBox mCompact, mmidSize, mfullSize;
    String userID;
    EditText msqm, mexactNumber, maddress, minputlotName;
    Button mnextBtn;
    ArrayList<String> mgarageType = new ArrayList<>();
    ArrayList<String> msupportedCars = new ArrayList<>();
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_listing);

        minputlotName = findViewById(R.id.inputlotName);
        msqm = findViewById(R.id.inputSqm);
        mexactNumber = findViewById(R.id.inputexactNumber);
        mSingle = findViewById(R.id.singleChbx);
        mMulti = findViewById(R.id.multiChbx);
        mUnder = findViewById(R.id.underChbx);
        mStreet = findViewById(R.id.streetChbx);
        mCompact = findViewById(R.id.compactChbx);
        mmidSize = findViewById(R.id.midsizeChbx);
        mfullSize = findViewById(R.id.fullsizeChbx);
        maddress = findViewById(R.id.addresseditText);
        mnextBtn = findViewById(R.id.modifyBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        mSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSingle.isChecked())
                    mgarageType.add("Single-level");
                else
                    mgarageType.remove("Single-level");
            }
        });
        mMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMulti.isChecked())
                    mgarageType.add("Multi-level");
                else
                    mgarageType.remove("Multi-level");
            }
        });
        mUnder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUnder.isChecked())
                    mgarageType.add("Underground");
                else
                    mgarageType.remove("Underground");
            }
        });
        mStreet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStreet.isChecked())
                    mgarageType.add("Street-side");
                else
                    mgarageType.remove("Street-side");
            }
        });
        mCompact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCompact.isChecked())
                    msupportedCars.add("Compact");
                else
                    msupportedCars.remove("Compact");
            }
        });
        mmidSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mmidSize.isChecked())
                    msupportedCars.add("Mid-Size");
                else
                    msupportedCars.remove("Mid-Size");
            }
        });
        mfullSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mfullSize.isChecked())
                    msupportedCars.add("Full-Size");
                else
                    msupportedCars.remove("Full-Size");
            }
        });

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference userRef = fStore.collection("users").document(userID);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String lotname = documentSnapshot.getString("Lot name");
                    minputlotName.setText(""+lotname);
                    String sqm = documentSnapshot.getString("Sqm");
                    msqm.setText(""+sqm);
                    String parking = documentSnapshot.getString("Exact number of slots");
                    mexactNumber.setText(""+parking);
                    String address = documentSnapshot.getString("Address");
                    maddress.setText(""+address);
                }
            }
        });

        mnextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputLotname = minputlotName.getText().toString();
                String sqm = msqm.getText().toString();
                String exactNumber = mexactNumber.getText().toString();
                ArrayList<String> garageType = mgarageType;
                ArrayList<String> supportedCars = msupportedCars;
                String address = maddress.getText().toString();

                userID = fAuth.getCurrentUser().getUid();
                DocumentReference userRef = fStore.collection("users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("Lot name", inputLotname);
                user.put("Sqm", sqm);
                user.put("Exact number of slots", exactNumber);
                user.put("Garage Type", garageType);
                user.put("Supported cars", supportedCars);
                user.put("Address", address);

                userRef.set(user, SetOptions.merge());


                startActivity(new Intent(getApplicationContext(), ModifyListing2.class));


            }
        });
    }
}