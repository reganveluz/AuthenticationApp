package com.example.authenticationapp.OwnerInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.view.View;
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
import java.util.Objects;

public class ParkingSlotStatus extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userID;
    private Integer number;
    private Button mslot1, mslot2, mslot3, mslot4, mslot5, mslot6, mslot7, mslot8, mslot9, mslot10,
            mslot11,mslot12,mslot13,mslot14,mslot15,mslot16,mslot17,mslot18,mslot19,mslot20;
    private String vacant = "1";
    private String reserved = "2";
    private Button mVacant, mReserved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_slot_status);


        mslot1 = findViewById(R.id.slot1Btn);
        mslot2 = findViewById(R.id.slot2Btn);
        mslot3 = findViewById(R.id.slot3Btn);
        mslot4 = findViewById(R.id.slot4Btn);
        mslot5 = findViewById(R.id.slot5Btn);
        mslot6 = findViewById(R.id.slot6Btn);
        mslot7 = findViewById(R.id.slot7Btn);
        mslot8 = findViewById(R.id.slot8Btn);
        mslot9 = findViewById(R.id.slot9Btn);
        mslot10 = findViewById(R.id.slot10Btn);
        mslot11 = findViewById(R.id.slot11Btn);
        mslot12 = findViewById(R.id.slot12Btn);
        mslot13 = findViewById(R.id.slot13Btn);
        mslot14 = findViewById(R.id.slot14Btn);
        mslot15 = findViewById(R.id.slot15Btn);
        mslot16 = findViewById(R.id.slot16Btn);
        mslot17 = findViewById(R.id.slot17Btn);
        mslot18 = findViewById(R.id.slot18Btn);
        mslot19 = findViewById(R.id.slot19Btn);
        mslot20 = findViewById(R.id.slot20Btn);

        String mcurrentListing = getIntent().getStringExtra("Garage name");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        mVacant = findViewById(R.id.vacantDesign);
        mReserved = findViewById(R.id.reservedDesign);

        mVacant.setClickable(false);
        mReserved.setClickable(false);


        DocumentReference updateSlotStatus = fStore.collection("Garage Locations").document(mcurrentListing);

        updateSlotStatus.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //slot 1
                if(Objects.equals(documentSnapshot.get("Slot 1"), vacant)){
                    mslot1.setBackgroundColor(getResources().getColor(R.color.purple_500));
                    }
                else if(Objects.equals(documentSnapshot.get("Slot 1"), reserved)){
                    mslot1.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot1.setVisibility(View.GONE);
                }
                //slot 2
                if(Objects.equals(documentSnapshot.get("Slot 2"), vacant)){
                    mslot2.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 2"), reserved)){
                    mslot2.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot2.setVisibility(View.GONE);
                }
                //slot 3
                if(Objects.equals(documentSnapshot.get("Slot 3"), vacant)){
                    mslot3.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 3"), reserved)){
                    mslot3.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot3.setVisibility(View.GONE);
                }
                //slot 4
                if(Objects.equals(documentSnapshot.get("Slot 4"), vacant)){
                    mslot4.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 4"), reserved)){
                    mslot4.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot4.setVisibility(View.GONE);
                }
                //slot 5
                if(Objects.equals(documentSnapshot.get("Slot 5"), vacant)){
                    mslot5.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 5"), reserved)){
                    mslot5.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot5.setVisibility(View.GONE);
                }
                //slot 6
                if(Objects.equals(documentSnapshot.get("Slot 6"), vacant)){
                    mslot6.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 6"), reserved)){
                    mslot6.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot6.setVisibility(View.GONE);
                }
                //slot 7
                if(Objects.equals(documentSnapshot.get("Slot 7"), vacant)){
                    mslot7.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 7"), reserved)){
                    mslot7.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot7.setVisibility(View.GONE);
                }
                //slot 8
                if(Objects.equals(documentSnapshot.get("Slot 8"), vacant)){
                    mslot8.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 8"), reserved)){
                    mslot8.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot8.setVisibility(View.GONE);
                }

                if(Objects.equals(documentSnapshot.get("Slot 9"), vacant)){
                    mslot9.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 9"), reserved)){
                    mslot9.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot9.setVisibility(View.GONE);
                }

                if(Objects.equals(documentSnapshot.get("Slot 10"), vacant)){
                    mslot10.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 10"), reserved)){
                    mslot10.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot10.setVisibility(View.GONE);
                }

                if(Objects.equals(documentSnapshot.get("Slot 11"), vacant)){
                    mslot11.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 11"), reserved)){
                    mslot11.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot11.setVisibility(View.GONE);
                }

                if(Objects.equals(documentSnapshot.get("Slot 12"), vacant)){
                    mslot12.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 12"), reserved)){
                    mslot12.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot12.setVisibility(View.GONE);
                }

                if(Objects.equals(documentSnapshot.get("Slot 13"), vacant)){
                    mslot13.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 13"), reserved)){
                    mslot13.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot13.setVisibility(View.GONE);
                }

                if(Objects.equals(documentSnapshot.get("Slot 14"), vacant)){
                    mslot14.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 14"), reserved)){
                    mslot14.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot14.setVisibility(View.GONE);
                }

                if(Objects.equals(documentSnapshot.get("Slot 15"), vacant)){
                    mslot15.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 15"), reserved)){
                    mslot15.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot15.setVisibility(View.GONE);
                }

                if(Objects.equals(documentSnapshot.get("Slot 16"), vacant)){
                    mslot16.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 16"), reserved)){
                    mslot16.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot16.setVisibility(View.GONE);
                }

                if(Objects.equals(documentSnapshot.get("Slot 17"), vacant)){
                    mslot17.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 17"), reserved)){
                    mslot17.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot17.setVisibility(View.GONE);
                }

                if(Objects.equals(documentSnapshot.get("Slot 18"), vacant)){
                    mslot18.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 18"), reserved)){
                    mslot18.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot18.setVisibility(View.GONE);
                }

                if(Objects.equals(documentSnapshot.get("Slot 19"), vacant)){
                    mslot19.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 19"), reserved)){
                    mslot19.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot19.setVisibility(View.GONE);
                }

                if(Objects.equals(documentSnapshot.get("Slot 20"), vacant)){
                    mslot20.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 20"), reserved)){
                    mslot20.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot20.setVisibility(View.GONE);
                }

                if(Objects.equals(documentSnapshot.get("Slot 20"), vacant)){
                    mslot20.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 20"), reserved)){
                    mslot20.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot20.setVisibility(View.GONE);
                }

                if(Objects.equals(documentSnapshot.get("Slot 20"), vacant)){
                    mslot20.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
                else if(Objects.equals(documentSnapshot.get("Slot 20"), reserved)){
                    mslot20.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen));
                }else{
                    mslot20.setVisibility(View.GONE);
                }
            }
        });




    }


}