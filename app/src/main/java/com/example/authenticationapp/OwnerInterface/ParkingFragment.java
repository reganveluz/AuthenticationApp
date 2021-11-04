package com.example.authenticationapp.OwnerInterface;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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

public class ParkingFragment extends Fragment {


    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID, lot_desc;
    EditText lotdescription;
    int ctr = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking,container,false);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        String[] parkingList = {"Modify Parking Slot", "Lot Description", "Gallery"};

        ListView listView = (ListView) view.findViewById(R.id.parkingLV);

        ArrayAdapter<String> listviewAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1,parkingList

        );
        listView.setAdapter(listviewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Object item = listView.getItemAtPosition(position);
                Toast.makeText(getActivity(), "You selected : " + item, Toast.LENGTH_SHORT).show();
                if (position == 0) {


                }  else if (position == 1) {
                    AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
                    mydialog.setTitle("Lot Description");

                    lotdescription = new EditText(getActivity());
                    mydialog.setView(lotdescription);

                    try {
                        DocumentReference getGarage = fStore.collection("users").document(userID);
                        getGarage.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String lotname = documentSnapshot.getString("Lot name");

                                DocumentReference documentReference4 = fStore.collection("Garage Locations").document(lotname);

                                documentReference4.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            lot_desc = documentSnapshot.getString("Lot Description");
                                            ctr = 1;
                                            lotdescription.setText(lot_desc);
                                            mydialog.setView(lotdescription);
                                        }
                                    }
                                });
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    mydialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DocumentReference getGarage = fStore.collection("users").document(userID);

                            getGarage.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String lotname = documentSnapshot.getString("Lot name");

                                    if (ctr == 1){
                                        Map<String, Object> update = new HashMap<>();
                                        update.put("Lot Description", lotdescription.getText().toString());

                                        DocumentReference documentReference = fStore.collection("Garage Locations").document(lotname);
                                        documentReference.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "Error Occurred", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else {
                                        fAuth = FirebaseAuth.getInstance();
                                        fStore = FirebaseFirestore.getInstance();

                                        userID = fAuth.getCurrentUser().getUid();
                                        DocumentReference userRef = fStore.collection("Garage Locations").document(lotname);
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("Lot Description", lotdescription.getText().toString());

                                        userRef.set(user, SetOptions.merge());
                                    }

                                }
                            });




                        }
                    });

                    mydialog.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which){
                            dialog.cancel();
                        }

                    });
                    mydialog.show();


                }
                else if (position == 2) {

                }
            }
        });

        return view;

    }

}
