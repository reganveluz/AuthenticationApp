package com.example.authenticationapp.DriverInterface;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.authenticationapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class PaymentWindow extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private TextView mgarageName, mLicense, mCar;
    private String userID;
    private Button mPayBtn;
    private Date currentTime = Calendar.getInstance().getTime();
    private EditText mTime;
    String mPeriod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_window);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        mgarageName = findViewById(R.id.garageName);
        mLicense = findViewById(R.id.licensePlate);
        mCar = findViewById(R.id.carModel);
        mPayBtn = findViewById(R.id.payBtn);
        CheckBox m3h = findViewById(R.id.checkBox3H);
        CheckBox m6h = findViewById(R.id.checkBox6H);
        CheckBox m9h = findViewById(R.id.checkBox9H);
        TextView mPrice = findViewById(R.id.price);
        mTime = findViewById(R.id.arrivalTime);

        m3h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m3h.isChecked()){
                    mPrice.setText("PHP 40.00");
                    mPrice.setVisibility(View.VISIBLE);
                    mPeriod = "3 Hours";
                    m6h.setClickable(false);
                    m9h.setClickable(false);
                }else{
                    mPrice.setVisibility(View.INVISIBLE);
                    m6h.setClickable(true);
                    m9h.setClickable(true);
                }
            }
        });
        m6h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m6h.isChecked()){
                    mPrice.setText(("PHP 100.00"));
                    mPrice.setVisibility(View.VISIBLE);
                    mPeriod = "6 Hours";
                    m3h.setClickable(false);
                    m9h.setClickable(false);
                }else{
                    mPrice.setVisibility(View.INVISIBLE);
                    m3h.setClickable(true);
                    m9h.setClickable(true);
                }

            }

        });
        m9h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m9h.isChecked()){
                    mPrice.setText(("PHP 160.00"));
                    mPrice.setVisibility(View.VISIBLE);
                    mPeriod = "9 Hours";
                    m3h.setClickable(false);
                    m6h.setClickable(false);
                }else{
                    mPrice.setVisibility(View.INVISIBLE);
                    m3h.setClickable(true);
                    m6h.setClickable(true);
                }
            }
        });

        String garageName = getIntent().getStringExtra("Garage ID");
        mgarageName.setText(garageName);


        mPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentWindow.this, BillingInformation.class);
                String total = mPrice.getText().toString();
                String arrivalTime = mTime.getText().toString();
                String transactionTime = currentTime.toString();
                String garageName = mgarageName.getText().toString();
                String reservationPeriod = mPeriod;
                String car = mCar.getText().toString();
                String license = mLicense.getText().toString();
                intent.putExtra("Garage Name", garageName);
                intent.putExtra("Amount", total);
                intent.putExtra("Time", arrivalTime);
                intent.putExtra("Transaction Time", transactionTime);
                intent.putExtra("Reservation Period", reservationPeriod);
                intent.putExtra("Car", car);
                intent.putExtra("License", license);
                startActivity(intent);
                finish();

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