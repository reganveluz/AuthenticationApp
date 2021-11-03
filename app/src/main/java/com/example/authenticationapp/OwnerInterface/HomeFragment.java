package com.example.authenticationapp.OwnerInterface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.authenticationapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    private StorageReference mStorageReference;
    private Button mAddListingToMapsBtn;
    private TextView mcurrentListing;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        View home = inflater.inflate(R.layout.fragment_home, container, false);

        //for new listing
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewListing.class);
                startActivity(intent);
            }
        };
        //to show information about existing listings
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CurrentListing.class);
                startActivity(intent);
            }
        };
        //parking slot status
        View.OnClickListener listener3 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ParkingSlotStatus.class);
                intent.putExtra("Garage name", mcurrentListing.getText().toString());
                startActivity(intent);
            }
        };

                View.OnClickListener listener4 = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), AddListingMaps.class);
                        startActivity(intent);
                    }
                };


                mAddListingToMapsBtn = (Button) home.findViewById(R.id.addToMaps);
                mAddListingToMapsBtn.setOnClickListener(listener4);

                Button mnewlistingBtn = (Button) home.findViewById(R.id.addnewlistingBtn);
                mnewlistingBtn.setOnClickListener(listener);

                Button mslotstatusBtn = (Button) home.findViewById(R.id.slotstatusBtn);
                mslotstatusBtn.setOnClickListener(listener3);

                mcurrentListing = (TextView) home.findViewById(R.id.currentListing);
                mcurrentListing.setOnClickListener(listener2);

                TextView tv1 = (TextView) home.findViewById(R.id.textView8);

                ImageView lotiv = (ImageView) home.findViewById(R.id.lotimageV);

                fAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();
                userID = fAuth.getCurrentUser().getUid();
                mStorageReference = FirebaseStorage.getInstance().getReference().child("Images/" + userID + ".Lot Images");

                try {
                    final File localFile = File.createTempFile("" + userID, ".Lot Images");
                    mStorageReference.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    lotiv.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


                userID = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userID);

                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String currentListing = documentSnapshot.getString("Lot name");
                        if (currentListing != null) {
                            mcurrentListing.setText("" + currentListing);
                            mcurrentListing.setVisibility(View.VISIBLE);
                            mnewlistingBtn.setVisibility(View.GONE);
                            mslotstatusBtn.setVisibility(View.VISIBLE);
                            mAddListingToMapsBtn.setVisibility(View.VISIBLE);
                            tv1.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Existing listing found", Toast.LENGTH_SHORT).show();
                        } else {
                            mcurrentListing.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "No listings found, please add new listing", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                return home;
            }
        }