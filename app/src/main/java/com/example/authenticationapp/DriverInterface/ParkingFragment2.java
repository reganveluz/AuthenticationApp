package com.example.authenticationapp.DriverInterface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.authenticationapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ParkingFragment2 extends Fragment {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View home = inflater.inflate(R.layout.fragment_parking2, container, false);

        TextView currentReservation = (TextView) home.findViewById(R.id.currentReservation);
        TextView recentR = (TextView) home.findViewById(R.id.recentReservation);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        DocumentReference transactionRef = fStore.collection("Transactions").document(userID);

        transactionRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
            if(documentSnapshot.exists()){
                String garagename = documentSnapshot.getString("Garage Name");
                if(garagename!=null){
                    currentReservation.setText(garagename);
                    currentReservation.setVisibility(View.VISIBLE);
                }else{
                    currentReservation.setVisibility(View.GONE);
                }
            }
                String recent = documentSnapshot.getString("Recent reservation");
                if(recent!=null){
                recentR.setText(recent);
                recentR.setVisibility(View.VISIBLE);
            }
                else{
                    recentR.setVisibility(View.GONE);
                }

            }
        });

        return home;

    }
}
