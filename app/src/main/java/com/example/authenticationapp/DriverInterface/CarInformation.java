package com.example.authenticationapp.DriverInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.authenticationapp.DriverInterface.Driver;
import com.example.authenticationapp.OwnerInterface.NewListing2;
import com.example.authenticationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class CarInformation extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    TextView muserName;
    EditText mcarID,mLicense;
    Button mSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_information);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        muserName = findViewById(R.id.userName);
        mcarID = findViewById(R.id.carID);
        mLicense = findViewById(R.id.licenseID);
        mSave = findViewById(R.id.allSetBtn);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String car = mcarID.getText().toString();
                String license = mLicense.getText().toString();

                userID = fAuth.getCurrentUser().getUid();
                DocumentReference userRef = fStore.collection("users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("Car Model",car);
                user.put("License Plate", license);

                userRef.set(user, SetOptions.merge());

                startActivity(new Intent(getApplicationContext(), Driver.class));

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