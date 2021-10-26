package com.example.authenticationapp.DriverInterface;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.authenticationapp.DriverInterface.HomeFragment2;
import com.example.authenticationapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class PaymentWindow extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private TextView mgarageName, mLicense, mCar;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_window);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        mgarageName = findViewById(R.id.garageName);
        mLicense = findViewById(R.id.licensePlate);
        mCar = findViewById(R.id.carModel);


        DocumentReference documentReference = fStore.collection("Garage Loader").document("Garage Name");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String grg = documentSnapshot.getString("Garage ID");
                mgarageName.setText(grg);

            }
        });

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference userRef = fStore.collection("users").document(userID);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String l = documentSnapshot.getString("License Plate");
                String c = documentSnapshot.getString("Car Model");

                mLicense.setText(l);
                mCar.setText(c);

                if(l!=null && c!=null){
                    mLicense.setVisibility(View.VISIBLE);
                    mCar.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}