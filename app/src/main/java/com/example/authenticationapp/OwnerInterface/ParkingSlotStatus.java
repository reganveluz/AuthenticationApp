package com.example.authenticationapp.OwnerInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.authenticationapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ParkingSlotStatus extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userID;
    private Integer number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_slot_status);

        LinearLayout mlayout = findViewById(R.id.layout1);

        String mcurrentListing = getIntent().getStringExtra("Garage name");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        DocumentReference updateSlotStatus = fStore.collection("Garage Locations").document(mcurrentListing);

        updateSlotStatus.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                for(int i =1; i<= number; i++){
                    String slotStatus = documentSnapshot.getString("Slot "+i);
                }


            }
        });


        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String slot = documentSnapshot.getString("Exact number of slots");
                try {
                     number = Integer.parseInt(slot);
                }catch (NumberFormatException ex){
                    ex.printStackTrace();
                }


                for (int i = 1; i <= number; i++) {
                    Button button = new Button(ParkingSlotStatus.this);

                    button.setId(i);

                    final int id = button.getId();

                    button.setText("Slot "+ i);


                    mlayout.addView(button);



                }
            }
        });





    }


}