package com.example.authenticationapp.OwnerInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authenticationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CurrentListing extends AppCompatActivity {
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    TextView mlotName, msizeSqm, mavailableSpace, mgarageType, msupportedCars, mAddress, mDays, mHours, mHours2;
    Button mmodifyBtn, mdeleteBtn;
    ImageView mimageView;
    private StorageReference mStorageReference;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_listing);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mlotName = findViewById(R.id.lotName);
        msizeSqm = findViewById(R.id.exactSize);
        mavailableSpace = findViewById(R.id.parkingSpace);
        mgarageType = findViewById(R.id.garagetypeTV);
        msupportedCars = findViewById(R.id.supportcarsTV);
        mAddress = findViewById(R.id.address);
        mDays = findViewById(R.id.operationalDays);
        mHours = findViewById(R.id.operationalHours);
        mHours2 = findViewById(R.id.operationalHours2);
        mmodifyBtn = findViewById(R.id.modifyBtn);
        mdeleteBtn = findViewById(R.id.deleteBtn);
        mimageView = findViewById(R.id.imageView);


        mmodifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ModifyListing.class));
            }
        });
        mdeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("Images/"+userID+".Lot Images");

        try {
            final File localFile = File.createTempFile(""+userID,".Lot Images");
            mStorageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(CurrentListing.this, "Image retrieved successfully", Toast.LENGTH_LONG).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            mimageView.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CurrentListing.this, "Image loading error", Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e){
            e.printStackTrace();
        }

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String lotname = documentSnapshot.getString("Lot name");
                    mlotName.setText("" + lotname);
                    String sizesqm = documentSnapshot.getString("Sqm");
                    msizeSqm.setText("" + sizesqm);
                    String availablespace = documentSnapshot.getString("Exact number of slots");
                    mavailableSpace.setText("" + availablespace);
                    String address = documentSnapshot.getString("Address");
                    mAddress.setText("" + address);
                    String hours1 = documentSnapshot.getString("Starting Time");
                    mHours.setText("" + hours1 + "    to");
                    String hours2 = documentSnapshot.getString("End Time");
                    mHours2.setText("" + hours2);


                }

            }
        });

        //get Array from database and view inside activity
        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference1 = fStore.collection("users").document(userID);

        documentReference1.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        String group_string = document.get("Garage Type").toString();
                        Log.d("Garage Type", group_string);
                        mgarageType.setText(group_string);

                        String group_string2 = document.get("Supported cars").toString();
                        Log.d("Supported Cars", group_string2);
                        msupportedCars.setText(group_string2);

                        String group_string3 = document.get("Operational Days").toString();
                        Log.d("Operational Days", group_string3);
                        mDays.setText(group_string3);
                    }
                });

    }


    public void delete() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete listing")
                .setMessage("Are you sure you want to delete this listing?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletelisting();
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void deletelisting() {

        DocumentReference documentReference = fStore.collection("users").document(userID);
        userID = fAuth.getCurrentUser().getUid();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        Map<String, Object> deletes = new HashMap<>();
        deletes.put("Lot name", FieldValue.delete());
        deletes.put("Sqm", FieldValue.delete());
        deletes.put("Exact number of slots", FieldValue.delete());
        deletes.put("Garage Type", FieldValue.delete());
        deletes.put("Supported cars", FieldValue.delete());
        deletes.put("Address", FieldValue.delete());
        deletes.put("Operational Days", FieldValue.delete());
        deletes.put("Starting Time", FieldValue.delete());
        deletes.put("End Time", FieldValue.delete());

        documentReference.update(deletes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CurrentListing.this, "Listing has been deleted", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(CurrentListing.this, Owner.class);
                    startActivity(i);
                } else {
                    Toast.makeText(CurrentListing.this, "Error deleting listing" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}



