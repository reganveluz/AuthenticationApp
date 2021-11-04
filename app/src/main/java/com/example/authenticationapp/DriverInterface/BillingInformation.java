package com.example.authenticationapp.DriverInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authenticationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BillingInformation extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userID;
    public TextView mPrice, mShowRefNo;
    private String mGarage, mTime, mReservationPeriod, mTransactionTime, mApproved = null, mCar, mLicense;
    private Button mFinished, mRefNo;
    private EditText newRefNo;
    private String slotAvailable;
    private int number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_information);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();


            String total = getIntent().getStringExtra("Amount");
            mPrice = findViewById(R.id.amount);
            mPrice.setText(total);

            String garageName = getIntent().getStringExtra("Garage Name");
            mGarage = garageName;

            String reservationPeriod = getIntent().getStringExtra("Reservation Period");
            mReservationPeriod = reservationPeriod;

            String arrivalTime = getIntent().getStringExtra("Time");
            mTime = arrivalTime;

            String transactionTime = getIntent().getStringExtra("Transaction Time");
            mTransactionTime = transactionTime;

            String car = getIntent().getStringExtra("Car");
            mCar = car;
            String license = getIntent().getStringExtra("License");
            mLicense = license;

            mFinished = findViewById(R.id.finishedPayment);
            mRefNo = findViewById(R.id.enterRefNo);
            mShowRefNo = findViewById(R.id.refNo);

        DocumentReference getSlot = fStore.collection("Garage Locations").document(mGarage);

        getSlot.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                slotAvailable = documentSnapshot.getString("Slots available");
                try {
                     number = Integer.parseInt(slotAvailable);
                }catch (NumberFormatException ex){
                    ex.printStackTrace();
                }
            }
        });



        mRefNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mydialog = new AlertDialog.Builder(BillingInformation.this);
                mydialog.setTitle("Reference No.");

                newRefNo = new EditText(BillingInformation.this);
                mydialog.setView(newRefNo);

                mydialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String referenceNo = newRefNo.getText().toString().trim();
                        DocumentReference transactionRef = fStore.collection("Transactions").document(userID);
                        Map<String, Object> transaction = new HashMap<>();

                        transaction.put("Reference Number", referenceNo);
                        transaction.put("Price", mPrice.getText().toString());
                        transaction.put("Approval", mApproved);


                        transactionRef.set(transaction);

                        mShowRefNo.setText("Ref #"+referenceNo);
                    }
                });
                mydialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mydialog.show();
            }
        });

        mFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(newRefNo!=null){
                    DocumentReference transactionRef = fStore.collection("Transactions").document(userID);
                    DocumentReference updateSlotStatus = fStore.collection("Garage Locations").document(mGarage);

                    transactionRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            String approval = documentSnapshot.getString("Approval");

                            if(approval != null){
                                Map<String, Object> transaction = new HashMap<>();

                                transaction.put("Transaction Date", mTransactionTime);
                                transaction.put("Garage Name", mGarage);
                                transaction.put("Time of Arrival", mTime);
                                transaction.put("Reservation Period", mReservationPeriod);
                                transaction.put("Price", mPrice.getText().toString());
                                transaction.put("Car model", mCar);
                                transaction.put("License ID", mLicense);

                                transactionRef.set(transaction, SetOptions.merge());

                                Intent intent = new Intent(BillingInformation.this, QRReceipt.class);
                                intent.putExtra("Car",mCar);
                                intent.putExtra("License",mLicense);

                                updateSlotStatus.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String vacancy = "1";
                                        String reserved = "2";

                                        for(int i = 1; i<= number; i++){
                                            String slot = documentSnapshot.getString("Slot "+i);

                                            if(Objects.equals(slot, vacancy)){
                                                Map<String, Object> update = new HashMap<>();
                                                update.put("Slot "+i, reserved);

                                                updateSlotStatus.set(update, SetOptions.merge());
                                                transactionRef.set(update, SetOptions.merge());

                                                break;

                                            }
                                        }

                                    }
                                });



                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(BillingInformation.this, "Payment not yet received", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(BillingInformation.this, "Please enter reference number", Toast.LENGTH_LONG).show();
                }


            }
        });


    }
}