package com.example.authenticationapp.OwnerInterface;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.authenticationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class editProfile extends Fragment {

    String  phone, email, address, lotname;
    EditText phone1, email1, address1, lotname1;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    StorageReference mStorageRef, mStorageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference4 = fStore.collection("users").document(userID);
        documentReference4.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                phone1 = (EditText)getView().findViewById(R.id.phone_edit);
                email1 = (EditText)getView().findViewById(R.id.email_edit);
                address1 = (EditText)getView().findViewById(R.id.address_edit);
                lotname1 = (EditText)getView().findViewById(R.id.lotname_edit);

                if (documentSnapshot.exists()) {
                    phone = documentSnapshot.getString("Phone");
                    phone1.setText(phone);
                    address = documentSnapshot.getString("Address");
                    address1.setText(address);
                    email = documentSnapshot.getString("Email");
                    email1.setText(email);
                    lotname = documentSnapshot.getString("Lot name");
                    lotname1.setText(lotname);
                }

                Button btn = (Button) getView().findViewById(R.id.payBtn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phone1.getText();
                        address1.getText();
                        lotname1.getText();



                        if ((phone1 != null)&&(address1!=null)&&(lotname1!=null)) {
                            Map<String, Object> update = new HashMap<>();
                            update.put("Phone", phone1.getText().toString());
                            update.put("Address", address1.getText().toString());
                            update.put("Lot name", lotname1.getText().toString());

                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            documentReference.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        AccountFragment acc = new AccountFragment();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.fragment_container, acc);
                                        fragmentTransaction.commit();
                                        Toast.makeText(getActivity(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        AccountFragment acc = new AccountFragment();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.fragment_container, acc);
                                        fragmentTransaction.commit();
                                        Toast.makeText(getActivity(), "Error updating" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                    }
                });


            }

        });








        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }
}