package com.example.authenticationapp.DriverInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.authenticationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
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
    public static final String CHANNEL_ID = "parking boy";
    private static final String CHANNEL_NAME = "parking boy";
    private static final String CHANNEL_DESC = "parking boy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrreceipt);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              displayNotification2();

              new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      displayNotification3();

                      new Handler().postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              displayNotification4();
                              startActivity(new Intent(getApplicationContext(), booking_ended.class));

                          }
                      }, 15000);
                  }
              }, 15000);
            }
        }, 20000);



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }



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
                displayNotification();

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

    private void displayNotification4() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle("Your reservation has ended, do you wish to extend?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, mBuilder.build());

    }

    private void displayNotification3() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle("You have arrived at your destination")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, mBuilder.build());
    }

    private void displayNotification2() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle("Your reservation starts in 15 minutes")
                .setContentText("Drive safely, and we wish you the best experience")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, mBuilder.build());

    }

    private void displayNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle("Parking Reservation Successful")
                .setContentText("Please arrive on time")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, mBuilder.build());

    }

}