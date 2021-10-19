package com.example.authenticationapp.OwnerInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.authenticationapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ParkingSlotStatus extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    Integer number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_slot_status);

        LinearLayout mlayout = findViewById(R.id.layout1);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();


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
                    Button button1 = new Button(ParkingSlotStatus.this);
                    button1.setText("Slot " + i);
                    // R.id won't be generated for us, so we need to create one
                    button1.setId(i);
                    // add generated button to view
                    if (i == 0) {
                        mlayout.addView(button1);
                    }
                    else {
                        mlayout.addView(button1);
                    }
                }
            }
        });

    }


}