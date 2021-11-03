package com.example.authenticationapp.OwnerInterface;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authenticationapp.OwnerInterface.AccountFragment;
import com.example.authenticationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class feedback extends Fragment {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    Button btn_submit;
    EditText edit_feedback;
    TextView tv;
    RatingBar ratingBar;
    String ctr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rv = inflater.inflate(R.layout.fragment_feedback, container, false);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        ratingBar = (RatingBar) rv.findViewById(R.id.ratingBar);

        btn_submit = (Button) rv.findViewById(R.id.btn_submit);

        edit_feedback = (EditText) rv.findViewById(R.id.edit_feedback);
        tv = (TextView) rv.findViewById(R.id.tv);
//        *************************************
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

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userID);
                Map<String,Object> insert = new HashMap<>();
                insert.put("Feedback: ",edit_feedback.getText().toString());
                insert.put("Rate", ctr);

                //document reference for easy access to db
                documentReference.set(insert, SetOptions.merge());

                AccountFragment acc = new AccountFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, acc);
                fragmentTransaction.commit();
            }
        });

        // Inflate the layout for this fragment
        return rv;
    }
}