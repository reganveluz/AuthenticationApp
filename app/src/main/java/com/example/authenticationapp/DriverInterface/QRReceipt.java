package com.example.authenticationapp.DriverInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.authenticationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firestore.v1.Write;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class QRReceipt extends AppCompatActivity {

    private TextView random;
    private TextView mclose;
    private ImageView ivOutput;
    private String mCarType, mLicenseID;
    private String successful = "Reservation Authorized";
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrreceipt);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        String mCar = getIntent().getStringExtra("Car");
        mCarType = mCar;
        String mLicense = getIntent().getStringExtra("License");
        mLicenseID = mLicense;

        ivOutput = findViewById(R.id.iv_output);
        random = findViewById(R.id.randomTV);
        mclose = findViewById(R.id.closeWindowTv);

        mclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Random r = new Random();
        int randomNumber = r.nextInt(999999999);
        random.setText("Transaction ID #"+randomNumber);

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference transactionRef = fStore.collection("Transactions").document(userID);
        Map<String, Object> transaction = new HashMap<>();

        transaction.put("Transaction #", random.getText().toString());

        transactionRef.set(transaction, SetOptions.merge());

        MultiFormatWriter writer = new MultiFormatWriter();

        try{
            BitMatrix matrix = writer.encode(successful + "\n" + mCarType + "\n" + mLicenseID, BarcodeFormat.QR_CODE, 350, 350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            ivOutput.setImageBitmap(bitmap);
        }   catch (WriterException e){
            e.printStackTrace();
        }

    }
}