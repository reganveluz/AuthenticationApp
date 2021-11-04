package com.example.authenticationapp.DriverInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.authenticationapp.DriverInterface.PaymentWindow;
import com.example.authenticationapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class booking_ended extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userID;
    private RatingBar ratingBar;
    String ctr;
    private TextView tv;
    private Button mExtend, mDismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_ended);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();


        ratingBar = findViewById(R.id.endrating);
        tv = findViewById(R.id.rateExperience);
        mExtend = findViewById(R.id.extendNotif);
        mDismiss = findViewById(R.id.dismissNotif);


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                ctr = String.valueOf(rating);

                if (rating == 0 || rating == 0.5 ||rating == 1){
                    tv.setText("Very Dissatisfied");
                }
                else if (rating == 2 || rating == 1.5){
                    tv.setText("Dissatisfied");
                }
                else if (rating == 3 || rating == 2.5) {
                    tv.setText("Satisfied");
                }
                else if (rating == 4 || rating == 3.5){
                    tv.setText("Very Satisfied");
                }
                else if (rating == 5|| rating == 4.5){
                    tv.setText("Excellent");
                }
            }
        });

        mExtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        mDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference deleteReservation = fStore.collection("Transactions").document(userID);
                DocumentReference grabName = fStore.collection("Transactions").document(userID);

                grabName.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> grab = new HashMap<>();
                        String recentReservation = documentSnapshot.getString("Garage Name");

                        grab.put("Recent reservation", recentReservation);

                        grabName.set(grab, SetOptions.merge());

                        Map<String, Object> deletes = new HashMap<>();
                        deletes.put("Approval", FieldValue.delete());
                        deletes.put("Car model", FieldValue.delete());
                        deletes.put("Garage Name", FieldValue.delete());
                        deletes.put("License ID", FieldValue.delete());
                        deletes.put("Price", FieldValue.delete());
                        deletes.put("Reference Number", FieldValue.delete());
                        deletes.put("Reservation Period", FieldValue.delete());
                        deletes.put("Time of Arrival", FieldValue.delete());
                        deletes.put("Price", FieldValue.delete());
                        deletes.put("Transaction #", FieldValue.delete());
                        deletes.put("Transaction Date", FieldValue.delete());

                        deleteReservation.set(deletes, SetOptions.merge());

                    }
                });

                finish();
            }
        });
    }
}